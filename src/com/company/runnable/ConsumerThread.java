package com.company.runnable;

import com.company.classes.storages.CompositeCircularBuffer;

public class ConsumerThread implements Runnable {
    private final CompositeCircularBuffer buffer;
    private final int nMessages;

    public ConsumerThread(CompositeCircularBuffer buffer, int nMessages) {
        this.buffer = buffer;
        this.nMessages = nMessages;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < nMessages; ++i) {
                var threadNumber = Thread.currentThread().getId();
                var message = buffer.getFrom().take();
                var text = "Thread " + threadNumber + " transferred message: '" + message + "'";

                buffer.getTo().put(text);
                Thread.sleep(500);
            }
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }
}