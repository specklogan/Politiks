package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.government.Town;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TownCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args == null || args.length == 0) {
            sender.sendMessage(Politik.errorMessage("Not enough parameters, type '/town help' for help"));
            return true;
        }

        String firstArg = args[0];

        if (firstArg.equalsIgnoreCase("create")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Politik.errorMessage("Server can't create towns"));
                return true;
            }
            if (args.length == 1) {
                sender.sendMessage(Politik.errorMessage("You need a town name to create a town"));
                return true;
            }
            PolitikPlayer player = PolitikPlayer.getPolitikPlayer((Player) sender);
            String townName = args[1];
           if (player.getTown() == null) {
               if (player.getNation() == null) {
                   //player needs a nation to make a town
                   sender.sendMessage(Politik.errorMessage("You need to be in a nation to create a town, join or create one using /nation [create/join]"));
               } else {
                   if (player.canPurchase(Town.TOWNCOST)) {
                       player.changeMoney(Town.TOWNCOST.negate());
                       sender.sendMessage(Politik.successMessage("Successfully created town: " + townName));
                       new Town(townName, player, player.getNation());
                   } else {
                       sender.sendMessage(Politik.errorMessage("Not enough funds to purchase town, $" + Town.TOWNCOST + " required"));
                   }
               }
           } else {
               sender.sendMessage(Politik.errorMessage("You're already in a town, leave it to create another"));
           }
        } else if (firstArg.equalsIgnoreCase("list")) {
            for (Nation nation: Nation.NATIONS) {
                for (Town town: nation.getTownList()) {
                    sender.sendMessage(town.getTownName() + ": run by " + town.getMayor().getDisplayName() + " in nation " + town.getNation().getNationName());
                }
            }
        } else {
            sender.sendMessage("---------------- Town Help ----------------");
            sender.sendMessage(Politik.detailMessage("/town create [town-name] : takes in a town name"));
            sender.sendMessage(Politik.detailMessage("/town list [@optional nation] : lists all towns in the map, or in a nation"));
            sender.sendMessage(Politik.detailMessage("/town help : outputs town command help"));

        }
        return true;
    }
}
