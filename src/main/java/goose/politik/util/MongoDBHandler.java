package goose.politik.util;

import com.mongodb.MongoClient;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import org.bson.Document;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Level;

public class MongoDBHandler {
    public static MongoClient mongoClient;
    public static MongoDatabase serverDB;
    public static MongoCollection<Document> playerCollection;

    public MongoDBHandler() {
        try {
            mongoClient = new MongoClient("localhost", 27017);
        } catch (Exception e) {
            Politik.getInstance().logger.log(Level.WARNING, "Can't find database");
            return;
        }
        serverDB = mongoClient.getDatabase("mcserver");
        playerCollection = serverDB.getCollection("players");
    }

    public static String loadValueFromDatabase(String key, UUID id) {
        Document playerDocument = playerCollection.find(eq("playerID", id.toString())).first();
        if (playerDocument == null) {
            Politik.getInstance().logger.log(Level.WARNING, "Error loading " + key + " from database");
            return "error";
        }
        return playerDocument.get(key).toString();
    }

    public static void savePlayerToDatabase(PolitikPlayer player) {
        Document playerObject = playerCollection.find(eq("playerID", player.getUUID().toString())).first();
        if (playerObject == null) {
            //if a player is new, and has left or is being saved
            Politik.getInstance().logger.log(Level.INFO, "New player is being saved to database: " + player.getDisplayName());

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
