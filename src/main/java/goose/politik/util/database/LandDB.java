package goose.politik.util.database;

import com.mongodb.client.MongoCursor;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import goose.politik.util.landUtil.lands.Farm;
import org.bson.Document;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class LandDB {
    public static void saveLand(Land land) {
        if (land.getFirstLocation().getWorld().getEnvironment() == World.Environment.NORMAL) {
            //check if land is in a database or not
            Document landDocument = DatabaseHandler.overworldLand.find(eq("_id", land.getUUID().toString())).first();

            if (landDocument == null) {
                //add new land to database
                DatabaseHandler.overworldLand.insertOne(land.toDocument());
            } else {
                //existing land
                DatabaseHandler.overworldLand.replaceOne(landDocument, land.toDocument());
            }
        }
    }

    //this method will find all non-normal lands
    public static ArrayList<Land> loadAllTickableLand() {
        ArrayList<Document> tickableLandDocuments = new ArrayList<>();
        ArrayList<Land> tickableLand = new ArrayList<>();
        //find all documents not equal to 'NORMAL' type
        Document query = new Document("type", new Document("$ne", "NORMAL"));
        MongoCursor<Document> cursor = DatabaseHandler.overworldLand.find(query).iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            tickableLandDocuments.add(document);
        }
        cursor.close();
        for (Document document : tickableLandDocuments) {
            String type = document.getString("type");
            UUID uuid = UUID.fromString(document.getString("_id"));
            //check if the
            if (type.equalsIgnoreCase("farm")) {
                Farm farm = Farm.liteLoad(document);
                farm.onTickEvent();
            }
        }

        return tickableLand;
    }

    public static void loadChunk(Chunk chunk) {
        //loads lands, if any, given a specific chunkKey

        ArrayList<Document> chunkClaims = new ArrayList<>();
        Document query = new Document("occupiedChunks", new Document("$elemMatch", new Document("$eq", ((Long)chunk.getChunkKey()).toString())));
        MongoCursor<Document> cursor = DatabaseHandler.overworldLand.find(query).iterator();
        while (cursor.hasNext()) {
            Document document = cursor.next();
            chunkClaims.add(document);
        }
        cursor.close();

        if (chunkClaims.isEmpty()) {
            return; //Nothing in that chunk
        }

        for (Document document : chunkClaims) {
            //We need to figure out what TYPE of document this is, and load it to it's respective
            //claim type
            String type = document.getString("type");
            UUID uuid = UUID.fromString(document.getString("_id"));
            //check the uuid to make sure no existing land is already in the world
            ArrayList<Land> existingLand = LandUtil.getLandListInChunk(chunk);

            /**
             * This is due to large lands being able to occupy chunks out of render distance, if a
             * smaller land is in a loaded chunk that is also occupied by a bigger land, this will ignore those larger chunk documents
             * and only load the unloaded ones.
             */
            boolean alreadyExists = false;
            if (existingLand != null) {
                for (Land land : existingLand) {
                    if (land.getUUID() == uuid) {
                        //if a land in this chunk already exists, ignore this document
                        alreadyExists = true;
                    }
                }
            }

            if (alreadyExists) {
                continue;
            }

            if (type.equalsIgnoreCase("normal")) {
                //load normal land
                Land.load(document);
            } else if (type.equalsIgnoreCase("farm")) {
                Farm.load(document, new Farm());
            }
        }
    }
}
