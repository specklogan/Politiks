package goose.politik.util.landUtil;

import goose.politik.util.landUtil.lands.Farm;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.HashMap;

public class LandConverter {
    //intended to convert between different kinds of land
    //seemlessly
    public static Land convertLand(Land convertee, Farm converted) {
        converted.setTownOwner(convertee.getTownOwner());
        converted.setNationOwner(convertee.getNationOwner());
        converted.setPlayerOwner(convertee.getPlayerOwner());
        converted.setBiome(convertee.getBiome());
        converted.setWorld(convertee.getWorld());
        converted.setEnvironment(convertee.getEnvironment());
        converted.setOccupiedChunks(convertee.getOccupiedChunks());
        converted.setFirstLocation(convertee.getFirstLocation());
        converted.setSecondLocation(convertee.getSecondLocation());
        converted.setUUID(convertee.getUUID());
        converted.setArea(convertee.getArea());

        HashMap<Long, ArrayList<Land>> landHashMap = LandUtil.landMap.get(World.Environment.NORMAL);
        //now that we have equal lands, we need to go ahead and remove the previous land from the landmap, and add the new ones
        for (Chunk chunk : converted.getOccupiedChunks()) {
            for (int i = 0; i < landHashMap.get(chunk.getChunkKey()).size(); i++) {
                Land land = landHashMap.get(chunk.getChunkKey()).get(i);
                if (land.equals(convertee)) {
                    //remove and replace
                    landHashMap.get(chunk.getChunkKey()).remove(land);
                    landHashMap.get(chunk.getChunkKey()).add(converted);
                }
            }
        }
        return converted;
    }
}
