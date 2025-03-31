package org.gooseapple.politiks.event.implementation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.core.land.ILand;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.ILandTable;
import org.gooseapple.politiks.database.IPlayerTable;
import org.gooseapple.politiks.util.Constants;

/**
 * Tracks player movement, and interaction events pertaining to land
 */
public class LandListener implements Listener {
    private IPlayerTable players;
    private ILandTable lands;

    public LandListener() {
        players = DatabaseManager.getDatabase().getPlayerTable();
        lands = DatabaseManager.getDatabase().getLandTable();
        Politiks.getInstance().getServer().getPluginManager().registerEvents(this, Politiks.getInstance());
    }

    @EventHandler
    public void PlayerMovement(PlayerMoveEvent event) {
        if (!event.hasChangedBlock()) {
            return;
        }

        ILand land = lands.GetLandFromLocation(event.getTo());
        PolitikPlayer player = players.GetPlayer(event.getPlayer());

        if (land == null) {
            if (player.getLastEnteredLand() != null) {
                event.getPlayer().sendActionBar(Constants.GreenMessage("Entering the wilderness."));
                player.setLastEnteredLand(null);
            }
            return;
        }

        if (player.getLastEnteredLand() == null) {
            player.setLastEnteredLand(land);
            event.getPlayer().sendActionBar(Constants.EventMessage("Entering Town " + land.GetTownOwner().getTownName()));
        }
    }
}
