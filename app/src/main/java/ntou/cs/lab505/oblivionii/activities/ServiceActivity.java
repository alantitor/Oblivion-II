package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ntou.cs.lab505.oblivionii.R;
import ntou.cs.lab505.oblivionii.database.BandSettingAdapter;
import ntou.cs.lab505.oblivionii.database.FreqSettingAdapter;
import ntou.cs.lab505.oblivionii.database.IOSettingAdapter;
import ntou.cs.lab505.oblivionii.sound.SoundService;

public class ServiceActivity extends Activity {

    boolean serviceState = false;
    SoundService soundService;
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


        Log.d("ServiceActivity", "in buttonService. in method.");
        if (serviceState == false) {
            //Log.d("ServiceActivity", "in buttonService. change to pause.");
            controlButton.setImageResource(R.drawable.ic_music_player_pause_lines_orange_128);
            serviceState = true;
        }else {
            //Log.d("ServiceActivity", "in buttonService. change to play.");
            controlButton.setImageResource(R.drawable.ic_music_player_play_orange_128);
            serviceState = false;
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

        if (ioSettingAdapter.getDataNumber() == 0 && freqSettingAdapter.getDataNumber() == 0 && bandSettingAdapter.getDataNumber() == 0) {
            state = false;
        } else {
            state = true;
        }

        ioSettingAdapter.close();
        freqSettingAdapter.close();
        bandSettingAdapter.close();

        Log.d("ServiceActivity", "in checkServiceState. state: " + state);

        return state;
    }
}
