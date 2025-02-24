package org.gooseapple.politiks.event.implementation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.IPlayerTable;
import org.gooseapple.politiks.util.Constants;

public class PlayerListener implements Listener {
    IPlayerTable playerTable;

    public PlayerListener() {
        Politiks.getInstance().getServer().getPluginManager().registerEvents(this, Politiks.getInstance());
        playerTable = DatabaseManager.getDatabase().getPlayerTable();
    }

    @EventHandler
    public void PlayerJoinEvent(PlayerJoinEvent event) {
        boolean exists = playerTable.PlayerExists(event.getPlayer());

        if (exists) {
            playerTable.SetPlayerOnline(event.getPlayer());
            event.joinMessage(Constants.DetailMessage(event.getPlayer().getName() + " has joined the server."));
        } else {
            PolitikPlayer player = playerTable.CreatePlayer(event.getPlayer());
            event.joinMessage(Constants.DetailMessage("Welcome " + player.getDisplayName() + " to the server!"));
        }
    }

    @EventHandler
    public void PlayerLeaveEvent(PlayerQuitEvent event) {
        event.quitMessage(Constants.DetailMessage(event.getPlayer().getName() + " has left the server."));
        PolitikPlayer player = playerTable.GetPlayer(event.getPlayer());
        if (player != null) {
            playerTable.SavePlayer(player);
        }
    }
}
