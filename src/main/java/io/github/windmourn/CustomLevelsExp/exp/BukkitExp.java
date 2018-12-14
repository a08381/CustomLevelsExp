package io.github.windmourn.CustomLevelsExp.exp;

import org.bukkit.entity.Player;

public abstract class BukkitExp {

    public int getExp(int totalExp) {
        return totalExp - getExpToLevel(getLevel(totalExp));
    }

    public int getExp(Player player) {
        return getExp(getTotalExp(player));
    }

    public void setExp(Player player, int exp) {
        setTotalExp(player, getExpToLevel(player.getLevel()) + exp);
    }

    public int getLevel(int totalExp) {
        int tempExp = 0;
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
        int exp = getTotalExp(player) - getExpToLevel(player.getLevel());
        setTotalExp(player, getExpToLevel(level) + exp);
    }

    public int getTotalExp(Player player) {
        return Math.round(getExpToLevel(player.getLevel()) + player.getExp());
    }

    public void setTotalExp(Player player, int totalExp) {
        if (totalExp < 0) {
            throw new IllegalArgumentException("Experience is negative!");
        }
        player.setExp(0.0F);
        player.setLevel(0);
        player.setTotalExperience(0);

        int exp = getExp(totalExp);
        int level = getLevel(totalExp);
        player.giveExpLevels(level);
        player.giveExp(exp);
    }

    public int getExpToLevel(int level) {
        int exp = 0;
        for (int currentLevel = 0; currentLevel < level; currentLevel++) {
            exp += getExpAtLevel(currentLevel);
        }
        return exp;
    }

    public int getExpUntilNextLevel(int totalExp) {
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
