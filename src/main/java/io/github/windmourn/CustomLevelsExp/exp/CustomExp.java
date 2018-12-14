package io.github.windmourn.CustomLevelsExp.exp;

import io.github.windmourn.CustomLevelsExp.entry.TotalExp;
import io.github.windmourn.CustomLevelsExp.util.PlayerUtil;
import io.github.windmourn.CustomLevelsExp.util.ServerVersion;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public abstract class CustomExp {

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

    public int getExp(long totalExp) {
        return (int) (totalExp - getExpToLevel(getLevel(totalExp)));
    }

    public int getExp(Player player) {
        return getExp(getTotalExp(player));
    }

    public void setExp(Player player, long exp) {
        setTotalExp(player, getExpToLevel(player.getLevel()) + exp);
    }

    public int getLevel(long totalExp) {
        long tempExp = 0;
        int level = 0;
        for (; tempExp <= totalExp; level++) {
            tempExp += getExpAtLevel(level);
        }
        return level - 1;
    }

    public int getLevel(Player player) {
        return getLevel(getTotalExp(player));
    }

    public void setLevel(Player player, int level) {
        long exp = getTotalExp(player) - getExpToLevel(player.getLevel());
        setTotalExp(player, getExpToLevel(level) + exp);
    }

    public long getTotalExp(Player player) {
        TotalExp exp = PlayerUtil.get(player);
        return exp.totalExp;
    }

    public void setTotalExp(Player player, long totalExp) {
        if (totalExp < 0) {
            throw new IllegalArgumentException("Experience is negative!");
        }
        TotalExp te = PlayerUtil.get(player);
        te.setTotalExp(totalExp);

        bukkitExp.setTotalExp(player, asyncTotalExperience(totalExp));
    }

    public int asyncTotalExperience(long exp) {
        int level = getLevel(exp);
        int exp2level = bukkitExp.getExpToLevel(level);
        if (exp2level < 0) return Integer.MAX_VALUE;
        int exp1 = getExp(exp) * bukkitExp.getExpAtLevel(level) / getExpAtLevel(level);
        return exp2level + exp1;
    }

    public long getExpToLevel(int level) {
        long exp = 0;
        for (int currentLevel = 0; currentLevel < level; currentLevel++) {
            exp += getExpAtLevel(currentLevel);
        }
        return exp;
    }

    public int getExpUntilNextLevel(long totalExp) {
        return getExpUntilNextLevel(getExp(totalExp), getLevel(totalExp));
    }

    public int getExpUntilNextLevel(int nowExp, int nextLevel) {
        return getExpAtLevel(nextLevel) - nowExp;
    }

    public int getExpUntilNextLevel(Player player) {
        return getExpUntilNextLevel(getTotalExp(player));
    }

    public abstract int getExpAtLevel(int level);

    public int getExpAtLevel(Player player) {
        return getExpAtLevel(getLevel(player));
    }
}
