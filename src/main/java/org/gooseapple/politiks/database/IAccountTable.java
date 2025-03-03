package org.gooseapple.politiks.database;

import org.gooseapple.politiks.core.currency.Account;
import org.gooseapple.politiks.core.currency.IAccountHolder;
import org.gooseapple.politiks.core.player.PolitikPlayer;

/**
 * Stores currency data for bank accounts, and player account
 */
public interface IAccountTable extends ITable {


    public Account CreateNewAccount(Account.AccountType type, IAccountHolder holder);
    public long GetNextAccountId();
    public Account GetPlayerAccount(PolitikPlayer p);
    public void LoadAllAccounts();
    public void SaveAllAccounts();
    public void SaveAccount(Account account);
}
