package org.gooseapple.politiks.core.currency;

import org.gooseapple.politiks.core.player.PolitikPlayer;

import java.math.BigDecimal;

/**
 * Used to store currency, can be attached to a holder
 */
public class Account {
    private BigDecimal amount;
    private IAccountHolder holder;
    private AccountType accountType;

    public enum AccountType {
        PERSONAL,
        BUILDING,
        BANK
    }

    public Account() {
        //default constructor
        amount = new BigDecimal(0);
    }

    public Account(BigDecimal amount) {
        this.amount = amount;
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

    public BigDecimal getAmount() {
        return this.amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setAmount(double amount) {
        this.amount = new BigDecimal(amount);
    }

    public boolean canWithdraw(double amount) {
        BigDecimal withdrawAmount = new BigDecimal(amount);
        return canWithdraw(withdrawAmount);
    }

    public boolean canWithdraw(BigDecimal amount) {
        var result = this.amount.compareTo(amount);
        return result >= 0;
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
