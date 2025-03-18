package org.gooseapple.politiks.core.town;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.math.BigDecimal;

public class TownConfig {
    private final Town town;
    public TownConfig(Town t) {
        this.town = t;
        TownEnterText = Component.text("Entering " + town.getTownName());
    }

    public boolean ExplosionsEnabled = false;
    public boolean MobSpawningEnabled = false;
    public boolean PVPEnabled = false;
    public boolean FireEnabled = false;
    public boolean OpenJoins = true;
    public TextComponent TownEnterText;
    public BigDecimal TeleportCost = new BigDecimal("0.25");
    public BigDecimal TaxAmount = new BigDecimal("5.00");
}
