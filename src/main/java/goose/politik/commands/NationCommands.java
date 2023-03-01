package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.util.government.Nation;
import goose.politik.util.government.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
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
        String firstArg = args[0];

        if (firstArg.equalsIgnoreCase("create")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Politik.errorMessage("Server can't create a nation"));
                return true;
            }
            if (args.length > 1) {
                //enough args
                String nationName = args[1];
                PolitikPlayer player = PolitikPlayer.getPolitikPlayer((Player) sender);
                //check if they can buy it
                if (player.canPurchase(Nation.NATIONCOST) && player.getNation() == null) {
                    player.changeMoney(Nation.NATIONCOST.negate());
                    new Nation(nationName, player);
                }
            } else {
                sender.sendMessage(Politik.errorMessage("You need to provide a name for the nation"));
            }

        } else if (firstArg.equalsIgnoreCase("list")) {
            //list all nations
            for (Nation nation : Nation.NATIONS) {
                sender.sendMessage("Nation: " + nation.getNationName() + " lead by " + nation.getLeader().getDisplayName());
            }
        }
        return true;
    }
}
