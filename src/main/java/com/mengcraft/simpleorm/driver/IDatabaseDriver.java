package com.mengcraft.simpleorm.driver;

import com.mengcraft.simpleorm.ORM;
import com.mengcraft.simpleorm.lib.LibraryLoader;
import com.mengcraft.simpleorm.lib.MavenLibrary;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class IDatabaseDriver {

    public static String validAndLoad(String jdbc) {
        URI uri = URI.create(jdbc);
        if (!Objects.equals(uri.getScheme(), "jdbc")) {
            throw new IllegalArgumentException(jdbc + " is not valid jdbc url");
        }
        uri = URI.create(uri.getSchemeSpecificPart());
        IDatabaseDriver driver = Registry.__.get(uri.getScheme());
        if (driver == null) {
            return jdbc;
        }
        driver.load();
        return jdbc;
    }

    protected abstract String clazz();

    protected abstract String protocol();

    protected abstract String description();

    private void load() {
        try {
            Class.forName(clazz());
        } catch (ClassNotFoundException e) {
            LibraryLoader.load(JavaPlugin.getPlugin(ORM.class), MavenLibrary.of(description()), true);
            load();
        }
    }

    public static class Registry {

        private static final Map<String, IDatabaseDriver> __ = new HashMap<>();

        static {
            register(new PostgreDriver());
        }

        private static void register(IDatabaseDriver driver) {
            __.put(driver.protocol(), driver);
        }
    }

}
