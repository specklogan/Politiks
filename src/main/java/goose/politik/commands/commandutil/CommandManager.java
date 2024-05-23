package goose.politik.commands.commandutil;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;

public abstract class CommandManager implements ICommand, TabExecutor, CommandExecutor {
    private final ArrayList<ICommand> subCommands = new ArrayList<>();

    @Override
    public ArrayList<ICommand> getSubcommands() {
        return subCommands;
    }

    public void addSubcommand(ICommand command) {
        subCommands.add(command);
    }

    public void removeSubcommand(ICommand command) {
        subCommands.remove(command);
    }

    public boolean containsSubcommand(ICommand command) {
        return subCommands.contains(command);
    }
}
