package goose.politik.events;

import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.event.block.BlockSpreadEvent;

public class SpreadEvent {
    public static void blockSpreadEvent(BlockSpreadEvent event) {
        Land land = LandUtil.blockInLand(event.getBlock());
        if (land != null) {
            event.setCancelled(true);
        }
    }
}
