package goose.politik.util.database;

import goose.politik.util.government.PolitikPlayer;
import org.bson.Document;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

public class PlayerDB {
    public static void loadAllPlayers() {
        //the goal for this is to allow players to have their money manipulated while offline, as they
        //are stored in java objects on server loading and unloading

        for (Document document : MongoDBHandler.playerCollection.find()) {
            PolitikPlayer player = new PolitikPlayer();
            //empty player, load every value from the database, when a player joins, just assign a 'message' to it
            String money = document.getString("money");
            UUID uuid = UUID.fromString(document.getString("playerID"));

            player.setMoney(new BigDecimal(money));
            player.setUUID(uuid);
            player.setDisplayName(document.getString("playerName"));
            player.setJob(document.getString("job"));
            player.setJoinDate(new BigInteger(document.getString("joinDate")));
            player.setInfamy(document.getInteger("infamy"));
            PolitikPlayer.playerList.put(uuid, player);
        }
    }
}
