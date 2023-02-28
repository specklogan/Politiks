package goose.politik.util.government;

import goose.politik.Politik;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Nation {
    private TextComponent enterMessage;
    private String nationName;
    private PolitikPlayer leader;
    private static final BigDecimal nationCost = new BigDecimal("1000");
    private ArrayList<PolitikPlayer> playerList;
    private ArrayList<Town> townList;

    //Political stuff
    private ArrayList<Nation> allies;
    private ArrayList<Nation> enemies;
    private BigDecimal taxRate = new BigDecimal("20");

    public String getNationName() {
        return this.nationName;
    }

    public ArrayList<Town> getTownList() {
        return this.townList;
    }

    public ArrayList<PolitikPlayer> getPlayerList() {
        return playerList;
    }

    public void addToPlayerList(PolitikPlayer player) {
        this.playerList.add(player);
    }

    public TextComponent getEnterMessage() {
        return this.enterMessage;
    }

    public PolitikPlayer getLeader() {
        return this.leader;
    }

    public ArrayList<Nation> getAllies() {
        return this.allies;
    }

    public ArrayList<Nation> getEnemies() {
        return this.enemies;
    }

    public void addEnemy(Nation enemy) {
        //do check to see if they currently have an ally, or if the enemy is already in the list
        if (!this.enemies.contains(enemy)) {
            if (this.allies.contains(enemy)) {
                //remove them from our allies and into our enemies
                this.allies.remove(enemy);
                this.enemies.add(enemy);
            }
        }
    }

    public BigDecimal getTaxRate() {
        return this.taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
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
