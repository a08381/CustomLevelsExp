package io.github.windmourn.CustomLevelsExp.listener;

import io.github.windmourn.CustomLevelsExp.Main;
import io.github.windmourn.CustomLevelsExp.task.DataCreateTask;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerLogin implements Listener {

    private Main main = Main.INSTANCE;

    @EventHandler
    public void onLogin(PlayerJoinEvent event) {
        new DataCreateTask(event.getPlayer().getName()).runTaskLater(main, 40);
    }

}
