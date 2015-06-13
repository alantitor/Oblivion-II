package ntou.cs.lab505.oblivionii.sound;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.sound.frequencyshift.FrequencyShift;
import ntou.cs.lab505.oblivionii.sound.soundgeneration.HarmonicsGeneration;

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
    int sampleRate = 16000;
    // sound vector
    short[] originSoundVector;
    short[] travelSoundVector;
    // function objects
    HarmonicsGeneration harmonicsGeneration;
    FrequencyShift frequencyShift;
    // data queues.
    LinkedBlockingQueue<short[]> pureToneQueue = new LinkedBlockingQueue<short[]>();
    LinkedBlockingQueue<short[]> freqShiftQueue = new LinkedBlockingQueue<short[]>();
    //LinkedBlockingQueue<short[][]> filterBankQueue = new LinkedBlockingQueue<short[][]>();



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
        //Log.d("PureToneTest", "in runTest. value: " + valueOutput);
        // check device type.  and check sample rate

        // initial object
        harmonicsGeneration = new HarmonicsGeneration(sampleRate);
        frequencyShift = new FrequencyShift(sampleRate, 1, valueSemitone, 0, 0);


        // generate sound.
        originSoundVector = harmonicsGeneration.generate(valueFreq, valueSec, valueDb, valueHarm, valueChannel);
        saveVectorToDataFile(originSoundVector, "origin");
        /**
         * check soundVector correctness.
         */


        // shift sound frequency.

        // filter sound.

        // gain sound.

        // output sound.


        // pipe sound.
        pureToneQueue.add(originSoundVector);

        frequencyShift.setInputDataQueue(pureToneQueue);
        frequencyShift.setOutputDataQueue(freqShiftQueue);


        // threads start.
        frequencyShift.threadStart();

        // thread stop????????
        // get thread broadcast.

        frequencyShift.threadStop();
    }

    /**
     * return time domain sound vector to activity.
     * @return
     */
    public short[] getOriginTimeDomainVector() {
        short[] temp = null;
        return temp;
    }

    private void saveVectorToDataFile(short[] data, String fileName) {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName + ".txt");
        FileOutputStream fOut;
        OutputStreamWriter fWriter;

        if (data == null) {
            return ;
        }

        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            fWriter = new OutputStreamWriter(fOut);
            for (int i = 0; i < data.length; i++) {
                fWriter.append(data[i] + ",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
