package goose.politik.events;

import goose.politik.player.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import goose.politik.util.text.TextUtil;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace {

    public static void blockPlaceEvent(BlockPlaceEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());

        Block block = event.getBlock();
        Land land = LandUtil.blockInLand(block);
        if (land != null) {
            //block was placed not in the wilderness
            if (!player.getPlayer().isOp()) {
                if (land.getPlayerOwner() != player) {
                    player.message(TextUtil.errorMessage("You can't place blocks here"));
                    event.setCancelled(true);
                }
            }
        }
    }
}
