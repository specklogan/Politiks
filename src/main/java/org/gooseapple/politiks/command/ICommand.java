package org.gooseapple.politiks.command;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public interface ICommand {
    public LiteralCommandNode<CommandSourceStack> build();
}
