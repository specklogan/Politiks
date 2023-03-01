package goose.politik.events;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import net.kyori.adventure.chat.ChatType;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveEvent {
    public static void playerMoveEvent(PlayerMoveEvent event) {
        //check if the player actually moved
        if (event.hasChangedBlock()) {
            Block movedBlock = event.getTo().getBlock();
            Land collidedLand = LandUtil.blockInLand(movedBlock);
            PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());
            if (collidedLand != null && collidedLand != player.getLastEnteredLand()) {
                //collided with something
                //check to make sure it isn't gonna repeat town mesage
                if (player.getLastEnteredLand() != null) {
                    if (player.getLastEnteredLand().getTownOwner() == collidedLand.getTownOwner()) {
                        return;
                    }
                }
                player.setLastEnteredLand(collidedLand);
                event.getPlayer().sendActionBar(Politik.eventMessage("Entering Town " + collidedLand.getTownOwner().getTownName()));
            } else if (collidedLand == null && !(player.getLastEnteredLand() == null)) {
                event.getPlayer().sendActionBar(Politik.eventMessage("Leaving Town " + player.getLastEnteredLand().getTownOwner().getTownName()));
                player.setLastEnteredLand(null);
            }
        }
    }
}
