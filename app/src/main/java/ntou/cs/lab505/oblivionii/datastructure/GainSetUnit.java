package ntou.cs.lab505.oblivionii.datastructure;

/**
 * Created by alan on 6/15/15.
 */
public class GainSetUnit {

    private int gain40;
    private int gain60;
    private int gain80;


    public GainSetUnit(int gain40, int gain60, int gain80) {
        this.gain40 = gain40;
        this.gain60 = gain60;
        this.gain80 = gain80;
    }

    public void setGain40(int gain40) {
        this.gain40 = gain40;
    }

    public void setGain60(int gain60) {
        this.gain60 = gain60;
    }

    public void setGain80(int gain80) {
        this.gain80 = gain80;
    }

    public int getGain40() {
        return gain40;
    }

    public int getGain60() {
        return gain60;
    }

    public int getGain80() {
        return gain80;
    }
}
