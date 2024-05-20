package goose.politik.events.LandEvents;

import goose.politik.Politik;
import goose.politik.util.database.LandDB;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

public class LandLoadUnloadEvent  {
    public static void onChunkUnload(ChunkUnloadEvent chunkUnloadEvent) {
        Chunk chunk = chunkUnloadEvent.getChunk();
        //check if that chunk has a land in it
        ArrayList<Land> lands = LandUtil.getLandListInChunk(chunk);

        /**
         * If a chunk is empty, we don't need to worry about unloading anything
         */
        if (lands == null || lands.isEmpty()) {
            return;
        }

        Politik.log("Unloading: " + lands);

        for (int i = 0; i < lands.size(); i++) {
            Land land = lands.get(i);
            //This returns a list of all chunks that a land occupies
            CopyOnWriteArrayList<Chunk> chunkArrayList = LandUtil.getChunksInLand(lands.get(i));

            /**
             * If a claim only spans one chunk, remove it from the world, and save it to the database.
             */
            if (chunkArrayList.size() == 1) {
                Politik.log("Unloading single-chunk land claim");
                LandDB.saveLand(land);
                LandUtil.landMap.get(chunkArrayList.get(0).getWorld().getEnvironment()).get(chunkArrayList.get(0).getChunkKey()).remove(land);
                continue;
            }

            //claim spans across multiple chunks
            boolean anyLoaded = false;
            for (Chunk ch : chunkArrayList) {
                if (ch.isLoaded()) {
                    anyLoaded = true;
                    break;
                }
            }

            /**
             * If any of the chunks in that land are still loaded, nothing is needed.
             */
            if (anyLoaded) {
                Politik.log("Chunk contained in multi-chunk claim unloaded, but more needed for it to unload");
                continue;
            }

            Politik.log("Unloading multi-chunk land");
            LandDB.saveLand(land);
            land.clear();
        }

    }

    public static void onChunkLoad(ChunkLoadEvent chunkLoadEvent) {
        LandDB.loadChunk(chunkLoadEvent.getChunk());
    }
}
