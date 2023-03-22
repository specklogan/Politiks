package goose.politik.events;

import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak {
    public static void blockBreakEvent(BlockBreakEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());
        //allows OP players to  do whatever they want
        if (!player.getPlayer().isOp()) {
            Land land = LandUtil.blockInLand(event.getBlock());
            if (land != null) {
                //let the same player destroy their own land
                if (land.getPlayerOwner() != player) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
