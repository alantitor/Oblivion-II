package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import ntou.cs.lab505.oblivionii.R;

public class FreqSettingActivity extends Activity {

    TextView textView;
    SeekBar seekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_freq_setting);

        textView = (TextView) findViewById(R.id.count_activity_freq_setting);
        seekBar = (SeekBar) findViewById(R.id.seekbar_activity_freq_setting);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

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


}
