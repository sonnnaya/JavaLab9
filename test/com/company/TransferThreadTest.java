package com.company;

import com.company.classes.Account;
import com.company.classes.Bank;
import com.company.runnable.TransferThread;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

class TransferThreadTest {
    private final int accountAmount = 100;
    private final double balance = 1000;
    private final Bank bank = new Bank(accountAmount, balance);
    private final int nThreads = 1000;
    private final ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);

    @Test
    void testMultiThreadTransfer() throws InterruptedException {
        var expected = bank.getBalance();

        IntStream.range(0, executor.getMaximumPoolSize()).
                forEachOrdered(x -> executor.execute(
                        new TransferThread(
                                bank,
                                new Account((int)(Math.random() * accountAmount), balance),
                                new Account((int)(Math.random() * accountAmount), balance),
                                balance)
                ));
        executor.shutdown();
        executor.awaitTermination(24L, TimeUnit.HOURS);

        var actual = bank.getBalance();
        bank.getAccounts().stream().forEach(TransferThreadTest::printAccount);
        Assertions.assertEquals(expected, actual);
    }

    public static void printAccount(Account account) {
        System.out.println("Account #" + account.getId());
        System.out.println("Balance: " + account.getBalance());
    }
}