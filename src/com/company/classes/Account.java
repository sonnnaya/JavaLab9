package com.company.classes;

import java.util.Objects;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Account {
    private final Integer id;
    private Double balance;
    private final ReadWriteLock lock;

    public Integer getId() { return id; }

    public Double getBalance() {
        lock.readLock().lock();
        try {
            return balance;
        }
        finally {
            lock.readLock().unlock();
        }
    }

    public Account(Integer id, Double balance) {
        this.id = id;
        this.balance = balance;
        this.lock = new ReentrantReadWriteLock();
    }

    public boolean updateBalance(Double amount) {
        lock.writeLock().lock();
        try {
            if (balance + amount < 0) return false;
            else {
                balance += amount;
                return true;
            }
        }
        finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public final int hashCode() { return Objects.hash(id, balance);  }

    @Override
    public final boolean equals(Object object) {
        if (!(object instanceof Account)) return false;
        if (object == this) return true;

        var account = (Account) object;
        var isSameId = Objects.equals(id, account.getId());
        var isSameBalance = Objects.equals(balance, account.getBalance());

        return isSameId && isSameBalance;
    }
}
