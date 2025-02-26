package org.gooseapple.politiks.command;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.gooseapple.politiks.command.implementation.BalanceCommand;
import org.jetbrains.annotations.NotNull;

public class CommandManager {
    public static void registerCommands(LifecycleEventManager<Plugin> lifecycleManager) {
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
                BalanceCommand command = new BalanceCommand();
                commands.registrar().register(command.build());
        });
    }
}
