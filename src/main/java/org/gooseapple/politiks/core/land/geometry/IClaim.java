package org.gooseapple.politiks.core.land.geometry;

import org.bukkit.Location;
import org.gooseapple.politiks.core.land.ILand;

import java.util.ArrayList;
import java.util.UUID;

public interface IClaim {
    public int GetEnvironmentID();
    public UUID getId();
    public int GetArea();
    public ILand GetLand();
    public void SetLand(ILand land);
    public void SetLandID(UUID id);
    public UUID GetLandID();


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
