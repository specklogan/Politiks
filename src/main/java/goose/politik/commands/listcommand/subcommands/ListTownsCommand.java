package goose.politik.commands.listcommand.subcommands;

import goose.politik.commands.commandutil.Subcommand;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListTownsCommand extends Subcommand {
    @Override
    public boolean execute(ArrayList<String> args, CommandSender sender) {
        return false;
    }

    @Override
    public String getCommandString() {
        return null;
    }

    @Override
    public TextComponent getDescription() {
        return null;
    }

    @Override
    public TextComponent getHelp() {
        return null;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return null;
    }
}
