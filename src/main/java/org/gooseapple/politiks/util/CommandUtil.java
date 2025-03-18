package org.gooseapple.politiks.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.DatabaseManager;

public class CommandUtil {
    public static PolitikPlayer GetPlayerFromCommandContext(CommandContext<CommandSourceStack> ctx) {
        Entity executor = ctx.getSource().getExecutor();
        if (!(executor instanceof Player player)) {
            return null;
        }

        return DatabaseManager.getDatabase().getPlayerTable().GetPlayer(player);
    }
}
