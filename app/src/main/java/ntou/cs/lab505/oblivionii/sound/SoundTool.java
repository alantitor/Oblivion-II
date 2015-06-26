package ntou.cs.lab505.oblivionii.sound;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;

/**
 * Created by alan on 6/12/15.
 */
public class SoundTool {

    /**
     * merge two one-channel sound vectors into a two-channels sound vector.
     * @param leftChannel
     * @param rightChannel
     * @return
     */
    public static short[] channelTwo2One(short[] leftChannel, short[] rightChannel) {

        short[] outputVector = null;

        if (leftChannel != null && rightChannel != null) {  // we have two channel data.
            outputVector = new short[leftChannel.length * 2];
            // data: LRLRLRLR...
            for (int i = 0; i < leftChannel.length; i++) {
                outputVector[i * 2] = leftChannel[i];
                outputVector[i * 2 + 1] = rightChannel[i];
            }
        } else if (leftChannel == null && rightChannel != null){  // we only have right channel data. set zero to left channel.
            outputVector = new short[rightChannel.length * 2];
            // data: 0R0R0R0R...
            for (int i = 0; i < rightChannel.length; i++) {
                outputVector[i * 2] = 0;
                outputVector[i * 2 + 1] = rightChannel[i];
            }
        } else if (leftChannel != null && rightChannel == null){  // we only have left channel data. set zero to right channel.
            outputVector = new short[leftChannel.length * 2];
            // data: L0L0L0L0...
            for (int i = 0; i < leftChannel.length; i++) {
                outputVector[i * 2] = leftChannel[i];
                outputVector[i * 2 + 1] = 0;
            }
        } else {
            outputVector = new short[2];
            outputVector[0] = 0;
            outputVector[1] = 0;
        }

        return outputVector;
    }

    /**
     * merge band vector two sound vector.
     * @param soundVectorUnits
     * @return
     */
    public static SoundVectorUnit channelMix(SoundVectorUnit[] soundVectorUnits) {
        Log.d("SoundTool", "in channelMix. band number: " + soundVectorUnits.length);
        SoundVectorUnit outputVector = null;

        if (soundVectorUnits[0].getChannelNumber() == 1) {  // one channel.
            short[] leftVector = new short[soundVectorUnits[0].getVectorLength()];
            int temp = 0;

            for (int i = 0; i < leftVector.length; i++) {
                temp = 0;
                for (int j = 0; j < soundVectorUnits.length; j++){
                    temp += soundVectorUnits[j].getLeftChannel()[i];
                }

                leftVector[i] = (short) temp;
            }
            outputVector = new SoundVectorUnit(leftVector);
        } else {  // two channels.
            short[] leftVector = new short[soundVectorUnits[0].getVectorLength()];
            short[] rightVector = new short[soundVectorUnits[0].getVectorLength()];
            int templ = 0;
            int tempr = 0;

            for (int i = 0; i < leftVector.length; i++) {
                templ = 0;
                tempr = 0;
                for (int j = 0; j < soundVectorUnits.length; j++) {
                    templ += soundVectorUnits[j].getLeftChannel()[i];
                    tempr += soundVectorUnits[j].getRightChannel()[i];
                }

                leftVector[i] = (short) templ;
                rightVector[i] = (short) tempr;
            }
            outputVector = new SoundVectorUnit(leftVector, rightVector);
        }

        return outputVector;
    }

    /*
     * 計算音量
     * data - 欲計算的資料
     * return 音量
     */
    public static int calculateDb(short[] data) {
        double sum = 0;

        for (int i = 0; i < data.length; i++) {
            sum += Math.pow(data[i], 2);
        }
        sum = 10 * Math.log10(sum / data.length);

        return (int)sum;
    }

    public static double calculateDbDouble(short[] data) {
        double sum = 0;

        for (int i = 0; i < data.length; i++) {
            sum += Math.pow(data[i], 2);
        }
        sum = 10 * Math.log10(sum / data.length);

        return sum;
    }

    public static void saveVectorToDataFile(short[] data, String fileName) {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName + ".txt");
        FileOutputStream fOut;
        OutputStreamWriter fWriter;

        if (data == null) {
            return ;
        }

        try {
            file.createNewFile();
            fOut = new FileOutputStream(file);
            fWriter = new OutputStreamWriter(fOut);
            for (int i = 0; i < data.length; i++) {
                fWriter.append(data[i] + ",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
