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
import ntou.cs.lab505.oblivionii.datastructure.BandSetUnit;
import ntou.cs.lab505.oblivionii.datastructure.GainSetUnit;
import ntou.cs.lab505.oblivionii.datastructure.IOSetUnit;
import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;
import ntou.cs.lab505.oblivionii.sound.filterbank.FilterBank;
import ntou.cs.lab505.oblivionii.sound.frequencyshift.FrequencyShift;
import ntou.cs.lab505.oblivionii.sound.gain.Gain;
import ntou.cs.lab505.oblivionii.stream.SoundInputPool;
import ntou.cs.lab505.oblivionii.stream.SoundOutputPool;

/**
 * Created by alan on 6/10/15.
 */
public class SoundService extends Service {


    int sampleRate = 16000;
    // function objects.
    SoundInputPool soundInputPool;
    SoundOutputPool soundOutputPool;
    FrequencyShift frequencyShift;
    FilterBank filterBank;
    Gain gain;
    // sound vector.
    short[] soundVector;
    // data queues.
    LinkedBlockingQueue<SoundVectorUnit> soundInputQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit> freqShiftQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit[]> filterBankQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit> gainQueue = new LinkedBlockingQueue<>();



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

        // read data from database.
        IOSetUnit ioSetUnit;
        int semitoneValue;
        ArrayList<BandGainSetUnit> bandGainSetUnitArrayList;

        IOSettingAdapter ioSettingAdapter = new IOSettingAdapter(this.getApplicationContext());
        ioSettingAdapter.open();
        ioSetUnit = ioSettingAdapter.getData();
        ioSettingAdapter.close();

        FreqSettingAdapter freqSettingAdapter = new FreqSettingAdapter(this.getApplicationContext());
        freqSettingAdapter.open();
        semitoneValue = freqSettingAdapter.getData();
        freqSettingAdapter.close();

        BandSettingAdapter bandSettingAdapter = new BandSettingAdapter(this.getApplicationContext());
        bandSettingAdapter.open();
        bandGainSetUnitArrayList = bandSettingAdapter.getData();
        bandSettingAdapter.close();

        int bsCount = 0;
        for (int i = 0; i < bandGainSetUnitArrayList.size(); i++) {
            if (bandGainSetUnitArrayList.get(i).getLr() == 0) {
                bsCount++;
            }
        }

        BandSetUnit[] bandSetUnit = new BandSetUnit[bsCount];
        GainSetUnit[] gainSetUnits = new GainSetUnit[bsCount];

        int bcACount = 0;
        for (int i = 0; i < bandGainSetUnitArrayList.size(); i++) {
            if (bandGainSetUnitArrayList.get(i).getLr() == 0) {
                bandSetUnit[bcACount] = new BandSetUnit(bandGainSetUnitArrayList.get(i).getLowBand(), bandGainSetUnitArrayList.get(i).getHighBand());
                gainSetUnits[bcACount] =new GainSetUnit(bandGainSetUnitArrayList.get(i).getGain40(), bandGainSetUnitArrayList.get(i).getGain60(), bandGainSetUnitArrayList.get(i).getGain80());
                bcACount++;
            }
        }


        // initial object.
        soundInputPool = new SoundInputPool(sampleRate, ioSetUnit.getInputType());
        soundOutputPool = new SoundOutputPool(sampleRate, 2, 0, ioSetUnit.getOutputType());
        frequencyShift = new FrequencyShift(sampleRate, 1, semitoneValue, 0, 0);
        filterBank = new FilterBank(sampleRate, 200, 3000);
        gain = new Gain(sampleRate, 15, 15, 15);

        // pipe data to queue;
        soundInputPool.setOutputDataQueu(soundInputQueue);

        frequencyShift.setInputDataQueue(soundInputQueue);
        frequencyShift.setOutputDataQueue(freqShiftQueue);

        filterBank.setInputDataQueue(freqShiftQueue);
        filterBank.setOutputDataQueue(filterBankQueue);

        gain.setInputDataQueue(filterBankQueue);
        gain.setOutputDataQueue(gainQueue);

        soundOutputPool.setInputDataQueue(gainQueue);
    }

    public void serviceStart() {
        soundInputPool.threadStart();
        soundOutputPool.threadStart();
        frequencyShift.threadStart();
        filterBank.threadStart();
        gain.threadStart();
    }

    public void serviceStop() {
        soundInputPool.threadStop();
        soundOutputPool.threadStop();
        frequencyShift.threadStop();
        filterBank.threadStop();
        gain.threadStop();
    }
}
