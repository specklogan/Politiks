package org.gooseapple.politiks.database;

import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.config.ConfigManager;
import org.gooseapple.politiks.database.implementation.mongo.MongoManager;
import org.gooseapple.politiks.util.Constants;
import org.gooseapple.politiks.util.Errors;

public class DatabaseManager {
    private static IDatabase database;
    public static void loadDatabase() {
        Constants.DatabaseType configValue = ConfigManager.getDatabaseType();
        if (configValue == Constants.DatabaseType.MONGO) {
            String mongoURI = ConfigManager.getMongoURI();
            database = new MongoManager(mongoURI);
        }

        boolean success = database.Initialize();
        if (!success) {
            //If the database didn't successfully start, then disable the plugin
            Errors.LogError("Unable to start database... disabling Politiks");
            Politiks.getInstance().getServer().getPluginManager().disablePlugin(Politiks.getInstance());
        }
    }

    public static IDatabase getDatabase() {
        return database;
    }
}
