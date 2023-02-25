package goose.politik.events;

import goose.politik.Politik;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveHandler {
    public static void playerJoin(PlayerJoinEvent event) {
        //check if it was the first time someone's joined

        Player player = event.getPlayer();

        if (!event.getPlayer().hasPlayedBefore()) {
            //first time playing set up some stuff
            Politik.mongoDB.addPlayer(event.getPlayer());
            final TextComponent component = Component.text("Welcome " + player.getName() + " to the server!").color(TextColor.color(255, 255, 85));
            event.joinMessage(component);

        } else {
            //normal join
            //add if people don't have it already
            final TextComponent component = Component.text("Welcome back " + player.getName() + " !").color(TextColor.color(255, 255, 85));
            event.joinMessage(component);
        }
    }

    public static void playerLeave(PlayerQuitEvent event) {
        //run some things on player leave
        Player player = event.getPlayer();
        Politik.mongoDB.setLogOutTime(player);
        final TextComponent component = Component.text(player.getName() + " has left, have a good day!").color(TextColor.color(255, 255, 85));
        event.quitMessage(component);
    }
}
