package io.github.windmourn.CustomLevelsExp.exp;

public class Exp_1_7 extends BukkitExp {

    @Override
    public int getExpAtLevel(int level) {
        if (level >= 30) {
            return 62 + (level - 30) * 7;
        } else if (level >= 15) {
            return 17 + (level - 15) * 3;
        } else {
            return 17;
        }
    }

}
