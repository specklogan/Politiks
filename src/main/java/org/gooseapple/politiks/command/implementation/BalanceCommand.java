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
                .executes(this::balance)
                .then(Commands.argument("player", ArgumentTypes.player())
                        .requires(sender -> sender.getSender().isOp())
                        .executes(this::balanceOfPlayer)).build();
    }

    private int balanceOfPlayer(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        try {
            var playerResolver = commandSourceStackCommandContext.getArgument("player", PlayerSelectorArgumentResolver.class);
            var targetPlayer = playerResolver.resolve(commandSourceStackCommandContext.getSource()).getFirst();
            if (targetPlayer != null) {
                PolitikPlayer p = players.GetPlayer(targetPlayer.getUniqueId());
                if (p != null) {

                }
            }
        } catch (Exception ex) {
            return 0;
        }
        return Command.SINGLE_SUCCESS;
    }

    private int balance(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {

        return Command.SINGLE_SUCCESS;
    }
}
