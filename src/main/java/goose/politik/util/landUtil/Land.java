package goose.politik.util.landUtil;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import org.bukkit.entity.Player;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.TreeMap;

public class Land {
    //will be an instance of land
    private static final int minSize = 16;
    private static final int maxSize = 4096;
    private static final BigDecimal costPerArea = new BigDecimal("0.25");
    private static HashMap<Integer, Land> hashMap = new HashMap<Integer, Land>();
    private String townOwner;
    private String nationOwner;
    private PolitikPlayer playerOwner;

    private String firstPos;
    private String secondPos;
    private int area;

    public static boolean playerInLand(Player player) {
        return false;
    }

    public static boolean claimOverlaps(Land land) {
        return false;
    }


    public static int getX(String position) {
        int x;
        String[] split = position.split(",");
        x = Integer.parseInt(split[0]);
        return x;
    }

    public static int getZ(String position) {
        int z;
        //in the format of 10,20 where 10 = x, 20 = z
        String[] split = position.split(",");
        z = Integer.parseInt(split[1]);
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
    public Land(String firstPos, String secondPos, PolitikPlayer player) {
        if (validLand(firstPos, secondPos)) {
            //it is valid
            this.firstPos = firstPos;
            this.playerOwner = player;
            this.secondPos = secondPos;
            BigDecimal cost = costPerArea.multiply(BigDecimal.valueOf(area));
            //check if player has enough money to cover it
            if (player.canPurchase(cost)) {
                this.playerOwner.message(Politik.successMessage("You've successfully claimed " + this.area + " blocks, costing you $" + cost));
                player.changeMoney(cost.negate());
            } else {
                player.message(Politik.errorMessage("You don't have enough funds to cover this purchase"));
            }
        } else {
            player.message(Politik.errorMessage("Area was too small, minimum area size is 16, maximum is 4096"));
        }
    }

    //It should cost 25 cents per block to claim,
}
