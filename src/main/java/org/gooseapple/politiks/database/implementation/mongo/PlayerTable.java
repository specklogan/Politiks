package org.gooseapple.politiks.database.implementation.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.IPlayerTable;
import org.gooseapple.politiks.util.Constants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerTable implements IPlayerTable {
    private MongoDatabase database;
    private MongoCollection<Document> table;
    private ConcurrentHashMap<UUID, PolitikPlayer> players = new ConcurrentHashMap<>();

    public PlayerTable(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public boolean create() {
        table = database.getCollection(PlayerTable.class.getName());

        //Use the player UUID as the index
        table.createIndex(Indexes.text(Constants.UUID));
        return true;
    }

    @Override
    public PolitikPlayer LoadPlayer(Player p) {
        PolitikPlayer player = new PolitikPlayer();
        player.setDisplayName(p.getName());
        player.setPlayer(p);
        player.setMoney(BigDecimal.ZERO);
        player.setUUID(p.getUniqueId());
        players.put(p.getUniqueId(), player);
        return player;
    }

    @Override
    public boolean SavePlayer(PolitikPlayer player) {
        return false;
    }

    @Override
    public void LoadAllPlayers() {
        for (Document document : table.find()) {
            PolitikPlayer player = new PolitikPlayer();
            //empty player, load every value from the database, when a player joins, just assign a 'message' to it
            String money = document.getString("money");
            UUID uuid = UUID.fromString(document.getString("playerID"));

            Player onlinePlayer = Politiks.getInstance().getServer().getPlayer(uuid);
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
            players.put(uuid, player);
        }
    }

    @Override
    public void SaveAllPlayers() {

    }

    @Override
    public PolitikPlayer GetPlayer(Player player) {
        if (players.contains(player.getUniqueId())) {
            return players.get(player.getUniqueId());
        }
        return null;
    }

    @Override
    public PolitikPlayer GetPlayer(String name) {
        for (UUID id : players.keySet()) {
            PolitikPlayer p = players.get(id);
            if (p.getDisplayName().equalsIgnoreCase(name)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public PolitikPlayer GetPlayer(UUID id) {
        return players.get(id);
    }
}
