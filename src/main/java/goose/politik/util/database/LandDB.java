package goose.politik.util.database;

import goose.politik.Politik;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.government.Town;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bson.Document;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class LandDB {
    public static void saveLand(Land land) {
        if (land.getFirstLocation().getWorld().getEnvironment() == World.Environment.NORMAL) {
            //save to overworld database
            int area = land.getArea();
            Location firstLocation = land.getFirstLocation();
            Location secondLocation = land.getSecondLocation();
            UUID playerOwner = land.getPlayerOwner().getUUID();
            UUID landIdentifier = land.getUUID();
            String townOwner = land.getTownOwner().getTownName();
            String nationOwner = land.getNationOwner().getNationName();

            //check if land is in a database or not
            Document landDocument = MongoDBHandler.overworldLand.find(eq("uuid", landIdentifier.toString())).first();

            if (landDocument == null) {
                //add new land to database
                Document newLandDocument = new Document();
                newLandDocument.put("uuid", landIdentifier.toString());
                newLandDocument.put("landOwner", playerOwner.toString());
                newLandDocument.put("firstLocation", firstLocation.getWorld().getName() + "," + firstLocation.getX() + "," + firstLocation.getY() + "," + firstLocation.getZ());
                newLandDocument.put("secondLocation", secondLocation.getWorld().getName() + "," + secondLocation.getX() + "," + secondLocation.getY() + "," + secondLocation.getZ());
                newLandDocument.put("area", area);
                newLandDocument.put("townOwner", townOwner);
                newLandDocument.put("nationOwner", nationOwner);
                MongoDBHandler.overworldLand.insertOne(newLandDocument);
            } else {
                //existing land
                Document newLandDocument = new Document();
                newLandDocument.put("uuid", landIdentifier.toString());
                newLandDocument.put("landOwner", playerOwner.toString());
                newLandDocument.put("firstLocation", firstLocation.getWorld().getName() + "," + firstLocation.getX() + "," + firstLocation.getY() + "," + firstLocation.getZ());
                newLandDocument.put("secondLocation", secondLocation.getWorld().getName() + "," + secondLocation.getX() + "," + secondLocation.getY() + "," + secondLocation.getZ());
                newLandDocument.put("area", area);
                newLandDocument.put("townOwner", townOwner);
                newLandDocument.put("nationOwner", nationOwner);
                MongoDBHandler.overworldLand.replaceOne(landDocument, newLandDocument);
            }
        }
    }

    public static void loadLands() {
        for (Document document : MongoDBHandler.overworldLand.find()) {
            UUID landIdentifier = UUID.fromString(document.getString("uuid"));
            PolitikPlayer landOwner = PolitikPlayer.getPolitikPlayerFromID(UUID.fromString(document.getString("landOwner")));
            String firstLoc = document.getString("firstLocation");
            String[] firstLocSplit = firstLoc.split(",");
            Location firstLocation = new Location(Politik.getInstance().getServer().getWorld(firstLocSplit[0]), Double.parseDouble(firstLocSplit[1]), Double.parseDouble(firstLocSplit[2]), Double.parseDouble(firstLocSplit[3]));
            String secondLoc = document.getString("secondLocation");
            String[] secondLocSplit = secondLoc.split(",");
            Location secondLocation = new Location(Politik.getInstance().getServer().getWorld(secondLocSplit[0]), Double.parseDouble(secondLocSplit[1]), Double.parseDouble(secondLocSplit[2]), Double.parseDouble(secondLocSplit[3]));
            String townOwner = document.getString("townOwner");
            String nationOwner = document.getString("nationOwner");

            Land land = new Land();
            land.setUUID(landIdentifier);
            land.setFirstLocation(firstLocation);
            land.setSecondLocation(secondLocation);
            land.setPlayerOwner(landOwner);
            land.setTownOwner(Town.getTownFromName(townOwner));
            land.setNationOwner(Nation.getNationFromName(nationOwner));

            LandUtil.getLandInLand(land, firstLocation.getChunk());
        }
    }
}
