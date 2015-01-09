package com.github.kylefeng.channel;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Lock;

import com.github.kylefeng.buffer.Buffer;
import com.github.kylefeng.concurrent.Exec;

public class ManyToManyChannel<T> implements MMC, WritePort, ReadPort, Channel {
    private LinkedList<T> takes;
    private LinkedList<T> puts;
    private Buffer<T>     buf;
    private Lock          mutex;
    private AtomicBoolean closed = new AtomicBoolean(false);

    @Override
    public void close() {
        mutex.lock();
        cleanup();
        if (isClosed()) {
            mutex.unlock();
        } else {
            closed.compareAndSet(false, true);
            if (buf != null && puts.isEmpty()) {
                return;
            }
            Iterator<T> iter = takes.iterator();
            while (iter.hasNext()) {
                Lock taker = (Lock) iter.next();

                taker.lock();
                boolean take_cb = true;
                taker.unlock();

                if (take_cb) {
                    T val = null;
                    if (buf != null && buf.count() > 0) {
                        val = buf.remove();
                    }
                    Exec.run(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }
                iter.remove();
            }
        }
        mutex.unlock();
    }

    @Override
    public boolean isClosed() {
        return closed.get();
    }

    @Override
    public Object take(Object port, Object fn1_handler) {
        return null;
    }

    @Override
    public void put(Object port, Object fn1_handler) {
    }

    @Override
    public void cleanup() {
        if (!takes.isEmpty()) {
            Iterator<T> iter = takes.iterator();
            while (iter.hasNext()) {

            }
        }

        if (!puts.isEmpty()) {
            Iterator<T> iter = puts.iterator();
            while (iter.hasNext()) {

            }
        }
    }

    @Override
    public void abort() {
    }

}
