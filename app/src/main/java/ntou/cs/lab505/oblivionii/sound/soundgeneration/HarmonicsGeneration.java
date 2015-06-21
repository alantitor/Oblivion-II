package ntou.cs.lab505.oblivionii.sound.soundgeneration;

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
     * @return
     */
    public short[] generate(int freq, int sec, int db, int order) {

        PureToneGeneration pureToneGeneration = new PureToneGeneration(sampleRate);
        short[] soundVector = new short[PureToneGeneration.pureToneExpectedLength(sampleRate, sec)];
        int tempFreq = freq;

        if (order <= 0) {
            return soundVector;
        }

        for (int i = 0; i < order; i++) {
            //Log.d("HarmonicsGeneration", "in generate. tempFreq: " + tempFreq);
            if (tempFreq > ((sampleRate / 2) * 0.9)) {
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

        return soundVector;
    }
}
