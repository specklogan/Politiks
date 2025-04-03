package org.gooseapple.politiks.database;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.gooseapple.politiks.core.land.ILand;
import org.gooseapple.politiks.core.land.geometry.IClaim;
import org.gooseapple.politiks.core.land.types.Land;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public interface ILandTable extends ITable {

    /**
     * Checks the in-memory land table in the dimension that the location is at for any lands
     * @param location
     * @return Null if not found
     */
    public ILand GetLandFromLocation(Location location);

    public ILand GetOrLoadLand(IClaim claim);

    /**
     * Used to check if a new land will overlap a preexisting one, before it will be added to the landmap
     * @param land
     * @return True if a conflict was detected
     */
    public boolean IsLandOverlapping(ILand land);
    public CopyOnWriteArrayList<ILand> GetLandInChunk(Chunk chunk);
    public CopyOnWriteArrayList<ILand> GetLandInChunk(long chunkKey, int environmentID);
    public void InsertNewLand(ILand land);

}
