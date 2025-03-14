package org.gooseapple.politiks.command.implementation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.gooseapple.politiks.command.ICommand;

public class TownCommand implements ICommand {
    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("town")
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.string())
                                .executes(this::createTown))).build();
    }

    private int createTown(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        String townName = StringArgumentType.getString(commandSourceStackCommandContext, "name");
        return Command.SINGLE_SUCCESS;
    }
}
