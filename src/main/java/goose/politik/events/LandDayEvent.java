package goose.politik.events;

import goose.politik.Politik;
import goose.politik.events.CustomEvents.DayCompleteEvent;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import goose.politik.util.landUtil.TickableLand;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class LandDayEvent implements Listener {
    @EventHandler
    public void onNewDayEvent(DayCompleteEvent event) {
        event.getServer().broadcast(Politik.detailMessage("---Production Cycles Calculated---"));

        HashMap<Long, ArrayList<Land>> landArrayMap = LandUtil.landMap.get(World.Environment.NORMAL);
        HashMap<UUID, Integer> completedLands = new HashMap<>();
        for (Long chunkLong : landArrayMap.keySet()) {
            for (Land land : landArrayMap.get(chunkLong)) {
                if (land instanceof TickableLand && completedLands.get(land.getUUID()) == null) {
                    //do tickable behavior
                    ((TickableLand) land).onTickEvent();
                    completedLands.put(land.getUUID(), 1);
                }
            }
        }
    }
}
