package io.github.windmourn.CustomLevelsExp.config;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import pw.yumc.YumCore.config.annotation.ConfigNode;
import pw.yumc.YumCore.config.inject.InjectConfigurationSection;

public class DataSource extends InjectConfigurationSection {

    @Getter
    @Setter
    @ConfigNode("driver")
    private String driver;

    @Getter
    @Setter
    @ConfigNode("url")
    private String url;

    @Getter
    @Setter
    @ConfigNode("username")
    private String username;

    @Getter
    @Setter
    @ConfigNode("password")
    private String password;

    public DataSource(ConfigurationSection config) {
        super(config);
    }
}
