package goose.politik.util.database;

import com.mongodb.client.MongoCursor;
import goose.politik.Politik;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.government.Town;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.mongodb.client.model.Filters.eq;

public class NationDB {
    public static void loadNations() {
        for (Document document : MongoDBHandler.nationCollection.find()) {
            //read through all saved nations
            String nationName = document.getString("nationName");
            UUID leaderUUID = UUID.fromString(document.getString("leaderUUID"));
            String capitol = document.getString("nationalCapitol");
            BigDecimal taxRate = new BigDecimal(document.getString("taxRate"));
            //TextComponent enterMessage = Component.text(enterMessageArr[1]);
            Politik.logger.log(Level.INFO, "Loading nation from database: " + capitol);
            Nation nation = new Nation(nationName, PolitikPlayer.getPolitikPlayerFromID(leaderUUID));
            //nation.setEnterMessage(enterMessage);
            nation.setTaxRate(taxRate);
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

        ArrayList<String> townList = new ArrayList<String>();
        for (Town town : nation.getTownList()) {
            townList.add(town.getTownName());
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
            updatedDocument.put("townList", townList);
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
            updatedDocument.put("townList", townList);
            updatedDocument.put("enterMessage", nation.getEnterMessage().toString());
            MongoDBHandler.nationCollection.replaceOne(nationDocument, updatedDocument);
        }
    }
}
