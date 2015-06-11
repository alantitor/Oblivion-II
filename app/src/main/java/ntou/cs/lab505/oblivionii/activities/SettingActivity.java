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

    }

    public void buttonFreqSetting(View view) {

    }

    public void buttonBandSetting(View view) {

    }

    public void buttonPureToneTest(View view) {
        Intent intent = new Intent(this, PureToneTestActivity.class);
        startActivity(intent);
    }

    public void buttonAudiogramTest(View view) {

    }

    public void buttonIntro(View view) {

    }

    public void buttonAbout(View view) {

    }

    public void buttonDatabase(View view) {

    }
}
