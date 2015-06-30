package ntou.cs.lab505.oblivionii.sound.bandgain;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.datastructure.BandGainSetUnit;
import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;
import ntou.cs.lab505.oblivionii.sound.filterbank.IIR;

/**
 * Created by alan on 7/1/15.
 */
public class BandGain extends Thread{

    private boolean threadState;
    private long threadStartTime;
    private long threadStopTime;
    private LinkedBlockingQueue<SoundVectorUnit> inputDataQueue;
    private LinkedBlockingQueue<SoundVectorUnit> outputDataQueue;

    // filter bank parameters.
    private int sampleRate;
    private int channelNumber;
    private final int filterOrder = 3;  // 設定濾波器階數. 階數愈高愈佳.
    private int filterBankNumberLeft;  // 記錄濾波器個數(頻帶數).
    private int filterBankNumberRight;

    // 動態頻帶切割
    private ArrayList<IIR> iirLeftList = new ArrayList<>();
    private ArrayList<IIR> iirRightList = new ArrayList<>();

    // gain parameters.
    private ArrayList<Double> gain40L = new ArrayList<>();
    private ArrayList<Double> gain60L = new ArrayList<>();
    private ArrayList<Double> gain80L = new ArrayList<>();
    private ArrayList<Double> gain40R = new ArrayList<>();
    private ArrayList<Double> gain60R = new ArrayList<>();
    private ArrayList<Double> gain80R = new ArrayList<>();


    public BandGain(int sampleRate, int channelNumber, ArrayList<BandGainSetUnit> bandGainSetUnits) {
        this.sampleRate = sampleRate;
        this.channelNumber = channelNumber;

        for (int count = 0; count < bandGainSetUnits.size(); count++) {
            if (bandGainSetUnits.get(count).getLr() == 0) {
                // left channel.

                filterBankNumberLeft++;
            } else {
                // right channel.

                filterBankNumberRight++;
            }
        }
    }

    public void setInputDataQueue(LinkedBlockingQueue<SoundVectorUnit> inputDataQueue) {
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
     * check thread state.
     * @return
     */
    public boolean threadState() {
        return this.threadState;
    }

    public void run() {
        Log.d("BandGain", "in run. thread start.");
        SoundVectorUnit inputUnit = null;
        SoundVectorUnit outputUnit = null;


        while (threadState) {
            // take data from queue.
            inputUnit = inputDataQueue.poll();

            // check data state.
            if (inputUnit == null) {
                continue;
            }
            if (inputUnit.getVectorLength() == 0) {
                continue;
            }
            //Log.d("BandGain", "in run. inputUnit length: " + inputUnit.getVectorLength());

            // process data.
            if (channelNumber == 1) {

            } else if (channelNumber == 2) {

            } else {
                //
            }

            // output data.
            outputDataQueue.add(outputUnit);
        }

        Log.d("BandGain", "in run. thread start.");
    }


}

