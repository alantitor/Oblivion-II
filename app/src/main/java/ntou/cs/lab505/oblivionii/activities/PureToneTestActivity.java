package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import ntou.cs.lab505.oblivionii.R;
import ntou.cs.lab505.oblivionii.sound.PureToneTest;
import ntou.cs.lab505.oblivionii.sound.PureToneTest.PureToneTestBinder;

public class PureToneTestActivity extends Activity {

    EditText ETFreq;
    EditText ETDb;
    EditText ETHarm;
    EditText ETSec;
    EditText ETBcLow;
    EditText ETBcHigh;
    EditText ETSemitone;
    EditText ETGain;
    RadioGroup RGChannel;
    RadioGroup RGOutput;

    private PureToneTest pureToneTest;
    boolean boundState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pure_tone_test);

        // get objects.
        ETFreq = (EditText) findViewById(R.id.et_freq_activity_pure_tone_test);
        ETDb = (EditText) findViewById(R.id.et_db_activity_pure_tone_test);
        ETHarm = (EditText) findViewById(R.id.et_harm_activity_pure_tone_test);
        ETSec = (EditText) findViewById(R.id.et_sec_activity_pure_tone_test);
        ETBcLow = (EditText) findViewById(R.id.et_lowband_activity_pure_tone_test);
        ETBcHigh = (EditText) findViewById(R.id.et_highband_activity_pure_tone_test);
        ETSemitone = (EditText) findViewById(R.id.et_semitone_activity_pure_tone_test);
        ETGain = (EditText) findViewById(R.id.et_gain_activity_pure_tone_test);
        RGChannel = (RadioGroup) findViewById(R.id.rg_channel_activity_pure_tone_test);
        RGOutput = (RadioGroup) findViewById(R.id.rg_output_activity_pure_tone_test);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //Log.d("PureToneTestActivity", "onServiceConnected");
            PureToneTestBinder mBinder = (PureToneTestBinder) service;
            pureToneTest = mBinder.getService();
            boundState = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            //Log.d("PureToneTestActivity", "onServiceDisconnected");
            boundState = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, PureToneTest.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (boundState) {
            unbindService(serviceConnection);
            boundState = false;
        }
    }

    public void buttonTest(View view) {

        // get input value.
        int valueFreq = Integer.parseInt(ETFreq.getText().toString());
        int valueDb = Integer.parseInt(ETDb.getText().toString());
        int valueHarm = Integer.parseInt(ETHarm.getText().toString());
        int valueSec = Integer.parseInt(ETSec.getText().toString());
        int valueBcLow = Integer.parseInt(ETBcLow.getText().toString());
        int valueBcHigh = Integer.parseInt(ETBcHigh.getText().toString());
        int valueSemitone = Integer.parseInt(ETSemitone.getText().toString());
        int valueGain = Integer.parseInt(ETGain.getText().toString());
        int valueChannel = 0;
        int valueOutput = 0;

        int selectedId = RGChannel.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.rb_channel_left_activity_pure_tone_test:
                valueChannel = 0;
                break;
            case R.id.rb_channel_right_activity_pure_tone_test:
                valueChannel = 1;
                break;
            default:
                valueChannel = 0;
        }

        selectedId = RGOutput.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.rb_output_speker_activity_pure_tone_test:
                valueOutput = 0;
                break;
            case R.id.rb_output_df_activity_pure_tone_test:
                valueOutput = 1;
                break;
            case R.id.rb_output_wf_activity_pure_tone_test:
                valueOutput = 2;
                break;
            default:
                valueOutput = 0;
        }

        // check value.
        //Log.d("PureToneTestActivity", "in buttonTest. valueFreq = " + valueFreq);
        //Log.d("PureToneTestActivity", "in buttonTest. valueDb = " + valueDb);
        //Log.d("PureToneTestActivity", "in buttonTest. valueHarm = " + valueHarm);
        //Log.d("PureToneTestActivity", "in buttonTest. valueSec = " + valueSec);
        //Log.d("PureToneTestActivity", "in buttonTest. valueBcLow = " + valueBcLow);
        //Log.d("PureToneTestActivity", "in buttonTest. valueBcHigh = " + valueBcHigh);
        //Log.d("PureToneTestActivity", "in buttonTest. valueSemitone = " + valueSemitone);
        //Log.d("PureToneTestActivity", "in buttonTest. valueGain = " + valueGain);
        //Log.d("PureToneTestActivity", "in buttonTest. valueChannel = " + valueChannel);
        //Log.d("PureToneTestActivity", "in buttonTest. valueOutput = " + valueOutput);


        if (boundState) {
            pureToneTest.initParameters(valueFreq, valueDb, valueHarm, valueSec, valueBcLow, valueBcHigh, valueSemitone, valueGain, valueChannel, valueOutput);
            pureToneTest.runTest();
        }
    }
}
