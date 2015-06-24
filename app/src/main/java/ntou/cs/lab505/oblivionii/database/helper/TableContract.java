package ntou.cs.lab505.oblivionii.database.helper;

import android.provider.BaseColumns;

/**
 * Created by alan on 6/22/15.
 */
public class TableContract implements BaseColumns {

    // sqlite db information
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "oblibion";
    public static final String TYPE_NULL = "NULL";
    public static final String TYPE_INTEGER = "INTEGER";
    public static final String TYPE_REAL = "REAL";
    public static final String TYPE_TEXT = "TEXT";
    public static final String TYPE_BLOB = "BLOB";
    // sqlite table name
    public static final String TABLE_IO = "io_setting";
    public static final String TABLE_FREQSHIFT = "freqshift_setting";
    public static final String TABLE_BAND = "band_setting";
    // sqlite db columns name
    // "io_setting" table content
    public static final String T_IO_USERID = "user_id";
    public static final String T_IO_CHANNEL = "channel_number";
    public static final String T_IO_INPUT = "input_stream";
    public static final String T_IO_OUTPUT = "output_stream";
    public static final String T_IO_STATE = "state";
    // "freqshift_setting" table content
    public static final String T_FREQSHIFT_USERID = "user_id";
    public static final String T_FREQSHIFT_SEMITONE = "semitone";
    public static final String T_FREQSHIFT_STATE = "state";
    // "band_setting" table content
    public static final String T_BAND_USERID = "user_id";
    public static final String T_BAND_LR = "lr";
    public static final String T_BAND_LOWBAND = "low_band";
    public static final String T_BAND_HIGHBAND = "high_band";
    public static final String T_BAND_GAIN40 = "gain40";
    public static final String T_BAND_GAIN60 = "gain60";
    public static final String T_BAND_GAIN80 = "gain80";
    public static final String T_BAND_STATE = "state";
}
