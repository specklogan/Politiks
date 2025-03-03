package org.gooseapple.politiks.command;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.Plugin;
import org.gooseapple.politiks.command.implementation.BalanceCommand;
import org.gooseapple.politiks.command.implementation.SetBalanceCommand;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CommandManager {
    private static ArrayList<ICommand> c = new ArrayList<>();

    public static void registerCommands(LifecycleEventManager<Plugin> lifecycleManager) {
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, commands -> {
            c.add(new BalanceCommand());
            c.add(new SetBalanceCommand());

            for (ICommand command : c) {
                commands.registrar().register(command.build());
            }
        });
    }
}
