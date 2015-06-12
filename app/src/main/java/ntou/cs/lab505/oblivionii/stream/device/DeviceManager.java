package ntou.cs.lab505.oblivionii.stream.device;

import android.media.AudioManager;

/**
 * Created by alan on 6/11/15.
 */
public class DeviceManager {

    private AudioManager audioManager = null;

    public DeviceManager(AudioManager audioManager) {
        this.audioManager = audioManager;
        audioManager.setMode(AudioManager.MODE_NORMAL);
    }

    public boolean getDeviceState(int type) {
        switch (type) {
            case 0:
                // Adjust output for headsets
                if (audioManager.isWiredHeadsetOn()) {
                    return true;
                } else {
                    return false;
                }
            case 1:
                // Adjust output for Bluetooth.
                if (audioManager.isBluetoothA2dpOn()) {
                    return true;
                } else {
                    return false;
                }
            default:
                return false;
        }
    }
}