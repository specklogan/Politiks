package goose.politik;

import goose.politik.commands.*;
import goose.politik.events.*;
import goose.politik.util.database.*;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.government.Town;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Politik extends JavaPlugin implements Listener {

    public static final String pluginVersion = "1.0";
    public static Politik plugin;
    public static Logger logger;
    public final MongoDBHandler mongoDB = new MongoDBHandler();

    public static TextComponent errorMessage(String text) {
        return Component.text(text).color(TextColor.color(255, 0, 0));
    }

    public static TextComponent eventMessage(String text) {
        return Component.text(text).color(TextColor.color(255, 255, 0));
    }

    public static TextComponent successMessage(String text) {
        return Component.text(text).color(TextColor.color(62, 255, 54));
    }

    public static TextComponent detailMessage(String text) {
        return Component.text(text).color(TextColor.color(84, 200, 255));
    }

    public static TextComponent infoMessage(String text) {
        return Component.text(text).color(TextColor.color(255, 255, 255));
    }


    public static final String lackPerms = "You lack the permissions to run this command";

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        logger = plugin.getLogger();
        getServer().getPluginManager().registerEvents(this,this);
        this.getLogger().log(Level.INFO, "Starting Politik Version " + pluginVersion);
        Objects.requireNonNull(getCommand("addmoney")).setExecutor(new AddMoneyCommand());
        Objects.requireNonNull(getCommand("balance")).setExecutor(new BalCommand());
        Objects.requireNonNull(getCommand("setmoney")).setExecutor(new SetMoneyCommand());
        Objects.requireNonNull(getCommand("jobset")).setExecutor(new SetJobCommand());
        Objects.requireNonNull(getCommand("claimtool")).setExecutor(new ClaimToolCommand());
        Objects.requireNonNull(getCommand("nation")).setExecutor(new NationCommands());
        Objects.requireNonNull(getCommand("town")).setExecutor(new TownCommand());
        Objects.requireNonNull(getCommand("list")).setExecutor(new List());
        Objects.requireNonNull(getCommand("land")).setExecutor(new LandCommand());

        //Add dimensions to the land handler
        LandUtil.addDimensionToLandMap(World.Environment.NORMAL);
        PlayerDB.loadAllPlayers();
        NationDB.loadNations();
        TownDB.loadTowns();
        //LandDB.loadLands();
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event){
        //send it to the other things
        JoinLeaveHandler.playerJoin(event);
    }

    @EventHandler
    public void serverSaveEvent(WorldSaveEvent event) {
        for (UUID player: PolitikPlayer.playerList.keySet()) {
            PolitikPlayer user = PolitikPlayer.playerList.get(player);
            user.savePlayer();
        }
        //saves the player just like when the server shuts down
        Nation.saveNations();
        Town.saveTowns();
        LandUtil.saveLands();
    }

    @EventHandler
    public void chunkUnloadEvent(ChunkUnloadEvent event) {
        LandLoadUnloadEvent.onChunkUnload(event);
    }

    @EventHandler
    public void chunkLoadEvent(ChunkLoadEvent event) {
        LandLoadUnloadEvent.onChunkLoad(event);
    }

    @EventHandler
    public void playerMoveEvent(PlayerMoveEvent event) {
        MoveEvent.playerMoveEvent(event);
    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        InteractEvent.playerInteract(event);
    }

    @EventHandler
    public void playerLeaveEvent(PlayerQuitEvent event) {
        JoinLeaveHandler.playerLeave(event);
    }

    @EventHandler
    public void shearEntityEvent(PlayerShearEntityEvent event) {
        JobEvent.shearEntityEvent(event);
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        //do stuff eventually
        BlockBreak.blockBreakEvent(event);
    }

    @EventHandler
    public void harvestEvent(PlayerHarvestBlockEvent event) {
        JobEvent.harvestCropEvent(event);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (UUID player: PolitikPlayer.playerList.keySet()) {
            PolitikPlayer user = PolitikPlayer.playerList.get(player);
            logger.log(Level.INFO, "Saving player " + user.getDisplayName());
            user.leave();
        }

        //save the nations and town
        Nation.saveNations();
        Town.saveTowns();
        LandUtil.saveLands();
    }

    public static Politik getInstance() {
        return plugin;
    }
}
