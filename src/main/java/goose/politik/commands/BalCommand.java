package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.government.PolitikPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BalCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            PolitikPlayer player = PolitikPlayer.getPolitikPlayer(((Player) sender));
            //player not console
            player.message(Politik.successMessage("Your balance is: $" + player.getMoney()));
        } else {
            sender.sendMessage(Politik.errorMessage("Do you really need this?"));
        }
        return true;
    }
}
