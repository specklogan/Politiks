package goose.politik.util.database;

import org.bson.Document;

public interface Saveable {
    public Document toDocument();
}
