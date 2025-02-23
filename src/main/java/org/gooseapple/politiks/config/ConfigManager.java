package org.gooseapple.politiks.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.util.Constants;

import java.io.File;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class ConfigManager {
    private static YamlConfiguration config;
    public static String ConfigURL = "politiks-config.yml";

    public static void loadConfig() {
        if (!configExist()) {
            Politiks.getInstance().saveResource(ConfigURL, false);
        }


        config = new YamlConfiguration();
        config.options().parseComments(true);
        File file = new File(Politiks.getInstance().getDataFolder() + "/" + ConfigURL);
        try {
            config.load(file);
            checkIfConfigLatest();
        } catch (Exception e) {

        }
    }

    public static YamlConfiguration getConfig() {
        return config;
    }

    public static String getMongoURI() {
        return getConfig().getString("politiks.database.connection-string");
    }

    public static Constants.DatabaseType getDatabaseType() {
        String type = getConfig().getString("politiks.database.database-type");
        if (type == null) {
            return Constants.DatabaseType.MONGO;
        }
        if (type.toUpperCase() == "SQLITE") {
            return Constants.DatabaseType.SQLITE;
        }
        return Constants.DatabaseType.MONGO;
    }

    private static void checkIfConfigLatest() {
        InputStreamReader internalConfigReader = new InputStreamReader(Objects.requireNonNull(Politiks.getInstance().getResource(ConfigURL)), StandardCharsets.UTF_8);
        YamlConfiguration internalConfig = YamlConfiguration.loadConfiguration(internalConfigReader);

        for (String string : internalConfig.getKeys(true)) {
            //Line is the same
            if (config.contains(string)) {
                continue;
            }
            config.set(string, internalConfig.get(string));
        }

        try {
            config.save(Politiks.getInstance().getDataFolder() + "/politiks-config.yml");
        } catch (Exception e) {

        }
    }

    public static BigDecimal getClaimToolCost() {
        return BigDecimal.valueOf(config.getDouble("politiks.land.claim-tool-cost"));
    }

    public static BigDecimal getCostPerArea() {
        return BigDecimal.valueOf(config.getDouble("politiks.land.cost"));
    }

    public static BigDecimal getNationCost() {
        return BigDecimal.valueOf(config.getDouble("politiks.nation.cost"));
    }

    public static BigDecimal getTownCost() {
        return BigDecimal.valueOf(config.getDouble("politiks.town.cost"));
    }

    public static int getMaxLandSize() {
        return config.getInt("politiks.land.max-size");
    }

    public static int getMinLandSize() {
        return config.getInt("politiks.land.min-size");
    }


    private static boolean configExist() {

        return new File(Politiks.getInstance().getDataFolder() + "/" + ConfigURL).exists();
    }

    public static boolean canTick() {
        return config.getBoolean("politiks.economy.do-production");
    }
}
