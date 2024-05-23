package goose.politik.commands.commandutil;

import net.kyori.adventure.text.TextComponent;

import java.util.ArrayList;

public interface ICommand {
    public void execute();

    public String getCommandString();
    public ArrayList<ICommand> getSubcommands();
    public TextComponent getDescription();
    public TextComponent getHelp();
}
