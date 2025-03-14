package org.gooseapple.politiks.database;

import org.gooseapple.politiks.core.nation.Nation;
import org.gooseapple.politiks.core.player.PolitikPlayer;

import java.util.concurrent.CopyOnWriteArrayList;

public interface INationTable extends ITable {
    public Nation CreateNation(PolitikPlayer leader, String name);
    public void LoadAllNations();
    public void SaveAllNations();
    public Nation GetNation(String name);
    public boolean NationNameExists(String name);

    public CopyOnWriteArrayList<PolitikPlayer> GetNationPlayers(Nation nation);
}
