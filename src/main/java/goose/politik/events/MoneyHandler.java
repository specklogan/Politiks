package goose.politik.events;

import goose.politik.Politik;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class MoneyHandler {
    public final void setMoney(double money, Player player) {
        Politik.mongoDB.setPlayerMoney(player, money);
    }

    public final double getMoney(Player player) {

        if (player != null) {
            return Politik.mongoDB.getPlayerMoney(player);
        } else {
            Politik.getInstance().logger.log(Level.INFO, "Error getting money for player");
            return -1;
        }
    }

    public final void changeMoney(double amount, Player player) {
        //will not let it go into the negatives
        double currentPlayerMoney = getMoney(player);
        if (currentPlayerMoney != -1) {
            if (currentPlayerMoney + amount < 0) {
                //less than zero
                setMoney(0, player);
            } else {
                setMoney(currentPlayerMoney+amount, player);
            }
        }

    }

    public final boolean canPurchase(double cost, double amount) {
        return amount - cost >= 0;
    }
}
