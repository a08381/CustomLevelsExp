package io.github.windmourn.CustomLevelsExp.task;

import io.github.windmourn.CustomLevelsExp.util.PlayerUtil;
import org.bukkit.scheduler.BukkitRunnable;

public class DataSaveTask extends BukkitRunnable {

    @Override
    public void run() {
        PlayerUtil.save();
    }

}
