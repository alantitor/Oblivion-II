package ntou.cs.lab505.oblivionii.sound;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;
import ntou.cs.lab505.oblivionii.sound.filterbank.FilterBank;
import ntou.cs.lab505.oblivionii.sound.frequencyshift.FrequencyShift;
import ntou.cs.lab505.oblivionii.sound.gain.Gain;
import ntou.cs.lab505.oblivionii.sound.soundgeneration.HarmonicsGeneration;
import ntou.cs.lab505.oblivionii.stream.SoundInputPool;
import ntou.cs.lab505.oblivionii.stream.SoundOutputPool;

/**
 * Created by alan on 6/10/15.
 */
public class PureToneTest extends Service {

    // sound parameters
    int valueFreq;
    int valueDb;
    int valueHarm;
    int valueSec;
    int valueBcLow;
    int valueBcHigh;
    int valueSemitone;
    int valueGain;
    int valueChannel = 0;
    int valueOutput = 0;
    int sampleRate = 8000;
    int frameSize = 1400;
    // sound vector
    short[] originSoundVector;
    // function objects
    SoundInputPool soundInputPool;
    HarmonicsGeneration harmonicsGeneration;
    FrequencyShift frequencyShift;
    FilterBank filterBank;
    Gain gain;
    SoundOutputPool soundOutputPool;
    // data queues.
    LinkedBlockingQueue<SoundVectorUnit> soundInputQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit> pureToneQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit> freqShiftQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit[]> filterBankQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit> gainQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit> tempQueue = new LinkedBlockingQueue<>();


    public class PureToneTestBinder extends Binder {
        public PureToneTest getService() {
            return PureToneTest.this;
        }
    }

    private final IBinder mBinder = new PureToneTestBinder();

    @Override
    public void onCreate() {
        //Log.d("PureToneTest", "in onCreate.");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        //Log.d("PureToneTest", "in onDestroy.");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Log.d("PureToneTest", "in onStartCommand.");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        //Log.d("PureToneTest", "in onBind.");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        //Log.d("PureToneTest", "in onUnbind.");
        return super.onUnbind(intent);
    }

    public void initParameters(int valueFreq, int valueDb, int valueHarm, int valueSec, int valueBcLow, int valueBcHigh, int valueSemitone, int valueGain, int valueChannel, int valueOutput) {
        this.valueFreq = valueFreq;
        this.valueDb = valueDb;
        this.valueHarm = valueHarm;
        this.valueSec = valueSec;
        this.valueBcLow = valueBcLow;
        this.valueBcHigh = valueBcHigh;
        this.valueSemitone = valueSemitone;
        this.valueGain = valueGain;
        this.valueChannel = valueChannel;
        this.valueOutput = valueOutput;
    }

    public void runTest() {

        // check device type. check sample rate. check channel number.


        // initial object
        harmonicsGeneration = new HarmonicsGeneration(sampleRate);
        frequencyShift = new FrequencyShift(sampleRate, 1, valueSemitone, 0, 0);
        filterBank = new FilterBank(sampleRate, valueBcLow, valueBcHigh);
        gain = new Gain(sampleRate, valueGain, valueGain, valueGain);  // 40 dB value. 60 dB value. 80 dB value.
        soundOutputPool = new SoundOutputPool(sampleRate, 2, valueChannel, valueOutput);


        /**
         * algorithm:
         *  (1) generate pure tone.
         *  (2) shift frequency.
         *  (3) filter bank.
         *  (4) gain bands.
         *  (5) output sound.
         */


        /*
        // generate sound.
        originSoundVector = harmonicsGeneration.generate(valueFreq, valueSec, valueDb, valueHarm);
        //saveVectorToDataFile(originSoundVector, "origin");


        // cut frame.
        SoundVectorUnit soundVectorUnit = null;

        if (originSoundVector.length < frameSize) {
            soundVectorUnit = new SoundVectorUnit(originSoundVector);
            pureToneQueue.add(soundVectorUnit);
        } else {
            int num = originSoundVector.length / frameSize;
            if (originSoundVector.length % frameSize != 0) {
                num++;
            }

            int start = 0;
            int size = 0;
            short[] tempSoundVector = new short[frameSize];
            for (int i = 0; i < num; i++) {
                start = frameSize * i;
                size = frameSize;
                if (start + size > originSoundVector.length) {
                    size = originSoundVector.length - start;
                }

                Log.d("debugArray", "array start: " + start);
                Log.d("debugArray", "array size: " + size);

                System.arraycopy(originSoundVector, start, tempSoundVector, 0, size);
                soundVectorUnit = new SoundVectorUnit(tempSoundVector);
                pureToneQueue.add(soundVectorUnit);
            }
        }
        */


        SoundVectorUnit soundVectorUnit = null;

        // generate pure tone.
        for (int count = 0; count < valueSec; count++) {
            // generate sound.
            originSoundVector = harmonicsGeneration.generate(valueFreq, 1, valueDb, valueHarm);

            // cut frame.
            if (originSoundVector.length < frameSize) {
                soundVectorUnit = new SoundVectorUnit(originSoundVector);
                pureToneQueue.add(soundVectorUnit);
            } else {
                int num = originSoundVector.length / frameSize;
                if (originSoundVector.length % frameSize != 0) {
                    num++;
                }

                int start = 0;
                int size = 0;
                short[] tempSoundVector = new short[frameSize];
                for (int i = 0; i < num; i++) {
                    start = frameSize * i;
                    size = frameSize;
                    if (start + size > originSoundVector.length) {
                        size = originSoundVector.length - start;
                    }

                    System.arraycopy(originSoundVector, start, tempSoundVector, 0, size);

                    if (tempSoundVector == null ||tempSoundVector.length == 0) {
                        continue;
                    }

                    soundVectorUnit = new SoundVectorUnit(tempSoundVector);
                    pureToneQueue.add(soundVectorUnit);
                }
            }
        }

        frequencyShift.setInputDataQueue(pureToneQueue);
        frequencyShift.setOutputDataQueue(freqShiftQueue);

        filterBank.setInputDataQueue(freqShiftQueue);
        filterBank.setOutputDataQueue(filterBankQueue);

        gain.setInputDataQueue(filterBankQueue);
        gain.setOutputDataQueue(gainQueue);

        soundOutputPool.setInputDataQueue(gainQueue);

        // threads start.
        frequencyShift.threadStart();
        filterBank.threadStart();
        gain.threadStart();
        soundOutputPool.threadStart();


        try {
            Thread.sleep(valueSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        frequencyShift.threadStop();
        filterBank.threadStop();
        gain.threadStop();
        soundOutputPool.threadStop();
    }

    boolean runState = false;
    public void runTestDebug() {

        if (runState) {
            soundInputPool.threadStop();
            frequencyShift.threadStop();
            filterBank.threadStop();
            gain.threadStop();
            soundOutputPool.threadStop();
            runState = false;

            return ;
        }


        // initial object
        soundInputPool = new SoundInputPool(sampleRate, 0);

        //harmonicsGeneration = new HarmonicsGeneration(sampleRate);

        frequencyShift = new FrequencyShift(sampleRate, 1, valueSemitone, 0, 0);

        filterBank = new FilterBank(sampleRate, valueBcLow, valueBcHigh);

        //BandSetUnit[ ] bandSetUnit = new BandSetUnit[4];
        //bandSetUnit[0] = new BandSetUnit(143, 280);
        //bandSetUnit[1] = new BandSetUnit(281, 561);
        //bandSetUnit[2] = new BandSetUnit(561, 1120);
        //bandSetUnit[3] = new BandSetUnit(1110, 2240);
        //bandSetUnit[4] = new BandSetUnit(2230, 3540);
        //filterBank = new FilterBank(sampleRate, bandSetUnit);

        gain = new Gain(sampleRate, valueGain, valueGain, valueGain);  // 40 dB value. 60 dB value. 80 dB value.

        soundOutputPool = new SoundOutputPool(sampleRate, 1, valueChannel, valueOutput);


        // generate sound.
        //originSoundVector = harmonicsGeneration.generate(valueFreq, valueSec, valueDb, valueHarm);
        //Log.d("PureToneTest", "in runTest. originSoundVector length: " + originSoundVector.length);
        //saveVectorToDataFile(originSoundVector, "origin");
        //SoundVectorUnit soundVectorUnit = new SoundVectorUnit(originSoundVector);
        //Log.d("PureToneTest", "in runTest. soundVectorUnit length: " + soundVectorUnit.getVectorLength());

        // pipe sound.
        soundInputPool.setOutputDataQueu(soundInputQueue);

        //pureToneQueue.add(soundVectorUnit);

        //frequencyShift.setInputDataQueue(pureToneQueue);
        //frequencyShift.setOutputDataQueue(freqShiftQueue);

        filterBank.setInputDataQueue(soundInputQueue);
        filterBank.setOutputDataQueue(filterBankQueue);

        gain.setInputDataQueue(filterBankQueue);
        gain.setOutputDataQueue(gainQueue);

        soundOutputPool.setInputDataQueue(gainQueue);


        // threads start.
        soundInputPool.threadStart();
        //frequencyShift.threadStat();
        filterBank.threadStart();
        gain.threadStart();
        soundOutputPool.threadStart();


        /*
        try {
            Thread.sleep(valueSec * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */


        //soundInputPool.threadStop();
        //frequencyShift.threadStop();
        //filterBank.threadStop();
        //gain.threadStop();
        //soundOutputPool.threadStop();
        runState = true;
    }

    /**
     * return time domain sound vector to activity.
     * @return
     */
    public short[] getOriginTimeDomainVector() {
        short[] temp = null;
        return temp;

        /**
         * wait for coding.
         */

    }
}
