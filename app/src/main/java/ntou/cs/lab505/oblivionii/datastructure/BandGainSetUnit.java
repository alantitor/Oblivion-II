package ntou.cs.lab505.oblivionii.datastructure;

/**
 * Created by alan on 6/24/15.
 */
public class BandGainSetUnit {

    private int lr;
    private BandSetUnit bandSetUnit;
    private GainSetUnit gainSetUnit;


    public BandGainSetUnit(int lr, int lowBand, int highBand, int gain40, int gain60, int gain80) {
        this.lr = lr;
        bandSetUnit = new BandSetUnit(lowBand, highBand);
        gainSetUnit = new GainSetUnit(gain40, gain60, gain80);
    }

    public void setLr(int lr) {
        this.lr = lr;
    }

    public void setLowBand(int lowBand) {
        this.bandSetUnit.setLowBand(lowBand);
    }

    public void setHighBand(int highBand) {
        this.bandSetUnit.setHighBand(highBand);
    }

    public void setGain40(int gain40) {
        this.gainSetUnit.setGain40(gain40);
    }

    public void setGain60(int gain60) {
        this.gainSetUnit.setGain60(gain60);
    }

    public void setGain80(int gain80) {
        this.gainSetUnit.setGain80(gain80);
    }

    public int getLr() {
        return this.lr;
    }

    public BandSetUnit getBandSetUnit() {
        return this.bandSetUnit;
    }

    public GainSetUnit getGainSetUnit() {
        return this.gainSetUnit;
    }

    public int getLowBand() {
        return this.bandSetUnit.getLowBand();
    }

    public int getHighBand() {
        return this.bandSetUnit.getHighBand();
    }

    public int getGain40() {
        return this.gainSetUnit.getGain40();
    }

    public int getGain60() {
        return this.gainSetUnit.getGain60();
    }

    public int getGain80() {
        return this.gainSetUnit.getGain60();
    }
}
