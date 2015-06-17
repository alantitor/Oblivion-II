package ntou.cs.lab505.oblivionii.sound.filterbank;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.datastructure.BandSetUnit;
import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;

public class FilterBank extends Thread {

    private boolean threadState = false;
    private LinkedBlockingQueue<SoundVectorUnit> inputDataQueue;
    private LinkedBlockingQueue<SoundVectorUnit[]> outputDataQueue;

    private int sampleRate = 16000;
    private final int filterOrder = 3;  // 設定濾波器階數. 階數愈高愈佳.
    private int filterBankNumber;  // 記錄濾波器個數(頻帶數).
    private int channelNumber;
    private long startTime;
    private long stopTime;

    // 動態頻帶切割
    private ArrayList<IIR> iirBandsLeft = null;
    private ArrayList<IIR> iirBandsRight = null;
    private SoundVectorUnit[] soundBandsLeft = null;
    private SoundVectorUnit[] soundBandsRight = null;


    /**
     * single band cut.
     * @param sampleRate
     * @param lowBand
     * @param highBand
     */
    public FilterBank(int sampleRate, int lowBand, int highBand) {
        this.sampleRate = sampleRate;
        this.filterBankNumber = 1;
        this.channelNumber = 1;
        this.iirBandsLeft = new ArrayList<>();
        this.iirBandsLeft.add(new IIR(this.filterOrder, sampleRate, lowBand, highBand));
        this.soundBandsLeft = new SoundVectorUnit[this.filterBankNumber];
    }

    public FilterBank(int sampleRate, int leftLowBand, int leftHighBand, int rightLowBand, int rightHighBand) {
        this.sampleRate = sampleRate;
        this.channelNumber = 2;
        this.filterBankNumber = 1;
        this.iirBandsLeft = new ArrayList<>();
        this.iirBandsRight = new ArrayList<>();
        this.iirBandsLeft.add(new IIR(this.filterOrder, sampleRate, leftLowBand, leftHighBand));
        this.iirBandsRight.add(new IIR(this.filterOrder, sampleRate, rightLowBand, rightHighBand));
        this.soundBandsLeft = new SoundVectorUnit[this.filterBankNumber];
        this.soundBandsRight = new SoundVectorUnit[this.filterBankNumber];
    }

    public FilterBank(int sample, BandSetUnit bandCutUnit) {
        /*not perfect type*/
    }

    public FilterBank(int sample, BandSetUnit leftChannel, BandSetUnit rightChannel) {
        /*not perfect type*/
    }

    /**
     * multi band cut.
     */
    public FilterBank() {
        // wait for coding.
    }

    public void setInputDataQueue(LinkedBlockingQueue<SoundVectorUnit> inputDataQueue) {
        this.inputDataQueue = inputDataQueue;
    }

    public void setOutputDataQueue(LinkedBlockingQueue<SoundVectorUnit[]> outputDataQueue) {
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
        Log.d("FilterBank", "in run. thread start.");
        SoundVectorUnit inputUnit = null;
        SoundVectorUnit outputUnit[] = new SoundVectorUnit[this.filterBankNumber];


        while (threadState) {
            this.startTime = System.nanoTime();
            // take data from queue.
            try {
                inputUnit = inputDataQueue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (inputUnit == null) {
                continue;
            }
            if (inputUnit.getVectorLength() == 0) {
                continue;
            }
            Log.d("FilterBank", "in run. inputUnit length: " + inputUnit.getVectorLength());


            if (this.channelNumber == 1) {
                for (int i = 0; i < this.filterBankNumber; i++) {
                    outputUnit[i] = new SoundVectorUnit(iirBandsLeft.get(i).process(inputUnit.getLeftChannel()));
                    Log.d("FilterBank", "in runTest. outputUnit length: " + outputUnit[i].getVectorLength());
                }
            } else if (this.channelNumber == 2) {
                for (int i = 0; i < this.filterBankNumber; i++) {
                    outputUnit[i] = new SoundVectorUnit(iirBandsLeft.get(i).process(inputUnit.getLeftChannel()),
                                                        iirBandsRight.get(i).process(inputUnit.getRightChannel()));
                    Log.d("FilterBank", "in runTest. outputUnit left length: " + outputUnit[i].getLeftChannel().length);
                    Log.d("FilterBank", "in runTest. outputUnit right length: " + outputUnit[i].getRightChannel().length);
                }
            } else {
                //
            }

            outputDataQueue.add(outputUnit);
            this.stopTime = System.nanoTime();
            Log.d("FilterBank", "in run. time: " + (double)(stopTime - startTime) / 1000000.0);
        }

        Log.d("FilterBank", "in run. thread stop.");
    }
}