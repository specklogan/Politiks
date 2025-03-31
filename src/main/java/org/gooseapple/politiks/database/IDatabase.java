package org.gooseapple.politiks.database;

public interface IDatabase {

    /**
     * Initialize the database, and return whether it was successfully initialized
     * @return
     */
    public boolean Initialize();
    public IPlayerTable getPlayerTable();
    public IAccountTable getAccountTable();

    void Save();
    public ITownTable getTownTable();
    public INationTable getNationTable();

    public ILandTable getLandTable();
}
