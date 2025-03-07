package org.gooseapple.politiks.database.implementation.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;
import org.gooseapple.politiks.database.ITownTable;
import org.gooseapple.politiks.util.Constants;

import java.util.UUID;

public class TownTable implements ITownTable {
    private MongoDatabase database;
    private MongoCollection<Document> table;

    public TownTable(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public Town GetTown(String name) {
        return null;
    }

    @Override
    public void LoadAllTowns() {

    }

    @Override
    public Town CreateTown(PolitikPlayer mayor) {
        UUID id = UUID.randomUUID();
        Town town = new Town(id);
        town.setMayor(mayor);
        town.addPlayer(mayor);

        //Set town account
        return town;
    }

    @Override
    public void SaveAllTowns() {

    }

    @Override
    public boolean CreateTable() {
        table = database.getCollection(TownTable.class.getSimpleName());

        //Use the town UUID as the index
        table.createIndex(Indexes.text(Constants.UUID));
        return true;
    }
}
