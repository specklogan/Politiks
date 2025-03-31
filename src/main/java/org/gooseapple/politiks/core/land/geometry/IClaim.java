package org.gooseapple.politiks.core.land.geometry;

import org.bukkit.Location;

import java.util.ArrayList;

public interface IClaim {
    public int GetEnvironmentID();
    public int GetArea();

    /**
     *
     * @return A list of ChunkKeys containing every chunk that the Claim spans
     */
    public ArrayList<Long> GetContainingChunks();
    public boolean IsSingleChunk();
    public boolean Overlaps(IClaim otherClaim);
    public boolean PointInClaim(Location location);

    Location getSecondLocation();

    Location getFirstLocation();
}
