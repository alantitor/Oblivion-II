package ntou.cs.lab505.oblivionii.stream;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;

/**
 * Created by alan on 6/11/15.
 */
public class SoundInputPool extends Thread {

    private boolean threadState;  // denote thread state.
    private LinkedBlockingQueue<SoundVectorUnit> outputDataQueue;

    private int sampleRate;
    private int mode;


    /**
     *
     * @param sampleRate
     * @param mode 0: microphone, 1: read from data file, 2: read from wmv file
     */
    public SoundInputPool(int sampleRate, int mode) {

        this.sampleRate = sampleRate;
        this.mode = mode;

        // initial object.
        switch (mode) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            default:
                this.mode = 0;

        }
    }

    /**
     *
     * @param outputDataQueue
     */
    public void setOutputDataQueu(LinkedBlockingQueue<SoundVectorUnit> outputDataQueue) {
        this.outputDataQueue = outputDataQueue;
    }

    /**
     * control thread state.  start thread.
     */
    public void threadStart() {
        this.threadState = true;
        this.start();
    }

    /**
     * control thread state.  stop thread.
     */
    public void threadStop() {
        this.threadState = false;
        this.interrupt();
    }

    public void run() {
        Log.d("SoundInputPool", "in run. process start.");


        Log.d("SoundInputPool", "in run. process stop.");
    }
}
