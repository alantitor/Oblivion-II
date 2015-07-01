package ntou.cs.lab505.oblivionii.sound.bandgain;

import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.datastructure.BandGainSetUnit;
import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;
import ntou.cs.lab505.oblivionii.sound.filterbank.IIR;

import static ntou.cs.lab505.oblivionii.sound.SoundTool.calculateDb;
import static ntou.cs.lab505.oblivionii.sound.SoundTool.mag2db;

/**
 * Created by alan on 7/1/15.
 */
public class BandGain extends Thread{

    // system parameters.
    private boolean threadState;
    private long threadStartTime;
    private long threadStopTime;
    private LinkedBlockingQueue<SoundVectorUnit> inputDataQueue;
    private LinkedBlockingQueue<SoundVectorUnit> outputDataQueue;

    // used to save processing sound.
    private ArrayList<short[]> soundBandListL = new ArrayList<>();
    private ArrayList<short[]> soundBandListR = new ArrayList<>();

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


    public BandGain(int sampleRate, int lowBand, int highBand, int gain40, int gain60, int gain80) {
        this.sampleRate = sampleRate;
        this.channelNumber = 1;
    }

    public BandGain(int sampleRate, int lowBandL, int highBandL, int gain40L, int gain60L, int gain80L, int lowBandR, int highBandR, int gain40R, int gain60R, int gain80R) {
        this.sampleRate = sampleRate;
        this.channelNumber = 2;
    }

    public BandGain(int sampleRate, int channelNumber, ArrayList<BandGainSetUnit> bandGainSetUnits) {
        this.sampleRate = sampleRate;
        this.channelNumber = channelNumber;

        for (int count = 0; count < bandGainSetUnits.size(); count++) {
            if (bandGainSetUnits.get(count).getLr() == 0) {
                // left channel.
                iirLeftList.add(new IIR(this.filterOrder, sampleRate, bandGainSetUnits.get(count).getLowBand(), bandGainSetUnits.get(count).getHighBand()));
                gain40L.add(mag2db(bandGainSetUnits.get(count).getGain40()));
                gain60L.add(mag2db(bandGainSetUnits.get(count).getGain60()));
                gain80L.add(mag2db(bandGainSetUnits.get(count).getGain80()));

                filterBankNumberLeft++;
            } else {
                // right channel.
                iirRightList.add(new IIR(this.filterOrder, sampleRate, bandGainSetUnits.get(count).getLowBand(), bandGainSetUnits.get(count).getHighBand()));
                gain40R.add(mag2db(bandGainSetUnits.get(count).getGain40()));
                gain60R.add(mag2db(bandGainSetUnits.get(count).getGain60()));
                gain80R.add(mag2db(bandGainSetUnits.get(count).getGain80()));

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
                // read left channel data.
                for (int count = 0; count < filterBankNumberLeft; count++) {
                    // cut bands.
                    soundBandListL.add(iirLeftList.get(count).process(inputUnit.getLeftChannel().clone()));  // should I use clone()?
                    // gain db.
                    soundBandListL.set(count, autoGain(soundBandListL.get(count), count, 0));
                }
            } else if (channelNumber == 2) {
                // read left channel data.
                for (int count = 0; count < filterBankNumberLeft; count++) {
                    soundBandListL.add(iirLeftList.get(count).process(inputUnit.getLeftChannel().clone()));
                    soundBandListL.set(count, autoGain(soundBandListL.get(count), count, 0));
                }
                // read right channel data.
                for (int count = 0; count < filterBankNumberRight; count++) {
                    soundBandListR.add(iirRightList.get(count).process(inputUnit.getLeftChannel().clone()));
                    soundBandListR.set(count, autoGain(soundBandListR.get(count), count, 1));
                }
            } else {
                //
            }


            // check data correction.
            Log.d("BandGain", "in run. left Channel number: " + soundBandListL.size());
            Log.d("BandGain", "in run. right Channel number: " + soundBandListR.size());
            //saveVectorToDataFile();


            // mix bands.
            // leftVector = ...
            // rightVector = ...


            // output data.
            //outputDataQueue.add(outputUnit);
            // clear temp space.
            soundBandListL.clear();
            soundBandListR.clear();
        }

        Log.d("BandGain", "in run. thread stop.");
    }

    private short[] autoGain(short[] dataVector, int index, int lr) {

        int db = calculateDb(dataVector);

        if (lr == 0) {  // left channel.
            if (db>= 40 && db < 60) {
                dataVector = processGain(dataVector, gain40L.get(index), db);
            } else if (db >= 60 && db < 80) {
                dataVector = processGain(dataVector, gain60L.get(index), db);
            } else if (db >= 80) {
                dataVector = processGain(dataVector, gain80L.get(index), db);
            } else {
                dataVector = processGain(dataVector, gain40L.get(index), db);
            }
        } else {  // right channel.
            if (db>= 40 && db < 60) {
                dataVector = processGain(dataVector, gain40R.get(index), db);
            } else if (db >= 60 && db < 80) {
                dataVector = processGain(dataVector, gain60R.get(index), db);
            } else if (db >= 80) {
                dataVector = processGain(dataVector, gain80R.get(index), db);
            } else {
                dataVector = processGain(dataVector, gain40R.get(index), db);
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
