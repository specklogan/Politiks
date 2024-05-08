package goose.politik.commands;

import goose.politik.Politik;

import java.util.Objects;

public class CommandRegister {

    public static void registerCommands() {
        Objects.requireNonNull(Politik.getInstance().getCommand("addmoney")).setExecutor(new AddMoneyCommand());
        Objects.requireNonNull(Politik.getInstance().getCommand("balance")).setExecutor(new BalCommand());
        Objects.requireNonNull(Politik.getInstance().getCommand("setmoney")).setExecutor(new SetMoneyCommand());
        Objects.requireNonNull(Politik.getInstance().getCommand("jobset")).setExecutor(new SetJobCommand());
        Objects.requireNonNull(Politik.getInstance().getCommand("claimtool")).setExecutor(new ClaimToolCommand());
        Objects.requireNonNull(Politik.getInstance().getCommand("nation")).setExecutor(new NationCommands());
        Objects.requireNonNull(Politik.getInstance().getCommand("town")).setExecutor(new TownCommand());
        Objects.requireNonNull(Politik.getInstance().getCommand("list")).setExecutor(new ListCommand());
        Objects.requireNonNull(Politik.getInstance().getCommand("land")).setExecutor(new LandCommand());
        Objects.requireNonNull(Politik.getInstance().getCommand("serverclaimtool")).setExecutor(new ServerClaimToolCommand());
    }
}
