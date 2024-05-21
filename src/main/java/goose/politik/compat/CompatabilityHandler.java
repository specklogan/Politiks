package goose.politik.compat;

public class CompatabilityHandler {
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
