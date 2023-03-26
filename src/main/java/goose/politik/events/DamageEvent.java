package goose.politik.events;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.Objects;

public class DamageEvent {
    public static void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            //player did damage
            PolitikPlayer player = PolitikPlayer.getPolitikPlayer(Objects.requireNonNull(((Player) event.getDamager()).getPlayer()));
            Land land = LandUtil.blockInLand(player.getPlayer().getLocation().getBlock());
            if (land != null) {
                //player did damage inside land
                if (!player.getPlayer().isOp()) {
                    if (land.getPlayerOwner() != player) {
                        player.message(Politik.errorMessage("You can't damage here"));
                        event.setCancelled(true);
                    }
                }
            }
        } else {
            if (event.getEntity() instanceof Player) {
                PolitikPlayer player = PolitikPlayer.getPolitikPlayer(Objects.requireNonNull(((Player) event.getEntity()).getPlayer()));
                Land land = LandUtil.blockInLand(player.getPlayer().getLocation().getBlock());
                if (land != null) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
