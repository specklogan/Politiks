package goose.politik.events;

import goose.politik.Politik;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;

import java.math.BigDecimal;
import java.util.logging.Level;

public class JobEvent {
    //run events for specific jobs
    public static void shearEntityEvent(PlayerShearEntityEvent event) {
        Player player = event.getPlayer();
        if (Politik.mongoDB.getPlayerJob(player).equalsIgnoreCase("rancher")) {
            //they can gain money from shearing
            Politik.moneyHandler.changeMoney(new BigDecimal("0.5"), player);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }
    }

    public static void harvestCropEvent(PlayerHarvestBlockEvent event) {
        Player player = event.getPlayer();
        if (Politik.mongoDB.getPlayerJob(player).equalsIgnoreCase("farmer")) {
            //farmer is farming
            Politik.moneyHandler.changeMoney(new BigDecimal("0.1"), player);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }
    }
}
