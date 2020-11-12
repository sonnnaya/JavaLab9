package com.company.runnable;

import com.company.classes.storages.CompositeCircularBuffer;

public class ProducerThread implements Runnable {
    private final CompositeCircularBuffer buffer;
    private final int nMessages;

    public ProducerThread(CompositeCircularBuffer buffer, int nMessages) {
        this.buffer = buffer;
        this.nMessages = nMessages;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < nMessages; ++i) {
                var threadNumber = Thread.currentThread().getId();
                var message = generateMessage();
                var text = "Thread " + threadNumber + " generated message: '" + message + "'";

                buffer.getFrom().put(text);
            }
        }
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }
    }

    public static String generateMessage() {
        return String.valueOf(Math.random());
    }
}
