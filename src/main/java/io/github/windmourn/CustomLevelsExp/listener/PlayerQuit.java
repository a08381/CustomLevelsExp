package io.github.windmourn.CustomLevelsExp.listener;

import io.github.windmourn.CustomLevelsExp.Main;
import io.github.windmourn.CustomLevelsExp.util.PlayerUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    private Main main = Main.INSTANCE;

    @EventHandler
    public void onLogin(PlayerQuitEvent event) {
        PlayerUtil.saveAndClose(event.getPlayer().getName());
    }

}
