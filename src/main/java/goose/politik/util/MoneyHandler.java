package goose.politik.util;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;

public class MoneyHandler {
    public static BigDecimal moneyRound(BigDecimal money) {
        //round it if there are more than two decimal places
        if (money.scale() > 2) {
            //more than two decimal places
            money = money.setScale(2, RoundingMode.CEILING);
        }
        return money;
    }
}
