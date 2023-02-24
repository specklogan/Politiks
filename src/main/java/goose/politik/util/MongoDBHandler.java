package goose.politik.util;

import com.mongodb.MongoClient;
import static com.mongodb.client.model.Filters.*;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import goose.politik.Politik;
import org.bson.Document;
import org.bukkit.entity.Player;

import java.time.Instant;
import java.util.UUID;
import java.util.logging.Level;

public class MongoDBHandler {
    private MongoClient mongoClient;
    private MongoDatabase serverDB;
    private MongoCollection<Document> playerCollection;

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

    public String getPlayerJob(Player player) {
        UUID playerID = player.getUniqueId();
        Document playerObject = playerCollection.find(eq("playerID", playerID.toString())).first();

        if (playerObject == null) {
            Politik.getInstance().logger.log(Level.WARNING, "Error finding player " + player.getName() + " in database");
            return "error";
        }

        return playerObject.get("job").toString();
    }

    public double getPlayerMoney(Player player) {
        UUID playerID = player.getUniqueId();
        Document playerObject = playerCollection.find(eq("playerID", playerID.toString())).first();

        if (playerObject == null) {
            Politik.getInstance().logger.log(Level.WARNING, "Error finding player " + player.getName() + " in database");
            return -1;
        }

        double moneyVal = Double.parseDouble(playerObject.get("money").toString());

        return moneyVal;
    }

    public void setPlayerMoney(Player player, double money) {
        UUID playerID = player.getUniqueId();
        Document playerObject = playerCollection.find(eq("playerID", playerID.toString())).first();

        if (playerObject == null) {
            Politik.getInstance().logger.log(Level.WARNING, "Error finding player " + player.getName() + " in database");
            return;
        }

        playerCollection.updateOne(playerObject, new Document("$set", new Document("money", money)));
    }

    public void setLogOutTime(Player player) {
        UUID playerID = player.getUniqueId();
        Document playerObject = playerCollection.find(eq("playerID", playerID.toString())).first();

        if (playerObject == null) {
            Politik.getInstance().logger.log(Level.WARNING, "Error finding player " + player.getName() + " in database");
            return;
        }

        playerCollection.updateOne(playerObject, new Document("$set", new Document("lastOnline", Instant.now().getEpochSecond())));
    }


    public void addPlayer(Player player) {
        /*
            This is intended to only be run once when a new player joins the server
         */


        //get uuid to store
        UUID uuid = player.getUniqueId();
        //only do this once
        Document playerObject = new Document("playerID", uuid.toString());
        playerObject.put("playerName", player.getName());
        playerObject.put("money", 0);
        playerObject.put("infamy", 0);
        playerObject.put("joinDate", Instant.now().getEpochSecond());
        playerObject.put("lastOnline", 0);
        playerObject.put("town", "none");
        playerObject.put("nation", "none");
        playerObject.put("job", "none");

        Politik.getInstance().logger.log(Level.INFO, "Adding new player to database: " + player.getName() + ": " + uuid);
        playerCollection.insertOne(playerObject);
    }
}
