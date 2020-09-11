package com.quantumtime.qc.utils;

import java.util.concurrent.ThreadFactory;

public class QCThreadFactory implements ThreadFactory{
    private int threadCounter = 0;
    private String namePrefix = null;

    public QCThreadFactory(String name) {
        this.namePrefix = name + "-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName(namePrefix + (++threadCounter));
        t.setDaemon(true);
        t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
