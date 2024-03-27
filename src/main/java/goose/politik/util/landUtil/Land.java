package goose.politik.util.landUtil;

import goose.politik.Politik;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.government.Town;
import org.bson.Document;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class Land {
    //will be an instance of land
    private static final int minSize = 16;
    private static final int maxSize = 8192;
    private static final BigDecimal costPerArea = new BigDecimal("0.25");
    public static final String type = "NORMAL";
    private Town townOwner;
    private Nation nationOwner;
    private PolitikPlayer playerOwner;
    private World.Environment environment;
    private World world;
    private Biome biome;

    private Location firstLocation;
    private Location secondLocation;
    private int area;
    private UUID uuid;

    //Initialize some values like is fire, tnt, pvp allowed
    private boolean explosionsEnabled = false;
    private boolean fireEnabled = false;
    private boolean pvpEnabled = false;

    public String getType() {
        return Land.type;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Block getFirstBlock() {
        return this.firstLocation.getBlock();
    }

    public Block getSecondBlock() {
        return this.firstLocation.getBlock();
    }

    public ArrayList<Chunk> occupiedChunks = new ArrayList<>();

    public ArrayList<Chunk> getOccupiedChunks() {
        return this.occupiedChunks;
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public void setOccupiedChunks(ArrayList<Chunk> occupiedChunks) {
        this.occupiedChunks = occupiedChunks;
    }

    public static int getX(String position) {
        int x;
        String[] split = position.split(",");
        x = Integer.parseInt(split[0]);
        return x;
    }

    public static int getY(String position) {
        int y;
        String[] split = position.split(",");
        y = Integer.parseInt(split[1]);
        return y;
    }

    public static int getZ(String position) {
        int z;
        //in the format of 10,20 where 10 = x, 20 = z
        String[] split = position.split(",");
        z = Integer.parseInt(split[2]);
        return z;
    }

    public PolitikPlayer getPlayerOwner() {
        return this.playerOwner;
    }

    public void setPlayerOwner(PolitikPlayer player) {
        this.playerOwner = player;
    }

    public void setNationOwner(Nation nation) {
        this.nationOwner = nation;
    }

    public Nation getNationOwner() {
        return this.nationOwner;
    }

    public void setTownOwner(Town town) {
        this.townOwner = town;
    }

    public Town getTownOwner() {
        return this.townOwner;
    }

    public void setPVP(boolean value) {
        this.pvpEnabled = value;
    }

    public void setExplosions(boolean value) {
        this.explosionsEnabled = value;
    }

    public void setFire(boolean value) {
        this.fireEnabled = value;
    }

    public boolean isFireEnabled() {
        return this.fireEnabled;
    }

    public boolean isExplosionsEnabled() {
        return this.explosionsEnabled;
    }

    public boolean isPvpEnabled() {
        return this.pvpEnabled;
    }

    public World.Environment getEnvironment() {
        return this.environment;
    }

    public String toString() {
        return (this.playerOwner.getDisplayName() + " " + this.getUUID());
    }


    public static int calculateArea(String firstPos, String secondPos) {
        return ((Math.abs(getX(firstPos) - getX(secondPos))+1) * (Math.abs(getZ(firstPos) - (getZ(secondPos)))+1));
    }

    public boolean validLand(String firstPos, String secondPos) {
        int totalArea = calculateArea(firstPos, secondPos);
        this.area = totalArea;
        return (totalArea <= Land.maxSize && totalArea >= Land.minSize);
    }

    public void setVisibleLand() {
        this.getFirstBlock().setType(Material.GOLD_BLOCK);
        this.getSecondBlock().setType(Material.GOLD_BLOCK);
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public Location getFirstLocation() {
        return firstLocation;
    }

    public void setFirstLocation(Location firstLocation) {
        this.firstLocation = firstLocation;
    }

    public Location getSecondLocation() {
        return secondLocation;
    }

    public void setSecondLocation(Location secondLocation) {
        this.secondLocation = secondLocation;
    }

    public void setEnvironment(World.Environment environment) {
        this.environment = environment;
    }

    public static void load(Document document) {
        UUID landIdentifier = UUID.fromString(document.getString("_id"));
        PolitikPlayer landOwner = PolitikPlayer.getPolitikPlayerFromID(UUID.fromString(document.getString("playerOwner")));
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
        land.setBiome(Biome.valueOf(document.getString("biome")));
        land.setFirstLocation(firstLocation);
        land.setEnvironment(World.Environment.valueOf(document.getString("environment")));
        land.setWorld(Bukkit.getServer().getWorld(document.getString("world")));
        land.setSecondLocation(secondLocation);
        land.setArea(document.getInteger("area"));
        land.setPlayerOwner(landOwner);
        land.setTownOwner(Town.getTownFromName(townOwner));
        land.setNationOwner(Nation.getNationFromName(nationOwner));

        ArrayList<Chunk> chunkArrayList = LandUtil.getChunksInLand(land);
        for (Chunk chunk : chunkArrayList) {
            LandUtil.addToLandMap(land, chunk);
        }
        land.setOccupiedChunks(chunkArrayList);
        LandUtil.landUUIDMap.get(World.Environment.valueOf(document.getString("environment"))).put(landIdentifier, land);
    }

    public Document toDocument() {
        //run some calculations
        ArrayList<String> chunkLongList = new ArrayList<>();
        for (Chunk chunk : this.occupiedChunks) {
            long key = chunk.getChunkKey();
            chunkLongList.add(Long.toString(key));
        }

        //you can call this to get a document from a land, can be overwritten by any of the subtypes
        Document landDocument = new Document();
        landDocument.put("_id", this.getUUID().toString());
        landDocument.put("playerOwner", this.getPlayerOwner().getUUID().toString());
        landDocument.put("firstLocation", this.firstLocation.getWorld().getName() + "," + this.firstLocation.getX() + "," + this.firstLocation.getY() + "," + this.firstLocation.getZ());
        landDocument.put("secondLocation", this.secondLocation.getWorld().getName() + "," + this.secondLocation.getX() + "," + this.secondLocation.getY() + "," + this.secondLocation.getZ());
        landDocument.put("biome", this.biome.toString());
        landDocument.put("environment", this.getEnvironment().name());
        landDocument.put("world", this.getWorld().getName());
        landDocument.put("type", "NORMAL");
        landDocument.put("area", this.area);
        landDocument.put("occupiedChunks", chunkLongList);
        landDocument.put("tickable", false);
        landDocument.put("townOwner", this.getTownOwner().getTownName());
        landDocument.put("nationOwner", this.getNationOwner().getNationName());
        return landDocument;
    }

    public Land(String firstPos, String secondPos, PolitikPlayer player, Chunk chunk) {
        //we need to make sure the player is in a town, or nation
        Nation playerNation = player.getNation();
        Town playerTown = player.getTown();
        if (playerNation == null || playerTown == null) {
            //error, becuase in order to claim land you need to join a town
            player.message(Politik.errorMessage("In order to claim land, you need to be in a town and nation"));
            return;
        }

        //Convert string location to Block block
        int fx = getX(firstPos);
        int fy = getY(firstPos);
        int fz = getZ(firstPos);
        Location firstLocation = new Location(chunk.getWorld(), fx, fy, fz);

        int sx = getX(secondPos);
        int sy = getY(secondPos);
        int sz = getZ(secondPos);
        Location secondLocation = new Location(chunk.getWorld(), sx, sy, sz);

        if (validLand(firstPos, secondPos)) {
            //it is valid
            this.playerOwner = player;
            this.environment = chunk.getWorld().getEnvironment();
            this.firstLocation = firstLocation;
            this.biome = firstLocation.getBlock().getBiome();
            this.secondLocation = secondLocation;
            this.setWorld(firstLocation.getWorld());
            this.setEnvironment(this.world.getEnvironment());
            this.setArea(Land.calculateArea(firstPos, secondPos));
            this.setUUID(UUID.randomUUID());
            BigDecimal cost = costPerArea.multiply(BigDecimal.valueOf(area));
            //check if player has enough money to cover it
            if (player.canPurchase(cost)) {
                Land result = LandUtil.getLandInLand(this, chunk);
                if (result == null) {
                    this.setOccupiedChunks(LandUtil.getChunksInLand(this));
                    this.playerOwner.message(Politik.successMessage("You've successfully claimed " + this.area + " blocks, costing you $" + cost));
                    player.changeMoney(cost.negate());
                    this.setNationOwner(playerNation);
                    this.setTownOwner(playerTown);
                } else {
                    this.playerOwner.message(Politik.errorMessage("Land claim overlaps with an existing claim"));
                }
            } else {
                player.message(Politik.errorMessage("You don't have enough funds to cover this purchase"));
            }
        } else {
            player.message(Politik.errorMessage("Incorrect area, minimum area size is 16, maximum is 8192"));
        }
    }

    public Land() {

    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }
}
