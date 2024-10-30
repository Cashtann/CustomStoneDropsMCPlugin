package me.Cashtann.customStoneDrop.commands;

import me.Cashtann.customStoneDrop.CustomStoneDrop;
import me.Cashtann.customStoneDrop.utilities.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;


public class MenuCommand implements CommandExecutor, Listener {
    private final CustomStoneDrop plugin;

    public MenuCommand(CustomStoneDrop plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        openMenu(player);
        return true;
    }

    private void openMenu(Player player) {
        Inventory menu = Bukkit.createInventory(null, 27, ChatColor.BLUE + "Items in Config");

        // Load items from config and add to inventory
        for (String key : plugin.getConfig().getConfigurationSection("items").getKeys(false)) {
            String itemMaterial = plugin.getConfig().getString("items." + key + ".item");
            double chance = plugin.getConfig().getDouble("items." + key + ".chance", 0.0);
            boolean glint = plugin.getConfig().getBoolean("items." + key + ".glint", false);
            Material material = Material.matchMaterial(itemMaterial);
            if (material != null) {
                ItemStack item = new ItemStack(material);

                // Set item meta
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ColorUtils.formatString(plugin.getConfig().getString("items." + key + ".name", key)));

                    List<String> lore = new ArrayList<>();
                    for (int i = 1; i <= 5; i++) { // Assuming you want to include up to 5 lore lines
                        String loreLine = plugin.getConfig().getString("items." + key + ".lore" + i, "");
                        if (!loreLine.isEmpty()) {
                            lore.add(ColorUtils.formatString(loreLine));
                        }
                    }
                    lore.add(ChatColor.YELLOW + "Chance: " + ChatColor.GREEN + chance * 100 + "%");
                    meta.setLore(lore);
                    if (glint) {
                        //meta.addEnchant(org.bukkit.enchantments.Enchantment.)
                        meta.addEnchant(Enchantment.LURE, 1, true);
                        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                    }
                    item.setItemMeta(meta);
                }

                menu.addItem(item);
            }
        }

        player.openInventory(menu);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equals(ChatColor.BLUE + "Items in Config")) {
            event.setCancelled(true); // Prevent items from being moved
        }
    }
}
