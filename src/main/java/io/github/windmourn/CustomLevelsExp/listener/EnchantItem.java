package io.github.windmourn.CustomLevelsExp.listener;

import io.github.windmourn.CustomLevelsExp.util.CustomExpUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantItem implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        if (player != null) {
            int levelCost = event.getExpLevelCost();
            int level = CustomExpUtil.getLevel(player);
            CustomExpUtil.setLevel(player, level - levelCost);
            event.setExpLevelCost(0);
        }
    }

}
