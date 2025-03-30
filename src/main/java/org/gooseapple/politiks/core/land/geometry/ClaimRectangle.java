package org.gooseapple.politiks.core.land.geometry;

import org.bukkit.Location;

public class ClaimRectangle implements IClaim {
    private int firstX;
    private int firstZ;
    private int secondX;
    private int secondZ;

    public ClaimRectangle(Location location, Location location2) {
        this.firstX = location.getBlockX();
        this.firstZ = location.getBlockZ();

        this.secondX = location2.getBlockX();
        this.secondZ = location2.getBlockZ();
    }

    public int GetArea() {
        int width = Math.abs(firstX - secondX) + 1;
        int height = Math.abs(firstZ - secondZ) + 1;
        return width * height;
    }

    @Override
    public boolean PointInClaim(Location location) {
        int minX = Math.min(firstX, secondX);
        int maxX = Math.max(firstX, secondX);
        int minZ = Math.min(firstZ, secondZ);
        int maxZ = Math.max(firstZ, secondZ);

        int x = location.getBlockX();
        int z = location.getBlockZ();

        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }
}
