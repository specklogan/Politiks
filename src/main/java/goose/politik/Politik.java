package goose.politik;

import goose.politik.commands.AddMoneyCommand;
import goose.politik.commands.BalCommand;
import goose.politik.commands.SetJobCommand;
import goose.politik.commands.SetMoneyCommand;
import goose.politik.events.JobEvent;
import goose.politik.events.JoinLeaveHandler;
import goose.politik.util.MoneyHandler;
import goose.politik.util.MongoDBHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class Politik extends JavaPlugin implements Listener {

    public static final String pluginVersion = "1.0";
    private static Politik plugin;
    public static final MoneyHandler moneyHandler = new MoneyHandler();
    public static final MongoDBHandler mongoDB = new MongoDBHandler();

    public static TextComponent errorMessage(String text) {
        TextComponent component = Component.text(text).color(TextColor.color(255, 0, 0));
        return component;
    }

    public static TextComponent successMessage(String text) {
        TextComponent component = Component.text(text).color(TextColor.color(62, 255, 54));
        return component;
    }

    public static final String lackPerms = "You lack the permissions to run this command";

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getServer().getPluginManager().registerEvents(this,this);
        this.getLogger().log(Level.INFO, "Starting Politik Version " + pluginVersion);
        getCommand("addmoney").setExecutor(new AddMoneyCommand());
        getCommand("balance").setExecutor(new BalCommand());
        getCommand("setmoney").setExecutor(new SetMoneyCommand());
        getCommand("jobset").setExecutor(new SetJobCommand());
    }

    @EventHandler
    public void playerJoinEvent(PlayerJoinEvent event){
        //send it to the other things
        JoinLeaveHandler.playerJoin(event);
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
        
    }

    @EventHandler
    public void harvestEvent(PlayerHarvestBlockEvent event) {
        JobEvent.harvestCropEvent(event);
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Politik getInstance() {
        return plugin;
    }
}
