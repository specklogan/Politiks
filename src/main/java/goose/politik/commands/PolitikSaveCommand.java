package goose.politik.commands;

import goose.politik.Politik;
import goose.politik.player.PolitikPlayer;
import goose.politik.util.text.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PolitikSaveCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!sender.isOp()) {
            return true;
        }
        Politik.getInstance().saveServer();
        return true;
    }
}
