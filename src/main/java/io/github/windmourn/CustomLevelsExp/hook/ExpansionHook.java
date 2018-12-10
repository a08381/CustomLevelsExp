package io.github.windmourn.CustomLevelsExp.hook;

import io.github.windmourn.CustomLevelsExp.util.CustomExpUtil;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class ExpansionHook extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "customlevel";
    }

    @Override
    public String getAuthor() {
        return "Windmourn";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onPlaceholderRequest(Player p, String params) {
        switch (params.toLowerCase()) {
            case "need": {
                return String.valueOf(CustomExpUtil.getExpUntilNextLevel(p));
            }
            case "exp": {
                return String.valueOf(CustomExpUtil.getExp(p));
            }
            case "level": {
                return String.valueOf(CustomExpUtil.getLevel(p));
            }
            case "totalexp": {
                return String.valueOf(CustomExpUtil.getTotalExperience(p));
            }
            default:
                return "";
        }
    }
}
