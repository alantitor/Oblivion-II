package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import ntou.cs.lab505.oblivionii.R;

public class BandSettingActivity extends Activity {

    private LinearLayout leftRootLayout;
    private LinearLayout rightRootLayout;
    private SeekBar leftSeekbar;
    private SeekBar rightSeekBar;
    private TextView leftCountTV;
    private TextView rightCountTV;
    private int leftItemNumber;
    private int rightItemNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_setting);

        leftCountTV = (TextView) findViewById(R.id.count_left_activity_band_setting);
        rightCountTV = (TextView) findViewById(R.id.count_right_activity_band_setting);
        leftRootLayout = (LinearLayout) findViewById(R.id.drawarea_left_activity_band_setting);
        rightRootLayout = (LinearLayout) findViewById(R.id.drawarea_right_activity_band_setting);
        leftSeekbar = (SeekBar) findViewById(R.id.seekbar_left_activity_band_setting);
        rightSeekBar = (SeekBar) findViewById(R.id.seekbar_right_activity_band_setting);

        leftSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                leftItemNumber = progress;
                leftCountTV.setText(String.valueOf(progress));
                leftRootLayout.removeAllViews();

                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (int count = 0; count < progress; count++) {
                    leftRootLayout.addView(layoutInflater.inflate(R.layout.view_bandgain, null));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rightItemNumber = progress;
                rightCountTV.setText(String.valueOf(progress));
                rightRootLayout.removeAllViews();

                LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                for (int count = 0; count < progress; count++) {
                    rightRootLayout.addView(layoutInflater.inflate(R.layout.view_bandgain, null));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    }

    @Override
    public void onPause() {
        super.onPause();

        View v;
        EditText lowBand, highband, gain40, gain60, gain80;
        ContentValues insertValues;

        // delete old data.


        // insert new data.

    }
}
