package goose.politik.util.config;

public class ConfigHandler {
    private static Config config;

    public static void loadConfig() {
        if (!configExist()) {
            //setup new config file
            Config config = new Config();
            return;
        }
        Config config = new Config();
    }

    public static String getURL() {
        return config.getURL();
    }

    private static boolean configExist() {

        return false;
    }

    public static boolean canTick() {
        return false;
    }
}
