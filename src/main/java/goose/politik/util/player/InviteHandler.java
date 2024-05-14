package goose.politik.util.player;

import goose.politik.util.government.Town;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class InviteHandler {
    private static ConcurrentHashMap<UUID, CopyOnWriteArrayList<Town>> inviteMap = new ConcurrentHashMap<>();

    public static void invitePlayer(Town town, PolitikPlayer player) {
        CopyOnWriteArrayList<Town> playerTownInvites = inviteMap.get(player.getUUID());

        if (playerTownInvites == null) {
            playerTownInvites = new CopyOnWriteArrayList<>();
        }

        if (playerTownInvites.contains(town)) {
            return;
        }

        playerTownInvites.add(town);
        inviteMap.put(player.getUUID(), playerTownInvites);
    }

    public static boolean inviteExist(Town town, PolitikPlayer player) {
        if (inviteMap.get(player.getUUID()) != null) {
            return inviteMap.get(player.getUUID()).contains(town);
        }
        return false;
    }

    public static void removePlayerInvite(Town town, PolitikPlayer player) {
        if (!inviteExist(town, player)) {
            return;
        }

        CopyOnWriteArrayList<Town> inviteList = inviteMap.get(player.getUUID());
        inviteList.remove(town);
    }

    public static void acceptInvite(Town town, PolitikPlayer player) {
        town.addPlayer(player);
        removePlayerInvite(town, player);
    }
}
