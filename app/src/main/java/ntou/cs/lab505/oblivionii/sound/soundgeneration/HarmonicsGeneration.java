package ntou.cs.lab505.oblivionii.sound.soundgeneration;

import static ntou.cs.lab505.oblivionii.sound.SoundTool.channelOne2Two;

/**
 * Created by alan on 6/12/15.
 */
public class HarmonicsGeneration {

    private int sampleRate;

    /**
     *
     * @param sampleRate
     */
    public HarmonicsGeneration(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * generate pure sound.
     * @param freq
     * @param sec
     * @param db
     * @param order
     * @param channel
     * @return
     */
    public short[] generate(int freq, int sec, int db, int order, int channel) {

        PureToneGeneration pureToneGeneration = new PureToneGeneration(sampleRate);
        short[] soundVector = new short[PureToneGeneration.pureToneExpectedLength(sampleRate, sec)];
        int tempFreq = freq;

        if (order <= 0) {
            return soundVector;
        }

        for (int i = 0; i < order; i++) {
            //Log.d("HarmonicsGeneration", "in generate. tempFreq: " + tempFreq);
            if (tempFreq > 7000) {
                break;
            }

            short[] tempVector = pureToneGeneration.generate(tempFreq, sec, db);

            for (int trace = 0; trace < soundVector.length; trace++) {
                int tt = soundVector[trace] + tempVector[trace];

                if (tt > Short.MAX_VALUE) {
                    soundVector[trace] = Short.MAX_VALUE;
                } else if (tt < Short.MIN_VALUE) {
                    soundVector[trace] = Short.MIN_VALUE;
                } else {
                    soundVector[trace] += tempVector[trace];
                }
            }

            tempFreq += tempFreq;
        }


        // process channel
        if (channel == 1) {
            return soundVector;
        } else if (channel == 2) {
            return channelOne2Two(soundVector);
        } else {  // force others as 1 channel.
            return soundVector;
        }
    }
}
