package goose.politik.util.government;

import goose.politik.Politik;
import goose.politik.util.database.MongoDBHandler;
import goose.politik.util.landUtil.Land;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;

public class PolitikPlayer {
    private String displayName;
    private UUID uuid;
    private int infamy;
    private Nation nation;
    private Town town;
    private Land lastEnteredLand;
    private BigInteger joinDate;
    private Player player;
    private String job;
    private BigDecimal money;

    public static HashMap<UUID, PolitikPlayer> playerList = new HashMap<>();
    //this stores every player

    public PolitikPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.displayName = player.getName();

        loadPlayer();
    }

    public PolitikPlayer() {
        //empty constructor when loaded from database
    }

    public static PolitikPlayer getPolitikPlayer(Player player) {
        return playerList.get(player.getUniqueId());
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

    public static Player getPlayerFromID(UUID playerID) {
        return playerList.get(playerID).getPlayer();
    }

    public void loadPlayer() {
        //RUN ONLY ON INITIAL PLAYER JOIN
        this.joinDate = new BigInteger(String.valueOf(Instant.now().getEpochSecond()));
        this.money = BigDecimal.ZERO;
        PolitikPlayer.playerList.put(this.uuid, this);
    }

    public void savePlayer() {
        //save to database, can be called without actually kicking the player
        MongoDBHandler.savePlayerToDatabase(this);
    }

    public void leave() {
        //called upon leave
        savePlayer();
        this.player = null;
        //set the player to null, so we can't message them
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

    public BigDecimal getMoney() {
        return this.money;
    }

    public BigInteger getJoinDate() {
        return this.joinDate;
    }

    public void message(TextComponent component) {
        this.player.sendMessage(component);
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

    public void setLastEnteredLand(Land land) {
        this.lastEnteredLand = land;
    }

    public Land getLastEnteredLand() {
        return this.lastEnteredLand;
    }

    public void changeMoney(BigDecimal amount) {
        BigDecimal currentPlayerMoney = getMoney();
        if (!currentPlayerMoney.equals(new BigDecimal("-1"))) {
            BigDecimal result = currentPlayerMoney.add(amount);
            if (result.compareTo(BigDecimal.ZERO) < 0) {
                //less than zero
                setMoney(BigDecimal.ZERO);
            } else {
                setMoney(result);
            }
        }
    }

    public boolean canPurchase(BigDecimal amount) {
        //will tell you if you're able to purchase something
        return this.money.subtract(amount).compareTo(BigDecimal.ZERO) >= 0;
    }

    public void setMoney(BigDecimal amount) {
        this.money = amount;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
