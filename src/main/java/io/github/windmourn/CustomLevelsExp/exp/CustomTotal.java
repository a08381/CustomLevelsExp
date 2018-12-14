package io.github.windmourn.CustomLevelsExp.exp;

import io.github.windmourn.CustomLevelsExp.Main;

import javax.script.ScriptException;

public class CustomTotal extends CustomExp {

    private Main main;

    public CustomTotal() {
        super();
        main = Main.INSTANCE;
    }

    @Override
    public int getExpAtLevel(int level) {
        if (map.containsKey(level)) return map.get(level);
        int exp1 = 0;
        int exp2 = 0;
        int level1 = level - 1;
        try {
            Double d = (Double) main.getFormula().invokeFunction("getExpAtLevel", level);
            exp1 = d.intValue();
            if (level >= 0) {
                d = (Double) main.getFormula().invokeFunction("getExpAtLevel", level1);
                exp2 = d.intValue();
            }
            exp1 = exp1 - exp2;
            if (exp1 > 0) map.put(level, exp1);
        } catch (NoSuchMethodException | ScriptException e) {
            e.printStackTrace();
        }
        return exp1;
    }

}
