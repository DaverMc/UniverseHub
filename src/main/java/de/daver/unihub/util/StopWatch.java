package de.daver.unihub.util;

public abstract class StopWatch {

    private long time;
    private long start;

    public StopWatch(){
        start = System.currentTimeMillis();
    }

    public abstract void run();

    public void start(){
        run();
        time = System.currentTimeMillis() - start;
    }

    public long getMillis(){
        return time;
    }

    public long getSeconds(){
        return time / 1000;
    }

    public long getMinutes(){
        return getSeconds() / 60;
    }

}
