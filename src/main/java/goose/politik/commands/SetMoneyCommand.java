package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.MoneyHandler;
import goose.politik.util.government.PolitikPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.util.logging.Level;

public class SetMoneyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender.isOp()) {
            //sender is valid sender
            //then validate all text
            if (args.length > 2) {
                sender.sendMessage(Politik.errorMessage("Too many arguments!"));
            } else if (args.length < 1) {
                sender.sendMessage(Politik.errorMessage("Too few arguments!"));
            } else {
                BigDecimal value;

                //between 1 and two
                if (args.length == 1) {
                    //setting money for self
                    if (!(sender instanceof Player)) {
                        //server cant set own money
                        sender.sendMessage(Politik.errorMessage("Server can't set its' own money"));
                        return true;
                    }
                    //player who is setting their own money
                    String amount = args[0];
                    try {
                        value = new BigDecimal(amount);
                        value = MoneyHandler.moneyRound(value);
                    } catch (Exception e) {
                        sender.sendMessage(Politik.errorMessage("First argument was not a number!"));
                        return true;
                    }

                    Player target = ((Player) sender).getPlayer();
                    if (target != null) {
                        PolitikPlayer targetPlayer = PolitikPlayer.getPolitikPlayer(target);
                        targetPlayer.message(Politik.successMessage("Successfully set " + targetPlayer.getDisplayName() + "'s money to $" + value));
                        targetPlayer.setMoney(value);
                    } else {
                        Politik.getInstance().logger.log(Level.WARNING, "Error, player was null line 42 setm");
                    }

                } else {
                    String player = args[0];
                    String amount = args[1];
                    try {
                        value = new BigDecimal(amount);
                        value = MoneyHandler.moneyRound(value);
                    } catch (Exception e) {
                        sender.sendMessage(Politik.errorMessage("Second argument was not a number!"));
                        return true;
                    }

                    //successful conversion
                    Player target = Politik.getInstance().getServer().getPlayer(player) ;

                    //doesnt work with offline players just yet

                    //verify target isn't null
                    if (target != null) {
                        //successful, player does exist
                        PolitikPlayer targetPlayer = PolitikPlayer.getPolitikPlayer(target);
                        Politik.getInstance().logger.log(Level.INFO, value + " amount set");
                        targetPlayer.setMoney(value);
                        sender.sendMessage(Politik.successMessage("Successfully set " + target.getName() + "'s money to $" + value));
                    } else {
                        sender.sendMessage(Politik.errorMessage(player + " does not exist"));
                    }
                }
            }
        } else {
            sender.sendMessage(Politik.errorMessage(Politik.lackPerms));
        }
        return true;
    }
}
