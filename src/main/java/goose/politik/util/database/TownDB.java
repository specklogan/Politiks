package goose.politik.util.database;

import goose.politik.util.government.Town;

public class TownDB {
    //We need to implement a custom town to database format
    /*
        Important things to save:
            1) UUID of members + their 'role'
            2) Nation that it is under
            3) Amount of money in town bank
            4) Spawn location
            5) Town name
     */

    public static void saveTown(Town town) {

    }

    public static void loadTowns() {
        //loads all towns into the nation that owns them
        //the loading process should go like this
        /*
            1) Load all players
            2) Load all nations
            3) Load all towns
            4) Load all lands
         */
        //loop through the town DB and load them one-by-one, add all of their members into the town and nation

    }
}
