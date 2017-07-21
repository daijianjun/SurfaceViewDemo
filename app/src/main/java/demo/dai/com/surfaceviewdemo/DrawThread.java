package demo.dai.com.surfaceviewdemo;

import android.os.Looper;

/**
 * 操作Surface View线程
 * Created by dai on 2017/7/18.
 */

public class DrawThread extends Thread {
    private Looper mLooper;

    @Override
    public void run() {
        Looper.prepare();
        synchronized (this) {
            mLooper = Looper.myLooper();
            notifyAll();
        }
        Looper.loop();
    }

    public Looper getLooper() {
        // If the thread has been started, wait until the looper has been created.
        synchronized (this) {
            while (isAlive() && mLooper == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
        }
        return mLooper;
    }
}
