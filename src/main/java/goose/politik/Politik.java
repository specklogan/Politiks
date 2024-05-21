package goose.politik;

import goose.politik.commands.*;
import goose.politik.compat.CompatabilityHandler;
import goose.politik.events.*;
import goose.politik.task.DayListener;
import goose.politik.events.LandEvents.LandLoadUnloadEvent;
import goose.politik.events.LandEvents.LandToolInteractEvent;
import goose.politik.task.FoliaSaveListener;
import goose.politik.task.Scheduler;
import goose.politik.config.ConfigHandler;
import goose.politik.util.database.*;
import goose.politik.government.nation.Nation;
import goose.politik.player.PolitikPlayer;
import goose.politik.government.town.Town;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class Politik extends JavaPlugin implements Listener {

    public static final String pluginVersion = "1.0";
    public static Politik plugin;
    public static Logger logger;
    private Scheduler.Task task;
    private Scheduler.Task foliaSaveTask;
    public static final String lackPerms = "You lack the permissions to run this command";

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        logger = plugin.getLogger();
        getServer().getPluginManager().registerEvents(this,this);

        //Load Config
        ConfigHandler.loadConfig();

        //Register commands
        CommandRegister.registerCommands();

        //Start Database
        DatabaseHandler.initializeDatabase();

        //Add dimensions to the land handler
        LandUtil.addDimensionToLandMap(World.Environment.NORMAL);
        LandUtil.landUUIDMap.put(World.Environment.NORMAL, new ConcurrentHashMap<>());
        PlayerDB.loadAllPlayers();
        NationDB.loadNations();
        TownDB.loadTowns();

        //Add our custom events/listeners
        getServer().getPluginManager().registerEvents(new LandToolInteractEvent(), this);
        getServer().getPluginManager().registerEvents(new LandDayEvent(), this);

        if (ConfigHandler.canTick()) {
            log(Level.WARNING, "Starting Ticking for Day Cycle");
            DayListener.getInstance().setWorldToCheck(plugin.getServer().getWorlds().get(0));
            task = Scheduler.runTimer(DayListener.getInstance(), 0, 20);
        }

        if (CompatabilityHandler.foliaEnabled()) {
            //Because folia does not call the 'server save event' I mimic it, every 5 minutes it will call the save method
            foliaSaveTask = Scheduler.runTimer(FoliaSaveListener.getInstance(), 0, 6000);
        }

    }

    public static void log(Level level, String message) {
        plugin.getLogger().log(level, message);
    }

    public static void log(String message) {
        plugin.getLogger().log(Level.INFO, message);
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

    public void saveServer() {
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
    public void blockPlaceEvent(BlockPlaceEvent event) {
        BlockPlace.blockPlaceEvent(event);
    }

    @EventHandler
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        DamageEvent.entityDamageByEntityEvent(event);
    }

    @EventHandler
    public void entityExplodeEvent(EntityExplodeEvent event) {
        ExplodeEvent.entityExplodeEvent(event);
    }

    @EventHandler
    public void blockExplodeEvent(BlockExplodeEvent event) {
        ExplodeEvent.blockExplodeEvent(event);
    }

    @EventHandler
    public void blockSpreadEvent(BlockSpreadEvent event) {
        SpreadEvent.blockSpreadEvent(event);
    }

    @EventHandler
    public void harvestEvent(PlayerHarvestBlockEvent event) {
        JobEvent.harvestCropEvent(event);
    }

    @EventHandler
    public void mobSpawnEvent(CreatureSpawnEvent event) {
        MobEvent.mobSpawnEvent(event);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (UUID player: PolitikPlayer.playerList.keySet()) {
            PolitikPlayer user = PolitikPlayer.playerList.get(player);
            log(Level.INFO, "Saving player " + user.getDisplayName());
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
