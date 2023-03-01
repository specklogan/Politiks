package goose.politik.util.government;

import goose.politik.Politik;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Nation {

    //Static stuff
    public static final BigDecimal NATIONCOST = new BigDecimal("1000");
    public static final ArrayList<Nation> NATIONS = new ArrayList<>();

    private TextComponent enterMessage;
    private String nationName;
    private PolitikPlayer leader;
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

    public void addTown(Town town) {
        if (this.townList == null) {
            this.townList = new ArrayList<>();
        }
        this.townList.add(town);
    }

    public ArrayList<PolitikPlayer> getPlayerList() {
        return playerList;
    }

    public void addToPlayerList(PolitikPlayer player) {
        if (this.playerList == null) {
            this.playerList = new ArrayList<>();
        }
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
        leader.message(Politik.successMessage("Successfully created nation: " + nationName));
        this.nationName = nationName;
        this.leader = leader;
        addToPlayerList(leader);
        leader.setNation(this);
        NATIONS.add(this);
    }
}
