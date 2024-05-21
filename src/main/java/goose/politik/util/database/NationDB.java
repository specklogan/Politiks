package goose.politik.util.database;

import goose.politik.government.nation.Nation;
import goose.politik.player.PolitikPlayer;
import goose.politik.government.town.Town;
import org.bson.Document;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class NationDB {
    public static void loadNations() {
        for (Document document : DatabaseHandler.nationCollection.find()) {
            //read through all saved nations
            String nationName = document.getString("nationName");
            UUID leaderUUID = UUID.fromString(document.getString("leaderUUID"));
            String capitol = document.getString("nationCapitol");
            BigDecimal taxRate = new BigDecimal(document.getString("taxRate"));
            Nation nation = new Nation(nationName, PolitikPlayer.getPolitikPlayerFromID(leaderUUID));
            nation.setTaxRate(taxRate);

            //create blank capitol town
            if (capitol != null) {
                Town capitolTown = TownDB.loadTown(capitol);
                if (capitolTown == null) {
                    continue;
                }
                capitolTown.setNationOwner(nation);
                capitolTown.setTownName(capitol);
                nation.addTown(capitolTown);
            }
        }
    }

    public static void saveNation(Nation nation) {
        //saves a nation
        Document nationDocument = DatabaseHandler.nationCollection.find(eq("nationName", nation.getNationName())).first();

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
            DatabaseHandler.nationCollection.insertOne(updatedDocument);
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
            DatabaseHandler.nationCollection.replaceOne(nationDocument, updatedDocument);
        }
    }
}
