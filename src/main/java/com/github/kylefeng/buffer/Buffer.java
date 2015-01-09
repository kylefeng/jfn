package com.github.kylefeng.buffer;

public interface Buffer<T> {
    public boolean isFull();

    public T remove();

    public Buffer<T> add(T item);

    public long count();

}
