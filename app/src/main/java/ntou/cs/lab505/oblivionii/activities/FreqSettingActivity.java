package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.TextView;

import ntou.cs.lab505.oblivionii.R;
import ntou.cs.lab505.oblivionii.database.FreqSettingAdapter;

public class FreqSettingActivity extends Activity {

    TextView textView;
    SeekBar seekBar;
    int seekValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freq_setting);

        textView = (TextView) findViewById(R.id.count_activity_freq_setting);
        seekBar = (SeekBar) findViewById(R.id.seekbar_activity_freq_setting);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("FreqSettingActivity", "in oncreate. seekBar value: " + progress);
                seekValue = progress;
                textView.setText(String.valueOf(seekToSemi(progress)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        FreqSettingAdapter freqSettingAdapter = new FreqSettingAdapter(this.getApplicationContext());
        freqSettingAdapter.open();
        seekValue = semiToSeek(freqSettingAdapter.getData());
        seekBar.setProgress(seekValue);
        freqSettingAdapter.close();
    }

    @Override
    public void onPause () {
        super.onPause();

        FreqSettingAdapter freqSettingAdapter = new FreqSettingAdapter(this.getApplicationContext());
        freqSettingAdapter.open();
        freqSettingAdapter.saveData(seekToSemi(seekValue));
        freqSettingAdapter.close();
    }


    private int seekToSemi(int seekValue) {
        return seekValue - 12;
    }

    private int semiToSeek(int semitone) {
        return semitone + 12;
    }
}
