package org.gooseapple.politiks.compat;

public class CompatManager {
    private static boolean checkFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static boolean foliaEnabled() {
        return checkFolia();
    }
}
