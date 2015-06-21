package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ntou.cs.lab505.oblivionii.R;

public class SettingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    public void buttonIOSetting(View view) {
        Intent intent = new Intent(this, IOSettingActivity.class);
        startActivity(intent);
    }

    public void buttonFreqSetting(View view) {
        Intent intent = new Intent(this, FreqSettingActivity.class);
        startActivity(intent);
    }

    public void buttonBandSetting(View view) {
        Intent intent = new Intent(this, BandSettingActivity.class);
        startActivity(intent);
    }

    public void buttonPureToneTest(View view) {
        Intent intent = new Intent(this, PureToneTestActivity.class);
        startActivity(intent);
    }

    public void buttonAudiogramTest(View view) {
        Intent intent = new Intent(this, AudiogramTestActivity.class);
        startActivity(intent);
    }

    public void buttonGuide(View view) {
        Intent intent = new Intent(this, GuideActivity.class);
        startActivity(intent);
    }

    public void buttonAbout(View view) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    public void buttonDatabase(View view) {
        Intent intent = new Intent(this, DatabaseActivity.class);
        startActivity(intent);
    }
}
