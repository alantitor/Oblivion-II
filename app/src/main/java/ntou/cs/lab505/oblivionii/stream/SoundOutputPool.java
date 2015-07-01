package ntou.cs.lab505.oblivionii.stream;

import android.util.Log;

import java.util.concurrent.LinkedBlockingQueue;

import ntou.cs.lab505.oblivionii.datastructure.SoundVectorUnit;
import ntou.cs.lab505.oblivionii.stream.device.Speaker;
import ntou.cs.lab505.oblivionii.stream.device.WriteFile;

import static ntou.cs.lab505.oblivionii.sound.SoundTool.channelTwo2One;

/**
 * Created by alan on 6/11/15.
 */
public class SoundOutputPool extends Thread {

    private boolean threadState;  // denote thread state.
    private LinkedBlockingQueue<SoundVectorUnit> inputDataQueue;

    private int sampleRate;
    private int channelNumber;
    private int lr;
    private int mode;

    Speaker speaker;
    WriteFile writeFile;


    /**
     *
     * @param sampleRate
     * @param channelNumber
     * @param lr
     * @param mode 0: speaker, 1: write to data file, 2: write to wmv file
     */
    public SoundOutputPool(int sampleRate, int channelNumber, int lr, int mode) {
        //Log.d("SoundOutputPool", "in SoundOutputPool. initial success.");

        this.sampleRate = sampleRate;
        this.channelNumber = channelNumber;
        this.lr = lr;
        this.mode = mode;

        // initial object.
        switch (mode) {
            case 0:
                speaker = new Speaker(sampleRate, channelNumber);
                break;
            case 1:
                writeFile = new WriteFile(0, "speaker");
                break;
            case 2:
                writeFile = new WriteFile(1, "speaker");
                break;
            default:
                this.mode = 0;
                speaker = new Speaker(sampleRate, channelNumber);
        }
    }

    /**
     * set data queue.  this queue is the source for processing.
     * @param inputDataQueue
     */
    public void setInputDataQueue(LinkedBlockingQueue<SoundVectorUnit> inputDataQueue) {
        this.inputDataQueue = inputDataQueue;
    }

    /**
     * control thread state.  start thread.
     */
    public void threadStart() {
        this.threadState = true;
        this.start();
    }

    /**
     * control thread state.  stop thread.
     */
    public void threadStop() {
        this.threadState = false;
        this.interrupt();
    }

    public void run() {
        Log.d("SoundOutputPool", "in run. process start.");
        SoundVectorUnit inputUnit;
        short[] outputDataVector = null;


        /**
         * Algorithm:
         *  (1) check device state.
         *  (2) reset mode.
         */


        // open output object.
        switch (mode) {
            case 0:
                speaker.open();
                break;
            case 1:
            case 2:
                writeFile.open();
                break;
            default:
                break;
        }


        // do function.
        while (threadState) {
            // take data from queue.
            inputUnit = inputDataQueue.poll();

            if (inputUnit == null) {
                continue;
            }
            if (inputUnit.getLeftChannel() == null || inputUnit.getVectorLength() == 0) {
                continue;
            }
            //Log.d("SoundOutputPool", "in run. inputUnit length: " + inputUnit.getVectorLength());


            // merge channel sound data.
            if (this.channelNumber == 1) {
                // one channel.
                outputDataVector = inputUnit.getLeftChannel();
                Log.d("SoundOutputPool", "in run. data: " + outputDataVector[101] + outputDataVector[102] + outputDataVector[103] + outputDataVector[104]);
            } else if (this.channelNumber == 2) {
                // two channel.
                if (inputUnit.getChannelNumber() == 1) {
                    if (lr == 0) {  // only output left ear.
                        //Log.d("SoundOutputPool", "in run. play sound at left ear.");
                        outputDataVector = channelTwo2One(inputUnit.getLeftChannel(), null);
                    } else if (lr == 1){  // only output right ear.
                        //Log.d("SoundOutputPool", "in run. play sound at right ear.");
                        outputDataVector = channelTwo2One(null, inputUnit.getLeftChannel());
                    } else {
                        //Log.d("SoundOutputPool", "in run. play sound at two ears.");
                        outputDataVector = channelTwo2One(inputUnit.getLeftChannel(), inputUnit.getLeftChannel());
                    }
                } else {
                    Log.d("SoundOutputPool", "in run. play sound at two ears.");
                    outputDataVector = channelTwo2One(inputUnit.getLeftChannel(), inputUnit.getRightChannel());
                }
            } else {
                // one channel.
                this.channelNumber = 1;  // !!!
                outputDataVector = inputUnit.getLeftChannel();
            }


            // pipe sound data.
            switch (mode) {
                case 0:
                    pipeVectorToSpeaker(outputDataVector);
                    break;
                case 1:
                case 2:
                    pipeVectorToFile(outputDataVector);
                    break;
                default:
                    break;
            }
        }


        // close output object.
        switch (mode) {
            case 0:
                speaker.close();
                break;
            case 1:
            case 2:
                writeFile.close();
                break;
            default:
                break;
        }

        Log.d("SoundOutputPool", "in run. process stop.");
    }

    private void pipeVectorToSpeaker(short[] outputVector) {
        speaker.write(outputVector);
    }

    private void pipeVectorToFile(short[] outputVector) {
        writeFile.write(outputVector);
    }
}
