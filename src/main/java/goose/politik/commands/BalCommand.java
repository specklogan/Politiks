package goose.politik.commands;

import goose.politik.Politik;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class BalCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            //player not console
            sender.sendMessage(Politik.successMessage("Your balance is: $" + Politik.moneyHandler.getMoney((Player) sender)));
        } else {
            sender.sendMessage(Politik.errorMessage("Do you really need this?"));
        }
        return true;
    }
}
