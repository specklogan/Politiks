package org.gooseapple.politiks.core.town;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.gooseapple.politiks.core.currency.Account;
import org.gooseapple.politiks.core.currency.IAccountHolder;
import org.gooseapple.politiks.core.nation.Nation;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.INationTable;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Town implements IAccountHolder {
    private String townName;
    private PolitikPlayer mayor;
    private Location spawnLocation;
    private UUID nationId;
    public TownConfig config;
    private final UUID id;
    private CopyOnWriteArrayList<PolitikPlayer> playerList = new CopyOnWriteArrayList<>();

    public Town(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
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

    public void setNationId(UUID nationId) {
        this.nationId = nationId;
    }

    private static INationTable GetTable() {
        return DatabaseManager.getDatabase().getNationTable();
    }

    public CopyOnWriteArrayList<PolitikPlayer> getPlayerList() {
        return playerList;
    }

    public void removePlayer(PolitikPlayer player) {
        this.playerList.remove(player);
        player.setTown(null);
        player.setNation(null);
    }

    public boolean containsPlayer(PolitikPlayer player) {
        return this.playerList.contains(player);
    }

    public void setPlayerList(CopyOnWriteArrayList<PolitikPlayer> playerList) {
        this.playerList = playerList;
    }

    public void addPlayer(PolitikPlayer player) {
        this.playerList.add(player);
    }

    public PolitikPlayer getMayor() {
        return this.mayor;
    }

    public Nation getNation() {
        return GetTable().GetNation(this.getNationId());
    }

    public UUID getNationId() {
        return this.nationId;
    }

    public void setSpawnLocation(Location location) {
        this.spawnLocation = location;
    }

    public Location getSpawnLocation() {
        return this.spawnLocation;
    }

    @Override
    public Account GetAccount() {
        return null;
    }

    @Override
    public void AddAccount(Account account) {

    }

    @Override
    public UUID GetAccountHolderUUID() {
        return this.id;
    }
}
