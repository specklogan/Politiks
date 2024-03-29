package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.economy.MoneyHandler;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.text.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.logging.Level;

public class SetMoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.isOp()) {
            //sender is valid sender
            //then validate all text
            if (args.length > 2) {
                sender.sendMessage(TextUtil.errorMessage("Too many arguments!"));
            } else if (args.length < 1) {
                sender.sendMessage(TextUtil.errorMessage("Too few arguments!"));
            } else {
                BigDecimal value;

                //between 1 and two
                if (args.length == 1) {
                    //setting money for self
                    if (!(sender instanceof Player)) {
                        //server cant set own money
                        sender.sendMessage(TextUtil.errorMessage("Server can't set its' own money"));
                        return true;
                    }
                    //player who is setting their own money
                    String amount = args[0];
                    try {
                        value = new BigDecimal(amount);
                        value = MoneyHandler.moneyRound(value);
                    } catch (Exception e) {
                        sender.sendMessage(TextUtil.errorMessage("First argument was not a number!"));
                        return true;
                    }

                    PolitikPlayer target = null;
                    for (UUID player : PolitikPlayer.playerList.keySet()) {
                        if (PolitikPlayer.playerList.get(player).getDisplayName().equalsIgnoreCase(args[0])) {
                            target = PolitikPlayer.playerList.get(player);
                        }
                    }
                    if (target != null) {
                        target.message(TextUtil.successMessage("Successfully set " + target.getDisplayName() + "'s money to $" + value));
                        target.setMoney(value);
                    } else {
                        Politik.logger.log(Level.WARNING, "Error, player was null");
                    }

                } else {
                    String player = args[0];
                    String amount = args[1];
                    try {
                        value = new BigDecimal(amount);
                        value = MoneyHandler.moneyRound(value);
                    } catch (Exception e) {
                        sender.sendMessage(TextUtil.errorMessage("Second argument was not a number!"));
                        return true;
                    }

                    //successful conversion
                    PolitikPlayer target = null;
                    for (UUID person : PolitikPlayer.playerList.keySet()) {
                        if (PolitikPlayer.playerList.get(person).getDisplayName().equalsIgnoreCase(args[0])) {
                            target = PolitikPlayer.playerList.get(person);
                        }
                    }

                    //verify target isn't null
                    if (target != null) {
                        //successful, player does exist
                        Politik.logger.log(Level.INFO, value + " amount set");
                        target.setMoney(value);
                        sender.sendMessage(TextUtil.successMessage("Successfully set " + target.getDisplayName() + "'s money to $" + value));
                    } else {
                        sender.sendMessage(TextUtil.errorMessage(player + " does not exist"));
                    }
                }
            }
        } else {
            sender.sendMessage(TextUtil.errorMessage(Politik.lackPerms));
        }
        return true;
    }
}
