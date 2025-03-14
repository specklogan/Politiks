package org.gooseapple.politiks.database.implementation.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.gooseapple.politiks.core.nation.Nation;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.INationTable;
import org.gooseapple.politiks.util.Constants;

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

    }

    @Override
    public void SaveAllNations() {

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
