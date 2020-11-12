package com.company.runnable;

import com.company.classes.Account;
import com.company.classes.Bank;

public class TransferThread implements Runnable {
    private final Bank bank;
    private final Account from;
    private final Account to;
    private final Double amount;

    public TransferThread(Bank bank, Account from, Account to, Double amount) {
        this.bank = bank;
        this.from = from;
        this.to = to;
        this.amount = amount;
    }

    @Override
    public void run() {
        bank.transfer(from, to, amount);
    }
}
