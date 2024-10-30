package me.Cashtann.customStoneDrop;

import me.Cashtann.customStoneDrop.commands.CSDCommand;
import me.Cashtann.customStoneDrop.commands.MenuCommand;
import me.Cashtann.customStoneDrop.listeners.StoneBreakListener;
import me.Cashtann.customStoneDrop.utilities.ItemUtils;
import me.Cashtann.customStoneDrop.utilities.ColorUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class CustomStoneDrop extends JavaPlugin {

    private static CustomStoneDrop plugin;

    private static String commandSuccessOutputPrefix;
    private static String commandFailedOutputPrefix;

    private static List<ItemStack> configDropItems;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;
        System.out.println(configDropItems);

        this.saveDefaultConfig();
        loadItems();


        //getServer().getPluginManager().registerEvents(new StoneBreakListener(), this);
        getServer().getPluginManager().registerEvents(new StoneBreakListener(configDropItems, getConfig()), this);


        getCommand("customstonedrop").setExecutor(new CSDCommand());
        getCommand("csdmenu").setExecutor(new MenuCommand(this));

        System.out.println("============================= ");
        System.out.println("Combat Ranking System Loaded! ");
        System.out.println("============================= ");

        //getServer().broadcastMessage(ColorUtils.formatString(" \n\n       &dCustom Stone Drops powered by Cashtann\n\n"));

        commandSuccessOutputPrefix = getPlugin().getConfig().getString("commands.prefix-success");
        commandFailedOutputPrefix = getPlugin().getConfig().getString("commands.prefix-failed");

    }

    public List<ItemStack> getItems() {
        return configDropItems;
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
    }

    public static String getCommandFailedOutputPrefix() {
        return commandFailedOutputPrefix;
    }

    public static String getCommandSuccessOutputPrefix() {
        return commandSuccessOutputPrefix;
    }




}
