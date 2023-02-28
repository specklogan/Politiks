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

        //check quickly if they are in the same chunk
        if (chunk == blockOne.getChunk()) {
            //both blocks are in the same chunk
            //now we check if a chunk is empty or not
            ArrayList<Land> chunkLand = getLandListInChunk(chunk);
            if (chunkLand == null) {
                //go ahead and add it
                return null;
            } else {
                //the chunk is not empty do a check
                for (int i =0; i < chunkLand.size(); i++) {
                    //loop through every land in here
                    Land tempLand = chunkLand.get(i);

                    int landOneLargestX;
                    int landOneSmallestX;
                    int landTwoSmallestX;
                    int landTwoLargestX;

                    //figure out which x is bigger on the first land
                    if (tempLand.getFirstBlock().getX() > tempLand.getSecondBlock().getX()) {
                        //first block is greater
                        //think of it like, the first position is EAST of the second position
                        landOneLargestX = tempLand.getFirstBlock().getX();
                        landOneSmallestX = tempLand.getSecondBlock().getX();

                    } else {
                        landOneLargestX = tempLand.getSecondBlock().getX();
                        landOneSmallestX = tempLand.getFirstBlock().getX();
                    }
                    if (land.getFirstBlock().getX() < land.getSecondBlock().getX()) {
                        landTwoSmallestX = land.getFirstBlock().getX();
                        landTwoLargestX = land.getSecondBlock().getX();
                    } else {
                        landTwoSmallestX = land.getSecondBlock().getX();
                        landTwoLargestX = land.getFirstBlock().getX();
                    }

                    int landOneLargestZ;
                    int landOneSmallestZ;
                    int landTwoSmallestZ;
                    int landTwoLargestZ;

                    //figure out if they are under or inside each other
                    if (tempLand.getFirstBlock().getZ() > tempLand.getSecondBlock().getZ()) {
                        landOneLargestZ = tempLand.getFirstBlock().getZ();
                        landOneSmallestZ = tempLand.getSecondBlock().getZ();
                    } else {
                        landOneLargestZ = tempLand.getSecondBlock().getZ();
                        landOneSmallestZ = tempLand.getFirstBlock().getZ();
                    }

                    if (land.getFirstBlock().getZ() < land.getSecondBlock().getZ()) {
                        landTwoSmallestZ = land.getFirstBlock().getZ();
                        landTwoLargestZ = land.getSecondBlock().getZ();
                    } else {
                        landTwoSmallestZ = land.getSecondBlock().getZ();
                        landTwoLargestZ = land.getFirstBlock().getZ();
                    }

                    if (landOneLargestX > landTwoSmallestX) {
                        //land one is to the right of the new land
                        if (landTwoLargestX - landOneSmallestX > 0) {
                            //collision on x axis, check the z axis next
                            if (landTwoLargestZ > landOneLargestZ) {
                                //new land is below old land
                                if (landTwoSmallestZ - landOneLargestZ < 0) {
                                    //collision on z axis
                                    return tempLand;
                                } else {
                                    //no collision
                                    return null;
                                }
                            } else {
                                //new land is above
                                if (landOneSmallestZ - landTwoLargestZ < 0) {
                                    return tempLand;
                                } else {
                                    return null;
                                }
                            }
                        } else {
                            //no collision on x axis, still need to check z axis
                            if (landTwoLargestZ > landOneLargestZ) {
                                //new land is below old land
                                if (landTwoSmallestZ - landOneLargestZ < 0) {
                                    //collision on z axis
                                    return tempLand;
                                } else {
                                    //no collision
                                    return null;
                                }
                            } else {
                                //new land is above
                                if (landOneSmallestZ - landTwoLargestZ < 0) {
                                    return tempLand;
                                } else {
                                    return null;
                                }
                            }
                        }


                    } else {
                        //land one is to the left of the new land
                        if (landTwoSmallestX - landOneLargestX < 0) {
                            //collision on x axis, check z next
                            if (landTwoLargestZ > landOneLargestZ) {
                                //new land is below old land
                                if (landTwoSmallestZ - landOneLargestZ < 0) {
                                    //collision on z axis
                                    return tempLand;
                                } else {
                                    //no collision
                                    return null;
                                }
                            } else {
                                //new land is above
                                if (landOneSmallestZ - landTwoLargestZ < 0) {
                                    return tempLand;
                                } else {
                                    return null;
                                }
                            }
                        } else {
                            //no collision on x axis, check z next
                            if (landTwoLargestZ > landOneLargestZ) {
                                //new land is below old land
                                if (landTwoSmallestZ - landOneLargestZ < 0) {
                                    //collision on z axis
                                    return tempLand;
                                } else {
                                    //no collision
                                    return null;
                                }
                            } else {
                                //new land is above
                                if (landOneSmallestZ - landTwoLargestZ < 0) {
                                    return tempLand;
                                } else {
                                    return null;
                                }
                            }
                        }
                    }
                }
            }
        } else {
            int deltaX = Math.abs(blockOne.getX() - blockTwo.getX()) + 1;
            int deltaZ = Math.abs(blockOne.getZ() - blockTwo.getZ()) + 1;
            //those tell you how big the rectangle will be, length and height
            //dont care for now, will do leader
        }

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
