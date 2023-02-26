package goose.politik.util.government;

import goose.politik.Politik;
import goose.politik.util.MoneyHandler;
import goose.politik.util.MongoDBHandler;
import org.bukkit.entity.Player;

import java.math.BigDecimal;

public class Nation {
    private String enterMessage;
    private String nationName;
    private Player leader;
    private static BigDecimal nationCost = new BigDecimal("1000");
    private Town[] townList;

    public static boolean playerInNation(Player player) {

        return false;
    }

    public Nation(String nationName, PolitikPlayer leader) {
        //create new nation
        //make sure they have the money to do so
        if (leader.canPurchase(nationCost)) {
            leader.message(Politik.successMessage("You've successfully bought your town for $1000"));
            leader.changeMoney(new BigDecimal("-1000"));
        }
    }
}
