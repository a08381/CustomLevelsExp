package io.github.windmourn.CustomLevelsExp.util;

import io.github.windmourn.CustomLevelsExp.Main;
import io.github.windmourn.CustomLevelsExp.entry.TotalExp;
import org.bukkit.entity.Player;

import javax.script.ScriptException;
import java.util.HashMap;
import java.util.Map;

public class CustomExpUtil {

    public static Map<Integer, Integer> map = new HashMap<>();

    private static Main main = Main.INSTANCE;

    /**
     * 获取当前等级经验
     *
     * @param player
     * @return
     */
    public static int getExp(Player player) {
        return getExp(getTotalExperience(player));
    }

    /**
     * 获取当前等级经验
     *
     * @param exp
     * @return
     */
    public static int getExp(int exp) {
        return exp - getExpToLevel(getLevel(exp));
    }

    /**
     * 此经验值等同于多少级
     *
     * @param player
     * @return
     */
    public static int getLevel(Player player) {
        return getLevel(getTotalExperience(player));
    }

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

    public static void setLevel(Player player, int level) {
        int totalExp = getTotalExperience(player);
        int exp = getExp(totalExp);
        setTotalExperience(player, getExpToLevel(level) + exp);
    }

    /**
     * 玩家总经验
     *
     * @param player
     * @return
     */
    public static int getTotalExperience(Player player) {
        TotalExp exp = PlayerUtil.get(player);
        return exp.totalExp;
    }

    public static void setTotalExperience(Player player, int exp) {
        if (exp < 0) {
            throw new IllegalArgumentException("Experience is negative!");
        }
        TotalExp te = PlayerUtil.get(player);
        te.setTotalExp(exp);

        ExpUtil.setTotalExperience(player, asyncTotalExperience(exp));
    }

    public static int asyncTotalExperience(int exp) {
        int level = getLevel(exp);
        int exp2level = ExpUtil.getExpToLevel(level);
        int exp1 = getExp(exp) * ExpUtil.getExpAtLevel(level) / getExpAtLevel(level);
        return exp2level + exp1;
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
        int exp = getExp(getTotalExperience(player));
        return getExpUntilNextLevel(exp, getLevel(exp));
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
        if (map.containsKey(level)) return map.get(level);
        int exp = 0;
        try {
            Double d = (Double) main.getFormula().invokeFunction("getExpAtLevel", level);
            exp = d.intValue();
            if (exp != 0) map.put(level, exp);
        } catch (NoSuchMethodException | ScriptException e) {
            e.printStackTrace();
        }
        return exp;
    }

    /**
     * 到达此等级需要多少经验(1级的经验)
     *
     * @param player
     * @return
     */
    public static int getExpAtLevel(Player player) {
        return getExpAtLevel(player);
    }

}
