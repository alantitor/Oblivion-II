package ntou.cs.lab505.oblivionii.stream.device;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * Created by alan on 2015/6/20.
 */
public class WriteFile {

    private int mode;
    private String path = Environment.getExternalStorageDirectory().toString() + "/Download/";
    private String filename;
    private String extenedname;

    File file;
    FileOutputStream fOut;
    OutputStreamWriter fWriter;


    public WriteFile(int mode, String filename) {
        this.mode = mode;
        this.filename = filename;

        if (mode == 1) {
            this.extenedname = ".wmv";  // do not forget ".".
        } else {
            this.extenedname = ".data";
        }
    }

    public void open() {

        file = new File(path + filename + extenedname);
        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            fWriter = new OutputStreamWriter(fOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            fWriter.close();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(short[] dataVector) {

        Log.d("WriteFile", "in write.");
        if (dataVector == null || dataVector.length == 0) {
            return ;
        }

        if (mode == 1) {  // write to wmv file.

            //

        } else {  // write to data file.
            for (int i = 0; i < dataVector.length; i++) {
                try {
                    fWriter.append(dataVector[i] + ",");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
