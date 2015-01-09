package com.github.kylefeng.channel;

public interface ReadPort {
    public Object take(Object port, Object fn1_handler);
}
