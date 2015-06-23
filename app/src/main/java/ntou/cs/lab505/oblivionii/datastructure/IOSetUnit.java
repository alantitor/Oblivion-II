package ntou.cs.lab505.oblivionii.datastructure;

/**
 * Created by alan on 6/22/15.
 */
public class IOSetUnit {

    private int channelNumber;
    private int inputType;
    private int outputType;

    public IOSetUnit() {
        this.channelNumber = 1;
        this.inputType = 0;
        this.outputType = 0;
    }

    public IOSetUnit(int channelNumber, int inputType, int outputType) {
        this.channelNumber = channelNumber;
        this.inputType = inputType;
        this.outputType = outputType;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public void setOutputType(int outputType) {
        this.outputType = outputType;
    }

    public void setData(int channelNumber, int inputType, int outputType) {
        this.channelNumber = channelNumber;
        this.inputType = inputType;
        this.outputType = outputType;
    }

    public int getChannelNumber() {
        return this.channelNumber;
    }

    public int getInputType() {
        return this.inputType;
    }

    public int getOutputType() {
        return this.outputType;
    }
}
