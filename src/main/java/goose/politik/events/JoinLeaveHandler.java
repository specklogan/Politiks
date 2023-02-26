package goose.politik.events;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.logging.Level;

public class JoinLeaveHandler {
    public static void playerJoin(PlayerJoinEvent event) {
        //check if it was the first time someone's joined
        new PolitikPlayer(event.getPlayer());
        event.joinMessage(Politik.eventMessage("Welcome " + event.getPlayer().getName() + " to the server"));
    }

    public static void playerLeave(PlayerQuitEvent event) {
        //run some things on player leave
        Player player = event.getPlayer();
        try {
            PolitikPlayer.getPolitikPlayer(player).leave();
        } catch (Exception e) {
            Politik.getInstance().logger.log(Level.WARNING, "Error in removing player, player not in the list, probably caused by reloading the server.");
        }
    }
}
