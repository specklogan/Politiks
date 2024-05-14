package goose.politik.commands;

import goose.politik.util.config.ConfigHandler;
import goose.politik.util.government.Nation;
import goose.politik.util.player.InviteHandler;
import goose.politik.util.player.PolitikPlayer;
import goose.politik.util.government.Town;
import goose.politik.util.text.TextUtil;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.codehaus.plexus.util.StringUtils;
import org.jetbrains.annotations.NotNull;

public class TownCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args == null || args.length == 0) {
            sender.sendMessage(TextUtil.errorMessage("Not enough parameters, type '/town help' for help"));
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(TextUtil.errorMessage("Only players can use town commands"));
            return true;
        }

        String firstArg = args[0];
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer((Player) sender);

        if (firstArg.equalsIgnoreCase("create")) {
            if (args.length == 1) {
                sender.sendMessage(TextUtil.errorMessage("You need a town name to create a town"));
                return true;
            }
            String townName = args[1];

            if (player.getTown() != null) {
                sender.sendMessage(TextUtil.errorMessage("You are already in a town, leave your town to create another"));
                return true;
            }

            if (player.getNation() == null) {
                sender.sendMessage(TextUtil.errorMessage("You need to be in a nation to create a town"));
                return true;
            }

            if (!player.canPurchase(ConfigHandler.getTownCost())) {
                player.message(TextUtil.errorMessage("You need $" + ConfigHandler.getTownCost() + " to purchase, you only have $" + player.getMoney()));
                return true;
            }

            player.changeMoney(ConfigHandler.getTownCost());
            sender.sendMessage(TextUtil.successMessage("Successfully created town: " + townName));
            Town newTown = new Town(townName, player, player.getNation());
            newTown.setSpawnLocation(((Player) sender).getLocation());
            if (player.getNation().getLeader() == player) {
                //they are in their own nation, and have not created a town
                player.getNation().setCapitol(newTown);
                player.getNation().addTown(newTown);
                player.setTown(newTown);
                newTown.addPlayer(player);
                player.message(TextUtil.detailMessage(player.getNation().getNationName() + "'s capitol set to " + townName));
            }


        } else if (firstArg.equalsIgnoreCase("list")) {
            for (Nation nation: Nation.NATIONS) {
                for (Town town: nation.getTownList()) {
                    sender.sendMessage(town.getTownName() + ": run by " + town.getMayor().getDisplayName() + " in nation " + town.getNation().getNationName());
                }
            }
        } else if (firstArg.equalsIgnoreCase("invite")) {
            if (!player.hasTown()) {
                player.message(TextUtil.errorMessage("You don't have a town!"));
                return true;
            }

            if (!player.isMayor()) {
                player.message(TextUtil.errorMessage("You can't invite players!"));
                return true;
            }

            String playerName = args[1];

            if (StringUtils.isEmpty(playerName) || !PolitikPlayer.playerExists(playerName)) {
                player.message(TextUtil.errorMessage("Couldn't invite " + playerName + " to the town!"));
                return true;
            }

            PolitikPlayer politikPlayer = PolitikPlayer.getPolitikPlayerFromUser(playerName);

            if (InviteHandler.inviteExist(player.getTown(), politikPlayer)) {
                player.message(TextUtil.errorMessage(playerName + " already invited to the town!"));
                return true;
            }

            if (politikPlayer.hasNation() || politikPlayer.hasTown()) {
                player.message(TextUtil.errorMessage(playerName + " is already in a town!"));
                return true;
            }

            InviteHandler.invitePlayer(player.getTown(), politikPlayer);
            player.message(TextUtil.detailMessage("Successfully invited " + playerName + " to the town."));
            politikPlayer.message(TextUtil.detailMessage("You've been invited to join " + player.getDisplayName() + "'s town " + player.getTown().getTownName()).clickEvent(ClickEvent.runCommand("/town join " + player.getTown().getTownName())));

        } else if (firstArg.equalsIgnoreCase("join")) {
            if (player.hasTown() || player.hasNation()) {
                player.message(TextUtil.errorMessage("You need to leave your town and nation to join another!"));
                return true;
            }
            String townName = args[1];
            Town town = Town.getTownFromName(townName);
            if (town == null) {
                player.message(TextUtil.errorMessage("The town " + townName + " doesn't exist!"));
                return true;
            }

            if (!InviteHandler.inviteExist(town, player)) { //TODO Permission based joining
                player.message(TextUtil.errorMessage(townName + " hasn't invited you!"));
                return true;
            }

            InviteHandler.acceptInvite(town, player);
            player.message(TextUtil.successMessage("Successfully joined " + town.getTownName()));
            town.getMayor().message(TextUtil.detailMessage(player.getDisplayName() + " has joined your town!"));

        }else {
            sender.sendMessage("---------------- Town Help ----------------");
            player.message(TextUtil.detailMessage("/town create [town-name] : takes in a town name"));
            player.message(TextUtil.detailMessage("/town list [@optional nation] : lists all towns in the map, or in a nation"));
            player.message(TextUtil.detailMessage("/town help : outputs town command help"));
            player.message(TextUtil.detailMessage("/town invite [player-name] : invites player to town"));
            player.message(TextUtil.detailMessage("/town leave : leave current town"));
        }
        return true;
    }
}
