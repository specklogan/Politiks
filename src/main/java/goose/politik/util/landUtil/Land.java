package goose.politik.util.landUtil;

import goose.politik.Politik;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.government.Town;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.math.BigDecimal;

public class Land {
    //will be an instance of land
    private static final int minSize = 16;
    private static final int maxSize = 8192;
    private static final BigDecimal costPerArea = new BigDecimal("0.25");
    private Town townOwner;
    private Nation nationOwner;
    private PolitikPlayer playerOwner;
    private World.Environment environment;

    private Location firstLocation;
    private Location secondLocation;
    private int area;

    //Initialize some values like is fire, tnt, pvp allowed
    private boolean explosionsEnabled = false;
    private boolean fireEnabled = false;
    private boolean pvpEnabled = false;



    public Block getFirstBlock() {
        return this.firstLocation.getBlock();
    }

    public Block getSecondBlock() {
        return this.firstLocation.getBlock();
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
        return (this.playerOwner.getDisplayName() + " is the owner, with an area of " + this.area + " in dimension " + this.environment + " in town " + this.townOwner.getTownName() + " in nation " + this.nationOwner.getNationName());
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
            this.secondLocation = secondLocation;
            BigDecimal cost = costPerArea.multiply(BigDecimal.valueOf(area));
            //check if player has enough money to cover it
            if (player.canPurchase(cost)) {
                Land result = LandUtil.getLandInLand(this, chunk);
                if (result == null) {
                    //do a further check to see if it overlaps
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
}
