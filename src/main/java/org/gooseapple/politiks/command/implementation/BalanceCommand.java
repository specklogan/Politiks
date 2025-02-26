package org.gooseapple.politiks.command.implementation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gooseapple.politiks.command.ICommand;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.IPlayerTable;
import org.gooseapple.politiks.util.Errors;

public class BalanceCommand implements ICommand {
    private IPlayerTable players;

    public BalanceCommand() {
        players = DatabaseManager.getDatabase().getPlayerTable();
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("balance")
                .executes(this::execute)
                .then(Commands.argument("player", ArgumentTypes.player())
                .executes(this::target)).build();
    }

    private int target(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        Errors.LogError("Test1");
        return Command.SINGLE_SUCCESS;
    }

    private int execute(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        Errors.LogError("Test2");
        return Command.SINGLE_SUCCESS;
    }
}
