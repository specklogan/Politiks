package goose.politik.events;

import goose.politik.Politik;
import goose.politik.util.MongoDBHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.logging.Level;

public class JoinLeaveHandler {
    public static void playerJoin(PlayerJoinEvent event) {
        //check if it was the first time someone's joined

        Player player = event.getPlayer();

        if (!event.getPlayer().hasPlayedBefore()) {
            //first time playing set up some stuff
            Politik.mongoDB.addPlayer(event.getPlayer());
            final TextComponent component = Component.text("Welcome " + event.getPlayer().getName() + " to the server!").color(TextColor.color(255, 255, 85));
            event.joinMessage(component);

        } else {
            //normal join
            //add if people don't have it already
            Politik.getInstance().logger.log(Level.INFO, player.getName() + " has rejoined, they have a money value of: " + Politik.mongoDB.getPlayerMoney(player));
            final TextComponent component = Component.text("Welcome back " + event.getPlayer().getName() + " !").color(TextColor.color(255, 255, 85));
            event.joinMessage(component);
        }
    }

    public static void playerLeave(PlayerQuitEvent event) {
        //run some things on player leave
        Player player = event.getPlayer();
        Politik.mongoDB.setLogOutTime(player);
        final TextComponent component = Component.text(event.getPlayer().getName() + " has left, have a good day!").color(TextColor.color(255, 255, 85));
        event.quitMessage(component);
    }


}
