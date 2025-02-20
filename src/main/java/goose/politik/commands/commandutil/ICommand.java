package goose.politik.commands.commandutil;

import net.kyori.adventure.text.TextComponent;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public interface ICommand {
    public boolean execute(ArrayList<String> args, CommandSender sender);

    public String getCommandString();
    public ArrayList<ICommand> getSubcommands();
    public TextComponent getDescription();
    public TextComponent getHelp();
}
