package io.github.windmourn.CustomLevelsExp.command;

import io.github.windmourn.CustomLevelsExp.Main;
import io.github.windmourn.CustomLevelsExp.util.CustomExpUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pw.yumc.YumCore.commands.CommandSub;
import pw.yumc.YumCore.commands.annotation.Cmd;
import pw.yumc.YumCore.commands.annotation.Help;
import pw.yumc.YumCore.commands.interfaces.Executor;

public class MainCommand implements Executor {

    private Main main = Main.INSTANCE;

    public MainCommand() {
        new CommandSub("customlevelsexp", this);
    }

    @Cmd(aliases = "exp", executor = Cmd.Executor.PLAYER)
    @Help("Get exp in this level.")
    public void exp(CommandSender sender) {
        Player player = (Player) sender;
        sender.sendMessage(String.format("You have %s exp upon the level %s.", CustomExpUtil.getExp(player), CustomExpUtil.getLevel(player)));
    }

    @Cmd(aliases = "need", executor = Cmd.Executor.PLAYER)
    @Help("Get exp needs to next level.")
    public void need(CommandSender sender) {
        Player player = (Player) sender;
        sender.sendMessage(String.format("You need %s exp to next level.", CustomExpUtil.getExpUntilNextLevel(player)));
    }

    @Cmd(aliases = "totalexp", executor = Cmd.Executor.PLAYER)
    @Help("Get Total Exp.")
    public void totalExp(CommandSender sender) {
        Player player = (Player) sender;
        sender.sendMessage(String.format("Your total Exp is %s", CustomExpUtil.getTotalExperience(player)));
    }

    @Cmd(aliases = "give", permission = "customlevelsexp.admin", minimumArguments = 1)
    @Help(value = "Give Player Exp (Player Must Be Online).", possibleArguments = "<Exp/Level> [PlayerName]")
    public void give(CommandSender sender, String exp, String playername) {
        if (playername == null || playername.isEmpty()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                giveExpOrLevel(player, exp);
            }
        } else {
            Player player = Bukkit.getPlayerExact(playername);
            if (player != null) {
                giveExpOrLevel(player, exp);
            }
        }
    }

    @Cmd(aliases = "set", permission = "customlevelsexp.admin", minimumArguments = 1)
    @Help(value = "Set Player Exp (Player Must Be Online).", possibleArguments = "<Exp/Level> [PlayerName]")
    public void set(CommandSender sender, String exp, String playername) {
        if (playername == null || playername.isEmpty()) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                setExpOrLevel(player, exp);
            }
        } else {
            Player player = Bukkit.getPlayerExact(playername);
            if (player != null) {
                setExpOrLevel(player, exp);
            }
        }
    }

    @Cmd(aliases = "reload", permission = "customlevelsexp.admin")
    @Help("Reload Config.")
    public void reload(CommandSender sender) {
        main.reloadConfig();
        sender.sendMessage(ChatColor.GREEN + "Reload Completed.");
    }

    private void giveExpOrLevel(Player player, String exp) {
        if (exp.toLowerCase().endsWith("l")) {
            int level = Integer.parseInt(exp.substring(0, exp.length() - 1));
            CustomExpUtil.setLevel(player, CustomExpUtil.getLevel(player) + level);
        } else {
            CustomExpUtil.setTotalExperience(player, CustomExpUtil.getTotalExperience(player) + Integer.parseInt(exp));
        }
    }

    private void setExpOrLevel(Player player, String exp) {
        if (exp.toLowerCase().endsWith("l")) {
            int level = Integer.parseInt(exp.substring(0, exp.length() - 1));
            CustomExpUtil.setLevel(player, level);
        } else {
            CustomExpUtil.setTotalExperience(player, Integer.parseInt(exp));
        }
    }

}
