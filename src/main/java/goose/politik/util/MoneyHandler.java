package goose.politik.util;

import goose.politik.Politik;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;

public class MoneyHandler {
    public final void setMoney(BigDecimal money, Player player) {
        Politik.mongoDB.setPlayerMoney(player, money);
    }

    public final BigDecimal getMoney(Player player) {

        if (player != null) {
            return Politik.mongoDB.getPlayerMoney(player);
        } else {
            Politik.getInstance().logger.log(Level.INFO, "Error getting money for player");
            return new BigDecimal("-1");
        }
    }

    public static BigDecimal moneyRound(BigDecimal money) {
        //round it if there are more than two decimal places
        if (money.scale() > 2) {
            //more than two decimal places
            money = money.setScale(2, RoundingMode.CEILING);
        }
        return money;
    }

    public final void changeMoney(BigDecimal amount, Player player) {
        //will not let it go into the negatives
        BigDecimal currentPlayerMoney = getMoney(player);
        if (!currentPlayerMoney.equals(new BigDecimal("-1"))) {
            BigDecimal result = currentPlayerMoney.add(amount);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                //less than zero
                setMoney(BigDecimal.ZERO, player);
            } else {
                setMoney(result, player);
            }
        }

    }

    public final boolean canPurchase(BigDecimal cost, BigDecimal amount) {
        return amount.subtract(cost).compareTo(BigDecimal.ZERO) >= 0;
    }
}
