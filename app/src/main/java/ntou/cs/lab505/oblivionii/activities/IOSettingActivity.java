package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import ntou.cs.lab505.oblivionii.R;
import ntou.cs.lab505.oblivionii.database.IOSettingAdapter;
import ntou.cs.lab505.oblivionii.datastructure.IOSetUnit;

public class IOSettingActivity extends Activity {

    RadioGroup RGChannel;
    RadioGroup RGInput;
    RadioGroup RGOutput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io_setting);
        // get objects.
        RGChannel = (RadioGroup) findViewById(R.id.rg_channel_activity_io_setting);
        RGInput = (RadioGroup) findViewById(R.id.rg_input_activity_io_setting);
        RGOutput = (RadioGroup) findViewById(R.id.rg_output_activity_io_setting);
    }

    @Override
    public void onResume() {
        super.onResume();

        IOSettingAdapter ioSettingAdapter = new IOSettingAdapter(this.getApplicationContext());
        ioSettingAdapter.open();
        IOSetUnit ioSetUnit = ioSettingAdapter.getData();
        ioSettingAdapter.close();

        RadioButton rb = null;

        switch (ioSetUnit.getChannelNumber()) {
            case 1:
                // one channel.
                rb = (RadioButton) findViewById(R.id.rb_channel_one_activity_io_setting);
                rb.setChecked(true);
                break;
            case 2:
                // two channel.
                rb = (RadioButton) findViewById(R.id.rb_channel_two_activity_io_setting);
                rb.setChecked(true);
                break;
            default:
                // one channel.
                rb = (RadioButton) findViewById(R.id.rb_channel_one_activity_io_setting);
                rb.setChecked(true);
        }

        switch (ioSetUnit.getInputType()) {
            case 0:
                // use microphone.
                rb = (RadioButton) findViewById(R.id.rb_input_microphone_activity_io_setting);
                rb.setChecked(true);
                break;
            case 1:
                // use phone inside stream.
                rb = (RadioButton) findViewById(R.id.rb_input_insidestream_activity_io_setting);
                rb.setChecked(true);
                break;
            case 2:
                // use data file.
                rb = (RadioButton) findViewById(R.id.rb_input_datafile_activity_io_setting);
                rb.setChecked(true);
                break;
            case 3:
                // use wmv file.
                rb = (RadioButton) findViewById(R.id.rb_input_wmv_activity_io_setting);
                rb.setChecked(true);
                break;
            default:
                // use microphone.
                rb = (RadioButton) findViewById(R.id.rb_input_microphone_activity_io_setting);
                rb.setChecked(true);
        }

        switch (ioSetUnit.getOutputType()) {
            case 0:
                // use speaker.
                rb = (RadioButton) findViewById(R.id.rb_output_speaker_activity_io_setting);
                rb.setChecked(true);
                break;
            case 1:
                // use data file.
                rb = (RadioButton) findViewById(R.id.rb_output_datafile_activity_io_setting);
                rb.setChecked(true);
                break;
            case 2:
                // use wmv file.
                rb = (RadioButton) findViewById(R.id.rb_output_wmv_activity_io_setting);
                rb.setChecked(true);
                break;
            default:
                // use speaker.
                rb = (RadioButton) findViewById(R.id.rb_output_speaker_activity_io_setting);
                rb.setChecked(true);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        int valueChannel;
        int valueInput;
        int valueOutput;

        int selectedId = RGChannel.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.rb_channel_one_activity_io_setting:
                valueChannel = 1;
                break;
            case R.id.rb_channel_two_activity_io_setting:
                valueChannel = 2;
                break;
            default:
                valueChannel = 1;
        }

        selectedId = RGInput.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.rb_input_microphone_activity_io_setting:
                valueInput = 0;
                break;
            case R.id.rb_input_insidestream_activity_io_setting:
                valueInput = 1;
                break;
            case R.id.rb_input_datafile_activity_io_setting:
                valueInput = 2;
                break;
            case R.id.rb_input_wmv_activity_io_setting:
                valueInput = 3;
                break;
            default:
                valueInput = 0;
        }

        selectedId = RGOutput.getCheckedRadioButtonId();
        switch (selectedId) {
            case R.id.rb_output_speaker_activity_io_setting:
                valueOutput = 0;
                break;
            case R.id.rb_output_datafile_activity_io_setting:
                valueOutput = 1;
                break;
            case R.id.rb_output_wmv_activity_io_setting:
                valueOutput = 2;
                break;
            default:
                valueOutput = 0;
        }

        //Log.d("IOSettingActivity", "in onPause. valueChannel: " + valueChannel);
        //Log.d("IOSettingActivity", "in onPause. valueInput: " + valueInput);
        //Log.d("IOSettingActivity", "in onPause. valueOutput: " + valueOutput);

        // save data to database.
        IOSettingAdapter ioSettingAdapter = new IOSettingAdapter(this.getApplicationContext());
        ioSettingAdapter.open();
        IOSetUnit ioSetUnit = new IOSetUnit(valueChannel, valueInput, valueOutput);
        ioSettingAdapter.saveData(ioSetUnit);
        ioSettingAdapter.close();
    }
}
