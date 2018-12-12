package io.github.windmourn.CustomLevelsExp.listener;

import io.github.windmourn.CustomLevelsExp.exp.CustomExp;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantItem implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEnchantItem(EnchantItemEvent event) {
        Player player = event.getEnchanter();
        if (player != null) {
            int levelCost = event.whichButton() + 1;
            int level = CustomExp.getInstance().getLevel(player);
            CustomExp.getInstance().setLevel(player, level - levelCost);
        }
    }

}
