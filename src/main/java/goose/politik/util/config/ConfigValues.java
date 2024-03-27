package goose.politik.util.config;

public class ConfigValues {
    private static boolean tickable = false;

    public static void loadConfig() {

    }

    public static boolean canTick() {
        return tickable;
    }
}
