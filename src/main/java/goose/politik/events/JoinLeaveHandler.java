package goose.politik.events;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;
import java.util.logging.Level;

public class JoinLeaveHandler {
    public static void playerJoin(PlayerJoinEvent event) {
        //Check if they are a valid PolitikPlayer ( have played before )
        if (!event.getPlayer().hasPlayedBefore()) {
            //run some stuff at first join
            new PolitikPlayer(event.getPlayer());
            event.joinMessage(Politik.eventMessage("Welcome " + event.getPlayer().getName() + " to the server"));
        } else {
            //now we have to figure out which one of the player objects they are
            for (UUID key : PolitikPlayer.playerList.keySet()) {
                if (PolitikPlayer.playerList.get(key).getUUID().equals(event.getPlayer().getUniqueId())) {
                    PolitikPlayer.playerList.get(key).setPlayer(event.getPlayer());
                }
            }
        }
    }

    public static void playerLeave(PlayerQuitEvent event) {
        //run some things on player leave
        Player player = event.getPlayer();
        try {
            PolitikPlayer.getPolitikPlayer(player).leave();
            event.quitMessage(Politik.eventMessage(player.getName() + " has left, thanks for playing!" ));
        } catch (Exception e) {
            Politik.logger.log(Level.WARNING, "Error in removing player, player not in the list, probably caused by reloading the server.");
        }
    }
}
