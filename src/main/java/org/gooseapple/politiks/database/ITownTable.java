package org.gooseapple.politiks.database;

import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.core.town.Town;

public interface ITownTable extends ITable{
    public Town GetTown(String name);
    public void LoadAllTowns();
    public Town CreateTown(PolitikPlayer mayor);
    public void SaveAllTowns();
}
