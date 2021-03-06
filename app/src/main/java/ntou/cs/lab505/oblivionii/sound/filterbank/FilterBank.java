package ntou.cs.lab505.oblivionii.sound.filterbank;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.datastructure.BandGainSetUnit;
import ntou.cs.lab505.oblivionii.datastructure.BandSetUnit;
import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;

public class FilterBank extends Thread {

    private boolean threadState = false;
    private LinkedBlockingQueue<SoundVectorUnit> inputDataQueue;
    private LinkedBlockingQueue<SoundVectorUnit[]> outputDataQueue;

    private int sampleRate;
    private int channelNumber;
    private final int filterOrder = 3;  // 設定濾波器階數. 階數愈高愈佳.
    private int filterBankNumber;  // 記錄濾波器個數(頻帶數).

    // 動態頻帶切割
    private ArrayList<IIR> iirBandsLeftList;
    private ArrayList<IIR> iirBandsRightList;

    private long threadStartTime;
    private long threadStopTime;


    /**
     * Algorithm:
     *  Input:
     *      SoundVectorUnit.
     *
     *  Output:
     *      SoundVectorUnit array.
     *
     *  Initial object:
     *
     *
     *  Run:
     *
     *
     *
     */


    /**
     * one-channel one-band cut.
     * @param sampleRate
     * @param lowBand
     * @param highBand
     */
    public FilterBank(int sampleRate, int lowBand, int highBand) {
        this.sampleRate = sampleRate;
        this.filterBankNumber = 1;
        this.channelNumber = 1;
        this.iirBandsLeftList = new ArrayList<>();

        this.iirBandsLeftList.add(new IIR(this.filterOrder, sampleRate, lowBand, highBand));
    }

    /**
     * two-channel one-band cut.
     * @param sampleRate
     * @param leftLowBand
     * @param leftHighBand
     * @param rightLowBand
     * @param rightHighBand
     */
    public FilterBank(int sampleRate, int leftLowBand, int leftHighBand, int rightLowBand, int rightHighBand) {
        this.sampleRate = sampleRate;
        this.channelNumber = 2;
        this.filterBankNumber = 1;
        this.iirBandsLeftList = new ArrayList<>();
        this.iirBandsRightList = new ArrayList<>();

        this.iirBandsLeftList.add(new IIR(this.filterOrder, sampleRate, leftLowBand, leftHighBand));
        this.iirBandsRightList.add(new IIR(this.filterOrder, sampleRate, rightLowBand, rightHighBand));
    }

    public FilterBank(int sampleRate, BandSetUnit[] bandCutUnit) {
        this.sampleRate = sampleRate;
        this.filterBankNumber = bandCutUnit.length;
        this.channelNumber = 1;
        this.iirBandsLeftList = new ArrayList<>();
        this.iirBandsRightList = new ArrayList<>();

        for (int i = 0; i < this.filterBankNumber; i++) {
            iirBandsLeftList.add(new IIR(this.filterOrder, sampleRate, bandCutUnit[i].getLowBand(), bandCutUnit[i].getHighBand()));
            //Log.d("FilterBank", "in FilterBank. low: " + bandCutUnit[i].getLowBand() + " , high: " + bandCutUnit[i].getHighBand());
        }
    }

    public FilterBank(int sampleRate, BandSetUnit[] leftChannel, BandSetUnit[] rightChannel) {
        this.sampleRate = sampleRate;
        this.filterBankNumber = leftChannel.length;
        this.channelNumber = 2;
        this.iirBandsLeftList = new ArrayList<>();
        this.iirBandsRightList = new ArrayList<>();

        for (int i = 0; i < this.filterBankNumber; i++) {
            iirBandsLeftList.add(new IIR(this.filterOrder, sampleRate, leftChannel[i].getLowBand(), leftChannel[i].getHighBand()));
            iirBandsRightList.add(new IIR(this.filterOrder, sampleRate, rightChannel[i].getLowBand(), rightChannel[i].getHighBand()));
        }
    }

    public FilterBank(int sampleRate, ArrayList<BandGainSetUnit> bandGainSetUnitArrayList) {

        /*
         * not finish.
         * do not use this contructor.
         * */

        /*
        this.sampleRate = sampleRate;

        for (int i = 0; i < bandGainSetUnitArrayList.size(); i++) {
            if (bandGainSetUnitArrayList.get(i).getLr() == 0) {
                // left channel.
                iirBandsLeftList.add(new IIR(this.filterOrder, sampleRate, bandGainSetUnitArrayList.get(i).getLowBand(), bandGainSetUnitArrayList.get(i).getHighBand()));
            } else {
                // right channel.
                iirBandsRightList.add(new IIR(this.filterOrder, sampleRate, bandGainSetUnitArrayList.get(i).getLowBand(), bandGainSetUnitArrayList.get(i).getHighBand()));
            }
        }

        if (iirBandsRightList.size() > 0) {
            this.channelNumber = 2;
        }
        */
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
     * check thread state.
     * @return
     */
    public boolean threadState() {
        return this.threadState;
    }

    /**
     * thread content.
     */
    public void run() {
        Log.d("FilterBank", "in run. thread start.");
        SoundVectorUnit inputUnit = null;
        SoundVectorUnit outputUnit[] = new SoundVectorUnit[this.filterBankNumber];


        int count = 0;
        while (threadState) {
            //this.startTime = System.nanoTime();
            // take data from queue.
            inputUnit = null;
            //inputUnit = inputDataQueue.take();
            inputUnit = inputDataQueue.poll();

            if (inputUnit == null) {
                continue;
            }
            if (inputUnit.getVectorLength() == 0) {
                continue;
            }
            //Log.d("FilterBank", "in run. inputUnit length: " + inputUnit.getVectorLength());


            if (this.channelNumber == 1) {
                for (int i = 0; i < this.filterBankNumber; i++) {
                    outputUnit[i] = new SoundVectorUnit(iirBandsLeftList.get(i).process(inputUnit.getLeftChannel().clone()));  // should I use clone()?
                    //Log.d("FilterBank", "in runTest. outputUnit length: " + outputUnit[i].getVectorLength());
                    //short[] t = outputUnit[i].getLeftChannel();
                    //Log.d("debug", "value: " + t[1000] + t[1001] + t[1002] + t[1003]);
                    //saveVectorToDataFile(outputUnit[i].getLeftChannel(), "filter" + i);
                }
            } else if (this.channelNumber == 2) {
                for (int i = 0; i < this.filterBankNumber; i++) {
                    outputUnit[i] = new SoundVectorUnit(iirBandsLeftList.get(i).process(inputUnit.getLeftChannel().clone()),
                                                        iirBandsRightList.get(i).process(inputUnit.getRightChannel().clone()));
                    //Log.d("FilterBank", "in runTest. outputUnit left length: " + outputUnit[i].getLeftChannel().length);
                    //Log.d("FilterBank", "in runTest. outputUnit right length: " + outputUnit[i].getRightChannel().length);
                }
            } else {
                //
            }

            outputDataQueue.add(outputUnit);
            //this.stopTime = System.nanoTime();
            //Log.d("FilterBank", "in run. time: " + (double)(stopTime - startTime) / 1000000.0);
        }

        Log.d("FilterBank", "in run. thread stop.");
    }

}