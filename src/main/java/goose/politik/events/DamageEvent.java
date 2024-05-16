package goose.politik.events;

import goose.politik.player.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import goose.politik.util.text.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

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
                        player.message(TextUtil.errorMessage("You can't damage here"));
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
