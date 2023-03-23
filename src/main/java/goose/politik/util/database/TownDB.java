package goose.politik.util.database;

import goose.politik.Politik;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.government.Town;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.World;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class TownDB {
    //We need to implement a custom town to database format
    /*
        Important things to save:
            1) UUID of members + their 'role'
            2) Nation that it is under
            3) Amount of money in town bank
            4) Spawn location
            5) Town name
     */

    public static void saveTown(Town town) {
        Document townDocument = MongoDBHandler.townCollection.find(eq("townName", town.getTownName())).first();

        String townName = town.getTownName();
        String locationStr = town.getSpawnLocation().getWorld().getName() + "," + town.getSpawnLocation().getX() + "," + town.getSpawnLocation().getY() + "," + town.getSpawnLocation().getZ();
        ArrayList<String> playerList = new ArrayList<>();
        for (PolitikPlayer player : town.getPlayerList()) {
            playerList.add(player.getUUID().toString());
        }
        Politik.logger.log(Level.INFO, "Saving Town: " + townName + " into the database");
        if (townDocument == null) {
            //town is new and hasn't been saved yet, create a blank database
            Document newTownDocument = new Document();
            newTownDocument.put("townName", townName);
            newTownDocument.put("nationOwner", town.getNationOwner().getNationName());
            newTownDocument.put("townMayor", town.getMayor().getUUID().toString());
            newTownDocument.put("enterMessage", town.getEnterMessage().toString());
            newTownDocument.put("spawnLocation", locationStr);
            newTownDocument.put("playerList", playerList);
            MongoDBHandler.townCollection.insertOne(newTownDocument);
        } else {
            //town already exists, just overwrite it
            Document newTownDocument = new Document();
            newTownDocument.put("townName", townName);
            newTownDocument.put("nationOwner", town.getNationOwner().getNationName());
            newTownDocument.put("townMayor", town.getMayor().getUUID().toString());
            newTownDocument.put("enterMessage", town.getEnterMessage().toString());
            newTownDocument.put("spawnLocation", locationStr);
            newTownDocument.put("playerList", playerList);
            MongoDBHandler.townCollection.replaceOne(townDocument, newTownDocument);
        }
    }

    public static void loadTowns() {
        //loads all towns into the nation that owns them
        //the loading process should go like this
        /*
            1) Load all players
            2) Load all nations
            3) Load all towns
            4) Load all lands
         */
        //loop through the town DB and load them one-by-one, add all of their members into the town and nation
        for (Document document : MongoDBHandler.townCollection.find()) {
            //read through all saved nations
            String townName = document.getString("townName");
            String nationOwner = document.getString("nationOwner");
            Nation nationObj = null;
            for (Nation nation : Nation.NATIONS) {
                if (nation.getNationName().equals(nationOwner)) {
                    nationObj = nation;
                }
            }
            if (nationObj == null) {
                Politik.logger.log(Level.SEVERE, "Error loading town: " + townName + " from database, no valid town owner");
                return;
            }
            UUID mayorUUID = UUID.fromString(document.getString("townMayor"));
            Politik.logger.log(Level.INFO, "Loading town from database: " + townName);
            Town town = new Town(townName, PolitikPlayer.getPolitikPlayerFromID(mayorUUID), nationObj);
            String spawnLocation = document.getString("spawnLocation");
            String[] splitLocation = spawnLocation.split(",");
            Location location = new Location(Politik.getInstance().getServer().getWorld(splitLocation[0]), Double.parseDouble(splitLocation[1]), Double.parseDouble(splitLocation[2]), Double.parseDouble(splitLocation[3]));
            town.setSpawnLocation(location);
            town.getNation().addTown(town);

            //now add all of the players
            List<String> playerList = document.getList("playerList", String.class);
            for (String plrStr : playerList) {
                PolitikPlayer player = PolitikPlayer.getPolitikPlayerFromID(UUID.fromString(plrStr));
                town.addPlayer(player);
            }
        }
    }
}
