package org.gooseapple.politiks.database.implementation.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOptions;
import org.bson.Document;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.gooseapple.politiks.core.land.ILand;
import org.gooseapple.politiks.core.land.geometry.ClaimRectangle;
import org.gooseapple.politiks.core.land.geometry.IClaim;
import org.gooseapple.politiks.core.land.types.Land;
import org.gooseapple.politiks.database.IClaimTable;
import org.gooseapple.politiks.database.ILandTable;
import org.gooseapple.politiks.util.Constants;
import org.gooseapple.politiks.util.LocationUtil;

import java.util.ArrayList;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class ClaimTable implements IClaimTable {
    private MongoDatabase database;
    private MongoCollection<Document> table;
    private LandTable landTable;

    public ClaimTable(MongoDatabase database, LandTable landTable) {
        this.database = database;
        this.landTable = landTable;
    }

    @Override
    public IClaim GetClaimForLand(ILand land) {
        return null;
    }

    @Override
    public void SaveClaim(IClaim claim) {
        Document document = ClaimToDocument(claim);
        Document filter = new Document(Constants.UUID, claim.getId().toString());
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        table.replaceOne(filter, document, options);
    }

    @Override
    public ArrayList<IClaim> GetClaimsInChunk(Chunk chunk) {
        ArrayList<IClaim> claims = new ArrayList<>();

        var iterator = table.find(eq(Constants.Chunks, chunk.getChunkKey()));

        for (var document : iterator) {

        }

        return claims;
    }

    @Override
    public void SaveAllClaims() {

    }

    private IClaim DocumentToClaim(Document document) {
        String type = document.getString(Constants.Type);
        Location firstLocation = LocationUtil.Deserialize(document.getString(Constants.FirstPosition));
        Location secondLocation = LocationUtil.Deserialize(document.getString(Constants.SecondPosition));
        UUID landId = UUID.fromString(document.getString(Constants.Land));
        UUID id = UUID.fromString(document.getString(Constants.UUID));

        if (type.equals(ClaimRectangle.class.getSimpleName())) {
            ClaimRectangle claim = new ClaimRectangle(firstLocation, secondLocation, id);
            claim.SetLandID(landId);
            claim.SetLand(landTable.GetOrLoadLand(claim));
            return claim;
        } else {
            //Sphere claim
            return null;
        }
    }

    private Document ClaimToDocument(IClaim claim) {
        Document document = new Document();
        document.put(Constants.UUID, claim.getId().toString());
        document.put(Constants.FirstPosition, LocationUtil.Serialize(claim.getFirstLocation()));
        document.put(Constants.SecondPosition, LocationUtil.Serialize(claim.getSecondLocation()));
        document.put(Constants.Land, claim.GetLand().GetID().toString());
        document.put(Constants.Chunks, claim.GetContainingChunks());
        document.put(Constants.Type, claim.getClass().getSimpleName());
        return document;
    }

    @Override
    public boolean CreateTable() {
        table = database.getCollection(ClaimTable.class.getSimpleName());

        //Use the land UUID as the primary key
        table.createIndex(Indexes.text(Constants.UUID));
        table.createIndex(Indexes.text(Constants.Land));
        return true;
    }
}
