package ntou.cs.lab505.oblivionii.database.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by alan on 6/22/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String CS = " , ";  // comma sep
    private static final String WS = " ";  // white space

    public DBHelper(Context context) {
        super(context, TableContract.DATABASE_NAME + ".db", null, TableContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        /*
         * be careful of sql create table command.
         * don't add "CS" at end of command.
         */
        // create sqlite table command.
        String SQL_CREATE_TABLE_IO =
                "CREATE TABLE" + WS + TableContract.TABLE_IO + " ( " +
                TableContract._ID + WS + TableContract.TYPE_INTEGER + WS + " PRIMARY KEY " + CS +
                TableContract.T_IO_USERID + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_IO_CHANNEL + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_IO_INPUT + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_IO_OUTPUT + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_IO_STATE + WS + TableContract.TYPE_INTEGER +
                " ) ";
        String SQL_CREATE_TABLE_FREQSHIFT =
                "CREATE TABLE" + WS + TableContract.TABLE_FREQSHIFT + " ( " +
                TableContract._ID + WS + TableContract.TYPE_INTEGER + WS + " PRIMARY KEY " + CS +
                TableContract.T_FREQSHIFT_USERID + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_FREQSHIFT_SEMITONE + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_FREQSHIFT_STATE + WS + TableContract.TYPE_INTEGER +
                " ) ";
        String SQL_CREATE_TABLE_BAND =
                "CREATE TABLE" + WS + TableContract.TABLE_BAND + " ( " +
                TableContract._ID + WS + TableContract.TYPE_INTEGER + WS + " PRIMARY KEY " + CS +
                TableContract.T_BAND_USERID + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_BAND_LR + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_BAND_LOWBAND + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_BAND_HIGHBAND + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_BAND_GAIN40 + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_BAND_GAIN60 + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_BAND_GAIN80 + WS + TableContract.TYPE_INTEGER + CS +
                TableContract.T_BAND_STATE + WS + TableContract.TYPE_INTEGER +
                " ) ";

        // exclude sqlite commands.
        db.execSQL(SQL_CREATE_TABLE_IO);
        db.execSQL(SQL_CREATE_TABLE_FREQSHIFT);
        db.execSQL(SQL_CREATE_TABLE_BAND);

        // initial db table columns.
        // needless.
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS" + WS;
        db.execSQL(SQL_DELETE_ENTRIES + TableContract.TABLE_IO);
        db.execSQL(SQL_DELETE_ENTRIES + TableContract.TABLE_FREQSHIFT);
        db.execSQL(SQL_DELETE_ENTRIES + TableContract.TABLE_BAND);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
