package org.gooseapple.politiks.core.land.geometry;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.gooseapple.politiks.core.land.ILand;

import java.util.ArrayList;
import java.util.UUID;

public class ClaimRectangle implements IClaim {
    private Location firstLocation;
    private Location secondLocation;
    private UUID id;
    private ILand land;
    private UUID landID;

    public ClaimRectangle(Location location, Location location2) {
        this.firstLocation = location;
        this.secondLocation = location2;
        id = UUID.randomUUID();
    }

    public ClaimRectangle(Location location, Location location2, UUID id) {
        this.firstLocation = location;
        this.secondLocation = location2;
        this.id = id;
    }

    @Override
    public int GetEnvironmentID() {
        return this.firstLocation.getWorld().getEnvironment().getId();
    }

    @Override
    public UUID getId() {
        return id;
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
    public ILand GetLand() {
        return land;
    }

    @Override
    public void SetLand(ILand land) {
        this.land = land;
    }

    @Override
    public void SetLandID(UUID id) {
        this.landID = id;
    }

    @Override
    public UUID GetLandID() {
        return this.landID;
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
