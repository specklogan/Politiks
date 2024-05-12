package goose.politik.task;

import goose.politik.Politik;
import goose.politik.compat.CompatabilityHandler;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

public class Scheduler {
    public static void run(Runnable runnable) {
        if (CompatabilityHandler.foliaEnabled()) {
            Politik.getInstance().getServer().getGlobalRegionScheduler().execute(Politik.getInstance(), runnable);
        }
    }

    public static Task runTimer(Runnable runnable, long delayTicks, long periodTicks) {
        if (CompatabilityHandler.foliaEnabled())
            return new Task(Bukkit.getGlobalRegionScheduler()
                    .runAtFixedRate(Politik.getInstance(), t -> runnable.run(), delayTicks < 1 ? 1 : delayTicks, periodTicks));
        else
            return new Task(Bukkit.getScheduler().runTaskTimer(Politik.getInstance(), runnable, delayTicks, periodTicks));
    }

    public static class Task {

        private Object foliaTask;
        private BukkitTask bukkitTask;

        Task(Object foliaTask) {
            this.foliaTask = foliaTask;
        }

        Task(BukkitTask bukkitTask) {
            this.bukkitTask = bukkitTask;
        }

        public void cancel() {
            if (foliaTask != null)
                ((ScheduledTask) foliaTask).cancel();
            else
                bukkitTask.cancel();
        }
    }
}
