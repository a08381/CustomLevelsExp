package io.github.windmourn.CustomLevelsExp.exp;

import io.github.windmourn.CustomLevelsExp.entry.TotalExp;
import io.github.windmourn.CustomLevelsExp.util.PlayerUtil;
import io.github.windmourn.CustomLevelsExp.util.ServerVersion;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class CustomExp extends IExp {

    private static BukkitExp bukkitExp;
    private static CustomExp instance;
    public Map<Integer, Integer> map = new HashMap<>();

    public CustomExp() {
        instance = this;
        bukkitExp = ServerVersion.getServerVersion().isGreaterThanOrEqual("1.8")
                ? new Exp_1_8()
                : new Exp_1_7();
    }

    public static CustomExp getInstance() {
        return instance;
    }

    public static BukkitExp getBukkitExp() {
        return bukkitExp;
    }

    @Override
    public void setExp(Player player, int exp) {
        setTotalExp(player, getExpToLevel(player.getLevel()) + exp);
    }

    @Override
    public void setLevel(Player player, int level) {
        int exp = getTotalExp(player) - getExpToLevel(player.getLevel());
        setTotalExp(player, getExpToLevel(level) + exp);
    }

    @Override
    public int getTotalExp(Player player) {
        TotalExp exp = PlayerUtil.get(player);
        return exp.totalExp;
    }

    @Override
    public void setTotalExp(Player player, int totalExp) {
        if (totalExp < 0) {
            throw new IllegalArgumentException("Experience is negative!");
        }
        TotalExp te = PlayerUtil.get(player);
        te.setTotalExp(totalExp);

        bukkitExp.setTotalExp(player, asyncTotalExperience(totalExp));
    }

    public int asyncTotalExperience(int exp) {
        int level = getLevel(exp);
        int exp2level = bukkitExp.getExpToLevel(level);
        int exp1 = getExp(exp) * bukkitExp.getExpAtLevel(level) / getExpAtLevel(level);
        return exp2level + exp1;
    }
}
