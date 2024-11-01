package me.Cashtann.customStoneDrop;

import me.Cashtann.customStoneDrop.commands.CSDCommand;
import me.Cashtann.customStoneDrop.commands.ItemGlintCommand;
import me.Cashtann.customStoneDrop.commands.MenuCommand;
import me.Cashtann.customStoneDrop.listeners.BlockBreakListener;
import me.Cashtann.customStoneDrop.utilities.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CustomStoneDrop extends JavaPlugin {

    private static CustomStoneDrop plugin;

    private static String commandSuccessOutputPrefix;
    private static String commandFailedOutputPrefix;

    private static List<ItemStack> configDropItems;

    private Set<Material> mineableMaterials = new HashSet<>();

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;
        System.out.println(configDropItems);

        this.saveDefaultConfig();
        loadMineableMaterials();
        loadItems();


        //getServer().getPluginManager().registerEvents(new StoneBreakListener(), this);
        getServer().getPluginManager().registerEvents(new BlockBreakListener(configDropItems, getConfig(), this), this);


        getCommand("customstonedrop").setExecutor(new CSDCommand());
        getCommand("csdmenu").setExecutor(new MenuCommand(this));
        getCommand("itemglint").setExecutor(new ItemGlintCommand());

        System.out.println("============================= ");
        System.out.println("Custom Stone Drop Loaded! ");
        System.out.println("============================= ");

        //getServer().broadcastMessage(ColorUtils.formatString(" \n\n       &dCustom Stone Drops powered by Cashtann\n\n"));

        commandSuccessOutputPrefix = getPlugin().getConfig().getString("commands.prefix-success");
        commandFailedOutputPrefix = getPlugin().getConfig().getString("commands.prefix-failed");

    }

    public List<ItemStack> getItems() {
        return configDropItems;
    }

    public Set<Material> getMineableMaterials() {
        return mineableMaterials;
    }

    private void loadMineableMaterials() {
        mineableMaterials.clear();
        FileConfiguration config = getConfig();
        List<String> materials = config.getStringList("mineable");

        for (String materialName : materials) {
            try {
                //Bukkit.broadcastMessage("Material " + materialName + " found");
                Material material = Material.valueOf(materialName);
                mineableMaterials.add(material);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Invalid material in mineable list: " + materialName);
            }
        }
    }

    public String getItemKey(ItemStack item) {
        for (String key : getConfig().getConfigurationSection("items").getKeys(false)) {
            if (getConfig().getString("items." + key + ".item").equals(item.getType().name())) {
                return key;
            }
        }
        return null;
    }

    public static CustomStoneDrop getPlugin() {
        return plugin;
    }

    public void loadItems() {
        // Load items from config
        configDropItems = ItemUtils.loadConfiguredItems(getConfig());
    }

    public void reloadPluginConfig() {
        reloadConfig();   // Reloads the config file from disk
        loadItems();      // Reloads the items list based on new config
        loadMineableMaterials();
    }

    public static String getCommandFailedOutputPrefix() {
        return commandFailedOutputPrefix;
    }

    public static String getCommandSuccessOutputPrefix() {
        return commandSuccessOutputPrefix;
    }




}
