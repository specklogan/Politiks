package org.gooseapple.politiks;

import org.bukkit.plugin.java.JavaPlugin;
import org.gooseapple.politiks.command.CommandManager;
import org.gooseapple.politiks.config.ConfigManager;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.event.EventManager;

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

        //Start Database
        DatabaseManager.loadDatabase();

        //Register Listeners
        EventManager.registerListeners();

        //Register Commands
        CommandManager.registerCommands();
    }

    @Override
    public void onDisable() {
        EventManager.handleShutdown();
    }

    public static Politiks getInstance() {
        return plugin;
    }
}
