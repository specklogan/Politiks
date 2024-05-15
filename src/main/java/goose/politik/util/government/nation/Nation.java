package goose.politik.util.government.nation;

import goose.politik.util.database.NationDB;
import goose.politik.util.government.town.Town;
import goose.politik.util.player.PolitikPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Nation {

    //Static stuff
    public static final CopyOnWriteArrayList<Nation> NATIONS = new CopyOnWriteArrayList<>();

    private TextComponent enterMessage = Component.text("");

    private String nationName;
    private PolitikPlayer leader;
    private final ArrayList<PolitikPlayer> playerList = new ArrayList<>();
    private final ArrayList<Town> townList = new ArrayList<>();

    //Political stuff
    private Town capitol;
    private final ArrayList<Nation> allies = new ArrayList<>();
    private final ArrayList<Nation> enemies = new ArrayList<>();
    private BigDecimal taxRate = new BigDecimal("20");

    public static void saveNations() {
        //saves everything to database
        for (Nation nation : Nation.NATIONS) {
            NationDB.saveNation(nation);
        }
    }

    public static Nation getNationFromName(String nationName) {
        for (Nation nation : Nation.NATIONS) {
            if (nation.getNationName().equals(nationName)) {
                return nation;
            }
        }
        return null;
    }

    public void setEnterMessage(TextComponent enterMessage) {
        this.enterMessage = enterMessage;
    }

    public String getNationName() {
        return this.nationName;
    }

    public ArrayList<Town> getTownList() {
        return this.townList;
    }

    public void addTown(Town town) {
        this.townList.add(town);
    }

    public ArrayList<PolitikPlayer> getPlayerList() {
        return playerList;
    }

    public void addPlayer(PolitikPlayer player) {
        this.playerList.add(player);
    }

    public void removePlayer(PolitikPlayer player) {
        this.playerList.remove(player);
    }

    public boolean containsPlayer(PolitikPlayer player) {
        return this.playerList.contains(player);
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
        addPlayer(leader);
        leader.setNation(this);
        NATIONS.add(this);
    }

    public Nation(String nationName, UUID leader, Town capitol, ArrayList<String> allies, ArrayList<String> enemies, BigDecimal taxRate, ArrayList<Town> towns) {
        //called to create a nation on loading of the database

    }
}
