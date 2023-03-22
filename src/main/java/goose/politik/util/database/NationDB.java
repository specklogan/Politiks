package goose.politik.util.database;

import com.mongodb.client.MongoCursor;
import goose.politik.util.government.Nation;
import goose.politik.util.government.Town;
import org.bson.Document;

import java.util.ArrayList;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class NationDB {
    public static void loadNations() {
        for (Document document : MongoDBHandler.nationCollection.find()) {
            //read through all saved nations
            String nationName = document.getString("nationName");
            UUID leaderUUID = UUID.fromString(document.getString("leaderUUID"));
//            String capitol = document.getString("nationalCapitol");
//            ArrayList<String> allyList = (ArrayList<String>) document.get("allyList");
//            new Nation(nationName, leaderUUID, capitol, );
        }
    }

    public static void saveNation(Nation nation) {
        //saves a nation
        Document nationDocument = MongoDBHandler.nationCollection.find(eq("nationName", nation.getNationName())).first();

        Town nationCapitol = nation.getCapitol();
        String capitolName = "none";
        if (nationCapitol != null) {
            //the nation has a capitol
            capitolName = nationCapitol.getTownName();
        }

        if (nationDocument == null) {
            //no nation exists, was created before this, so go ahead and replace it
            Document updatedDocument = new Document();
            updatedDocument.put("nationName", nation.getNationName());
            updatedDocument.put("leaderUUID", nation.getLeader().getUUID().toString());
            updatedDocument.put("nationCapitol", capitolName);
            updatedDocument.put("allyList", nation.getAllies().toString());
            updatedDocument.put("enemyList", nation.getEnemies().toString());
            updatedDocument.put("taxRate", nation.getTaxRate().toString());
            updatedDocument.put("townList", nation.getTownList().toString());
            updatedDocument.put("enterMessage", nation.getEnterMessage().toString());
            MongoDBHandler.nationCollection.insertOne(updatedDocument);
        } else {
            //nation already exist
            Document updatedDocument = new Document();
            updatedDocument.put("nationName", nation.getNationName());
            updatedDocument.put("leaderUUID", nation.getLeader().getUUID().toString());
            updatedDocument.put("nationCapitol", capitolName);
            updatedDocument.put("allyList", nation.getAllies().toString());
            updatedDocument.put("enemyList", nation.getEnemies().toString());
            updatedDocument.put("taxRate", nation.getTaxRate().toString());
            updatedDocument.put("townList", nation.getTownList().toString());
            updatedDocument.put("enterMessage", nation.getEnterMessage().toString());
            MongoDBHandler.nationCollection.replaceOne(nationDocument, updatedDocument);
        }
    }
}
