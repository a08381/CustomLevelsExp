package io.github.windmourn.CustomLevelsExp;

import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.Query;
import com.mengcraft.simpleorm.DatabaseException;
import com.mengcraft.simpleorm.EbeanHandler;
import io.github.windmourn.CustomLevelsExp.command.MainCommand;
import io.github.windmourn.CustomLevelsExp.config.Config;
import io.github.windmourn.CustomLevelsExp.entry.TotalExp;
import io.github.windmourn.CustomLevelsExp.hook.PlaceholderAPIHook;
import io.github.windmourn.CustomLevelsExp.listener.ExpChange;
import io.github.windmourn.CustomLevelsExp.listener.PlayerLogin;
import io.github.windmourn.CustomLevelsExp.listener.PlayerQuit;
import io.github.windmourn.CustomLevelsExp.task.DataSaveTask;
import io.github.windmourn.CustomLevelsExp.util.CustomExpUtil;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.util.Collection;

public class Main extends JavaPlugin {

    public static Main INSTANCE;

    private EbeanServer data;
    private ScriptEngine engine;
    private Invocable formula;

    private File configFile;
    private Config newConfig;

    @Override
    public void onLoad() {
        INSTANCE = this;
        configFile = new File(getDataFolder(), "/config.yml");
        if (!configFile.exists()) saveResource("config.yml", true);
        newConfig = new Config(configFile);
    }

    @Override
    public void onEnable() {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("nashorn");
        if (engine == null) engine = manager.getEngineByName("ECMAScript");

        reloadConfig();

        PluginManager pm = getServer().getPluginManager();

        if (pm.isPluginEnabled("PlaceholderAPI")) PlaceholderAPIHook.hook();

        pm.registerEvents(new ExpChange(), this);
        //pm.registerEvents(new EnchantItem(), this);
        pm.registerEvents(new PlayerLogin(), this);
        pm.registerEvents(new PlayerQuit(), this);

        new MainCommand();

        new DataSaveTask().runTaskTimerAsynchronously(this, newConfig.getSavetime() * 20, newConfig.getSavetime() * 20);
    }

    public <T> Query<T> find(Class<T> aClass) {
        return data.find(aClass);
    }

    public <T> T createEntityBean(Class<T> aClass) {
        return data.createEntityBean(aClass);
    }

    public void save(Object o) {
        data.save(o);
    }

    public int save(Collection<?> collection) {
        return data.save(collection);
    }

    public Config getConf() {
        return newConfig;
    }

    public ScriptEngine getEngine() {
        return engine;
    }

    public Invocable getFormula() {
        return formula;
    }

    public EbeanServer getData() {
        return data;
    }

    @Override
    public void reloadConfig() {
        newConfig.reload();
        initialize();
        try {
            engine.eval("function getExpAtLevel(level) {" + newConfig.getFormula() + "}");
            formula = (Invocable) engine;
            CustomExpUtil.map.clear();
        } catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveConfig() {
        newConfig.save();
    }

    private void initialize() {
        EbeanHandler handler = new EbeanHandler(this);

        handler.setDriver(newConfig.getDataSource().getDriver());
        handler.setUrl(newConfig.getDataSource().getUrl().replace("$datafolder", getDataFolder().getPath().replace("\\", "/")));
        handler.setUser(newConfig.getDataSource().getUsername());
        handler.setPassword(newConfig.getDataSource().getPassword());

        handler.define(TotalExp.class);

        try {
            handler.initialize();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }

        handler.install();

        data = handler.getServer();
    }
}
