package goose.politik.util.landUtil;

import goose.politik.Politik;
import goose.politik.util.MoneyHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.logging.Level;

public class Land {
    //will be an instance of land
    private static final int minSize = 16;
    private static final int maxSize = 4096;
    private static final BigDecimal costPerArea = new BigDecimal("0.25");
    private String townOwner;
    private String nationOwner;
    private Player playerOwner;

    private String firstPos;
    private String secondPos;
    private int area;

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
        boolean valid = (totalArea <= Land.maxSize && totalArea >= Land.minSize);
        return valid;
    }
    public Land(String firstPos, String secondPos, Player player) {
        if (validLand(firstPos, secondPos)) {
            //it is valid
            this.firstPos = firstPos;
            this.playerOwner = player;
            this.secondPos = secondPos;
            BigDecimal cost = costPerArea.multiply(BigDecimal.valueOf(area));
            //check if player has enough money to cover it
            if (Politik.moneyHandler.canPurchase(cost, Politik.moneyHandler.getMoney(player))) {
                this.playerOwner.sendMessage(Politik.successMessage("You've successfully claimed " + this.area + " blocks, costing you $" + cost.toString()));
                Politik.moneyHandler.changeMoney(cost.negate(), player);
            } else {
                this.playerOwner.sendMessage(Politik.errorMessage("You don't have enough funds to cover this purchase"));
            }
        } else {
            player.sendMessage(Politik.errorMessage("Area was too small, minimum area size is 16, maximum is 4096"));
        }
    }

    //It should cost 25 cents per block to claim,
}
