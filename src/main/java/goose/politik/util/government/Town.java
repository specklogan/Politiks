package goose.politik.util.government;

import goose.politik.Politik;
import org.bukkit.Location;

import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Town {
    private String townName;
    private PolitikPlayer mayor;
    private TextComponent enterMessage;
    private Location spawnLocation;
    private Nation nationOwner;
    public static final BigDecimal TOWNCOST = new BigDecimal("250");
    private ArrayList<PolitikPlayer> playerList;

    //static
    public static Town getTownFromName(String townName) {
        for (Nation nation : Nation.NATIONS) {
            for (Town town : nation.getTownList()) {
                if (town.getTownName().equals(townName)) {
                    return town;
                }
            }
        }
        return null;
    }

    public String getTownName() {
        return this.townName;
    }

    public void addPlayer(PolitikPlayer player) {
        if (this.playerList == null) {
            this.playerList = new ArrayList<>();
        }
        this.playerList.add(player);
    }

    public PolitikPlayer getMayor() {
        return this.mayor;
    }

    public Nation getNation() {
        return this.nationOwner;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    public Town(String townName, PolitikPlayer mayor, Nation owner) {
        this.townName = townName;
        this.mayor = mayor;
        this.nationOwner = owner;
        this.addPlayer(mayor);
        owner.addTown(this);
        mayor.setTown(this);
    }
}
