package goose.politik.commands;

import goose.politik.player.PolitikPlayer;
import goose.politik.util.landUtil.Land;
import goose.politik.util.landUtil.LandUtil;
import goose.politik.util.text.TextUtil;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LandCommand implements CommandExecutor {

    public void sendHelp(PolitikPlayer player) {
        player.message(TextUtil.infoMessage("---------------- Land Help ----------------"));
        player.message(TextUtil.detailMessage("/land list : lists all lands you own"));
        player.message(TextUtil.detailMessage("/land location : outputs land at your location"));
        player.message(TextUtil.detailMessage("/land help : outputs land command help"));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only a player can execute land commands");
            return true;
        }
        PolitikPlayer player = PolitikPlayer.getPolitikPlayer((Player)sender);
        if (args == null || args.length == 0) {
            sendHelp(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("location")) {
            Block playerLoc = player.getPlayer().getLocation().getBlock();
            Land land = LandUtil.blockInLand(playerLoc);
            if (land != null) {
                player.message(TextUtil.detailMessage("Land Claim: owned by " + land.getPlayerOwner().getDisplayName() + " : " + land.getUUID() + " area of: " + land.getArea() + " in the town of: " + land.getTownOwner().getTownName() + " in the nation: " + land.getNationOwner().getNationName()));
            } else {
                player.message(TextUtil.errorMessage("You aren't in any land claims"));
            }
        }

        return true;
    }
}
