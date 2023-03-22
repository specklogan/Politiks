package goose.politik.util.database;

import com.mongodb.client.MongoCursor;
import goose.politik.util.government.Nation;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

public class NationDB {
    public static void loadNations() {
        for (Document document : MongoDBHandler.nationCollection.find()) {
            //read through all saved nations
            String nationName = document.getString("nationName");
//            UUID leaderUUID = UUID.fromString(document.getString("leaderUUID"));
//            String capitol = document.getString("nationalCapitol");
//            ArrayList<String> allyList = (ArrayList<String>) document.get("allyList");
//            new Nation(nationName, leaderUUID, capitol, );
        }
    }
}
