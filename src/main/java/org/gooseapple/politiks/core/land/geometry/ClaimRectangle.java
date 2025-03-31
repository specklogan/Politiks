package org.gooseapple.politiks.core.land.geometry;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

public class ClaimRectangle implements IClaim {
    private Location firstLocation;
    private Location secondLocation;

    public ClaimRectangle(Location location, Location location2) {
        this.firstLocation = location;
        this.secondLocation = location2;
    }

    @Override
    public int GetEnvironmentID() {
        return this.firstLocation.getWorld().getEnvironment().getId();
    }

    @Override
    public int GetArea() {

        int firstX = firstLocation.getBlockX();
        int secondX = secondLocation.getBlockX();
        int firstZ = firstLocation.getBlockZ();
        int secondZ = secondLocation.getBlockZ();
        int width = Math.abs(firstX - secondX) + 1;
        int height = Math.abs(firstZ - secondZ) + 1;
        return width * height;
    }

    @Override
    public ArrayList<Long> GetContainingChunks() {
        ArrayList<Long> ChunkKeys = new ArrayList<>();

        if (IsSingleChunk()) {
            ChunkKeys.add(firstLocation.getChunk().getChunkKey());
            return ChunkKeys; //This is a quick check to see if the claim lies in the same chunk
        }

        World world = firstLocation.getWorld();
        int minX = Math.min(firstLocation.getBlockX(), secondLocation.getBlockX());
        int maxX = Math.max(firstLocation.getBlockX(), secondLocation.getBlockX());
        int minZ = Math.min(firstLocation.getBlockZ(), secondLocation.getBlockZ());
        int maxZ = Math.max(firstLocation.getBlockZ(), secondLocation.getBlockZ());

        int minChunkX = minX >> 4;
        int maxChunkX = maxX >> 4;
        int minChunkZ = minZ >> 4;
        int maxChunkZ = maxZ >> 4;

        for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
                ChunkKeys.add(world.getChunkAt(x, z).getChunkKey());
            }
        }

        return ChunkKeys;
    }

    @Override
    public boolean IsSingleChunk() {
        if (firstLocation.getChunk().getChunkKey() == secondLocation.getChunk().getChunkKey()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean Overlaps(IClaim otherClaim) {
        int firstX = firstLocation.getBlockX();
        int secondX = secondLocation.getBlockX();
        int firstZ = firstLocation.getBlockZ();
        int secondZ = secondLocation.getBlockZ();
        int minX = Math.min(firstX, secondX);
        int maxX = Math.max(firstX, secondX);
        int minZ = Math.min(firstZ, secondZ);
        int maxZ = Math.max(firstZ, secondZ);

        Location otherFirst = otherClaim.getFirstLocation();
        Location otherSecond = otherClaim.getSecondLocation();
        int otherMinX = Math.min(otherFirst.getBlockX(), otherSecond.getBlockX());
        int otherMaxX = Math.max(otherFirst.getBlockX(), otherSecond.getBlockX());
        int otherMinZ = Math.min(otherFirst.getBlockZ(), otherSecond.getBlockZ());
        int otherMaxZ = Math.max(otherFirst.getBlockZ(), otherSecond.getBlockZ());

        boolean overlapsX = maxX >= otherMinX && minX <= otherMaxX;
        boolean overlapsZ = maxZ >= otherMinZ && minZ <= otherMaxZ;

        return overlapsX && overlapsZ;
    }

    @Override
    public boolean PointInClaim(Location location) {
        int firstX = firstLocation.getBlockX();
        int secondX = secondLocation.getBlockX();
        int firstZ = firstLocation.getBlockZ();
        int secondZ = secondLocation.getBlockZ();

        int minX = Math.min(firstX, secondX);
        int maxX = Math.max(firstX, secondX);
        int minZ = Math.min(firstZ, secondZ);
        int maxZ = Math.max(firstZ, secondZ);

        int x = location.getBlockX();
        int z = location.getBlockZ();

        return x >= minX && x <= maxX && z >= minZ && z <= maxZ;
    }

    @Override
    public Location getSecondLocation() {
        return this.secondLocation;
    }

    @Override
    public Location getFirstLocation() {
        return this.firstLocation;
    }
}
