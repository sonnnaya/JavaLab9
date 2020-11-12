package com.company.classes.storages;

public class CompositeCircularBuffer {
    private final CircleBuffer<String> from;
    private final CircleBuffer<String> to;

    public CompositeCircularBuffer(int capacityFrom, int capacityTo) {
        this.from = new CircleBuffer<>(capacityFrom);
        this.to = new CircleBuffer<>(capacityTo);
    }

    public CircleBuffer<String> getFrom() {
        return from;
    }

    public CircleBuffer<String> getTo() {
        return to;
    }
}
