package ntou.cs.lab505.oblivionii.sound.gain;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;

import static ntou.cs.lab505.oblivionii.sound.SoundTool.calculateDb;
import static ntou.cs.lab505.oblivionii.sound.SoundTool.channelMix;
import static ntou.cs.lab505.oblivionii.sound.SoundTool.saveVectorToDataFile;

/**
 * Created by alan on 6/10/15.
 */
public class Gain extends Thread {

    private boolean threadState;
    private LinkedBlockingQueue<SoundVectorUnit[]> inputDataQueue;
    private LinkedBlockingQueue<SoundVectorUnit> outputDataQueue;

    private int sampleRate = 16000;
    private int bandNumber;
    private int channelNumber;
    private long startTime;
    private long stopTime;
    // gain parameters.
    private ArrayList<Double> gain40L = new ArrayList<>();
    private ArrayList<Double> gain60L = new ArrayList<>();
    private ArrayList<Double> gain80L = new ArrayList<>();
    private ArrayList<Double> gain40R = new ArrayList<>();
    private ArrayList<Double> gain60R = new ArrayList<>();
    private ArrayList<Double> gain80R = new ArrayList<>();
    // gain temp vectors.
    private ArrayList<short[]> tempVector = new ArrayList<>();


    public Gain(int sampleRate, double gain40, double gain60, double gain80) {
        this.sampleRate = sampleRate;
        this.channelNumber = 1;
        this.bandNumber = 1;
        this.gain40L.add(Math.pow(10, gain40 / 20));
        this.gain60L.add(Math.pow(10, gain60 / 20));
        this.gain80L.add(Math.pow(10, gain80 / 20));
    }

    public Gain(int sampleRate, double gain40L, double gain60L, double gain80L, double gain40R, double gain60R, double gain80R) {
        this.sampleRate = sampleRate;
        this.channelNumber = 2;
        this.bandNumber = 1;
        this.gain40L.add(Math.pow(10, gain40L / 20));
        this.gain60L.add(Math.pow(10, gain60L / 20));
        this.gain80L.add(Math.pow(10, gain80L / 20));
        this.gain40R.add(Math.pow(10, gain40R / 20));
        this.gain60R.add(Math.pow(10, gain60R / 20));
        this.gain80R.add(Math.pow(10, gain80R / 20));
    }

    public void setInputDataQueue(LinkedBlockingQueue<SoundVectorUnit[]> inputDataQueue) {
        this.inputDataQueue = inputDataQueue;
    }

    public void setOutputDataQueue(LinkedBlockingQueue<SoundVectorUnit> outputDataQueue) {
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

    /**
     * thread content.
     */
    public void run() {
        Log.d("Gain", "process start.");
        SoundVectorUnit inputUnit[] = null;
        SoundVectorUnit outputUnit = null;


        int count = 0;
        while (threadState) {
            inputUnit = inputDataQueue.poll();

            if (inputUnit == null) {
                continue;
            }
            if (inputUnit.length == 0) {
                continue;
            }
            Log.d("Gain", "in run. inputUnit length: " + inputUnit[0].getVectorLength());


            if (this.channelNumber == 1) {
                // extra bands.
                for (int i = 0; i < this.bandNumber; i++) {
                    Log.d("Gain", "in run. before gain, db: " + calculateDb(inputUnit[i].getLeftChannel()));
                    inputUnit[i] = new SoundVectorUnit(autoGain(inputUnit[i].getLeftChannel(), i, 0));
                    Log.d("Gain", "in run. after gain, db: " + calculateDb(inputUnit[i].getLeftChannel()));
                    saveVectorToDataFile(inputUnit[i].getLeftChannel(), "gain" + (count));
                }

                // mix bands.
                outputUnit = channelMix(inputUnit);
                saveVectorToDataFile(outputUnit.getLeftChannel(), "mix" + (count++));
            } else if (this.channelNumber == 2) {
                for (int i = 0; i < this.bandNumber; i++) {
                    inputUnit[i].setLeftChannel(autoGain(inputUnit[i].getLeftChannel(), i, 0));
                    inputUnit[i].setRightChannel(autoGain(inputUnit[i].getRightChannel(), i, 1));
                }

                // mix bands.
                outputUnit = channelMix(inputUnit);
            } else {
                //
            }

            // pop sound vector.
            outputDataQueue.add(outputUnit);
        }

        Log.d("Gain", "process stop.");
    }

    private short[] autoGain(short[] dataVector, int index, int lr) {

        int db = calculateDb(dataVector);

        if (lr == 0) {
            if (db >= 40 && db < 60) {
                dataVector = processGain(dataVector, gain40L.get(index), db);
            } else if (db >= 60 && db < 80) {
                dataVector = processGain(dataVector, gain60L.get(index), db);
            } else if (db >= 80) {
                dataVector = processGain(dataVector, gain80L.get(index), db);
            } else {
                //
            }
        } else {
            if (db >= 40 && db < 60) {
                dataVector = processGain(dataVector, gain40R.get(index), db);
            } else if (db >= 60 && db < 80) {
                dataVector = processGain(dataVector, gain60R.get(index), db);
            } else if (db >= 80) {
                dataVector = processGain(dataVector, gain80R.get(index), db);
            }
        }

        return dataVector;
    }

    private short[] processGain(short[] soundVector, double gainValue, int db) {

        int elem = 0;

        for (int i = 0; i < soundVector.length; i++) {
            elem = (int) (soundVector[i] * gainValue);

            if (elem > Short.MAX_VALUE) {
                soundVector[i] = Short.MAX_VALUE;
            } else if (elem < Short.MIN_VALUE) {
                soundVector[i] = Short.MIN_VALUE;
            } else {
                soundVector[i] = (short) elem;
            }
        }

        return soundVector;
    }
}