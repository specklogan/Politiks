package org.gooseapple.politiks.database;

import org.gooseapple.politiks.core.nation.Nation;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;

import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public interface ITownTable extends ITable{
    public Town GetTown(String name);
    public Town GetTown(UUID id);
    public boolean TownNameExists(String name);
    public Nation GetNationFromTown(Town t);
    public void LoadAllTowns();
    public CopyOnWriteArrayList<Town> GetAllTowns();

    /**
     * Returns and adds the town to the datastore
     * @param mayor
     * @return
     */
    public Town CreateTown(PolitikPlayer mayor, String name);
    public void SaveAllTowns();
}
