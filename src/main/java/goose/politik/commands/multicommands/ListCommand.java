package goose.politik.commands.multicommands;

import goose.politik.commands.commandutil.CommandManager;
import goose.politik.commands.commandutil.ICommand;
import goose.politik.util.text.TextUtil;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.util.RGBLike;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListCommand extends CommandManager {
    @Override
    public String getCommandString() {
        return null;
    }

    @Override
    public TextComponent getDescription() {
        return TextUtil.detailMessage("List values in Politik, takes in a ").append(TextUtil.eventMessage("<list arg>"));
    }

    @Override
    public TextComponent getHelp() {
        return getDescription();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;

        player.sendMessage(getDescription());
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
