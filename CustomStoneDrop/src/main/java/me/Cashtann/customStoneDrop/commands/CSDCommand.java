package me.Cashtann.customStoneDrop.commands;

import me.Cashtann.customStoneDrop.CustomStoneDrop;
import me.Cashtann.customStoneDrop.utilities.PlayerMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CSDCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {


        //sendCommandOutput(true, player, "Reloading config...");
        long timeElapsed = System.currentTimeMillis();

        CustomStoneDrop.getPlugin().reloadPluginConfig();

        timeElapsed = System.currentTimeMillis() - timeElapsed;
        String message = "Configuration reloaded successfully in " + String.valueOf(timeElapsed) + " ms";

        if (commandSender instanceof Player player) {
            PlayerMessage.sendCommandOutput(true, player, message);
        } else {
            CustomStoneDrop.getPlugin().getLogger().info(message);
        }
        return true;
    }
}
