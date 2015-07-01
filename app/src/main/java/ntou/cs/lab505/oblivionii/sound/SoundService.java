package ntou.cs.lab505.oblivionii.sound;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.database.BandSettingAdapter;
import ntou.cs.lab505.oblivionii.database.FreqSettingAdapter;
import ntou.cs.lab505.oblivionii.database.IOSettingAdapter;
import ntou.cs.lab505.oblivionii.datastructure.BandGainSetUnit;
import ntou.cs.lab505.oblivionii.datastructure.IOSetUnit;
import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;
import ntou.cs.lab505.oblivionii.sound.bandgain.BandGain;
import ntou.cs.lab505.oblivionii.sound.frequencyshift.FrequencyShift;
import ntou.cs.lab505.oblivionii.sound.soundgeneration.HarmonicsGeneration;
import ntou.cs.lab505.oblivionii.stream.SoundInputPool;
import ntou.cs.lab505.oblivionii.stream.SoundOutputPool;

/**
 * Created by alan on 6/10/15.
 */
public class SoundService extends Service {


    int sampleRate = 16000;
    boolean serviceState;
    // function objects.
    SoundInputPool soundInputPool;
    SoundOutputPool soundOutputPool;
    FrequencyShift frequencyShift;
    BandGain bandGain;
    // sound vector.
    //short[] soundVector;
    // data queues.
    LinkedBlockingQueue<SoundVectorUnit> soundInputQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit> freqShiftQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit> bandGainQueue = new LinkedBlockingQueue<>();


    public class SoundServiceBinder extends Binder {
        public SoundService getService() {
            return SoundService.this;
        }
    }

    private final IBinder mBinder = new SoundServiceBinder();

    @Override
    public void onCreate() {
        Log.d("SoundService", "in onCreate.");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("SoundService", "in onDestroy.");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void initService() {
        Log.d("SoundService", "in initService. inital service.");
        // check device state.


        // state flag.
        serviceState = false;


        // read data from database.
        IOSetUnit ioSetUnit;
        int semitoneValue;
        ArrayList<BandGainSetUnit> bandGainSetUnitArrayList;
        // read IO setting data.
        IOSettingAdapter ioSettingAdapter = new IOSettingAdapter(this.getApplicationContext());
        ioSettingAdapter.open();
        ioSetUnit = ioSettingAdapter.getData();
        ioSettingAdapter.close();
        // read freqshift setting data.
        FreqSettingAdapter freqSettingAdapter = new FreqSettingAdapter(this.getApplicationContext());
        freqSettingAdapter.open();
        semitoneValue = freqSettingAdapter.getData();
        freqSettingAdapter.close();
        // read band gain setting data.
        BandSettingAdapter bandSettingAdapter = new BandSettingAdapter(this.getApplicationContext());
        bandSettingAdapter.open();
        bandGainSetUnitArrayList = bandSettingAdapter.getData();
        bandSettingAdapter.close();


        // initial object.
        //soundInputPool = new SoundInputPool(sampleRate, ioSetUnit.getInputType());




        frequencyShift = new FrequencyShift(sampleRate, 1, semitoneValue, 0, 0);
        bandGain = new BandGain(sampleRate, ioSetUnit.getChannelNumber(), bandGainSetUnitArrayList);
        soundOutputPool = new SoundOutputPool(sampleRate, ioSetUnit.getChannelNumber(), 2, ioSetUnit.getOutputType());  // one channel have strange bug.


        // pipe data.
        //soundInputPool.setOutputDataQueu(soundInputQueue);


        frequencyShift.setInputDataQueue(soundInputQueue);
        frequencyShift.setOutputDataQueue(freqShiftQueue);
        bandGain.setInputDataQueue(freqShiftQueue);
        bandGain.setOutputDataQueue(bandGainQueue);
        soundOutputPool.setInputDataQueue(bandGainQueue);
    }

    public void serviceStart() {
        serviceState = true;
        //soundInputPool.threadStart();
        frequencyShift.threadStart();
        bandGain.threadStart();
        soundOutputPool.threadStart();


        HarmonicsGeneration harmonicsGeneration = new HarmonicsGeneration(sampleRate);
        SoundVectorUnit soundVectorUnit;
        // generate pure tone.
        for (int count = 0; count < 2; count++) {
            // generate sound.
            short[] originSoundVector = harmonicsGeneration.generate(1000, 1, 60, 1);

            // cut frame.
            if (originSoundVector.length < 2000) {
                soundVectorUnit = new SoundVectorUnit(originSoundVector);
                soundInputQueue.add(soundVectorUnit);
            } else {
                int num = originSoundVector.length / 2000;
                if (originSoundVector.length % 2000 != 0) {
                    num++;
                }

                int start = 0;
                int size = 0;
                short[] tempSoundVector = new short[2000];
                for (int i = 0; i < num; i++) {
                    start = 2000 * i;
                    size = 2000;
                    if (start + size > originSoundVector.length) {
                        size = originSoundVector.length - start;
                    }

                    System.arraycopy(originSoundVector, start, tempSoundVector, 0, size);

                    if (tempSoundVector == null ||tempSoundVector.length == 0) {
                        continue;
                    }

                    soundVectorUnit = new SoundVectorUnit(tempSoundVector);
                    soundInputQueue.add(soundVectorUnit);
                }
            }
        }
    }

    public void serviceStop() {
        //soundInputPool.threadStop();
        frequencyShift.threadStop();
        bandGain.threadStop();
        soundOutputPool.threadStop();
        serviceState = false;
    }

    public boolean getServiceState() {
        return serviceState;
    }
}
