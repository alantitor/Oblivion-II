package ntou.cs.lab505.oblivionii.sound.frequencyshift;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by alan on 6/12/15.
 */
public class FrequencyShift extends Thread {

    private boolean threadState;  // denote thread state.
    private LinkedBlockingQueue<short[]> inputDataQueue;
    private LinkedBlockingQueue<short[]> outputDataQueue;

    private JNISoundTouch soundtouch = new JNISoundTouch();  // sound process object
    private int sampleRate;
    private int channels;
    private int pitchSemiTones;
    private float rateChange;
    private float tempoChange;

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
    public void setInputDataQueue(LinkedBlockingQueue<short[]> inputDataQueue) {
        this.inputDataQueue = inputDataQueue;
    }

    /**
     * set data queue.  this queue used to save processed data.
     * @param outputDataQueue
     */
    public void setOutputDataQueue(LinkedBlockingQueue<short[]> outputDataQueue) {
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

    public void run() {
        Log.d("FrequencyShift", "in run. process start.");

        // set sound parameters
        soundtouch.setSampleRate(sampleRate);
        soundtouch.setChannels(channels);
        soundtouch.setPitchSemiTones(pitchSemiTones);  // Changes the sound pitch or key while keeping the original tempo (speed).
        soundtouch.setRateChange(rateChange);  // Changes both tempo and pitch together as if a vinyl disc was played at different RPM rate.
        soundtouch.setTempoChange(tempoChange);  // Changes the sound to play at faster or slower tempo than originally without affecting the sound pitch.

        short[] tempBuff;
        short[] tempBuff2;

        try {
            while (threadState) {
                tempBuff = inputDataQueue.take();

                if (tempBuff != null) {
                    soundtouch.putSamples(tempBuff, tempBuff.length);

                    do {
                        tempBuff2 = soundtouch.receiveSamples();
                        outputDataQueue.add(tempBuff2);
                    } while (tempBuff2.length > 0);
                }
            }
        } catch (Throwable e) {
            // do nothing
        }

        Log.d("FrequencyShift", "in run. process stop.");
    }
}
