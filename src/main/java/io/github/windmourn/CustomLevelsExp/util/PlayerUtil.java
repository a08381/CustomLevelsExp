package io.github.windmourn.CustomLevelsExp.util;

import io.github.windmourn.CustomLevelsExp.Main;
import io.github.windmourn.CustomLevelsExp.entry.TotalExp;
import io.github.windmourn.CustomLevelsExp.exp.CustomExp;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.Map;

public class PlayerUtil {

    private static final Map<String, TotalExp> map = new LinkedHashMap<>();

    private static Main main = Main.INSTANCE;

    public static TotalExp create(Player player) {
        synchronized (map) {
            TotalExp exp = main.find(TotalExp.class)
                    .where("playername = :playername")
                    .setParameter("playername", player.getName())
                    .findUnique();
            if (exp == null) {
                exp = main.createEntityBean(TotalExp.class);
                exp.setPlayerName(player.getName());
                exp.setTotalExp(CustomExp.getBukkitExp().getTotalExp(player));
                main.save(exp);
            }
            map.put(player.getName(), exp);
            return exp;
        }
    }

    public static TotalExp create(String playername) {
        synchronized (map) {
            TotalExp exp = main.find(TotalExp.class)
                    .where("playername = :playername")
                    .setParameter("playername", playername)
                    .findUnique();
            if (exp == null) {
                exp = main.createEntityBean(TotalExp.class);
                exp.setPlayerName(playername);
                Player player = Bukkit.getPlayerExact(playername);
                exp.setTotalExp(player == null ? 0 : CustomExp.getBukkitExp().getTotalExp(player));
                main.save(exp);
            }
            map.put(playername, exp);
            return exp;
        }
    }

    public static TotalExp get(Player player) {
        synchronized (map) {
            return map.containsKey(player.getName()) ? map.get(player.getName()) : create(player);
        }
    }

    public static TotalExp get(String playername) {
        synchronized (map) {
            return map.containsKey(playername) ? map.get(playername) : create(playername);
        }
    }

    public static int save() {
        synchronized (map) {
            return main.save(map.values());
        }
    }

    public static void saveAndClose(String playername) {
        synchronized (map) {
            TotalExp exp = map.get(playername);
            if (exp == null) return;
            map.remove(playername);
            main.save(exp);
        }
    }

}
