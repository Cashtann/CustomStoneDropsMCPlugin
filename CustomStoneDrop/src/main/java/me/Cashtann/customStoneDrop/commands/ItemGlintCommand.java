package me.Cashtann.customStoneDrop.commands;

import me.Cashtann.customStoneDrop.utilities.ColorUtils;
import me.Cashtann.customStoneDrop.utilities.PlayerMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemGlintCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (commandSender instanceof Player player) {
            ItemStack itemInHand = player.getInventory().getItemInMainHand();
            if (itemInHand == null || itemInHand.getType().isAir()) {
                PlayerMessage.sendCommandOutput(false, player, "You are not holding an item!");
                return true;
            }

            itemInHand.addUnsafeEnchantment(Enchantment.LURE, 1);

            // Hide enchantments in the item lore
            ItemMeta meta = itemInHand.getItemMeta();
            if (meta != null) {
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemInHand.setItemMeta(meta);
            }

            PlayerMessage.sendCommandOutput(true, player, "Applied glint effect to the item!");
        } else {
            commandSender.sendMessage("This command can only be used by player");
        }


        return true;
    }
}
