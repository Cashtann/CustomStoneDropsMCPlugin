package me.Cashtann.customStoneDrop.listeners;

import me.Cashtann.customStoneDrop.CustomStoneDrop;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Random;


public class BlockBreakListener implements Listener {

    private final List<ItemStack> items;
    private final FileConfiguration config;
    private final Random random;
    private final CustomStoneDrop plugin;

    public BlockBreakListener(List<ItemStack> items, FileConfiguration config, CustomStoneDrop plugin) {
        this.items = items;
        this.config = config;
        this.random = new Random();
        this.plugin = plugin;
    }


    @EventHandler
    public void onPlayerBreakStone(BlockBreakEvent event) {

        Material brokenBlock = event.getBlock().getType();

        if (!plugin.getMineableMaterials().contains(brokenBlock)) return;

        Player player = event.getPlayer();
        boolean enabled = CustomStoneDrop.getPlugin().getConfig().getBoolean("enabled");
        if (enabled) {
            if (player.hasPermission("csd.drop") && player.getGameMode() == GameMode.SURVIVAL) {
                for (ItemStack item : items) {
                    String itemKey = findItemKey(item);
                    if (itemKey == null) continue;

                    double chance = config.getDouble("items." + itemKey + ".chance", 0.0);
                    //PlayerMessage.sendFormattedMessage(player, String.valueOf(random.nextDouble()));
                    if (random.nextDouble() <= chance) {
                        event.getPlayer().getInventory().addItem(item);
                    }
                }
            }
        }
    }

    // Helper function to get item key in the config based on ItemStack properties
    private String findItemKey(ItemStack item) {
        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            String materialName = config.getString("items." + key + ".item");
            Material material = Material.getMaterial(materialName);
            if (material != null && material == item.getType()) {
                return key;
            }
        }
        return null;
    }
}
