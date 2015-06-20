package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import ntou.cs.lab505.oblivionii.R;

public class ServiceActivity extends Activity {

    boolean serviceState = false;

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
        Log.d("ServiceActivity", "in buttonService. in method.");
        if (serviceState == false) {
            Log.d("ServiceActivity", "in buttonService. change to pause.");
            controlButton.setImageResource(R.drawable.ic_music_player_pause_lines_orange_128);
            serviceState = true;
        }else {
            Log.d("ServiceActivity", "in buttonService. change to play.");
            controlButton.setImageResource(R.drawable.ic_music_player_play_orange_128);
            serviceState = false;
        }
    }

    public void buttonSetting(View view) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    private void checkServiceState() {

    }
}
