package goose.politik.util.government;

import java.awt.*;
import java.math.BigDecimal;

public class Town {
    private String townName;
    private PolitikPlayer mayor;
    private TextComponent enterMessage;
    private static final BigDecimal townCost = new BigDecimal("250");

    public String getTownName() {
        return this.townName;
    }
}
