package org.gooseapple.politiks.database.implementation.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bukkit.entity.Player;
import org.gooseapple.politiks.Politiks;
import org.gooseapple.politiks.core.currency.Account;
import org.gooseapple.politiks.core.currency.IAccountHolder;
import org.gooseapple.politiks.core.player.PolitikPlayer;
import org.gooseapple.politiks.database.DatabaseManager;
import org.gooseapple.politiks.database.IAccountTable;
import org.gooseapple.politiks.util.Constants;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AccountTable implements IAccountTable {
    private MongoDatabase database;
    private MongoCollection<Document> table;

    public AccountTable(MongoDatabase database) {
        this.database = database;
    }

    //This stores the accounts tied to a player for quicker access
    private ConcurrentHashMap<UUID, ArrayList<Account>> playerAccountMap = new ConcurrentHashMap<>();
    //Stores town, land, and nation accounts
    private ConcurrentHashMap<UUID, ArrayList<Account>> otherAccountMap = new ConcurrentHashMap<>();


    @Override
    public boolean CreateTable() {
        table = database.getCollection(AccountTable.class.getSimpleName());
        table.createIndex(Indexes.text(Constants.AccountID));

        return true;
    }

    @Override
    public Account CreateNewAccount(Account.AccountType type, IAccountHolder holder) {
        long newId = GetNextAccountId();
        Account account = new Account(type);
        account.setId(newId);
        account.setHolder(holder);

        //Add them to the account map if they are a player
        if (holder instanceof PolitikPlayer) {
            PolitikPlayer p = (PolitikPlayer) holder;
            boolean exists = playerAccountMap.containsKey(p.getUUID());
            if (!exists) {
                playerAccountMap.put(p.getUUID(), new ArrayList<>());
            }

            playerAccountMap.get(p.getUUID()).add(account);
        }

        //TODO: Implement the bank code account creation later

        return account;
    }

    @Override
    public long GetNextAccountId() {
        Document highestDocument = table.find().sort(Sorts.descending(Constants.AccountID)).first();

        if (highestDocument != null) {
            long value = highestDocument.getLong(Constants.AccountID);
            return value + 1;
        }
        return 0; //Base case, return 0 so the first account will use the ID of 0
    }

    @Override
    public Account GetPlayerAccount(PolitikPlayer p) {
        var accounts = playerAccountMap.get(p.getUUID());
        if (accounts == null || accounts.isEmpty()) {
            return null;
        }
        return accounts.getFirst();
    }

    @Override
    public void LoadAllAccounts() {
        for (Document document : table.find()) {
            long id = document.getLong(Constants.AccountID);
            Account.AccountType type = Account.AccountType.valueOf(document.getString("AccountType"));
            Account account = new Account(type);
            account.setId(id);
            account.setAmount(new BigDecimal(document.getString("Balance")));

            UUID holderUUID = UUID.fromString(document.getString("AccountHolder"));
            if (type == Account.AccountType.PERSONAL) {

                PolitikPlayer p = DatabaseManager.getDatabase().getPlayerTable().GetPlayer(holderUUID);
                AddExistingAccountToPlayer(account, p);
            } else {
                
            }
        }
    }

    private void AddExistingAccountToPlayer(Account account, PolitikPlayer player) {
        boolean exists = playerAccountMap.containsKey(player.getUUID());
        if (!exists) {
            playerAccountMap.put(player.getUUID(), new ArrayList<>());
        }
        account.setHolder(player);
        var list = playerAccountMap.get(player.getUUID());
        list.add(account);
    }

    @Override
    public void SaveAllAccounts() {
        List<WriteModel<Document>> operation = new ArrayList<>();
        for (UUID id : playerAccountMap.keySet()) {

            for (Account account : playerAccountMap.get(id)) {
                Document accountDocument = AccountToDocument(account);
                Document filter = new Document(Constants.AccountID, account.getId());

                ReplaceOneModel<Document> replaceOneModel = new ReplaceOneModel<>(
                        filter,
                        accountDocument,
                        new ReplaceOptions().upsert(true)
                );
                operation.add(replaceOneModel);
            }
        }

        for (UUID id : otherAccountMap.keySet()) {
            for (Account account : otherAccountMap.get(id)) {
                Document accountDocument = AccountToDocument(account);
                Document filter = new Document(Constants.AccountID, account.getId());

                ReplaceOneModel<Document> replaceOneModel = new ReplaceOneModel<>(
                        filter,
                        accountDocument,
                        new ReplaceOptions().upsert(true)
                );
                operation.add(replaceOneModel);
            }
        }

        if (operation.isEmpty()) {
            return;
        }
        table.bulkWrite(operation);
    }

    private Document AccountToDocument(Account account) {
        Document document = new Document();
        document.put(Constants.AccountID, account.getId());
        document.put("AccountType", account.getAccountType());
        document.put("Balance", account.getBalance().toString());
        document.put("AccountHolder", account.getHolder().GetAccountHolderUUID().toString());
        return document;
    }

    @Override
    public void SaveAccount(Account account) {
        Document document = AccountToDocument(account);
        Document filter = new Document(Constants.AccountID, account.getId());
        UpdateOptions options = new UpdateOptions().upsert(true);
        table.updateOne(filter, document, options);
    }

    @Override
    public Account GetOtherAccount(UUID id) {
        ArrayList<Account> accounts = otherAccountMap.get(id);
        if (accounts == null || accounts.isEmpty()) {
            return null;
        }
        return accounts.getFirst();
    }
}
