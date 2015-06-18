package ntou.cs.lab505.oblivionii.datastructure;

/**
 * Created by alan on 6/15/15.
 */
public class BandSetUnit {
    private int lowBand;
    private int highBand;

    public BandSetUnit(int lowBand, int highBand) {
        this.lowBand = lowBand;
        this.highBand = highBand;
    }

    public void setLowBand(int lowBand) {
        this.lowBand = lowBand;
    }

    public void setHighBand(int highBand) {
        this.highBand = highBand;
    }

    public int getLowBand() {
        return this.lowBand;
    }

    public int getHighBand() {
        return this.highBand;
    }
}
