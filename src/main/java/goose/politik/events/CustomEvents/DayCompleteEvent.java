package goose.politik.events.CustomEvents;

import goose.politik.Politik;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.Date;

public class DayCompleteEvent extends Event implements Cancellable {
    private boolean isCancelled;
    private final Server server;
    private final World world;
    private final Date serverDate = new Date();
    private Long worldTime;

    public Date getServerDate() {
        return serverDate;
    }

    public Long getWorldTime() {
        return worldTime;
    }

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public Server getServer() {
        return server;
    }

    public World getWorld() {
        return world;
    }

    public DayCompleteEvent(World world) {
        this.isCancelled = false;
        this.worldTime = world.getGameTime();
        this.server = Politik.getInstance().getServer();
        this.world = world;
    }
    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.isCancelled = cancel;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
