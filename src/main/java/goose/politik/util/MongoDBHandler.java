package goose.politik.util;

import com.mongodb.MongoClient;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import org.bson.Document;
import org.bukkit.Chunk;
import org.bukkit.World;

import java.lang.reflect.Array;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class MongoDBHandler {
    public static MongoClient mongoClient;
    public static MongoDatabase serverDB;
    public static MongoCollection<Document> playerCollection;
    public static MongoCollection<Document> overworldLand;
    public static MongoCollection<Document> netherLand;
    public static MongoCollection<Document> endLand;



    public MongoDBHandler() {
        try {
            mongoClient = new MongoClient("localhost", 27017);
        } catch (Exception e) {
            Politik.logger.log(Level.WARNING, "Can't find database");
            return;
        }
        serverDB = mongoClient.getDatabase("mcserver");
        playerCollection = serverDB.getCollection("players");
        overworldLand = serverDB.getCollection("overworld_Land");
        netherLand = serverDB.getCollection("nether_Land");
        endLand = serverDB.getCollection("end_Land");
    }

    public static String loadValueFromDatabase(String key, UUID id) {
        Document playerDocument = playerCollection.find(eq("playerID", id.toString())).first();
        if (playerDocument == null) {
            Politik.logger.log(Level.WARNING, "Error loading " + key + " from database");
            return "error";
        }
        return playerDocument.get(key).toString();
    }

    public static void saveLandToChunk(Chunk chunk, Land land) {
        Document chunkDocument = overworldLand.find(eq("chunkKey", chunk.getChunkKey())).first();
        if (chunkDocument == null) {
            //chunk is empty, we can go ahead and save it to the database
        }
    }

    public static ArrayList<Land> loadLandFromChunk(Chunk chunk) {

        World.Environment chunkDimension = chunk.getWorld().getEnvironment();
        //allows us to get land from custom dimensions
        if (chunkDimension == World.Environment.NORMAL) {
            //overworld
            Document chunkDocument = overworldLand.find(eq("chunkKey", chunk.getChunkKey())).first();
            //if it returns empty, that means nothing is there
            if (chunkDocument == null) {
                return null;
            }
            //chunk does exist with land in it
            ArrayList<Land> claimsInChunk;



        } else if (chunkDimension == World.Environment.NETHER) {
            //nether

        } else if (chunkDimension == World.Environment.THE_END) {
            //end

        }

        return null;
    }

    public static void savePlayerToDatabase(PolitikPlayer player) {
        Document playerObject = playerCollection.find(eq("playerID", player.getUUID().toString())).first();
        if (playerObject == null) {
            //if a player is new, and has left or is being saved
            Politik.logger.log(Level.INFO, "New player is being saved to database: " + player.getDisplayName());

            //create blank document
            Document updatedDocument = new Document();
            updatedDocument.put("playerID", player.getUUID().toString());
            updatedDocument.put("playerName", player.getDisplayName());
            updatedDocument.put("joinDate", player.getJoinDate().toString());
            updatedDocument.put("lastOnline", Instant.now().getEpochSecond());
            updatedDocument.put("job", player.getJob());
            updatedDocument.put("money", player.getMoney().toString());
            updatedDocument.put("infamy", player.getInfamy());
            updatedDocument.put("nation", "none");
            updatedDocument.put("town", "none");
            playerCollection.insertOne(updatedDocument);

        } else {
            Document updatedDocument = new Document();
            updatedDocument.put("playerID", player.getUUID().toString());
            updatedDocument.put("playerName", player.getDisplayName());
            updatedDocument.put("joinDate", player.getJoinDate().toString());
            updatedDocument.put("lastOnline", Instant.now().getEpochSecond());
            updatedDocument.put("job", player.getJob());
            updatedDocument.put("money", player.getMoney().toString());
            updatedDocument.put("infamy", player.getInfamy());
            updatedDocument.put("nation", "none");
            updatedDocument.put("town", "none");
            playerCollection.replaceOne(playerObject, updatedDocument);
        }
    }
}
