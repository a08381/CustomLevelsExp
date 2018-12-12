package io.github.windmourn.CustomLevelsExp.exp;

public class Exp_1_8 extends BukkitExp {

    @Override
    public int getExpAtLevel(int level) {
        if (level <= 15) {
            return 2 * level + 7;
        }
        if (level <= 30) {
            return 5 * level - 38;
        }
        return 9 * level - 158;
    }
}
