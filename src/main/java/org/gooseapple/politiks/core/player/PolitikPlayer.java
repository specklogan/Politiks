package org.gooseapple.politiks.core.player;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.gooseapple.politiks.core.currency.Account;
import org.gooseapple.politiks.core.currency.IAccountHolder;
import org.gooseapple.politiks.core.nation.Nation;
import org.gooseapple.politiks.core.town.Town;
import org.gooseapple.politiks.database.DatabaseManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class PolitikPlayer implements IAccountHolder {
    private String displayName;
    private UUID uuid;
    private int infamy;
    private Nation nation;
    private Town town;
//   private Land lastEnteredLand;
    private BigInteger joinDate;
    private Player player;
    private String job;

    public void teleport(Location location) {
        this.player.teleport(location);
    }

    public CompletableFuture<Boolean> teleportAsync(Location location) {
        return this.player.teleportAsync(location);
    }

    public static PolitikPlayer getPolitikPlayer(Player player) {
        return DatabaseManager.getDatabase().getPlayerTable().GetPlayer(player);
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setInfamy(int infamy) {
        this.infamy = infamy;
    }

    public void setJoinDate(BigInteger number) {
        this.joinDate = number;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getInfamy() {
        return this.infamy;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public Nation getNation() {
        return this.nation;
    }

    public Town getTown() {
        return this.town;
    }

    public void setNation(Nation nation) {
        this.nation = nation;
    }

    public void setTown(Town town) {
        this.town = town;
    }

    public BigInteger getJoinDate() {
        return this.joinDate;
    }

    public void message(TextComponent component) {
        if (this.player == null || !this.player.isOnline()) {
            return;
        }
        this.player.sendMessage(component);
    }

    public boolean isOnline() {
        return this.player == null || !this.player.isOnline();
    }

    public PlayerInventory getInventory() {
        return this.player.getInventory();
    }

    public String getJob() {
        return this.job;
    }

    public void setJob(String job) {
        this.job = job;
    }

//    public void setLastEnteredLand(Land land) {
//        this.lastEnteredLand = land;
//    }
//
//    public Land getLastEnteredLand() {
//        return this.lastEnteredLand;
//    }

    public boolean hasTown() {
        return this.town != null;
    }

    public boolean hasNation() {
        return this.nation != null;
    }

    public boolean isMayor() {
        if (hasTown()) {
            if (town.getMayor() == this) {
                return true;
            }
        }

        return false;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public Account GetAccount() {
        return DatabaseManager.getDatabase().getAccountTable().GetPlayerAccount(this);
    }

    @Override
    public void AddAccount(Account account) {
        //DatabaseManager.getDatabase().getAccountTable()
    }

    @Override
    public UUID GetAccountHolderUUID() {
        return this.uuid;
    }
}
