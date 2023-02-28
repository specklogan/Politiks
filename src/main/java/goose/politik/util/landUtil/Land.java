package goose.politik.util.landUtil;

import goose.politik.Politik;
import goose.politik.util.MongoDBHandler;
import goose.politik.util.government.PolitikPlayer;
import io.papermc.paper.math.BlockPosition;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Land {
    //will be an instance of land
    private static final int minSize = 16;
    private static final int maxSize = 8192;
    private static final BigDecimal costPerArea = new BigDecimal("0.25");
    private String townOwner;
    private String nationOwner;
    private PolitikPlayer playerOwner;
    private World.Environment environment;

    private String firstPos;
    private String secondPos;
    private Block firstBlock;
    private Block secondBlock;
    private int area;

    public String getFirstPos() {
        return this.firstPos;
    }

    public String getSecondPos() {
        return this.secondPos;
    }

    public Block getFirstBlock() {
        return this.firstBlock;
    }

    public Block getSecondBlock() {
        return this.secondBlock;
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

    public static int calculateArea(String firstPos, String secondPos) {
        return ((Math.abs(getX(firstPos) - getX(secondPos))+1) * (Math.abs(getZ(firstPos) - (getZ(secondPos)))+1));
    }

    public boolean validLand(String firstPos, String secondPos) {
        int totalArea = calculateArea(firstPos, secondPos);
        this.area = totalArea;
        return (totalArea <= Land.maxSize && totalArea >= Land.minSize);
    }

    public void setVisibleLand() {
        this.firstBlock.setType(Material.GOLD_BLOCK);
        this.secondBlock.setType(Material.GOLD_BLOCK);
    }
    public Land(String firstPos, String secondPos, PolitikPlayer player, Chunk chunk) {
        //Convert string location to Block block
        int fx = getX(firstPos);
        int fy = getY(firstPos);
        int fz = getZ(firstPos);
        Block firstBlock = chunk.getWorld().getBlockAt(fx,fy,fz);

        int sx = getX(secondPos);
        int sy = getY(secondPos);
        int sz = getZ(secondPos);
        Block secondBlock = chunk.getWorld().getBlockAt(sx,sy,sz);

        if (validLand(firstPos, secondPos)) {
            //it is valid
            this.firstPos = firstPos;
            this.playerOwner = player;
            this.secondPos = secondPos;
            this.environment = chunk.getWorld().getEnvironment();
            this.firstBlock = firstBlock;
            this.secondBlock = secondBlock;
            BigDecimal cost = costPerArea.multiply(BigDecimal.valueOf(area));
            //check if player has enough money to cover it
            if (player.canPurchase(cost)) {
                Land result = LandUtil.getLandInLand(this, chunk);
                if (result == null) {
                    //do a further check to see if it overlaps
                    this.playerOwner.message(Politik.successMessage("You've successfully claimed " + this.area + " blocks, costing you $" + cost));
                    player.changeMoney(cost.negate());
                } else {
                    System.out.println(result);
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
