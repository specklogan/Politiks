package goose.politik.util.config;

import goose.politik.util.database.Saveable;
import org.bson.Document;

/**
 * Used as a default template for generating a config file if none exists.
 */
public class Config {
    private boolean tickable = false;
    private String connectionURL = "mongodb://localhost:27017";
    private int tickFrequency = 1; //in days

    public String getURL() {
        return connectionURL;
    }
}
