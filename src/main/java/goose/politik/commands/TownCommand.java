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

        if (!(sender instanceof Player)) {
            sender.sendMessage(Politik.errorMessage("Only players can use town commands"));
            return true;
        }

        String firstArg = args[0];
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer((Player) sender);

        if (firstArg.equalsIgnoreCase("create")) {
            if (args.length == 1) {
                sender.sendMessage(Politik.errorMessage("You need a town name to create a town"));
                return true;
            }
            String townName = args[1];

            if (player.getTown() != null) {
                sender.sendMessage(Politik.errorMessage("You are already in a town, leave your town to create another"));
                return true;
            }

            if (player.getNation() == null) {
                sender.sendMessage(Politik.errorMessage("You need to be in a nation to create a town"));
                return true;
            }

            if (!player.canPurchase(Town.TOWNCOST)) {
                player.message(Politik.errorMessage("You lack the funds to purchase a town"));
                return true;
            }

            player.changeMoney(Town.TOWNCOST.negate());
            sender.sendMessage(Politik.successMessage("Successfully created town: " + townName));
            Town newTown = new Town(townName, player, player.getNation());
            if (player.getNation().getLeader() == player) {
                //they are in their own nation, and have not created a town
                player.getNation().setCapitol(newTown);
                player.message(Politik.detailMessage(player.getNation().getNationName() + "'s capitol set to " + townName));
            }


        } else if (firstArg.equalsIgnoreCase("list")) {
            for (Nation nation: Nation.NATIONS) {
                for (Town town: nation.getTownList()) {
                    sender.sendMessage(town.getTownName() + ": run by " + town.getMayor().getDisplayName() + " in nation " + town.getNation().getNationName());
                }
            }
        } else {
            sender.sendMessage("---------------- Town Help ----------------");
            player.message(Politik.detailMessage("/town create [town-name] : takes in a town name"));
            player.message(Politik.detailMessage("/town list [@optional nation] : lists all towns in the map, or in a nation"));
            player.message(Politik.detailMessage("/town help : outputs town command help"));


        }
        return true;
    }
}
