package org.gooseapple.politiks;

import org.bukkit.plugin.java.JavaPlugin;
import org.gooseapple.politiks.config.ConfigManager;

import java.util.logging.Logger;

public final class Politiks extends JavaPlugin {
    public static Politiks plugin;
    public static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        logger = plugin.getLogger();

        //Load Config
        ConfigManager.loadConfig();

        //Register Listeners

        //Register Commands

        //Start Database
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Politiks getInstance() {
        return plugin;
    }
}
