package goose.politik.util.landUtil;

import goose.politik.Politik;
import jdk.jfr.internal.Logger;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

public class LandUtil {

    public static final HashMap<World.Environment, HashMap<Long, ArrayList<Land>>> landMap = new HashMap<>();

    public static ArrayList<Land> getLandListInChunk(Chunk chunk) {
        return landMap.get(chunk.getWorld().getEnvironment()).get(chunk.getChunkKey());
    }

    public static void addDimensionToLandMap(World.Environment dimension) {
        landMap.put(dimension, new HashMap<>());
    }

    public static Land getLandInLand(Land land, Chunk chunk) {
        Block blockOne = land.getFirstBlock();
        Block blockTwo = land.getSecondBlock();
        World world = chunk.getWorld();

        //check quickly if they are in the same chunk
        if (chunk == blockOne.getChunk()) {
            //both blocks are in the same chunk
            //now we check if a chunk is empty or not
            ArrayList<Land> chunkLand = getLandListInChunk(chunk);
            if (chunkLand != null) {
                //the chunk is not empty do a check
                for (Land tempLand : chunkLand) {
                    //loop through every land in here
                    Block firstLandFirstBlock = tempLand.getFirstBlock();
                    Block firstLandSecondBlock = tempLand.getSecondBlock();

                    Block secondLandFirstBlock = land.getFirstBlock();
                    Block secondLandSecondBlock = land.getSecondBlock();

                    //quick check to ensure that the Block1 of one land isnt the same as of the other
                    if (firstLandFirstBlock.equals(secondLandFirstBlock) || firstLandFirstBlock.equals(secondLandSecondBlock) || firstLandSecondBlock.equals(secondLandFirstBlock) || firstLandSecondBlock.equals(secondLandSecondBlock)) {
                        return tempLand;
                    }

                    int landOneLargestX;
                    int landOneSmallestX;
                    int landTwoSmallestX;
                    int landTwoLargestX;

                    //figure out which x is bigger on the first land
                    if (firstLandFirstBlock.getX() > firstLandSecondBlock.getX()) {
                        //first block is greater
                        //think of it like, the first position is EAST of the second position
                        landOneLargestX = firstLandFirstBlock.getX();
                        landOneSmallestX = firstLandSecondBlock.getX();

                    } else {
                        landOneLargestX = firstLandSecondBlock.getX();
                        landOneSmallestX = firstLandFirstBlock.getX();
                    }
                    if (secondLandFirstBlock.getX() < secondLandSecondBlock.getX()) {
                        landTwoSmallestX = secondLandFirstBlock.getX();
                        landTwoLargestX = secondLandSecondBlock.getX();
                    } else {
                        landTwoSmallestX = secondLandSecondBlock.getX();
                        landTwoLargestX = secondLandFirstBlock.getX();
                    }

                    int landOneLargestZ;
                    int landOneSmallestZ;
                    int landTwoSmallestZ;
                    int landTwoLargestZ;

                    if (firstLandFirstBlock.getZ() > firstLandSecondBlock.getZ()) {
                        landOneLargestZ = firstLandFirstBlock.getZ();
                        landOneSmallestZ = firstLandSecondBlock.getZ();
                    } else {
                        landOneLargestZ = firstLandSecondBlock.getZ();
                        landOneSmallestZ = firstLandFirstBlock.getZ();
                    }

                    if (secondLandFirstBlock.getZ() < secondLandSecondBlock.getZ()) {
                        landTwoSmallestZ = secondLandFirstBlock.getZ();
                        landTwoLargestZ = secondLandSecondBlock.getZ();
                    } else {
                        landTwoSmallestZ = secondLandSecondBlock.getZ();
                        landTwoLargestZ = secondLandFirstBlock.getZ();
                    }

                    //given all of our smallest, and greatest x and z for each corner
                    //we can now calculate everything

                    boolean xCollision = false;
                    boolean zCollision = false;
                    if (landOneSmallestX < landTwoLargestX && landOneLargestX > landTwoSmallestX) {
                        //x collision
                        xCollision = true;
                    }
                    if (landOneSmallestZ < landTwoLargestZ && landOneLargestZ > landTwoSmallestZ) {
                        //z collision
                        zCollision = true;
                    }

                    if (xCollision && zCollision) {
                        //it did actually collide
                        return tempLand;
                    }
                    //since it did not collide, we can go ahead and add it
                    LandUtil.addToLandMap(land, chunk);
                    return null;
                }
            }
        } else {
            //Trying to make a multi chunk claim
            //To do this, we need to check all of the chunks, then check all of those claims
            Chunk firstBlockChunk = land.getFirstBlock().getChunk();
            Chunk secondBlockChunk = land.getSecondBlock().getChunk();
            Politik.logger.log(Level.INFO, "FirstChunk: " + firstBlockChunk.getX() + " " + firstBlockChunk.getZ());
            Politik.logger.log(Level.INFO, "Second: " + secondBlockChunk.getX() + " " + secondBlockChunk.getZ());

            //you can think of this as:
            /*
                |1stpos, x |
                |x, 2ndpos |

                Where the two positions are in different chunks, and we treat it like a 2D array, looping through the x and z chunks
             */
            //Now we have to figure out which z, and x is less
            int minX, maxX, minZ, maxZ;
            if (firstBlockChunk.getX() > secondBlockChunk.getX()) {
                maxX = firstBlockChunk.getX();
                minX = secondBlockChunk.getX();
            } else {
                maxX = secondBlockChunk.getX();
                minX = firstBlockChunk.getX();
            }
            if (firstBlockChunk.getZ() > secondBlockChunk.getZ()) {
                maxZ = firstBlockChunk.getZ();
                minZ = secondBlockChunk.getZ();
            } else {
                maxZ = secondBlockChunk.getZ();
                minZ = firstBlockChunk.getZ();
            }

            //create an empty list so that, if successful, it'll add these chunks to the map
            ArrayList<Chunk> successChunks = new ArrayList<>();

            //now knowing the minimum, we have to loop through it like a 2d array
            for (int i = minX; i <= maxX; i++) {
                for (int v = minZ; v <= maxZ; v++) {
                    Politik.logger.log(Level.INFO, "Looping through chunk " + i + " " + v);
                    ArrayList<Land> chunkLand = getLandListInChunk(world.getChunkAt(i,v));
                    if (chunkLand != null) {
                        // chunk is not empty
                        for (Land tempLand : chunkLand) {
                            //loop through every land in here
                            Block firstLandFirstBlock = tempLand.getFirstBlock();
                            Block firstLandSecondBlock = tempLand.getSecondBlock();

                            Block secondLandFirstBlock = land.getFirstBlock();
                            Block secondLandSecondBlock = land.getSecondBlock();

                            //quick check to ensure that the Block1 of one land isnt the same as of the other
                            if (firstLandFirstBlock.equals(secondLandFirstBlock) || firstLandFirstBlock.equals(secondLandSecondBlock) || firstLandSecondBlock.equals(secondLandFirstBlock) || firstLandSecondBlock.equals(secondLandSecondBlock)) {
                                return tempLand;
                            }

                            int landOneLargestX;
                            int landOneSmallestX;
                            int landTwoSmallestX;
                            int landTwoLargestX;

                            //figure out which x is bigger on the first land
                            if (firstLandFirstBlock.getX() > firstLandSecondBlock.getX()) {
                                //first block is greater
                                //think of it like, the first position is EAST of the second position
                                landOneLargestX = firstLandFirstBlock.getX();
                                landOneSmallestX = firstLandSecondBlock.getX();

                            } else {
                                landOneLargestX = firstLandSecondBlock.getX();
                                landOneSmallestX = firstLandFirstBlock.getX();
                            }
                            if (secondLandFirstBlock.getX() < secondLandSecondBlock.getX()) {
                                landTwoSmallestX = secondLandFirstBlock.getX();
                                landTwoLargestX = secondLandSecondBlock.getX();
                            } else {
                                landTwoSmallestX = secondLandSecondBlock.getX();
                                landTwoLargestX = secondLandFirstBlock.getX();
                            }

                            int landOneLargestZ;
                            int landOneSmallestZ;
                            int landTwoSmallestZ;
                            int landTwoLargestZ;

                            if (firstLandFirstBlock.getZ() > firstLandSecondBlock.getZ()) {
                                landOneLargestZ = firstLandFirstBlock.getZ();
                                landOneSmallestZ = firstLandSecondBlock.getZ();
                            } else {
                                landOneLargestZ = firstLandSecondBlock.getZ();
                                landOneSmallestZ = firstLandFirstBlock.getZ();
                            }

                            if (secondLandFirstBlock.getZ() < secondLandSecondBlock.getZ()) {
                                landTwoSmallestZ = secondLandFirstBlock.getZ();
                                landTwoLargestZ = secondLandSecondBlock.getZ();
                            } else {
                                landTwoSmallestZ = secondLandSecondBlock.getZ();
                                landTwoLargestZ = secondLandFirstBlock.getZ();
                            }

                            //given all of our smallest, and greatest x and z for each corner
                            //we can now calculate everything

                            boolean xCollision = false;
                            boolean zCollision = false;
                            if (landOneSmallestX < landTwoLargestX && landOneLargestX > landTwoSmallestX) {
                                //x collision
                                xCollision = true;
                            }
                            if (landOneSmallestZ < landTwoLargestZ && landOneLargestZ > landTwoSmallestZ) {
                                //z collision
                                zCollision = true;
                            }

                            if (xCollision && zCollision) {
                                //it did actually collide
                                return tempLand;
                            }
                            //succeeded here
                            successChunks.add(world.getChunkAt(i,v));
                        }
                    } else {
                        successChunks.add(world.getChunkAt(i,v));
                        //chunk was empty
                    }

                }
            }
            //if it gets to this point, that means that it hasn't encountered something blocking it
            //so, it is good to add to the list
            for (Chunk successChunk : successChunks) {
                Politik.logger.log(Level.INFO, "Chunk had no collisions " + successChunk.getX() + " " + successChunk.getZ());
                addToLandMap(land, successChunk);
            }
            return null;
        }
        addToLandMap(land, chunk);
        return null;
    }

    public static Land blockInLand(Block block) {
        //returns true or false if a block was in a land
        ArrayList<Land> landsInChunk = getLandListInChunk(block.getChunk());
        if (landsInChunk == null) {
            //empty chunk, continue
            return null;
        } else {
            //do some calculations
            //we can assume no lands overlap that are stored
            //because when they are added, we check that that's not the case

            for (int i=0; i<landsInChunk.size(); i++) {
                //iterate over all of the lands in the chunk
            }

            return null;
        }
    }

    public static int addToLandMap(Land land, Chunk chunk) {
        //adds a land object to the hashmap
        if (chunk.getWorld().getEnvironment() == World.Environment.NORMAL) {
            //add a chunk to the map
            HashMap<Long, ArrayList<Land>> overworldLandMap = landMap.get(chunk.getWorld().getEnvironment());
            ArrayList<Land> chunkList = overworldLandMap.get(chunk.getChunkKey());
            if (chunkList == null) {
                //nothing added in land yet
                chunkList = new ArrayList<>();
                chunkList.add(land);
                overworldLandMap.put(chunk.getChunkKey(), chunkList);
                return 0;
            } else {
                chunkList.add(land);
                overworldLandMap.put(chunk.getChunkKey(), chunkList);
            }
        }
        return -1;
    }
}