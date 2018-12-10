package io.github.windmourn.CustomLevelsExp.hook;

import me.clip.placeholderapi.PlaceholderAPI;

public class PlaceholderAPIHook {

    public static void hook() {
        PlaceholderAPI.registerExpansion(new ExpansionHook());
    }

}
