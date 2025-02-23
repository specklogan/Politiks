package org.gooseapple.politiks.database;

public interface IDatabase {

    /**
     * Initialize the database, and return whether it was successfully initialized
     * @return
     */
    public boolean Initialize();
    public IPlayerTable getPlayerTable();
}
