package goose.politik.permissions;

import goose.politik.player.PolitikPlayer;
import goose.politik.util.landUtil.Land;

/**
 * When permissions are more complex, they will be split up, but the permissions are roughly the same
 */
public class PermissionHandler {
    public static boolean PlayerCanBreak(PolitikPlayer player, Land land) {
        if (land==null) {
            return true;
        }

        if (player.getPlayer().isOp()) {
            return true;
        }

        if (land.getPlayerOwner() == player) {
            return true;
        }

        if (player.getTown() == land.getTownOwner()) {
            return true;
        }
        return false;
    }

    public static boolean PlayerCanPlace(PolitikPlayer player, Land land) {
        if (land==null) {
            return true;
        }
        if (player.getPlayer().isOp()) {
            return true;
        }
        if (land.getPlayerOwner() == player) {
            return true;
        }
        if (player.getTown() == land.getTownOwner()) {
            return true;
        }
        return false;
    }

    public static boolean PlayerCanInteract(PolitikPlayer player, Land land) {
        if (land==null) {
            return true;
        }
        if (player.getPlayer().isOp()) {
            return true;
        }
        if (land.getPlayerOwner() == player) {
            return true;
        }
        if (player.getTown() == land.getTownOwner()) {
            return true;
        }
        return false;
    }
}
