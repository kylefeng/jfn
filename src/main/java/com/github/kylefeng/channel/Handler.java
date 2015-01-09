package com.github.kylefeng.channel;

public interface Handler {
    public boolean isActive();

    public long getLockId();

    public void commit();

}
