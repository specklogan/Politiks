package org.gooseapple.politiks.database.implementation.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.core.currency.Account;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.IPlayerTable;
import org.gooseapple.politiks.database.ITownTable;
import org.gooseapple.politiks.util.Constants;
import org.gooseapple.politiks.util.LocationUtil;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class TownTable implements ITownTable {
    private MongoDatabase database;
    private final ConcurrentHashMap<UUID, Town> towns = new ConcurrentHashMap<>();
    private MongoCollection<Document> table;

    public TownTable(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public Town GetTown(String name) {
        for (UUID id : towns.keySet()) {
            Town t = towns.get(id);
            if (t.getTownName().equals(name)) {
                return t;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void LoadAllTowns() {
        IPlayerTable playerTable = DatabaseManager.getDatabase().getPlayerTable();
        for (Document document : table.find()) {
            UUID uuid = UUID.fromString(document.getString(Constants.UUID));
            Town t = new Town(uuid);
            t.setTownName(document.getString("Name"));
            PolitikPlayer mayor = playerTable.GetPlayer(UUID.fromString(document.getString(Constants.UUID)));
            t.setMayor(mayor);
            t.setSpawnLocation(LocationUtil.Deserialize(document.getString("Spawn_Location")));

            CopyOnWriteArrayList<PolitikPlayer> players = new CopyOnWriteArrayList<>();
            ArrayList<String> dbPlayers = (ArrayList<String>) document.get("Player_IDs");
            for (String s : dbPlayers) {
                PolitikPlayer p = playerTable.GetPlayer(UUID.fromString(s));
                players.add(p);
            }

            //TODO: Set nation owner once implemented

            t.setPlayerList(players);
        }
    }

    private Document TownToDocument(Town t) {
        Document document = new Document();
        document.put(Constants.UUID, t.getId().toString());
        document.put("Name", t.getTownName());
        document.put("Mayor_ID", t.getMayor().getUUID());
        document.put("Spawn_Location", LocationUtil.Serialize(t.getSpawnLocation()));
        document.put("Nation_ID", t.getNation().getId().toString());
        ArrayList<String> players = new ArrayList<>();
        for (PolitikPlayer p : t.getPlayerList()) {
            players.add(p.getUUID().toString());
        }
        document.put("Player_IDs", players);
        return document;
    }

    @Override
    public Town CreateTown(PolitikPlayer mayor) {
        UUID id = UUID.randomUUID();
        Town town = new Town(id);
        town.setMayor(mayor);
        town.addPlayer(mayor);

        towns.put(id, town);

        //Set town account
        return town;
    }

    @Override
    public void SaveAllTowns() {
        List<WriteModel<Document>> operation = new ArrayList<>();
        for (UUID id : towns.keySet()) {
            Town t = towns.get(id);
            Document townDocument = TownToDocument(t);
            Document filter = new Document(Constants.UUID, t.getId());

            ReplaceOneModel<Document> replaceOneModel = new ReplaceOneModel<>(
                    filter,
                    townDocument,
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
    public boolean CreateTable() {
        table = database.getCollection(TownTable.class.getSimpleName());

        //Use the town UUID as the index
        table.createIndex(Indexes.text(Constants.UUID));
        return true;
    }
}
