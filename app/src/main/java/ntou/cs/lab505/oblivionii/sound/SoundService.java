package ntou.cs.lab505.oblivionii.sound;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.database.IOSettingAdapter;
import ntou.cs.lab505.oblivionii.datastructure.BandGainSetUnit;
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
    // sound vector.
    short[] soundVector;
    // data queues.
    LinkedBlockingQueue<SoundVectorUnit> soundInputQueue = new LinkedBlockingQueue<>();
    LinkedBlockingQueue<SoundVectorUnit> freqShiftQueue = new LinkedBlockingQueue<>();


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
        BandGainSetUnit bandGainSetUnit;

        IOSettingAdapter ioSettingAdapter = new IOSettingAdapter(this.getApplicationContext());
        ioSettingAdapter.open();
        ioSetUnit = ioSettingAdapter.getData();
        ioSettingAdapter.close();

        // initial object.
        soundInputPool = new SoundInputPool(sampleRate, ioSetUnit.getInputType());
        soundOutputPool = new SoundOutputPool(sampleRate, ioSetUnit.getChannelNumber(), 0, ioSetUnit.getOutputType());

        // pipe data to queue;
        soundInputPool.setOutputDataQueu(soundInputQueue);
        soundOutputPool.setInputDataQueue(soundInputQueue);
    }

    public void serviceStart() {
        soundInputPool.threadStart();
        soundOutputPool.threadStart();
    }

    public void serviceStop() {
        soundInputPool.threadStop();
        soundOutputPool.threadStop();
    }
}
