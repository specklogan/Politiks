package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class NationCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //this is called when the player does /nation, from there we can check what specific thing they are looking for
        for (Long chunkID: LandUtil.landMap.get(World.Environment.NORMAL).keySet()) {
           ArrayList<Land> lands = LandUtil.landMap.get(World.Environment.NORMAL).get(chunkID);
           for (int i =0; i < lands.size(); i++) {
               lands.get(i).setVisibleLand();
           }
        }
        Politik.logger.log(Level.INFO, LandUtil.landMap.toString());
        return true;
    }
}
