package goose.politik.util.government;

import goose.politik.Politik;
import goose.politik.util.MongoDBHandler;
import net.kyori.adventure.text.TextComponent;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import static com.mongodb.client.model.Filters.eq;

public class PolitikPlayer {
    private final String displayName;
    private final UUID uuid;
    private int infamy;
    private Nation nation;
    private Town town;
    private BigInteger joinDate;
    private final Player player;
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

    public static PolitikPlayer getPolitikPlayer(Player player) {
        return playerList.get(player.getUniqueId());
    }

    public Player getPlayer() {
        return this.player;
    }

    public static Player getPlayerFromID(UUID playerID) {
        return playerList.get(playerID).getPlayer();
    }

    public void loadPlayer() {
        //load from database, called when player joins
        if (this.player.hasPlayedBefore()) {
            //normal load, player has played before

            //load all of the values from the database
            this.money = new BigDecimal(MongoDBHandler.loadValueFromDatabase("money", this.uuid));
            this.joinDate = new BigInteger(MongoDBHandler.loadValueFromDatabase("joinDate", this.uuid));
            this.job = MongoDBHandler.loadValueFromDatabase("job", this.uuid);
            this.infamy = Integer.parseInt(MongoDBHandler.loadValueFromDatabase("infamy", this.uuid));

        } else {
            this.joinDate = new BigInteger(String.valueOf(Instant.now().getEpochSecond()));
            this.money = BigDecimal.ZERO;
        }
        PolitikPlayer.playerList.put(this.uuid, this);
    }

    public void savePlayer() {
        //save to database, can be called without actually kicking the player
        MongoDBHandler.savePlayerToDatabase(this);
    }

    public void leave() {
        //called upon leave
        this.message(Politik.eventMessage(this.displayName + " has left the server, thank you for playing!"));
        savePlayer();
        PolitikPlayer.playerList.remove(this.uuid);
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
}
