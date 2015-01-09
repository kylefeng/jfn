package com.github.kylefeng.buffer;

import java.util.LinkedList;

public class FixedBuffer<T> implements Buffer<T> {
    private LinkedList<T> buf = new LinkedList<T>();
    private long          n;

    public FixedBuffer(long n) {
        this.n = n;
    }

    @Override
    public boolean isFull() {
        return buf.size() >= n;
    }

    @Override
    public T remove() {
        return buf.removeLast();
    }

    @Override
    public Buffer<T> add(T item) {
        buf.addFirst(item);
        return this;
    }

    @Override
    public long count() {
        return buf.size();
    }

}
