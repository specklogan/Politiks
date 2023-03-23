package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.database.NationDB;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

public class NationCommands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //this is called when the player does /nation, from there we can check what specific thing they are looking for
        if (args.length == 0) {
            sender.sendMessage(Politik.errorMessage("Not enough parameters, type '/nation help' for help"));
            return true;
        }

        if (!(sender instanceof Player)) {
            //server can't execute commands
            sender.sendMessage(Politik.errorMessage("Server can't issue nation commands"));
            return true;
        }

        PolitikPlayer player = PolitikPlayer.getPolitikPlayer((Player) sender);
        String firstArg = args[0];

        if (firstArg.equalsIgnoreCase("create")) {
            if (args.length > 1) {
                //enough args
                String nationName = args[1];
                //check if they can buy it
                if (!player.canPurchase(Nation.NATIONCOST)) {
                    player.message(Politik.errorMessage("You lack the funds to purchase a nation"));
                    return true;
                }
                //make sure they arent in a nation
                if (!(player.getNation() == null)) {
                    player.message(Politik.errorMessage("You are already in a nation, leave your nation to create another"));
                    return true;
                }

                //make sure they aren't in a town
                if (!(player.getTown() == null)) {
                    player.message(Politik.errorMessage("You are already in a town, you can't create a new nation while in a town"));
                    return true;
                }

                //check if the nation name is already taken
                for (Nation nation : Nation.NATIONS) {
                    if (nation.getNationName().equalsIgnoreCase(nationName)) {
                        player.message(Politik.errorMessage("Nation name is already taken"));
                        return true;
                    }
                }

                //all of our checks were successful, go ahead and create it
                player.changeMoney(Nation.NATIONCOST.negate());
                player.message(Politik.successMessage("Successfully created " + nationName + " , next create a town for your capitol"));
                Nation nation = new Nation(nationName, player);
                nation.setEnterMessage(Component.text("Entering Nation " + nationName));

            } else {
                sender.sendMessage(Politik.errorMessage("You need to provide a name for the nation"));
            }

        } else if (firstArg.equalsIgnoreCase("list")) {
            //list all nations
            for (Nation nation : Nation.NATIONS) {
                sender.sendMessage("Nation: " + nation.getNationName() + " lead by " + nation.getLeader().getDisplayName());
            }
        } else if (firstArg.equalsIgnoreCase("spawn")) {
            //spawn at nation home


        } else {
            NationDB.saveNation(player.getNation());
            //list nation help
            sender.sendMessage("---------------- Nation Help ----------------");
            sender.sendMessage(Politik.detailMessage("/nation create [nation-name] : takes in a nation name"));
            sender.sendMessage(Politik.detailMessage("/nation list [@optional enemy,ally] : lists all nation in the map"));
            sender.sendMessage(Politik.detailMessage("/nation help : outputs nation command help"));
        }
        return true;
    }
}
