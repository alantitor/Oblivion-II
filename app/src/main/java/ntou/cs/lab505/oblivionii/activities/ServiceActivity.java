package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ntou.cs.lab505.oblivionii.R;
import ntou.cs.lab505.oblivionii.database.BandSettingAdapter;
import ntou.cs.lab505.oblivionii.database.FreqSettingAdapter;
import ntou.cs.lab505.oblivionii.database.IOSettingAdapter;
import ntou.cs.lab505.oblivionii.sound.SoundService;
import ntou.cs.lab505.oblivionii.sound.SoundService.SoundServiceBinder;

public class ServiceActivity extends Activity {

    boolean serviceState = false;  // denote is start service or not. it doesn't denote SoundService object's work state.
    boolean boundState = false;  // denote service bind state.
    // service object.
    private SoundService soundService;

    ImageView controlButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        // allow volume buttons.
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        // get object.
        controlButton = (ImageView) findViewById(R.id.servicecontrol_activity_service);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("ServiceActivity", "onSrviceConnected.");
            SoundServiceBinder mBinder = (SoundServiceBinder) service;
            soundService = mBinder.getService();
            boundState = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("ServiceActivity", "onServiceDisconnected.");
            boundState = false;
        }
    };

    @Override
    protected void onStart() {
        Log.d("ServiceActivity", "in onStart.");
        super.onStart();

        Intent intent = new Intent(this, SoundService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (boundState) {
            if (soundService.getServiceState()) {
                soundService.serviceStop();
                controlButton.setImageResource(R.drawable.ic_music_player_play_orange_128);
                serviceState = false;
            }

            unbindService(serviceConnection);
            boundState = false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (checkServiceState() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView message = new TextView(this);
            message.setText("請先設定助聽器參數");
            //message.setGravity(Gravity.CENTER);
            builder.setView(message);
            builder.setPositiveButton("OK", null);
            AlertDialog dialong = builder.show();
            dialong.show();

            return ;
        }
    }

    public void buttonService(View view) {

        if (checkServiceState() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            TextView message = new TextView(this);
            message.setText("請先設定助聽器參數");
            //message.setGravity(Gravity.CENTER);
            builder.setView(message);
            builder.setPositiveButton("OK", null);
            AlertDialog dialong = builder.show();
            dialong.show();

            return ;
        }


        if (serviceState == false) {
            if (boundState) {
                soundService.initService();


                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                controlButton.setImageResource(R.drawable.ic_music_player_pause_lines_orange_128);
                serviceState = true;
                soundService.serviceStart();
            }
        }else {
            if (boundState) {
                soundService.serviceStop();
                controlButton.setImageResource(R.drawable.ic_music_player_play_orange_128);
                serviceState = false;
            }
        }
    }

    public void buttonSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private boolean checkServiceState() {

        boolean state = false;

        IOSettingAdapter ioSettingAdapter = new IOSettingAdapter(this.getApplicationContext());
        FreqSettingAdapter freqSettingAdapter = new FreqSettingAdapter(this.getApplicationContext());
        BandSettingAdapter bandSettingAdapter = new BandSettingAdapter(this.getApplicationContext());

        ioSettingAdapter.open();
        freqSettingAdapter.open();
        bandSettingAdapter.open();

        if (ioSettingAdapter.getDataNumber() == 0 || freqSettingAdapter.getDataNumber() == 0 || bandSettingAdapter.getDataNumber() == 0) {
            state = false;
        } else {
            state = true;
        }

        ioSettingAdapter.close();
        freqSettingAdapter.close();
        bandSettingAdapter.close();

        return state;
    }
}
