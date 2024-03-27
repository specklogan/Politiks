package goose.politik.util.government;

import goose.politik.Politik;
import goose.politik.util.database.TownDB;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Town {
    private String townName;
    private PolitikPlayer mayor;
    private TextComponent enterMessage = Component.text("");
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

    public static void saveTowns() {
        for (Nation nation : Nation.NATIONS) {
            for (Town town : nation.getTownList()) {
                TownDB.saveTown(town);
            }
        }
    }

    public String getTownName() {
        return this.townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public void setMayor(PolitikPlayer mayor) {
        this.mayor = mayor;
    }

    public TextComponent getEnterMessage() {
        return enterMessage;
    }

    public void setEnterMessage(TextComponent enterMessage) {
        this.enterMessage = enterMessage;
    }

    public Nation getNationOwner() {
        return nationOwner;
    }

    public void setNationOwner(Nation nationOwner) {
        this.nationOwner = nationOwner;
    }

    public ArrayList<PolitikPlayer> getPlayerList() {
        return playerList;
    }

    public void setPlayerList(ArrayList<PolitikPlayer> playerList) {
        this.playerList = playerList;
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
    }

    public Town() {

    }
}
