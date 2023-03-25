package goose.politik.util.database;

import com.google.gson.Gson;
import com.mongodb.Cursor;
import com.mongodb.MongoClient;
import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import goose.politik.Politik;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.government.Town;
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
    public static MongoCollection<Document> nationCollection;
    public static MongoCollection<Document> townCollection;
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
        nationCollection = serverDB.getCollection("nations");
        townCollection = serverDB.getCollection("towns");
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

    public static void savePlayerToDatabase(PolitikPlayer player) {
        Document playerObject = playerCollection.find(eq("playerID", player.getUUID().toString())).first();
        if (playerObject == null) {
            //if a player is new, and has left or is being saved
            Politik.logger.log(Level.INFO, "New player is being saved to database: " + player.getDisplayName());

            //check if they are part of a town/nation
            Nation playerNation = player.getNation();
            String playerNationName = "none";
            if (playerNation != null) {
                playerNationName = playerNation.getNationName();
            }
            Town playerTown = player.getTown();
            String playerTownName = "none";
            if (playerTown != null) {
                playerTownName = playerTown.getTownName();
            }

            //create blank document
            Document updatedDocument = new Document();
            updatedDocument.put("playerID", player.getUUID().toString());
            updatedDocument.put("playerName", player.getDisplayName());
            updatedDocument.put("joinDate", player.getJoinDate().toString());
            updatedDocument.put("lastOnline", Instant.now().getEpochSecond());
            updatedDocument.put("job", player.getJob());
            updatedDocument.put("money", player.getMoney().toString());
            updatedDocument.put("infamy", player.getInfamy());
            updatedDocument.put("nation", playerNationName);
            updatedDocument.put("town", playerTownName);
            playerCollection.insertOne(updatedDocument);

        } else {

            //check if they are part of a town/nation
            Nation playerNation = player.getNation();
            String playerNationName = "none";
            if (playerNation != null) {
                playerNationName = playerNation.getNationName();
            }
            Town playerTown = player.getTown();
            String playerTownName = "none";
            if (playerTown != null) {
                playerTownName = playerTown.getTownName();
            }

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
