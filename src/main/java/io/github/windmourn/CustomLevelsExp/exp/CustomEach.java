package io.github.windmourn.CustomLevelsExp.exp;

import io.github.windmourn.CustomLevelsExp.Main;

import javax.script.ScriptException;

public class CustomEach extends CustomExp {

    private Main main;

    public CustomEach() {
        super();
        main = Main.INSTANCE;
    }

    @Override
    public int getExpAtLevel(int level) {
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

}
