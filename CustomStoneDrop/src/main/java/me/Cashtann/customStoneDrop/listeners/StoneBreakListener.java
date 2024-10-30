package me.Cashtann.customStoneDrop.listeners;

import me.Cashtann.customStoneDrop.CustomStoneDrop;
import me.Cashtann.customStoneDrop.utilities.PlayerMessage;
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


public class StoneBreakListener implements Listener {

    private final List<ItemStack> items;
    private final FileConfiguration config;
    private final Random random;

    public StoneBreakListener(List<ItemStack> items, FileConfiguration config) {
        this.items = items;
        this.config = config;
        this.random = new Random();
    }


    @EventHandler
    public void onPlayerBreakStone(BlockBreakEvent event) {
        Player player = event.getPlayer();
        boolean enabled = CustomStoneDrop.getPlugin().getConfig().getBoolean("enabled");
        if (enabled) {
            if (player.hasPermission("csd.drop") && player.getGameMode() == GameMode.SURVIVAL && event.getBlock().getBlockData().getMaterial() == Material.STONE) {
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
