package io.github.windmourn.CustomLevelsExp.util;

import org.bukkit.entity.Player;

public class ExpUtil {

    /**
     * 此经验值等同于多少级
     *
     * @param exp
     * @return
     */
    public static int getLevel(int exp) {
        int level = 0;
        for (int i = getExpAtLevel(level); i < exp; ) {
            exp -= i;
            level++;
            i = getExpAtLevel(level);
        }
        return level;
    }

    /**
     * 玩家总经验
     *
     * @param player
     * @return
     */
    public static int getTotalExperience(Player player) {
        int exp = Math.round(getExpToLevel(player) + player.getExp());
        return exp;
    }

    public static void setTotalExperience(Player player, int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Experience is negative!");
        }
        player.setExp(0.0F);
        player.setLevel(0);
        player.setTotalExperience(0);
        int amount = exp;
        while (amount > 0) {
            int expToLevel = getExpAtLevel(player);
            amount -= expToLevel;
            if (amount >= 0) {
                player.giveExp(expToLevel);
            } else {
                amount += expToLevel;
                player.giveExp(amount);
                amount = 0;
            }
        }
    }

    /**
     * 到达此等级总共需要多少经验
     *
     * @param player
     * @return
     */
    public static int getExpToLevel(Player player) {
        return getExpToLevel(player.getLevel());
    }

    /**
     * 到达此等级总共需要多少经验
     *
     * @param level
     * @return
     */
    public static int getExpToLevel(int level) {
        int currentLevel = 0;
        int exp = 0;
        while (currentLevel < level) {
            exp += getExpAtLevel(currentLevel);
            currentLevel++;
        }
        if (exp < 0) {
            exp = 2147483647;
        }
        return exp;
    }

    /**
     * 到下一等级还需多少经验
     *
     * @param player
     * @return
     */
    public static int getExpUntilNextLevel(Player player) {
        int exp = (int) player.getExp();
        return getExpUntilNextLevel(exp, player.getLevel());
    }

    /**
     * 到下一等级还需多少经验
     *
     * @param nowExp
     * @param nextLevel
     * @return
     */
    public static int getExpUntilNextLevel(int nowExp, int nextLevel) {
        return getExpAtLevel(nextLevel) - nowExp;
    }

    /**
     * 到达此等级需要多少经验(1级的经验)
     *
     * @param level
     * @return
     */
    public static int getExpAtLevel(int level) {
        if (level <= 15) {
            return 2 * level + 7;
        }
        if ((level >= 16) && (level <= 30)) {
            return 5 * level - 38;
        }
        return 9 * level - 158;
    }

    /**
     * 到达此等级需要多少经验(1级的经验)
     *
     * @param player
     * @return
     */
    public static int getExpAtLevel(Player player) {
        return getExpAtLevel(player.getLevel());
    }

}
