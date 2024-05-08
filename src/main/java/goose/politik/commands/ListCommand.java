package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class ListCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args[0].equalsIgnoreCase("players")) {
            for (UUID key : PolitikPlayer.playerList.keySet()) {
                PolitikPlayer player = PolitikPlayer.playerList.get(key);
                Politik.logger.log(Level.INFO, "Player's name: " + player.getDisplayName());
                Politik.logger.log(Level.INFO, "Player's money: " + player.getMoney());
                Politik.logger.log(Level.INFO, "Player :" + player.getPlayer());
            }
            return true;
        } else if (args[0].equalsIgnoreCase("lands")) {
            for (UUID uuid : LandUtil.landUUIDMap.get(World.Environment.NORMAL).keySet()) {
                Politik.log(Level.INFO, LandUtil.landUUIDMap.get(World.Environment.NORMAL).get(uuid).toString());
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
