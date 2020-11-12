package com.company.classes;

import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

public class Bank {
    private final ArrayList<Account> accounts;
    private final ReadWriteLock lock;

    public Bank(Integer accountAmount, Double sameBalance) {
        if (accountAmount <= 0 || sameBalance < 0) throw new IllegalArgumentException();

        this.accounts = new ArrayList<>(accountAmount);
        IntStream.range(0, accountAmount).forEachOrdered(x -> accounts.add(new Account(x, sameBalance)));

        lock = new ReentrantReadWriteLock();
    }

    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public Account getAccount(Account account) {
        return accounts.stream().filter(x -> x.getId().equals(account.getId())).findFirst().get();
    }

    public void transfer(Account from, Account to, Double amount) {
        if (amount < 0)
            throw new IllegalArgumentException();

        if (getAccount(from).updateBalance(-amount))
            getAccount(to).updateBalance(amount);
    }

    public Double getBalance() {
        lock.readLock().lock();
        try {
            return accounts.stream().
                    map(Account::getBalance).
                    reduce(Double::sum).
                    get();
        }
        finally {
            lock.readLock().unlock();
        }
    }
}
