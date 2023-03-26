package goose.politik.events;

import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import java.math.BigDecimal;

public class JobEvent {
    //run events for specific jobs
    public static void shearEntityEvent(PlayerShearEntityEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());
        Player physicalPlayer = player.getPlayer();
        if (player.getJob().equalsIgnoreCase("rancher")) {
            Land land = LandUtil.blockInLand(event.getEntity().getLocation().getBlock());
            if (land != null) {
                if (land.getPlayerOwner() != player) {
                    event.setCancelled(true);
                    return;
                }
            }
            //they can gain money from shearing
            player.changeMoney(new BigDecimal("0.5"));
            physicalPlayer.playSound(physicalPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }
    }

    public static void harvestCropEvent(PlayerHarvestBlockEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());
        Player physicalPlayer = player.getPlayer();
        if (player.getJob().equalsIgnoreCase("farmer")) {
            //farmer is farming
            player.changeMoney(new BigDecimal("0.1"));
            physicalPlayer.playSound(physicalPlayer.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);
        }
    }
}
