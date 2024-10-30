package me.Cashtann.customStoneDrop.utilities;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ItemUtils {

    public static List<ItemStack> loadConfiguredItems(FileConfiguration config) {
        List<ItemStack> items = new ArrayList<>();
        ConfigurationSection itemsSection = config.getConfigurationSection("items");
        if (itemsSection == null) return items;

        for (String key : itemsSection.getKeys(false)) {
            ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
            if (itemSection == null) continue;

            String materialName = itemSection.getString("item");
            Material material = Material.getMaterial(materialName);
            if (material == null) continue;

            ItemStack itemStack = new ItemStack(material);
            ItemMeta meta = itemStack.getItemMeta();
            if (meta == null) continue;

            String name = itemSection.getString("name", "");
            meta.setDisplayName(ColorUtils.formatString(name));

            boolean glint = itemSection.getBoolean("glint", false);
            meta.setDisplayName(ColorUtils.formatString(name));

            List<String> lore = new ArrayList<>();
            for (int i = 1; i <= 5; i++) {
                String loreLine = itemSection.getString("lore" + i, "");
                if (!loreLine.isEmpty()) {
                    lore.add(ColorUtils.formatString(loreLine));
                }
            }

            if (glint) {
                //meta.addEnchant(org.bukkit.enchantments.Enchantment.)
                meta.addEnchant(Enchantment.LURE, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            meta.setLore(lore);
            itemStack.setItemMeta(meta);


            items.add(itemStack);
        }

        return items;
    }
}
