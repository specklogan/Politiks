package goose.politik.events.CustomEvents;

import goose.politik.Politik;
import goose.politik.events.CustomEvents.DayCompleteEvent;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;

public class DayListener extends BukkitRunnable {
    private long lastTime;
    private World worldToCheck;
    private int dayFrequency;
    private int currDay;

    public DayListener(World world, int dayFrequency) {
        this.worldToCheck = world;
        this.dayFrequency = dayFrequency;
        this.lastTime = world.getTime();
    }

    @Override
    public void run() {
        long currTime = this.worldToCheck.getTime();
        //double daysPassed = (double)(currTime-this.lastTime)/24000.0;
        if (currTime < lastTime) {
            //a day has passed
            if (dayFrequency == 1) {
                //one day has passed
                this.lastTime = currTime;
                worldToCheck.setTime(currTime);
                DayCompleteEvent event = new DayCompleteEvent(this.worldToCheck);
                Politik.getInstance().getServer().getPluginManager().callEvent(event);
            } else {
                currDay++;
                if (currDay == dayFrequency) {
                    currDay = 0;
                    this.lastTime = currTime;
                    worldToCheck.setTime(currTime);
                    DayCompleteEvent event = new DayCompleteEvent(this.worldToCheck);
                    Politik.getInstance().getServer().getPluginManager().callEvent(event);
                }
            }
        }
    }
}
