package ntou.cs.lab505.oblivionii.sound.frequencyshift;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;

/**
 * Created by alan on 6/12/15.
 */
public class FrequencyShift extends Thread {

    private boolean threadState;  // denote thread state.
    private LinkedBlockingQueue<SoundVectorUnit> inputDataQueue;
    private LinkedBlockingQueue<SoundVectorUnit> outputDataQueue;

    private JNISoundTouch soundtouch = new JNISoundTouch();  // sound process object
    private int sampleRate;
    private int channels;
    private int pitchSemiTones;
    private float rateChange;
    private float tempoChange;
    private long startTime;
    private long stopTime;

    /**
     * constructor
     */
    public FrequencyShift() {
        this.sampleRate = 16000;
        this.channels = 1;
        this.pitchSemiTones = 0;
        this.rateChange = 0.0f;
        this.tempoChange = 0.0f;
    }

    public FrequencyShift(int sampleRate, int channels, int pitchSemiTones, int rateChange, int tempoChange) {
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.pitchSemiTones = pitchSemiTones;
        this.rateChange = rateChange;
        this.tempoChange = tempoChange;
    }

    /**
     * set data queue.  this queue is the source for processing.
     * @param inputDataQueue
     */
    public void setInputDataQueue(LinkedBlockingQueue<SoundVectorUnit> inputDataQueue) {
        this.inputDataQueue = inputDataQueue;
    }

    /**
     * set data queue.  this queue used to save processed data.
     * @param outputDataQueue
     */
    public void setOutputDataQueue(LinkedBlockingQueue<SoundVectorUnit> outputDataQueue) {
        this.outputDataQueue = outputDataQueue;
    }

    /**
     *
     * @param sampleRate
     * @param channels
     * @param pitchSemiTones
     * @param rateChange
     * @param tempoChange
     */
    public void setSoundParameters(int sampleRate, int channels, int pitchSemiTones, float rateChange, float tempoChange) {
        this.sampleRate = sampleRate;
        this.channels = channels;
        this.pitchSemiTones = pitchSemiTones;
        this.rateChange = rateChange;
        this.tempoChange = tempoChange;
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
     * check thread state.
     * @return
     */
    public boolean threadState() {
        return this.threadState;
    }

    public void run() {
        Log.d("FrequencyShift", "in run. process start.");

        // set sound parameters
        soundtouch.setSampleRate(sampleRate);
        soundtouch.setChannels(channels);
        soundtouch.setPitchSemiTones(pitchSemiTones);  // Changes the sound pitch or key while keeping the original tempo (speed).
        soundtouch.setRateChange(rateChange);  // Changes both tempo and pitch together as if a vinyl disc was played at different RPM rate.
        soundtouch.setTempoChange(tempoChange);  // Changes the sound to play at faster or slower tempo than originally without affecting the sound pitch.

        SoundVectorUnit inputUnit = null;
        SoundVectorUnit outputUnit = new SoundVectorUnit(null);

        int count = 0;
        while (threadState) {
            //this.startTime = System.currentTimeMillis();
            this.startTime = System.nanoTime();
            // take data from queue.
            inputUnit = null;
            inputUnit = inputDataQueue.poll();

            if (inputUnit == null) {
                continue;
            }
            if (inputUnit.getVectorLength() == 0) {
                continue;
            }
            //Log.d("FrequencyShift", "in run. inputUnit length: " + inputUnit.getVectorLength());


            if (inputUnit.getLeftChannel() != null) {
                //Log.d("Frequency", "in runTest. get data.");
                soundtouch.putSamples(inputUnit.getLeftChannel(), inputUnit.getLeftChannel().length);

                do {
                    outputUnit.setLeftChannel(soundtouch.receiveSamples());
                    //Log.d("FrequencyShift", "in run. outputUnit length: " + outputUnit.getVectorLength());

                    // stop some time.
                    // close it.
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //outputDataQueue.put(outputUnit);
                    outputDataQueue.add(outputUnit);
                } while (outputUnit.getLeftChannel().length > 0);
            }

            this.stopTime = System.nanoTime();
            Log.d("FrequencyShift", "in run. time: " + (double)(stopTime - startTime) / 1000000.0);
        }

        Log.d("FrequencyShift", "in run. process stop.");
    }

    public long usedTime() {
        return (stopTime - startTime);
    }
}
