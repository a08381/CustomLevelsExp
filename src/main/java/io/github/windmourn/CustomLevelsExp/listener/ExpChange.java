package io.github.windmourn.CustomLevelsExp.listener;

import io.github.windmourn.CustomLevelsExp.exp.CustomExp;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class ExpChange implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onExpChange(PlayerExpChangeEvent event) {
        int exp = event.getAmount();
        Player player = event.getPlayer();
        CustomExp.getInstance().setTotalExp(player, CustomExp.getInstance().getTotalExp(player) + exp);
        event.setAmount(0);
    }

}
