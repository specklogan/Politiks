package org.gooseapple.politiks.database.implementation.mongo;

import com.mongodb.client.MongoDatabase;
import org.gooseapple.politiks.database.IAccountTable;

public class AccountTable implements IAccountTable {
    private MongoDatabase database;
    public AccountTable(MongoDatabase database) {
        this.database = database;
    }

    @Override
    public boolean CreateTable() {

        return true;
    }
}
