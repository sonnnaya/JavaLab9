package com.company.classes.storages;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CircleBuffer<T> {
    private final ReentrantLock lock;
    private final Condition isFull;
    private final Condition isEmpty;

    private final Object[] buffer;
    private int amount;

    private int startIndex;
    private int endIndex;

    public CircleBuffer(int capacity) {
        this.lock = new ReentrantLock();
        this.isFull = lock.newCondition();
        this.isEmpty = lock.newCondition();

        this.buffer = new Object[capacity];
        this.amount = 0;

        this.startIndex = 0;
        this.endIndex = 0;
    }

    public Object[] getBuffer() {
        return buffer;
    }

    public void put(T object) throws InterruptedException {
        lock.lock();
        try {
            while (amount == buffer.length) isFull.await();

            buffer[endIndex] = object;
            if (++endIndex == buffer.length) endIndex = 0;
            amount += 1;

            isEmpty.signal();
        }
        finally {
            lock.unlock();
        }
    }

    public T take() throws InterruptedException {
        lock.lock();
        try {
            while (amount == 0) isEmpty.await();

            var object = (T) buffer[startIndex];
            buffer[startIndex] = null;
            if (++startIndex == buffer.length) startIndex = 0;
            amount -= 1;

            isFull.signal();
            return object;
        }
        finally {
            lock.unlock();
        }
    }
}


