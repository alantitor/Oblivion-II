package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

import ntou.cs.lab505.oblivionii.R;
import ntou.cs.lab505.oblivionii.database.BandSettingAdapter;
import ntou.cs.lab505.oblivionii.database.IOSettingAdapter;
import ntou.cs.lab505.oblivionii.datastructure.BandGainSetUnit;

public class BandSettingActivity extends Activity {

    private int channelNumber;
    private LinearLayout rightControlLayout;
    private LinearLayout leftRootLayout;
    private LinearLayout rightRootLayout;
    private SeekBar leftSeekbar;
    private SeekBar rightSeekBar;
    private TextView leftControlTV;
    private TextView leftCountTV;
    private TextView rightCountTV;
    private int leftItemNumber;
    private int rightItemNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_band_setting);

        rightControlLayout = (LinearLayout) findViewById(R.id.control_right_activity_band_setting);
        leftRootLayout = (LinearLayout) findViewById(R.id.drawarea_left_activity_band_setting);
        rightRootLayout = (LinearLayout) findViewById(R.id.drawarea_right_activity_band_setting);
        leftControlTV = (TextView) findViewById(R.id.controltitle_left_activity_band_setting);
        leftCountTV = (TextView) findViewById(R.id.count_left_activity_band_setting);
        rightCountTV = (TextView) findViewById(R.id.count_right_activity_band_setting);
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


        // check channel number.
        IOSettingAdapter ioSettingAdapter = new IOSettingAdapter(this.getApplicationContext());
        ioSettingAdapter.open();
        this.channelNumber = ioSettingAdapter.getData().getChannelNumber();
        //Log.d("BandSettingActivity", "in onCreate. channel number: " + this.channelNumber);
        ioSettingAdapter.close();

        if (this.channelNumber == 1) {
            leftControlTV.setText("聲道");
            rightControlLayout.setVisibility(View.GONE);
        }
    }

    /**
     *
     */
    @Override
    public void onResume() {
        super.onResume();

        // get data from database.
        BandSettingAdapter bandSettingAdapter = new BandSettingAdapter(this.getApplicationContext());
        bandSettingAdapter.open();
        ArrayList<BandGainSetUnit> bandGainSetUnitArrayList = bandSettingAdapter.getData();
        bandSettingAdapter.close();

        // load data to layout.


        Log.d("BandSettingActivity", "in onResume. size:" + bandGainSetUnitArrayList.size());

        for (int count = 0; count < bandGainSetUnitArrayList.size(); count++) {
            if (bandGainSetUnitArrayList.get(count).getLr() == 0) {

            } else if (bandGainSetUnitArrayList.get(count).getLr() == 1) {

            } else {
                Log.d("BandSettingActivity", "in onResume. continue.");
            }
        }

        // set seekBar progress.

    }

    /**
     *
     */
    @Override
    public void onPause() {
        super.onPause();

        View v;
        EditText lowBand, highBand, gain40, gain60, gain80;

        // delete old data.
        BandSettingAdapter bandSettingAdapter = new BandSettingAdapter(this.getApplicationContext());
        bandSettingAdapter.open();
        bandSettingAdapter.deleteData();


        ArrayList<BandGainSetUnit> bandGainSetUnitArrayList = new ArrayList<>();
        BandGainSetUnit bandGainSetUnit = null;

        // insert new data.
        // process left channel data.
        for (int count = 0; count < leftItemNumber; count++) {
            // get values from view fields.
            v = leftRootLayout.getChildAt(count);
            lowBand = (EditText) v.findViewById(R.id.lowBand_view_bandgain);
            highBand = (EditText) v.findViewById(R.id.highband_view_bandgain);
            gain40 = (EditText) v.findViewById(R.id.gain40_view_bandgain);
            gain60 = (EditText) v.findViewById(R.id.gain60_view_bandgain);
            gain80 = (EditText) v.findViewById(R.id.gain80_view_bandgain);
            //Log.d("BandSettingActivity", "in onPause. lowBand: " + lowBand.getText().toString());
            //Log.d("BandSettingActivity", "in onPause. highBand: " + highBand.getText().toString());
            //Log.d("BandSettingActivity", "in onPause. gain40Band: " + gain40.getText().toString());
            //Log.d("BandSettingActivity", "in onPause. gain60Band: " + gain60.getText().toString());
            //Log.d("BandSettingActivity", "in onPause. gain80Band: " + gain80.getText().toString());


            // generate object to save values.
            bandGainSetUnit = new  BandGainSetUnit(0,
                                                    Integer.parseInt(lowBand.getText().toString()),
                                                    Integer.parseInt(highBand.getText().toString()),
                                                    Integer.parseInt(gain40.getText().toString()),
                                                    Integer.parseInt(gain60.getText().toString()),
                                                    Integer.parseInt(gain80.getText().toString()));
            bandGainSetUnitArrayList.add(bandGainSetUnit);

        }

        // process right channel data.
        if (this.channelNumber == 2) {
            for (int count = 0; count < rightItemNumber; count ++) {
                // get values from view fields.
                v = rightRootLayout.getChildAt(count);
                lowBand = (EditText) v.findViewById(R.id.lowBand_view_bandgain);
                highBand = (EditText) v.findViewById(R.id.highband_view_bandgain);
                gain40 = (EditText) v.findViewById(R.id.gain40_view_bandgain);
                gain60 = (EditText) v.findViewById(R.id.gain60_view_bandgain);
                gain80 = (EditText) v.findViewById(R.id.gain80_view_bandgain);

                // generate object to save values.
                bandGainSetUnit = new  BandGainSetUnit(1,
                                                        Integer.parseInt(lowBand.getText().toString()),
                                                        Integer.parseInt(highBand.getText().toString()),
                                                        Integer.parseInt(gain40.getText().toString()),
                                                        Integer.parseInt(gain60.getText().toString()),
                                                        Integer.parseInt(gain80.getText().toString()));
                bandGainSetUnitArrayList.add(bandGainSetUnit);
            }
        }

        // save data to database.
        bandSettingAdapter.saveData(bandGainSetUnitArrayList);
        // close database
        bandSettingAdapter.close();
    }
}
