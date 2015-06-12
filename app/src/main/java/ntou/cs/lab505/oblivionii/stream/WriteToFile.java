package ntou.cs.lab505.oblivionii.stream;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by alan on 6/12/15.
 */
public class WriteToFile extends Thread {

    private boolean threadState = false;
    private LinkedBlockingQueue<short[]> inputDataQueue;
    private String fileName;


    public WriteToFile(String filename) {
        this.fileName = filename;
    }

    /**
     * set data queue.  this queue is the source for processing.
     * @param inputDataQueue
     */
    public void setInputDataQueue(LinkedBlockingQueue<short[]> inputDataQueue) {
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

    /**
     *
     */
    public void run() {



    }
}
