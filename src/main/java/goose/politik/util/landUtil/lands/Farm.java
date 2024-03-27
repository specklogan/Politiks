package goose.politik.util.landUtil.lands;

import goose.politik.Politik;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.government.Town;
import goose.politik.util.landUtil.LandUtil;
import goose.politik.util.landUtil.TickableLand;
import org.bson.Document;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class Farm extends TickableLand {

    public static final String type = "FARM";

    public Farm() {

    }

    @Override
    public String getType() {
        return Farm.type;
    }

    @Override
    public void onTickEvent() {
        //run code for a farm on each event
        if (this.getBiome() == Biome.PLAINS) {
            //boost production
            int amount = 2 * getProductionValue();
            ItemStack items = new ItemStack(Material.WHEAT, amount);
            if (this.getPlayerOwner().getPlayer() != null) {
                this.getPlayerOwner().getPlayer().getInventory().addItem(items);
            }
        } else {
            int amount = getProductionValue();
            ItemStack items = new ItemStack(Material.WHEAT, amount);
            if (this.getPlayerOwner().getPlayer() != null) {
                this.getPlayerOwner().getPlayer().getInventory().addItem(items);
            }
        }
    }

    @Override
    public Document toDocument() {
        Document farmDocument = super.toDocument();
        farmDocument.replace("tickable", true);
        farmDocument.replace("type", "FARM");
        return farmDocument;
    }


    public static Farm liteLoad(Document document) {
        //intended to load the bare minimum values to make loading for landtick as smooth as possible
        Farm farm = new Farm();
        farm.setBiome(Biome.valueOf(document.getString("biome")));
        farm.setArea(document.getInteger("area"));
        farm.setPlayerOwner(PolitikPlayer.getPolitikPlayerFromID(UUID.fromString(document.getString("playerOwner"))));
        farm.setNationOwner(Nation.getNationFromName(document.getString("nationOwner")));
        farm.setTownOwner(Town.getTownFromName(document.getString("townOwner")));
        farm.setProductionValue(farm.getArea()/10);
        return farm;
    }


    public static void load(Document document, Farm farm) {
        farm.setArea(document.getInteger("area"));
        farm.setUUID(UUID.fromString(document.getString("_id")));
        farm.setTownOwner(Town.getTownFromName(document.getString("townOwner")));
        farm.setNationOwner(Nation.getNationFromName(document.getString("nationOwner")));
        farm.setPlayerOwner(PolitikPlayer.getPolitikPlayerFromID(UUID.fromString(document.getString("playerOwner"))));
        farm.setProductionValue(farm.getArea()/10);

        //setup location
        String firstLoc = document.getString("firstLocation");
        String[] firstLocSplit = firstLoc.split(",");
        String secondLoc = document.getString("secondLocation");
        String[] secondLocSplit = secondLoc.split(",");
        Location firstLocation = new Location(Politik.getInstance().getServer().getWorld(firstLocSplit[0]), Double.parseDouble(firstLocSplit[1]), Double.parseDouble(firstLocSplit[2]), Double.parseDouble(firstLocSplit[3]));
        Location secondLocation = new Location(Politik.getInstance().getServer().getWorld(secondLocSplit[0]), Double.parseDouble(secondLocSplit[1]), Double.parseDouble(secondLocSplit[2]), Double.parseDouble(secondLocSplit[3]));

        farm.setBiome(Biome.valueOf(document.getString("biome")));
        farm.setEnvironment(World.Environment.valueOf(document.getString("environment")));
        farm.setWorld(Politik.getInstance().getServer().getWorld(document.getString("world")));
        farm.setFirstLocation(firstLocation);
        farm.setSecondLocation(secondLocation);
        ArrayList<Chunk> chunkArrayList = LandUtil.getChunksInLand(farm);
        for (Chunk chunk : chunkArrayList) {
            LandUtil.addToLandMap(farm, chunk);
        }
        LandUtil.landUUIDMap.get(World.Environment.valueOf(document.getString("environment"))).put(UUID.fromString(document.getString("_id")), farm);
        farm.setOccupiedChunks(chunkArrayList);
    }
}
