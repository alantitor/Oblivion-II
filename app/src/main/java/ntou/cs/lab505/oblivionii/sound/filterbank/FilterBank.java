package ntou.cs.lab505.oblivionii.sound.filterbank;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

public class FilterBank extends Thread {

    private boolean threadState = false;
    private LinkedBlockingQueue<short[]> inputDataQueue;
    private LinkedBlockingQueue<short[][]> outputDataQueue;

    private int filterOrder = 3;  // 設定濾波器階數. 階數愈高愈佳.
    private int filterBankNumber;  // 記錄濾波器個數(頻帶數).

    // 動態頻帶切割
    private IIR[] iirBandsLeft = null;


    /**
     * single band cut.
     * @param sampleRate
     * @param lowBand
     * @param highBand
     */
    public FilterBank(int sampleRate, int lowBand, int highBand) {
        this.filterBankNumber = 1;
        iirBandsLeft = new IIR[1];
        iirBandsLeft[0] = new IIR(this.filterOrder, sampleRate, lowBand, highBand);
    }

    public FilterBank(int sampleRate, int leftLowBand, int leftHighBand, int rightLowBand, int rightHighBand) {

    }

    /**
     * multi band cut.
     */
    public FilterBank() {
        // wait for coding.
    }

    public void setInputDataQueue(LinkedBlockingQueue<short[]> inputDataQueue) {
        this.inputDataQueue = inputDataQueue;
    }

    public void setOutputDataQueue(LinkedBlockingQueue<short[][]> outputDataQueue) {
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
        short[] inputVector = null;
        short[][] outputVector = null;

        while (threadState) {
            // buffer
            try {
                // take data from queue.
                inputVector = inputDataQueue.take();
                if (inputVector == null || inputVector.length == 0) {
                    continue;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            outputVector = new short[iirBandsLeft.length][];

            for (int i = 0; i < iirBandsLeft.length; i++) {
                outputVector[i] = iirBandsLeft[i].process(inputVector);
            }

            outputDataQueue.add(outputVector);
        }
        Log.d("FilterBank", "in run. thread stop.");
    }

    private short[] bandcut(short[] soundVector) {

        return soundVector;
    }
}