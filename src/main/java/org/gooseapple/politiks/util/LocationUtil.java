package org.gooseapple.politiks.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.gooseapple.politiks.Politiks;

public class LocationUtil {
    public static Location Deserialize(String input) {
        String[] args = input.split(" ");
        World world = Politiks.getInstance().getServer().getWorld(args[0]);

        int x = Integer.parseInt(args[1]);
        int y = Integer.parseInt(args[2]);
        int z = Integer.parseInt(args[3]);

        return new Location(world, x,y,z);
    }

    public static String Serialize(Location location) {
        return location.getWorld().getName() + " " + (int)location.getX() + " " + (int)location.getY() + " " + (int)location.getZ();
    }
}
