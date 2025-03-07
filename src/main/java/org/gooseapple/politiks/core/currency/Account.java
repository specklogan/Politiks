package org.gooseapple.politiks.core.currency;

import org.bson.types.ObjectId;
import org.gooseapple.politiks.core.player.PolitikPlayer;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Used to store currency, can be attached to a holder
 */
public class Account {
    private BigDecimal amount;
    private long id;
    private IAccountHolder holder;
    private AccountType accountType;

    public enum AccountType {
        PERSONAL,
        BUILDING,
        TOWN,
        NATION,
        BANK
    }

    public Account(AccountType type) {
        //default constructor
        amount = new BigDecimal(0);
        amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public Account(BigDecimal amount) {
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public IAccountHolder getHolder() {
        return holder;
    }

    public void setHolder(IAccountHolder holder) {
        this.holder = holder;
        if (holder instanceof PolitikPlayer) {
            accountType = AccountType.PERSONAL;
        }
    }

    public AccountType getAccountType() {
        return this.accountType;
    }

    public BigDecimal getBalance() {
        return this.amount;
    }

    public String getBalanceFormatted() {
        return "$" + amount.toString();
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAmount(double amount) {
        this.amount = new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN);
    }

    public boolean canWithdraw(double amount) {
        BigDecimal withdrawAmount = new BigDecimal(amount);
        return canWithdraw(withdrawAmount);
    }

    public boolean canWithdraw(BigDecimal amount) {
        var result = this.amount.compareTo(amount);
        return result >= 0;
    }

    public void deposit(double amount) {
        BigDecimal depositAmount = new BigDecimal(amount);
        deposit(depositAmount);
    }

    public void deposit(BigDecimal amount) {
        this.amount = this.amount.add(amount);
    }

    /**
     * Withdraws from the account, and returns true if the account has greater to or equal to the withdraw amount, returns false and doesn't write if it was unsucessful
     * @param amount
     * @return
     */
    public boolean safeWithdraw(double amount) {
        if (canWithdraw(amount)) {
            withdraw(amount);
            return true;
        }
        return false;
    }

    public void withdraw(double amount) {
        BigDecimal withDrawAmount = new BigDecimal(amount);
        withdraw(withDrawAmount);
    }

    /**
     * Withdraws from the account, and returns true if the account has greater to or equal to the withdraw amount, returns false and doesn't write if it was unsucessful
     * @param amount
     * @return
     */
    public boolean safeWithdraw(BigDecimal amount) {
        if (canWithdraw(amount)) {
            withdraw(amount);
            return true;
        }
        return false;
    }

    public void withdraw(BigDecimal amount) {
        this.amount = this.amount.subtract(amount);
    }
}
