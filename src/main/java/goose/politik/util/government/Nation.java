package goose.politik.util.government;

import goose.politik.util.database.MongoDBHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

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
    private Town capitol;
    private ArrayList<Nation> allies = new ArrayList<>();
    private ArrayList<Nation> enemies = new ArrayList<>();
    private BigDecimal taxRate = new BigDecimal("20");

    public static void saveNations() {
        //saves everything to database
        for (Nation nation : Nation.NATIONS) {
            MongoDBHandler.saveNation(nation);
        }
    }

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

    public void setCapitol(Town capitol) {
        this.capitol = capitol;
    }

    public Town getCapitol() {
        return this.capitol;
    }



    public Nation(String nationName, PolitikPlayer leader) {
        this.nationName = nationName;
        this.leader = leader;
        addToPlayerList(leader);
        leader.setNation(this);
        NATIONS.add(this);
    }

    public Nation(String nationName, UUID leader, Town capitol, ArrayList<String> allies, ArrayList<String> enemies, BigDecimal taxRate, ArrayList<Town> towns) {
        //called to create a nation on loading of the database

    }
}
