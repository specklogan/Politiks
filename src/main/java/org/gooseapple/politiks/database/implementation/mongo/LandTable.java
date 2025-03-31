package org.gooseapple.politiks.database.implementation.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.core.land.ILand;
import org.gooseapple.politiks.core.land.geometry.IClaim;
import org.gooseapple.politiks.core.land.types.Land;
import org.gooseapple.politiks.database.ILandTable;
import org.gooseapple.politiks.util.Constants;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class LandTable implements ILandTable {
    private MongoDatabase database;
    private MongoCollection<Document> table;

    private final ConcurrentHashMap<Integer, ConcurrentHashMap<Long, CopyOnWriteArrayList<ILand>>> worldLandHashmap = new ConcurrentHashMap<>();

    public LandTable(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public ILand GetLandFromLocation(Location location) {
        int environmentID = location.getWorld().getEnvironment().getId();

        //No lands have been declared in this dimension yet
        if (!worldLandHashmap.containsKey(environmentID)) {
            return null;
        }

        ConcurrentHashMap<Long, CopyOnWriteArrayList<ILand>> environmentLands = worldLandHashmap.get(environmentID);

        //Check if the location's chunk has a value in the hashmap
        if (!environmentLands.containsKey(location.getChunk().getChunkKey())) {
            return null;
        }

        //There is a Land in this chunk, now iterate through every land and find a collision
        for (ILand land : environmentLands.get(location.getChunk().getChunkKey())) {
            if (land.GetClaim().PointInClaim(location)) {
                return land;
            }
        }

        return null;
    }

    @Override
    public boolean IsLandOverlapping(ILand land) {
        var chunks = land.GetClaim().GetContainingChunks();
        IClaim newClaim = land.GetClaim();
        for (long chunkID : chunks) {

            var landsInChunk = GetLandInChunk(chunkID, land.GetClaim().GetEnvironmentID());
            if (landsInChunk == null || landsInChunk.isEmpty()) {
                continue; //No lands to consider in this chunk
            }

            //Check if the land is overlapping
            for (ILand chunkLand : landsInChunk) {
                if (newClaim.Overlaps(chunkLand.GetClaim())) {
                    return true;
                }
            }
        }

        return false;
    }

    @Nullable
    @Override
    public CopyOnWriteArrayList<ILand> GetLandInChunk(Chunk chunk) {
        int environmentID = chunk.getWorld().getEnvironment().getId();


        //No lands have been declared in this dimension yet
        if (!worldLandHashmap.containsKey(environmentID)) {
            return null;
        }

        return worldLandHashmap.get(environmentID).get(chunk.getChunkKey());
    }

    @Override
    public CopyOnWriteArrayList<ILand> GetLandInChunk(long chunkKey, int environmentID) {

        //No lands have been declared in this dimension yet
        if (!worldLandHashmap.containsKey(environmentID)) {
            return null;
        }

        return worldLandHashmap.get(environmentID).get(chunkKey);
    }

    private void AddLandToChunk(int environmentID, long chunk, ILand land) {
        //Dimension hasn't had any claims yet, insert new values
        if (!worldLandHashmap.containsKey(environmentID)) {
            worldLandHashmap.put(environmentID, new ConcurrentHashMap<>());
        }

        ConcurrentHashMap<Long, CopyOnWriteArrayList<ILand>> environmentLands = worldLandHashmap.get(environmentID);

        //Chunk hasn't had a land claim before, insert new values
        if (!environmentLands.containsKey(chunk)) {
            environmentLands.put(chunk, new CopyOnWriteArrayList<>());
        }

        environmentLands.get(chunk).add(land);
    }

    @Override
    public void InsertNewLand(ILand land) {
        int environmentID = land.GetClaim().GetEnvironmentID();
        IClaim claim = land.GetClaim();

        for (long chunkID : claim.GetContainingChunks()) {
            AddLandToChunk(environmentID, chunkID, land);
        }
    }

    @Override
    public boolean CreateTable() {
        table = database.getCollection(LandTable.class.getSimpleName());

        //Use the land UUID as the primary key
        table.createIndex(Indexes.text(Constants.UUID));
        return true;
    }
}
