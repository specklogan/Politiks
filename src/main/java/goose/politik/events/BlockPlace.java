package goose.politik.events;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.logging.Level;

public class BlockPlace {

    public static void blockPlaceEvent(BlockPlaceEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());

        Block block = event.getBlock();
        Land land = LandUtil.blockInLand(block);
        if (land != null) {
            //block was placed not in the wilderness
            if (!player.getPlayer().isOp()) {
                if (land.getPlayerOwner() != player) {
                    player.message(Politik.errorMessage("You can't place blocks here"));
                    event.setCancelled(true);
                }
            }
        }
    }
}
