package org.gooseapple.politiks.database;

import org.bukkit.Chunk;
import org.gooseapple.politiks.core.land.ILand;
import org.gooseapple.politiks.core.land.geometry.IClaim;
import org.gooseapple.politiks.core.land.types.Land;

import java.util.ArrayList;

public interface IClaimTable extends ITable {
    public IClaim GetClaimForLand(ILand land);
    public void SaveClaim(IClaim claim);
    public ArrayList<IClaim> GetClaimsInChunk(Chunk chunk);
}
