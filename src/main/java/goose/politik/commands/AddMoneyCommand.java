package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.MoneyHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.math.BigDecimal;
import java.util.logging.Level;

public class AddMoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender,Command command,String label, String[] args) {
        if (sender.isOp()) {
            //they are allowed to run the function
            if (args.length > 2) {
                sender.sendMessage(Politik.errorMessage("You supplied too many arguments!"));
            } else if (args.length < 1) {
                //too few args
                sender.sendMessage(Politik.errorMessage("You supplied too few arguments!"));
            } else {
                if (args.length == 1) {

                    BigDecimal amount;
                    try {
                        amount = new BigDecimal(args[0]);
                    } catch (NumberFormatException e) {
                        Politik.errorMessage("Error in command, value wasn't a number.");
                        return false;
                    }
                    //apply to themselves
                    //also make sure you aren't the console
                    if (sender instanceof Player) {
                        //add money to themselves
                        amount = MoneyHandler.moneyRound(amount);

                        sender.sendMessage(Politik.successMessage("Successfully gave yourself $" + amount + "."));
                        Politik.moneyHandler.changeMoney(amount, (Player) sender);
                    } else{
                        //console did this
                        sender.sendMessage(Politik.errorMessage("Server can't own money!"));
                    }
                } else {
                    BigDecimal amount;
                    try {
                        amount = new BigDecimal(args[1]);
                    } catch (NumberFormatException e) {
                        sender.sendMessage(Politik.errorMessage("Error in command, value wasn't a number."));
                        return false;
                    }
                    //apply to another person
                    String player = args[0];
                    Player receivingPlayer = Politik.getInstance().getServer().getPlayer(player);
                    amount = MoneyHandler.moneyRound(amount);

                    if (receivingPlayer == null) {
                        sender.sendMessage(Politik.errorMessage("Player " + player + " does not exist, or is not online!"));
                    } else {
                        //player does exist give them money
                        sender.sendMessage(Politik.successMessage("Successfully gave " + receivingPlayer.getName()  + " $" + amount + "."));
                        
                    }
                }
            }
        } else {
            sender.sendMessage(Politik.errorMessage("You don't have permission to run this command!"));
        }
        return true;
    }
}
