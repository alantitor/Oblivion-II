package ntou.cs.lab505.oblivionii.stream.device;

/**
 * Created by alan on 6/11/15.
 */
public class Speaker {



    /**
     * check which output device is used.
     * @param type
     * @return
     */
    public static boolean checkOutputDeviceState(int type) {

        boolean state = false;

        switch (type) {
            case 0:
                // build in speaker
                state = isSpeakerphoneOn();
                break;
            case 1:
                // headset speaker
                state = isWiredHeadsetOn();
                break;
            case 2:
                // bluetooth speaker
                state = isBluetoothA2dpOn();
                break;
            default:
                state = false;
        }


        return state;
    }

    private static boolean isWiredHeadsetOn() {
        boolean state = false;

        return state;
    }

    private static boolean isSpeakerphoneOn() {
        boolean state = false;

        return state;
    }

    private static boolean isBluetoothA2dpOn() {
        boolean state = false;

        return state;
    }
}
