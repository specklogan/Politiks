package org.gooseapple.politiks.command.implementation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.inventory.ItemStack;
import org.gooseapple.politiks.command.ICommand;
import org.gooseapple.politiks.config.ConfigManager;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.ITownTable;
import org.gooseapple.politiks.item.ClaimTool;
import org.gooseapple.politiks.ui.LandUI;
import org.gooseapple.politiks.util.CommandUtil;
import org.gooseapple.politiks.util.Constants;

public class LandCommand implements ICommand {
    private ITownTable townTable;


    public LandCommand() {
        this.townTable = DatabaseManager.getDatabase().getTownTable();
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("land")
                .then(Commands.literal("gui")
                        .executes(this::handleGui))
                .then(Commands.literal("claimtool")
                        .executes(this::giveClaimTool)).build();
    }

    private int giveClaimTool(CommandContext<CommandSourceStack> ctx) {
        PolitikPlayer p = CommandUtil.GetPlayerFromCommandContext(ctx);
        if (p == null) {
            return Command.SINGLE_SUCCESS;
        }

        ItemStack claimTool = ClaimTool.GetItem();
        p.getPlayer().getInventory().addItem(claimTool);

        return Command.SINGLE_SUCCESS;
    }

    private int handleGui(CommandContext<CommandSourceStack> ctx) {
        PolitikPlayer player = CommandUtil.GetPlayerFromCommandContext(ctx);
        if (player == null) {
            return Command.SINGLE_SUCCESS;
        }

        LandUI ui = new LandUI();
        ui.CreateGUI();
        ui.ShowGUI(player);
        return Command.SINGLE_SUCCESS;
    }
}
