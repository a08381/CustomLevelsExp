package com.mengcraft.simpleorm;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import lombok.Cleanup;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import redis.clients.jedis.BinaryJedisPubSub;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.util.Pool;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.mengcraft.simpleorm.ORM.nil;

public class RedisWrapper {

    private final Pool<Jedis> pool;
    private MessageFilter messageFilter;

    RedisWrapper(String url, GenericObjectPoolConfig config) {
        pool = new JedisPool(config, URI.create(url));
    }

    RedisWrapper(Pool<Jedis> pool) {
        this.pool = pool;
    }

    public static RedisWrapper b(String sentinel, String url, int conn) {
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        if (conn >= 1) {
            config.setMaxTotal(conn);
        }
        if (sentinel == null || sentinel.isEmpty()) {
            return new RedisWrapper(url, config);
        }
        String[] split = url.split(";");
        Set<String> b = new HashSet<>(split.length);
        for (String line : split) {
            URI uri = URI.create(line);
            b.add(uri.getHost() + ':' + uri.getPort());
        }
        return new RedisWrapper(new JedisSentinelPool(sentinel, b, config));
    }

    public String ping() throws JedisConnectionException {
        return call(jd -> jd.ping());
    }

    public <T> T call(Function<Jedis, T> function) {
        @Cleanup Jedis jedis = pool.getResource();
        return function.apply(jedis);
    }

    public void publish(String channel, String message) {
        publish(channel, message.getBytes(StandardCharsets.UTF_8));
    }

    public void publish(String channel, byte[] message) {
        open(client -> client.publish(channel.getBytes(StandardCharsets.UTF_8), message));
    }

    public void open(Consumer<Jedis> consumer) {
        @Cleanup Jedis jedis = pool.getResource();
        consumer.accept(jedis);
    }

    public synchronized void subscribe(String channel, Consumer<byte[]> consumer) {
        if (nil(messageFilter)) {
            messageFilter = new MessageFilter();
            new Thread(() -> open(client -> client.subscribe(messageFilter, channel.getBytes(StandardCharsets.UTF_8)))).start();
        } else if (!messageFilter.handled.containsKey(channel)) {
            messageFilter.subscribe(channel.getBytes(StandardCharsets.UTF_8));
        }
        messageFilter.handled.put(channel, consumer);
    }

    public synchronized void unsubscribeAll() {
        if (nil(messageFilter)) {
            return;
        }
        messageFilter.unsubscribe();
        messageFilter = null;
    }

    @SneakyThrows
    public synchronized void unsubscribe(String channel) {
        if (nil(messageFilter)) {
            return;
        }

        if (!messageFilter.handled.containsKey(channel)) {
            return;
        }

        messageFilter.unsubscribe(channel.getBytes(StandardCharsets.UTF_8));
        if (!messageFilter.handled.isEmpty()) {
            return;
        }

        messageFilter = null;
    }

    public synchronized void unsubscribe(String channel, Consumer<byte[]> consumer) {
        if (nil(messageFilter)) {
            return;
        }

        if (!messageFilter.handled.containsKey(channel)) {
            return;
        }

        boolean removal = messageFilter.handled.remove(channel, consumer);
        if (!removal) {
            return;
        }

        if (messageFilter.handled.containsKey(channel)) {
            return;
        }

        messageFilter.unsubscribe(channel.getBytes(StandardCharsets.UTF_8));
        if (!messageFilter.handled.isEmpty()) {
            return;
        }

        messageFilter = null;
    }

    public void set(String key, Object obj) {
        set(key, obj, 0);
    }

    public void set(String key, Object obj, int expire) {
        open(jedis -> {
            jedis.set(key, JSONObject.toJSONString(ORM.serialize(obj)));
            if (expire >= 1) {
                jedis.expire(key, expire);
            }
        });
    }

    public <T> T get(String key, Class<T> clazz) {
        return call(jedis -> {
            String str = jedis.get(key);
            if (str == null) {
                return null;
            }
            return ORM.deserialize(clazz, (Map<String, Object>) JSONValue.parse(str));
        });
    }

    private static class MessageFilter extends BinaryJedisPubSub {

        private final Multimap<String, Consumer<byte[]>> handled = HashMultimap.create();

        @SneakyThrows
        public void onMessage(byte[] channel, byte[] message) {
            Collection<Consumer<byte[]>> allConsumer = handled.get(new String(channel, StandardCharsets.UTF_8));
            if (allConsumer.isEmpty()) {
                return;
            }

            for (Consumer<byte[]> consumer : allConsumer) consumer.accept(message);
        }

        @Override
        @SneakyThrows
        public void subscribe(@NonNull byte[]... channels) {
            try {
                super.subscribe(channels);
            } catch (NullPointerException e) {
                TimeUnit.MILLISECONDS.sleep(0);
                subscribe(channels);
            }
        }
    }
}
