package goose.politik.util.database;

import goose.politik.Politik;
import org.bukkit.Location;

public class WorldStringLoader {
    public static Location loadLocation(String location) {
        //convert a string location to an actual location
        String[] splitLocation = location.split(",");
        return new Location(Politik.getInstance().getServer().getWorld(splitLocation[0]), Double.parseDouble(splitLocation[1]), Double.parseDouble(splitLocation[2]), Double.parseDouble(splitLocation[3]));
    }
}
