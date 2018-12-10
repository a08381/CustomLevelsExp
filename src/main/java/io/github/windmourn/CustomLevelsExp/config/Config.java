package io.github.windmourn.CustomLevelsExp.config;

import lombok.Getter;
import lombok.Setter;
import pw.yumc.YumCore.config.annotation.ConfigNode;
import pw.yumc.YumCore.config.inject.InjectConfig;

import java.io.File;

public class Config extends InjectConfig {

    @Getter
    @Setter
    @ConfigNode("dataSource")
    public DataSource dataSource;

    @Getter
    @Setter
    @ConfigNode("savetime")
    public int savetime;

    @Getter
    @Setter
    @ConfigNode("formula")
    public String formula;

    public Config(File file) {
        super(file);
    }
}