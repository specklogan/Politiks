package goose.politik.events;

import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ExplodeEvent {
    public static void blockExplodeEvent(BlockExplodeEvent event) {
        //we need to check each of the exploded blocks to see if they are in land or not
        for (int i = 0; i < event.blockList().size(); i++) {
            Land land = LandUtil.blockInLand(event.blockList().get(i));
            if (land != null) {
                event.blockList().remove(i);
                i--;
            }
        }
    }

    public static void entityExplodeEvent(EntityExplodeEvent event) {
        for (int i = 0; i < event.blockList().size(); i++) {
            Land land = LandUtil.blockInLand(event.blockList().get(i));
            if (land != null) {
                event.blockList().remove(i);
                i--;
            }
        }
    }
}
