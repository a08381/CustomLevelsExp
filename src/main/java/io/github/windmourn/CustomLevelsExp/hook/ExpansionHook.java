package io.github.windmourn.CustomLevelsExp.hook;

import io.github.windmourn.CustomLevelsExp.exp.CustomExp;
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
                return String.valueOf(CustomExp.getInstance().getExpUntilNextLevel(p));
            }
            case "exp": {
                return String.valueOf(CustomExp.getInstance().getExp(p));
            }
            case "level": {
                return String.valueOf(CustomExp.getInstance().getLevel(p));
            }
            case "totalexp": {
                return String.valueOf(CustomExp.getInstance().getTotalExp(p));
            }
            default:
                return "";
        }
    }
}
