package goose.politik.util.database;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import org.bson.Document;
import org.bukkit.entity.Player;

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

            Player onlinePlayer = Politik.getInstance().getServer().getPlayer(uuid);
            if (onlinePlayer != null) {
                player.setPlayer(onlinePlayer);
            }

            player.setMoney(new BigDecimal(money));
            player.setUUID(uuid);
            player.setDisplayName(document.getString("playerName"));
            String job = document.getString("job");
            if (job == null) {
                job = "none";
            }
            player.setJob(job);
            player.setJoinDate(new BigInteger(document.getString("joinDate")));
            player.setInfamy(document.getInteger("infamy"));
            PolitikPlayer.playerList.put(uuid, player);
        }
    }
}
