package org.gooseapple.politiks.database.implementation.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import com.mongodb.client.model.WriteModel;
import org.bson.Document;
import org.checkerframework.checker.units.qual.N;
import org.gooseapple.politiks.core.nation.Nation;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.INationTable;
import org.gooseapple.politiks.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class NationTable implements INationTable {
    private MongoDatabase database;
    private MongoCollection<Document> table;
    private ConcurrentHashMap<UUID, Nation> nations = new ConcurrentHashMap<>();

    public NationTable(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public Nation CreateNation(PolitikPlayer leader, String name) {
        UUID id = UUID.randomUUID();
        Nation nation = new Nation(id);
        nation.setCapitol(leader.getTown());
        nation.setLeader(leader);

        return nation;
    }

    @Override
    public void LoadAllNations() {
        for (Document d : table.find()) {
            Nation n = NationFromDocument(d);
            nations.put(n.getId(), n);
        }
    }

    @Override
    public void SaveAllNations() {
        List<WriteModel<Document>> operation = new ArrayList<>();

        for (UUID id : nations.keySet()) {
            Document nationDoc = NationToDocument(nations.get(id));

            Document filter = new Document(Constants.UUID, nations.get(id).getId().toString());

            ReplaceOneModel<Document> replaceOneModel = new ReplaceOneModel<>(
                    filter,
                    nationDoc,
                    new ReplaceOptions().upsert(true)
            );
            operation.add(replaceOneModel);
        }

        if (operation.isEmpty()) {
            return;
        }
        table.bulkWrite(operation);
    }

    private Document NationToDocument(Nation n) {
        Document d = new Document();
        d.put(Constants.UUID, n.getId().toString());
        d.put(Constants.Name, n.getNationName());
        d.put(Constants.Leader, n.getLeader().getUUID().toString());
        d.put(Constants.Capitol, n.getCapitol().getId().toString());
        return d;
    }

    private Nation NationFromDocument(Document d) {
        UUID id = UUID.fromString(d.getString(Constants.UUID));

        Nation n = new Nation(id);
        n.setName(d.getString(Constants.Name));
        PolitikPlayer p = DatabaseManager.getDatabase().getPlayerTable().GetPlayer(UUID.fromString(d.getString(Constants.Leader)));
        n.setLeader(p);

        Town t = DatabaseManager.getDatabase().getTownTable().GetTown(UUID.fromString(d.getString(Constants.Capitol)));

        n.setCapitol(t);

        return n;
    }

    @Override
    public Nation GetNation(String name) {
        for (UUID id : nations.keySet()) {
            Nation n = nations.get(id);
            if (n.getNationName().equalsIgnoreCase(name)) {
                return n;
            }
        }
        return null;
    }

    @Override
    public Nation GetNation(UUID id) {
        if (id == null) {
            return null;
        }
        if (!nations.containsKey(id)) {
            return null;
        }
        return nations.get(id);
    }

    @Override
    public boolean NationNameExists(String name) {
        for (UUID id : nations.keySet()) {
            Nation n = nations.get(id);
            if (n.getNationName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public CopyOnWriteArrayList<PolitikPlayer> GetNationPlayers(Nation nation) {
        return null;
    }

    @Override
    public boolean CreateTable() {
        table = database.getCollection(NationTable.class.getSimpleName());

        //Use the town UUID as the index
        table.createIndex(Indexes.text(Constants.UUID));
        return true;
    }
}
