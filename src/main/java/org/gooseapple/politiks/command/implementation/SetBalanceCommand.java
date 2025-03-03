package org.gooseapple.politiks.command.implementation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gooseapple.politiks.command.ICommand;
import org.gooseapple.politiks.core.currency.Account;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.IPlayerTable;
import org.gooseapple.politiks.util.Constants;

public class SetBalanceCommand implements ICommand {
    private IPlayerTable players;

    public SetBalanceCommand() {
        players = DatabaseManager.getDatabase().getPlayerTable();
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        var command = Commands.literal("setbalance")
                .then(Commands.argument("player", ArgumentTypes.player())
                        .then(Commands.argument("balance", DoubleArgumentType.doubleArg()).executes(this::setBalance)));

        return command.build();
    }

    private int setBalance(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        try {
            var playerResolver = commandSourceStackCommandContext.getArgument("player", PlayerSelectorArgumentResolver.class);
            var amount = commandSourceStackCommandContext.getArgument("balance", double.class);
            var targetPlayer = playerResolver.resolve(commandSourceStackCommandContext.getSource()).getFirst();
            if (targetPlayer != null) {
                PolitikPlayer p = players.GetPlayer(targetPlayer.getUniqueId());
                if (p != null) {
                    p.GetAccount().setAmount(amount);
                    p.message(Constants.DetailMessage("Your account balance was set to " + p.GetAccount().getBalanceFormatted()));
                }
            }
        } catch (Exception ex) {

        }
        return Command.SINGLE_SUCCESS;
    }
}
