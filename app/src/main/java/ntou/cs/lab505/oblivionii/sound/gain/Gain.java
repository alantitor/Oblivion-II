package ntou.cs.lab505.oblivionii.sound.gain;

/**
 * Created by alan on 6/10/15.
 */
public class Gain {
    private double gain;

    public Gain(double gain) {
        this.gain = Math.pow(10, gain / 20);
    }

    public short[] process(short[] data) {
        for (int i = 0; i < data.length; i++) {
            int temp = data[i];
            temp = (int) (temp * this.gain);

            // check overflow.
            if (temp > Short.MAX_VALUE) {
                data[i] = Short.MAX_VALUE;
            } else if (temp < Short.MIN_VALUE) {
                data[i] = Short.MIN_VALUE;
            } else {
                data[i] = (short) temp;
            }
        }

        return data;
    }

    /*
     * 計算音量
     * data - 欲計算的資料
     * return 音量
     */
    public static int calculateDb(short[] data) {
        //short min = data[0];
        double sum = 0;

        for (int i = 0; i < data.length; i++) {
            sum += Math.pow(data[i], 2);
        }
        sum = 10 * Math.log10(sum / data.length);

        return (int)sum;
    }

    public static double calculateDbDouble(short[] data) {
        //short min = data[0];
        double sum = 0;

        for (int i = 0; i < data.length; i++) {
            sum += Math.pow(data[i], 2);
        }
        sum = 10 * Math.log10(sum / data.length);

        return sum;
    }
}