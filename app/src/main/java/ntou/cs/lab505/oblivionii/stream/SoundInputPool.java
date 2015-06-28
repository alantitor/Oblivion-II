package ntou.cs.lab505.oblivionii.stream;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;
import ntou.cs.lab505.oblivionii.stream.device.Microphone;
import ntou.cs.lab505.oblivionii.stream.device.ReadFile;

/**
 * Created by alan on 6/11/15.
 */
public class SoundInputPool extends Thread {

    private boolean threadState;  // denote thread state.
    private LinkedBlockingQueue<SoundVectorUnit> outputDataQueue;

    private int sampleRate;
    private int mode;

    Microphone microphone;
    ReadFile readFile;


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
                microphone = new Microphone(sampleRate);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
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
        SoundVectorUnit outputUnit;
        short[] outputDataVector = null;

        //Log.d("SoundInputPool", "in run. mode: " + mode);

        // open input object.
        switch (mode) {
            case 0:
                microphone.open();
                break;
            case 1:
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }


        // do function.
        while (threadState) {
            outputDataVector = null;

            switch (mode) {
                case 0:
                    outputDataVector = pipeMicrophoneToVector();
                    break;
                case 1:
                case 2:
                    break;
                case 3:
                    break;
                default:
                    break;
            }


            if (outputDataVector != null && outputDataVector.length > 0) {
                //Log.d("SoundInputPool", "in run. dataVector length: " + outputDataVector.length);
                outputUnit = new SoundVectorUnit(outputDataVector);
                outputDataQueue.add(outputUnit);
            }
        }


        //  close input object.
        switch (mode) {
            case 0:
                microphone.close();
                break;
            case 1:
            case 2:
                break;
            case 3:
                break;
            default:
                break;
        }

        Log.d("SoundInputPool", "in run. process stop.");
    }

    private short[] pipeMicrophoneToVector() {
        return microphone.read();
    }

    private short[] pipeFileToVector() {
        return null;
    }
}
