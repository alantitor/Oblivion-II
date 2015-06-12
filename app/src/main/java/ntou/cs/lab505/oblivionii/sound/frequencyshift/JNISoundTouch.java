package ntou.cs.lab505.oblivionii.sound.frequencyshift;

/**
 * Created by alan on 6/12/15.
 */
public class JNISoundTouch {

    public native void setSampleRate(int sampleRate);
    public native void setChannels(int channel);
    public native void setTempoChange(float newTempo);
    public native void setPitchSemiTones(int newPitch);
    public native void setRateChange(float newRate);
    public native void putSamples(short[] samples, int len);
    public native short[] receiveSamples();

    static{
        System.loadLibrary("soundtouch");
        //System.loadLibrary("app");
    }
}
