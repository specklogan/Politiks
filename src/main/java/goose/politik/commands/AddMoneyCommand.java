package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.economy.MoneyHandler;
import goose.politik.util.government.PolitikPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;


public class AddMoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
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
                        PolitikPlayer.getPolitikPlayer((Player) sender).changeMoney(amount);
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
                    PolitikPlayer senderPlayer = PolitikPlayer.getPolitikPlayer((Player) sender);

                    if (receivingPlayer == null) {
                        sender.sendMessage(Politik.errorMessage("Player " + player + " does not exist, or is not online!"));
                    } else {
                        //player does exist give them money
                        PolitikPlayer receivingPPlayer = PolitikPlayer.getPolitikPlayer(receivingPlayer);
                        receivingPPlayer.message(Politik.successMessage("You were added $" + amount + " from " + senderPlayer.getDisplayName()));
                        senderPlayer.message(Politik.successMessage("Successfully gave " + receivingPlayer.getName()  + " $" + amount + "."));
                        receivingPPlayer.changeMoney(amount);
                    }
                }
            }
        } else {
            sender.sendMessage(Politik.errorMessage("You don't have permission to run this command!"));
        }
        return true;
    }
}
