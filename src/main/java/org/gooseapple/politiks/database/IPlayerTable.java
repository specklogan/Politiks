package org.gooseapple.politiks.database;

import org.bukkit.entity.Player;
import org.gooseapple.politiks.core.player.PolitikPlayer;

import java.util.UUID;

public interface IPlayerTable extends ITable {
    /**
     * Creates a new player, only if the player is new to the server
     * @param player
     * @return
     */
    public PolitikPlayer LoadPlayer(Player player);
    public boolean SavePlayer(PolitikPlayer player);
    public void LoadAllPlayers();
    public void SaveAllPlayers();
    public PolitikPlayer GetPlayer(Player player);

    /**
     * Searches the player map for a player with the name provided.
     * @param name
     * @return Null if not found, PolitikPlayer otherwise
     */
    public PolitikPlayer GetPlayer(String name);
    /**
     * Searches the player map for a player with the UUID provided.
     * @param id
     * @return Null if not found, PolitikPlayer otherwise
     */
    public PolitikPlayer GetPlayer(UUID id);
}
