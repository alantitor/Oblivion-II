package ntou.cs.lab505.oblivionii.sound;

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
        short[] outputVector = new short[leftChannel.length * 2];

        for (int i = 0; i < leftChannel.length; i++) {
            outputVector[i * 2] = leftChannel[i];
            outputVector[i * 2 + 1] = rightChannel[i];
        }

        return outputVector;
    }

    /**
     * copy one channel sound vector to two channels sound vector.
     * @param soundVector
     * @return
     */
    public static short[] channelOne2Two(short[] soundVector) {

        short[] outputVector = new short[soundVector.length * 2];

        for (int i = 0; i < soundVector.length; i++) {
            outputVector[i * 2] = soundVector[i];
            outputVector[i * 2 + 1] = soundVector[i];
        }

        return outputVector;
    }

    /**
     * merge band vector two sound vector.
     * @param bandVector
     * @return
     */
    public static short[] channelMix(short[][] bandVector) {

        //short[] outputVector = new short[bandVector[0].length];  // is this statement safe??????????

        /**
         * how to get two dimensional array row and col length?
         */

        return null;
    }
}
