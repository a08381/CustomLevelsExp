package io.github.windmourn.CustomLevelsExp.listener;

import io.github.windmourn.CustomLevelsExp.util.CustomExpUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerExpChangeEvent;

public class ExpChange implements Listener {

    @EventHandler
    public void onExpChange(PlayerExpChangeEvent event) {
        int exp = event.getAmount();
        Player player = event.getPlayer();
        CustomExpUtil.setTotalExperience(player, CustomExpUtil.getTotalExperience(player) + exp);
        event.setAmount(0);
    }

}
