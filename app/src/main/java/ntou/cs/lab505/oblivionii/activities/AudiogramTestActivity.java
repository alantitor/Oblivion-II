package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import ntou.cs.lab505.oblivionii.R;

public class AudiogramTestActivity extends Activity {

    private Spinner PureToneFreq;
    private EditText PureToneVolme;
    private Button PureToneStart;
    private CheckBox useFilter;

    //private Speaker speaker;
    private PureToneGraph graph;

    private RadioButton left;
    private RadioButton right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audiogram_test);

        left = (RadioButton)findViewById(R.id.LeftRadio);
        right = (RadioButton)findViewById(R.id.RightRadio);

        PureToneFreq = (Spinner)findViewById(R.id.PureToneFreq);

        List<String> list = new ArrayList<String>();
        list.add("250");
        list.add("500");
        list.add("1000");
        list.add("2000");
        list.add("4000");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        PureToneFreq.setAdapter(dataAdapter);

        PureToneVolme = (EditText)findViewById(R.id.PureToneVolume);
        PureToneStart = (Button)findViewById(R.id.PureToneStart);
        PureToneStart.setOnClickListener(new OnClick());

        graph = (PureToneGraph)findViewById(R.id.pureToneGraph1);


    }

    private class OnClick implements View.OnClickListener {
        public void onClick(View arg0)
        {
            if(arg0.getId()==R.id.PureToneStart)
            {
                String freq_s = String.valueOf(PureToneFreq.getSelectedItem().toString());
                String db_s = PureToneVolme.getText().toString();
                int freq = Integer.parseInt(freq_s);
                int db = Integer.parseInt(db_s);
                if(left.isChecked())
                    graph.setData(freq, db, 0);
                if(right.isChecked())
                    graph.setData(freq, db, 1);
                //Toast.makeText(PureToneTest.this, "pass", 5).show();
                switch(freq)
                {
                    case 250:
                        db = db+26;
                        break;
                    case 500:
                        db+=12;
                        break;
                    case 1000:
                        db+=7;
                        break;
                    case 2000:
                        db+=7;
                        break;
                    case 4000:
                        db+=10;
                        break;
                }


            }
        }
    }
}
