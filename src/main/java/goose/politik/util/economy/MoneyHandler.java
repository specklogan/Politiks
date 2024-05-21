package goose.politik.util.economy;

import java.math.BigDecimal;
import java.math.RoundingMode;

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
