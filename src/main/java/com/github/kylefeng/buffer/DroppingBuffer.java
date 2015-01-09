package com.github.kylefeng.buffer;

import java.util.LinkedList;

import com.github.kylefeng.channel.UnblockingBuffer;

public class DroppingBuffer<T> implements Buffer<T>, UnblockingBuffer {
    private LinkedList<T> buf = new LinkedList<T>();
    private long          n;

    public DroppingBuffer(long n) {
        this.n = n;
    }

    @Override
    public boolean isFull() {
        return false;
    }

    @Override
    public T remove() {
        return buf.removeLast();
    }

    @Override
    public Buffer<T> add(T item) {
        if (buf.size() < n) {
            buf.addFirst(item);
        }
        return this;
    }

    @Override
    public long count() {
        return buf.size();
    }

}
