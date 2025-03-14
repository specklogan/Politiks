package org.gooseapple.politiks.event.implementation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.IAccountTable;
import org.gooseapple.politiks.database.IPlayerTable;

public class ServerListener implements Listener {

    public ServerListener() {
        Politiks.getInstance().getServer().getPluginManager().registerEvents(this, Politiks.getInstance());
    }

    @EventHandler
    public void WorldSaveEvent(WorldSaveEvent event) {
        DatabaseManager.Save();
    }


    public void HandleShutdown() {
        DatabaseManager.Save();
    }
}
