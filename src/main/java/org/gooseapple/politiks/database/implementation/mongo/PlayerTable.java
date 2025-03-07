package org.gooseapple.politiks.database.implementation.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.core.currency.Account;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.IPlayerTable;
import org.gooseapple.politiks.util.Constants;
import org.gooseapple.politiks.util.Errors;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    public boolean CreateTable() {
        table = database.getCollection(PlayerTable.class.getSimpleName());

        //Use the player UUID as the index
        table.createIndex(Indexes.text(Constants.UUID));
        return true;
    }

    @Override
    public PolitikPlayer CreatePlayer(Player p) {
        PolitikPlayer player = new PolitikPlayer();
        player.setDisplayName(p.getName());
        player.setPlayer(p);
        player.setUUID(p.getUniqueId());
        Date currentDate = new Date();
        player.setJoinDate(BigInteger.valueOf(currentDate.getTime()));
        players.put(p.getUniqueId(), player);

        //Handle the currency generation
        Account account = DatabaseManager.getDatabase().getAccountTable().CreateNewAccount(Account.AccountType.PERSONAL, player);


        return player;
    }

    @Override
    public boolean SavePlayer(PolitikPlayer player) {
        Document document = PlayerToDocument(player);
        Document filter = new Document(Constants.UUID, player.getUUID().toString());
        ReplaceOptions options = new ReplaceOptions().upsert(true);
        table.replaceOne(filter, document, options);
        return true;
    }

    @Override
    public void LoadAllPlayers() {
        for (Document document : table.find()) {
            PolitikPlayer player = new PolitikPlayer();
            //empty player, load every value from the database, when a player joins, just assign a 'message' to it
            UUID uuid = UUID.fromString(document.getString(Constants.UUID));

            Player onlinePlayer = Politiks.getInstance().getServer().getPlayer(uuid);
            if (onlinePlayer != null) {
                player.setPlayer(onlinePlayer);
            }

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

    private Document PlayerToDocument(PolitikPlayer player) {
        Document document = new Document();
        document.put(Constants.UUID, player.getUUID().toString());
        document.put("playerName", player.getDisplayName());
        document.put("joinDate", player.getJoinDate().toString());
        document.put("lastOnline", Instant.now().getEpochSecond());
        document.put("job", player.getJob());
        document.put("infamy", player.getInfamy());
        document.put("nation", "");
        document.put("town", "");
        return document;
    }   

    @Override
    public void SaveAllPlayers() {
        List<WriteModel<Document>> operation = new ArrayList<>();
        for (UUID id : players.keySet()) {
            PolitikPlayer player = players.get(id);
            Document playerDocument = PlayerToDocument(player);
            Document filter = new Document(Constants.UUID, player.getUUID().toString());

            ReplaceOneModel<Document> replaceOneModel = new ReplaceOneModel<>(
                    filter,
                    playerDocument,
                    new ReplaceOptions().upsert(true)
            );
            operation.add(replaceOneModel);
        }
        if (operation.isEmpty()) {
            return;
        }
        table.bulkWrite(operation);
    }

    @Override
    public PolitikPlayer GetPlayer(Player player) {
        if (players.containsKey(player.getUniqueId())) {
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

    @Override
    public boolean PlayerExists(Player player) {
        return players.containsKey(player.getUniqueId());
    }

    @Override
    public void SetPlayerOnline(Player player) {
        PolitikPlayer p = players.get(player.getUniqueId());
        if (p != null) {
            p.setPlayer(player);
        }
    }
}
