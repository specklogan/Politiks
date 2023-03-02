package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.logging.Level;

public class List implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        for (UUID key : PolitikPlayer.playerList.keySet()) {
            PolitikPlayer player = PolitikPlayer.playerList.get(key);
            Politik.logger.log(Level.INFO, "Player's name: " + player.getDisplayName());
            Politik.logger.log(Level.INFO, "Player's money: " + player.getMoney());
            Politik.logger.log(Level.INFO, "Player :" + player.getPlayer());
        }
        return true;
    }
}
