package com.company;

import com.company.classes.storages.CompositeCircularBuffer;
import com.company.runnable.ConsumerThread;
import com.company.runnable.ProducerThread;

import java.util.concurrent.*;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        var capacityFrom = 10;
        var nProducerThreads = 5;
        var nConsumerThreads = 2;
        var nMessages = 100;

        var buffer = new CompositeCircularBuffer(capacityFrom, nMessages);

        var executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(7,
                runnable -> {
                    Thread thread = Executors.defaultThreadFactory().newThread(runnable);
                    thread.setDaemon(true);
                    return thread;
                });

        var producerThread = new ProducerThread(buffer, nMessages / nProducerThreads);
        var consumerThread = new ConsumerThread(buffer, nMessages / nConsumerThreads);

        IntStream.range(0, nConsumerThreads).forEachOrdered(x -> executor.execute(consumerThread));
        IntStream.range(0, nProducerThreads).forEachOrdered(x -> executor.execute(producerThread));

        for (int i = 0; i < nMessages; ++i)
            System.out.println(buffer.getTo().take());

//        executor.shutdown();
//        executor.awaitTermination(24L, TimeUnit.HOURS);
    }
}
