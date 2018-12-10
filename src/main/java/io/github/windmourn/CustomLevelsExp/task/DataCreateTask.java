package io.github.windmourn.CustomLevelsExp.task;

import io.github.windmourn.CustomLevelsExp.util.CustomExpUtil;
import io.github.windmourn.CustomLevelsExp.util.ExpUtil;
import io.github.windmourn.CustomLevelsExp.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class DataCreateTask extends BukkitRunnable {

    private String playername;

    public DataCreateTask(String playername) {
        this.playername = playername;
    }

    @Override
    public void run() {
        Player player = Bukkit.getPlayerExact(playername);
        if (player != null) {
            PlayerUtil.create(player);
            int exp = CustomExpUtil.getTotalExperience(player);
            int newExp = CustomExpUtil.asyncTotalExperience(exp);
            ExpUtil.setTotalExperience(player, newExp);
        }
    }
}
