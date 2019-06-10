package rs.raf.infview.util;

import rs.raf.infview.observer.ChangeObserver;

public class DelayRunner {

    private final ChangeObserver observer;
    private final long delay;

    public DelayRunner(ChangeObserver data, long delay) {
        this.observer = data;
        this.delay = delay;
    }

    public void run() {
        new Thread() {
            @Override
            public void run() {
                try {
                    while(!isInterrupted()) {
                        observer.onChange();
                        sleep(delay);
                    }
                }catch(InterruptedException e) {
                    interrupt();
                }
            }
        }.start();
    }
}
