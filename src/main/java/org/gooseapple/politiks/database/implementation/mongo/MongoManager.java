package org.gooseapple.politiks.database.implementation.mongo;
import static org.gooseapple.politiks.util.Errors.LogError;

import com.mongodb.ConnectionString;
import com.mongodb.LoggerSettings;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClientFactory;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.database.IAccountTable;
import org.gooseapple.politiks.database.IDatabase;
import org.gooseapple.politiks.database.IPlayerTable;

public class MongoManager implements IDatabase {
    private String connectionString;
    private MongoDatabase database;
    private MongoClient client;

    /*
        Database Tables
     */
    private PlayerTable playerTable;
    private AccountTable accountTable;
    private TownTable townTable;

    public MongoManager(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public boolean Initialize() {
        try {
            ConnectionString cString = new ConnectionString(connectionString);
            MongoClient mongoClient = MongoClients.create(MongoClientSettings.builder()
                    .applyConnectionString(cString).build());
            client = mongoClient;
            database = mongoClient.getDatabase(Politiks.getInstance().getName());

            playerTable = new PlayerTable(database);
            playerTable.CreateTable();

            accountTable = new AccountTable(database);
            accountTable.CreateTable();

            townTable = new TownTable(database);
            townTable.CreateTable();

            playerTable.LoadAllPlayers();
            townTable.LoadAllTowns();
            accountTable.LoadAllAccounts();
        } catch (Exception ex) {
            LogError("Unable to initialize mongo client using connection string: " + connectionString);
            return false;
        }

        return true;
    }

    @Override
    public IPlayerTable getPlayerTable() {
        return playerTable;
    }

    @Override
    public IAccountTable getAccountTable() {
        return accountTable;
    }

    @Override
    public void Save() {
        playerTable.SaveAllPlayers();
        accountTable.SaveAllAccounts();
        townTable.SaveAllTowns();
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

}
