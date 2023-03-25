package goose.politik.events;

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
import java.util.logging.Level;

public class LandLoadUnloadEvent  {
    public static void onChunkUnload(ChunkUnloadEvent chunkUnloadEvent) {
        Chunk chunk = chunkUnloadEvent.getChunk();
        //check if that chunk has a land in it
        ArrayList<Land> lands = LandUtil.getLandListInChunk(chunk);
        if (lands != null) {
            for (int i = 0; i < lands.size(); i++) {
                ArrayList<Chunk> chunkArrayList = LandUtil.getChunksInLand(lands.get(i));
                if (chunkArrayList.size() == 1) {
                    LandDB.saveLand(lands.get(i));
                    LandUtil.landMap.get(chunkArrayList.get(0).getWorld().getEnvironment()).get(chunkArrayList.get(0).getChunkKey()).remove(lands.get(i));
                } else {
                    //claim spans across multiple chunks
                    boolean anyLoaded = false;
                    for (Chunk chunkArray : chunkArrayList) {
                        if (chunk.isLoaded()) {
                            anyLoaded = true;
                        }
                    }
                    //if none of the claims chunks are loaded then we can go ahead and remove it
                    if (!anyLoaded) {
                        //none of chunk's claims are loaded so remove it
                        LandDB.saveLand(lands.get(i));
                        for (Chunk chunkClaim : lands.get(i).getOccupiedChunks()) {
                            LandUtil.landMap.get(chunkArrayList.get(0).getWorld().getEnvironment()).get(chunkClaim.getChunkKey()).remove(lands.get(i));
                        }
                    }
                }
            }
        }
    }

    public static void onChunkLoad(ChunkLoadEvent chunkLoadEvent) {
        Long chunkKey = chunkLoadEvent.getChunk().getChunkKey();
        LandDB.loadChunk(chunkKey);
    }
}
