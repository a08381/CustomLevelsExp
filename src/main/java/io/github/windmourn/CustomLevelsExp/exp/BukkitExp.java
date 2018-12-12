package io.github.windmourn.CustomLevelsExp.exp;

import org.bukkit.entity.Player;

public abstract class BukkitExp extends IExp {

    @Override
    public void setExp(Player player, int exp) {
        setTotalExp(player, getExpToLevel(player.getLevel()) + exp);
    }

    @Override
    public void setLevel(Player player, int level) {
        int exp = getTotalExp(player) - getExpToLevel(player.getLevel());
        setTotalExp(player, getExpToLevel(level) + exp);
    }

    @Override
    public int getTotalExp(Player player) {
        return Math.round(getExpToLevel(player.getLevel()) + player.getExp());
    }

    @Override
    public void setTotalExp(Player player, int totalExp) {
        if (totalExp < 0) {
            throw new IllegalArgumentException("Experience is negative!");
        }
        player.setExp(0.0F);
        player.setLevel(0);
        player.setTotalExperience(0);

        int exp = getExp(totalExp);
        int level = getLevel(totalExp);
        player.giveExpLevels(level);
        player.giveExp(exp);
    }
}
