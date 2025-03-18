package org.gooseapple.politiks.command.implementation;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.gooseapple.politiks.command.CommandManager;
import org.gooseapple.politiks.command.ICommand;
import org.gooseapple.politiks.config.ConfigManager;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.ITownTable;
import org.gooseapple.politiks.util.CommandUtil;
import org.gooseapple.politiks.util.Constants;

public class TownCommand implements ICommand {
    private ITownTable townTable;


    public TownCommand() {
        this.townTable = DatabaseManager.getDatabase().getTownTable();
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> build() {
        return Commands.literal("town")
                .then(Commands.literal("join")
                        .executes(this::joinTown))
                .then(Commands.literal("list")
                        .executes(this::listTowns))
                .then(Commands.literal("create")
                        .then(Commands.argument("name", StringArgumentType.word())
                                .executes(this::createTown))).build();
    }

    private int joinTown(CommandContext<CommandSourceStack> ctx) {
        return Command.SINGLE_SUCCESS;
    }

    private int listTowns(CommandContext<CommandSourceStack> ctx) {
        PolitikPlayer player = CommandUtil.GetPlayerFromCommandContext(ctx);
        if (player != null) {
            var towns = DatabaseManager.getDatabase().getTownTable().GetAllTowns();
            player.message(Constants.DetailMessage("------- Showing Towns -------"));
            for (Town t : towns) {
                player.message(Constants.DetailMessage(t.getTownName() + " with a size of " + t.getPlayerList().size() + " players"));
            }
            player.message(Constants.DetailMessage("----------------------------"));

        }

        return Command.SINGLE_SUCCESS;
    }

    private int createTown(CommandContext<CommandSourceStack> commandSourceStackCommandContext) {
        String townName = StringArgumentType.getString(commandSourceStackCommandContext, "name");
        boolean townExists = townTable.TownNameExists(townName);
        PolitikPlayer p = CommandUtil.GetPlayerFromCommandContext(commandSourceStackCommandContext);

        if (p == null) {
            return Command.SINGLE_SUCCESS;
        }

        if (p.hasTown()) {
            p.message(Constants.ErrorMessage("You must leave your current town of " + p.getTown().getTownName() + " in order to found your own town."));
            return Command.SINGLE_SUCCESS;
        }

        if (!p.GetAccount().canWithdraw(ConfigManager.getTownCost())) {
            p.message(Constants.ErrorMessage("You have insufficient funds to found a town! The required amount is $" + ConfigManager.getTownCost().toString()));
            return Command.SINGLE_SUCCESS;
        }

        if (townExists) {
            p.message(Constants.ErrorMessage("A town with the name of " + townName + " already exists, please select a different name."));
            return Command.SINGLE_SUCCESS;
        }

        Town t = townTable.CreateTown(p, townName);
        p.GetAccount().withdraw(ConfigManager.getTownCost());
        p.message(Constants.SuccessMessage(townName + " was successfully founded! \n Use '/town help' for more information"));

        //Handle nation setting if needed (is not required)
        if (p.hasNation()) {
            t.setNationId(p.getNation().getId());
            p.message(Constants.DetailMessage(townName + " joined arms with the nation of " + p.getNation().getNationName()));
        }

        return Command.SINGLE_SUCCESS;
    }
}
