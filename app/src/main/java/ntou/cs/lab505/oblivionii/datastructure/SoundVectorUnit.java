package ntou.cs.lab505.oblivionii.datastructure;

/**
 * Created by alan on 6/15/15.
 */
public class SoundVectorUnit {

    private short[] leftChannel = null;
    private short[] rightChannel = null;
    private int channelNumber = 1;

    public SoundVectorUnit(short[] soundVector) {
        this.leftChannel = soundVector;
        channelNumber = 1;
    }

    public SoundVectorUnit(short[] leftChannel, short[] rightChannel) {
        this.leftChannel = leftChannel;
        this.rightChannel = rightChannel;
        channelNumber = 2;
    }

    public void setLeftChannel(short[] leftChannel) {
        this.leftChannel = leftChannel;
    }

    public void setRightChannel(short[] rightChannel) {
        this.rightChannel = rightChannel;
    }

    public void setChannels(short[] leftChannel, short[] rightChannel) {
        this.leftChannel = leftChannel;
        this.rightChannel = rightChannel;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

    public short[] getLeftChannel() {
        return this.leftChannel;
    }

    public short[] getRightChannel() {
        return this.rightChannel;
    }

    public int getChannelNumber() {
        return this.channelNumber;
    }

    public int getVectorLength() {
        return leftChannel.length;
    }
}
