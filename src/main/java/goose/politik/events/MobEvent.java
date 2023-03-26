package goose.politik.events;

import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.block.Block;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class MobEvent {
    public static
    void mobSpawnEvent(CreatureSpawnEvent event) {
        if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER_EGG) {
            return;
        }
        Block spawnedBlock = event.getLocation().getBlock();
        Land land = LandUtil.blockInLand(spawnedBlock);
        if (land != null) {
            event.setCancelled(true);
        }
    }
}
