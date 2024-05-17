package goose.politik.events;

import goose.politik.permissions.PermissionHandler;
import goose.politik.player.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import goose.politik.util.text.TextUtil;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak {
    public static void blockBreakEvent(BlockBreakEvent event) {
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer(event.getPlayer());
        Land land = LandUtil.blockInLand(event.getBlock());
        //allows OP players to  do whatever they want
        if (!PermissionHandler.PlayerCanBreak(player, land)) {
            player.message(TextUtil.errorMessage("You can't break blocks here"));
            event.setCancelled(true);
        }
    }
}
