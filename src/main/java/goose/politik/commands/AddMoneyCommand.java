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
import java.util.UUID;


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
                    PolitikPlayer receivingPlayer = null;
                    for (UUID person : PolitikPlayer.playerList.keySet()) {
                        if (PolitikPlayer.playerList.get(person).getDisplayName().equalsIgnoreCase(args[0])) {
                            receivingPlayer = PolitikPlayer.playerList.get(person);
                        }
                    }
                    amount = MoneyHandler.moneyRound(amount);
                    if (sender instanceof Player) {
                        PolitikPlayer senderPlayer = PolitikPlayer.getPolitikPlayer((Player) sender);
                        if (receivingPlayer == null) {
                            sender.sendMessage(Politik.errorMessage("Player " + args[0] + " does not exist"));
                        } else {
                            //player does exist give them money
                            if (receivingPlayer.getPlayer() == null) {
                                senderPlayer.message(Politik.successMessage("Successfully gave " + receivingPlayer.getDisplayName()  + " $" + amount + "."));
                                receivingPlayer.changeMoney(amount);
                            } else {
                                receivingPlayer.message(Politik.successMessage("You were added $" + amount + " from " + senderPlayer.getDisplayName()));
                                senderPlayer.message(Politik.successMessage("Successfully gave " + receivingPlayer.getDisplayName()  + " $" + amount + "."));
                                receivingPlayer.changeMoney(amount);
                            }
                        }
                    } else {
                        if (receivingPlayer == null) {
                            sender.sendMessage(Politik.errorMessage("Player " + args[0] + " does not exist"));
                        } else {
                            //player does exist give them money
                            if (receivingPlayer.getPlayer() == null) {
                                //offline
                                receivingPlayer.changeMoney(amount);
                                sender.sendMessage(Politik.successMessage("Successfully added $" + amount + " to" + args[0]));
                            } else {
                                receivingPlayer.message(Politik.successMessage("You were added $" + amount + " from Server"));
                                sender.sendMessage(Politik.successMessage("Successfully added $" + amount + " to" + args[0]));
                                receivingPlayer.changeMoney(amount);
                            }
                        }
                    }

                }
            }
        } else {
            sender.sendMessage(Politik.errorMessage("You don't have permission to run this command!"));
        }
        return true;
    }
}
