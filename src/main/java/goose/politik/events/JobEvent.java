package goose.politik.events;

import goose.politik.Politik;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class JobEvent {
    //run events for specific jobs
    public static void shearEntityEvent(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        if (Politik.mongoDB.getPlayerJob(player).equalsIgnoreCase("rancher")) {
            //they can gain money from shearing
            Politik.moneyHandler.changeMoney(1, player);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }
    }
}
