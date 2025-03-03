package org.gooseapple.politiks.core.currency;

import java.util.UUID;

public interface IAccountHolder {
    public Account GetAccount();
    public void AddAccount(Account account);
    public UUID GetAccountHolderUUID();
}
