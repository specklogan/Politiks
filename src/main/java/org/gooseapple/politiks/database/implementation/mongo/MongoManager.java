package org.gooseapple.politiks.database.implementation.mongo;
import static org.gooseapple.politiks.util.Errors.LogError;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.database.IDatabase;
import org.gooseapple.politiks.database.IPlayerTable;

public class MongoManager implements IDatabase {
    private String connectionString;
    private MongoDatabase database;

    /*
        Database Tables
     */
    private PlayerTable playerTable;

    public MongoManager(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public boolean Initialize() {
        try (MongoClient mongoClient = MongoClients.create(connectionString)) {
            database = mongoClient.getDatabase(Politiks.plugin.getName());

            playerTable = new PlayerTable(database);
            playerTable.create();

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

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

}
