package goose.politik.commands;

import goose.politik.util.config.ConfigHandler;
import goose.politik.util.government.Nation;
import goose.politik.util.player.PolitikPlayer;
import goose.politik.util.text.TextUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NationCommands implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        //this is called when the player does /nation, from there we can check what specific thing they are looking for
        if (args.length == 0) {
            sender.sendMessage(TextUtil.errorMessage("Not enough parameters, type '/nation help' for help"));
            return true;
        }

        if (!(sender instanceof Player)) {
            //server can't execute commands
            return false;
        }

        PolitikPlayer player = PolitikPlayer.getPolitikPlayer((Player) sender);
        String firstArg = args[0];

        if (firstArg.equalsIgnoreCase("create")) {
            if (args.length > 1) {
                //enough args
                String nationName = args[1];
                //check if they can buy it
                if (!player.canPurchase(ConfigHandler.getNationCost())) {
                    player.message(TextUtil.errorMessage("You need $" + ConfigHandler.getNationCost() + " to purchase, you only have $" + player.getMoney()));
                    return true;
                }
                //make sure they arent in a nation
                if (!(player.getNation() == null)) {
                    player.message(TextUtil.errorMessage("You are already in a nation, leave your nation to create another"));
                    return true;
                }

                //make sure they aren't in a town
                if (!(player.getTown() == null)) {
                    player.message(TextUtil.errorMessage("You are already in a town, you can't create a new nation while in a town"));
                    return true;
                }

                //check if the nation name is already taken
                for (Nation nation : Nation.NATIONS) {
                    if (nation.getNationName().equalsIgnoreCase(nationName)) {
                        player.message(TextUtil.errorMessage("Nation name is already taken"));
                        return true;
                    }
                }

                //all of our checks were successful, go ahead and create it
                player.changeMoney(ConfigHandler.getNationCost().negate());
                player.message(TextUtil.successMessage("Successfully created " + nationName + " , next create a town for your capitol"));
                Nation nation = new Nation(nationName, player);
                nation.setEnterMessage(Component.text("Entering Nation " + nationName));

            } else {
                sender.sendMessage(TextUtil.errorMessage("You need to provide a name for the nation"));
            }

        } else if (firstArg.equalsIgnoreCase("list")) {
            //list all nations
            for (Nation nation : Nation.NATIONS) {
                sender.sendMessage("Nation: " + nation.getNationName() + " lead by " + nation.getLeader().getDisplayName());
            }
        } else if (firstArg.equalsIgnoreCase("spawn")) {
            //spawn at nation home


        } else {
            //NationDB.saveNation(player.getNation()); Not sure why I did this?
            //list nation help
            sender.sendMessage("---------------- Nation Help ----------------");
            sender.sendMessage(TextUtil.detailMessage("/nation create [nation-name] : takes in a nation name"));
            sender.sendMessage(TextUtil.detailMessage("/nation list [@optional enemy,ally] : lists all nation in the map"));
            sender.sendMessage(TextUtil.detailMessage("/nation help : outputs nation command help"));
        }
        return true;
    }
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("create", "list", "help");
        }
        if (args.length == 2 && args[0].equals("list")) {
            return Arrays.asList("enemy", "ally");
        }

        return new ArrayList<>();
    }
}
