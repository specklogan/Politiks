package org.gooseapple.politiks.core.town;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.gooseapple.politiks.core.currency.Account;
import org.gooseapple.politiks.core.currency.IAccountHolder;
import org.gooseapple.politiks.core.nation.Nation;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.DatabaseManager;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class Town implements IAccountHolder {
    private String townName;
    private PolitikPlayer mayor;
    private TextComponent enterMessage = Component.text("");
    private Location spawnLocation;
    private Nation nationOwner;
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

    public CopyOnWriteArrayList<PolitikPlayer> getPlayerList() {
        return playerList;
    }

    public void removePlayer(PolitikPlayer player) {
        this.nationOwner.removePlayer(player);
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
        this.nationOwner.addPlayer(player);
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
