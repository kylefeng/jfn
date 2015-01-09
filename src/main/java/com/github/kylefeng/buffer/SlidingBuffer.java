package com.github.kylefeng.buffer;

import java.util.LinkedList;

import com.github.kylefeng.channel.UnblockingBuffer;

public class SlidingBuffer<T> implements Buffer<T>, UnblockingBuffer {
    private LinkedList<T> buf = new LinkedList<T>();
    private long          n;

    public SlidingBuffer(long n) {
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
        if (buf.size() == n) {
            this.remove();
        }
        buf.addFirst(item);
        return this;
    }

    @Override
    public long count() {
        return buf.size();
    }

}
