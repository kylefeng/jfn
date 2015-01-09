package com.github.kylefeng.channel;

public interface Channel {
    public void close();

    public boolean isClosed();
}
