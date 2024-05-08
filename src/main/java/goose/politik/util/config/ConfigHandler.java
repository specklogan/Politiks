package goose.politik.util.config;

import goose.politik.Politik;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.logging.Level;

public class ConfigHandler {
    private static YamlConfiguration config;

    public static void loadConfig() {
        if (!configExist()) {
            Politik.plugin.saveResource("politik-config.yml", false);
        }


        config = new YamlConfiguration();
        config.options().parseComments(true);
        File file = new File(Politik.plugin.getDataFolder() + "/politik-config.yml");
        try {
            config.load(file);
            checkIfConfigLatest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static YamlConfiguration getConfig() {
        return config;
    }

    public static String getURI() {
        return getConfig().getString("politik.database.connection-string");
    }

    private static void checkIfConfigLatest() {
        InputStreamReader internalConfigReader = new InputStreamReader(Objects.requireNonNull(Politik.getInstance().getResource("politik-config.yml")), StandardCharsets.UTF_8);
        YamlConfiguration internalConfig = YamlConfiguration.loadConfiguration(internalConfigReader);

        for (String string : internalConfig.getKeys(true)) {
            //Line is the same
            if (config.contains(string)) {
                continue;
            }
            config.set(string, internalConfig.get(string));
        }

        try {
            config.save(Politik.plugin.getDataFolder() + "/politik-config.yml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static BigDecimal getClaimToolCost() {
        return BigDecimal.valueOf(config.getDouble("politik.land.claim-tool-cost"));
    }

    public static BigDecimal getCostPerArea() {
        return BigDecimal.valueOf(config.getDouble("politik.land.cost"));
    }

    public static BigDecimal getNationCost() {
        return BigDecimal.valueOf(config.getDouble("politik.nation.cost"));
    }

    public static BigDecimal getTownCost() {
        return BigDecimal.valueOf(config.getDouble("politik.town.cost"));
    }

    public static int getMaxLandSize() {
        return config.getInt("politik.land.max-size");
    }

    public static int getMinLandSize() {
        return config.getInt("politik.land.min-size");
    }


    private static boolean configExist() {

        return new File(Politik.plugin.getDataFolder() + "/politik-config.yml").exists();
    }

    public static boolean canTick() {
        return false;
    }
}
