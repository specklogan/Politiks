package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.events.JobEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.logging.Level;

public class SetJobCommand implements CommandExecutor {

    public static String[] jobList = new String[] {"Farmer", "Rancher", "Fisher", "Assassin", "Builder", "Miner", "Explorer"};

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player;

        if (sender instanceof Player) {
            player = (Player)sender;
        } else {
            sender.sendMessage(Politik.errorMessage("A server can't get a job."));
            return true;
        }

        if (args != null) {
            if (args.length == 1) {
                String job = args[0];
                Politik.getInstance().logger.log(Level.INFO, "Chosen job: " + job);

                //check if the string job is a valid member of joblist otherwise we say no
                for (int i = 0; i < jobList.length; i++) {
                    if (job.equalsIgnoreCase(jobList[i])) {
                        //valid job
                        if (Politik.mongoDB.getPlayerJob(player).equalsIgnoreCase(job)) {
                            //already has this job, don't let them change
                            sender.sendMessage(Politik.errorMessage("You already have the job " + job));
                        } else {
                            //player has different job, let them change
                            Politik.mongoDB.setPlayerJob(player, job);
                            sender.sendMessage(Politik.successMessage("Successfully changed job to " + job));
                        }
                    }
                }
            } else {
                sender.sendMessage(Politik.errorMessage("Too many arguments provided"));
            }
        }
        return true;
    }
}
