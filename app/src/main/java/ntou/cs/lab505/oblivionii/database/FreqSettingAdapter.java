package ntou.cs.lab505.oblivionii.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ntou.cs.lab505.oblivionii.database.helper.DBHelper;
import ntou.cs.lab505.oblivionii.database.helper.DBParams;
import ntou.cs.lab505.oblivionii.database.helper.TableContract;

/**
 * Created by alan on 6/22/15.
 */
public class FreqSettingAdapter {

    Context mCtx;
    DBHelper mDbHelper;
    SQLiteDatabase mDb;

    public FreqSettingAdapter(Context context) {
        this.mCtx = context;
    }

    public DBHelper open() {
        this.mDbHelper = new DBHelper(this.mCtx);
        this.mDb = this.mDbHelper.getWritableDatabase();
        return this.mDbHelper;
    }

    public void close() {
        this.mDbHelper.close();
    }

    /**
     *
     * @param semitone
     */
    public void saveData(int semitone) {

        String[] projection = {TableContract._ID,
                                TableContract.T_FREQSHIFT_USERID};
        String selection = TableContract.T_FREQSHIFT_USERID + " = ? ";
        String[] selectionArgs = {DBParams.USER_ID};
        String sortOrder = "";
        Cursor c = mDb.query(TableContract.TABLE_FREQSHIFT, projection, selection, selectionArgs, null, null, sortOrder);
        c.moveToFirst();

        if (c.getCount() != 1) {
            ContentValues insertValues = new ContentValues();
            insertValues.put(TableContract.T_FREQSHIFT_USERID, DBParams.USER_ID);
            insertValues.put(TableContract.T_FREQSHIFT_SEMITONE, semitone);
            insertValues.put(TableContract.T_FREQSHIFT_STATE, 1);
            mDb.insert(TableContract.TABLE_FREQSHIFT, null, insertValues);
        } else {
            //long db_id = Long.parseLong(c.getString(c.getColumnIndex(TableContract._ID)));
            mDb.execSQL("UPDATE " + TableContract.TABLE_FREQSHIFT +
                            " SET " + TableContract.T_FREQSHIFT_SEMITONE + " = " + semitone +
                            " WHERE " + TableContract.T_FREQSHIFT_USERID + " = " + DBParams.USER_ID);
        }
    }

    public int getData() {

        int semitone = 0;

        String[] projection = {TableContract.T_FREQSHIFT_USERID,
                                    TableContract.T_FREQSHIFT_SEMITONE};
        String selection = TableContract.T_FREQSHIFT_USERID + " = ? ";
        String[] selectionArgs = {DBParams.USER_ID};
        String sortOrder = "";
        Cursor c = mDb.query(TableContract.TABLE_FREQSHIFT, projection, selection, selectionArgs, null, null, sortOrder);
        c.moveToFirst();

        if (c.getCount() == 1) {
            //Log.d("FreqSettingAdapter", "in getData. string: " + c.getString(c.getColumnIndex(TableContract.T_FREQSHIFT_SEMITONE)));
            semitone = Integer.parseInt(c.getString(c.getColumnIndex(TableContract.T_FREQSHIFT_SEMITONE)));
        }

        Log.d("FreqSettingAdapter", "in getData. data: " + semitone);

        return semitone;
    }

    public int getDataNumber() {

        String[] projection = {TableContract.T_FREQSHIFT_USERID};
        String selection = TableContract.T_FREQSHIFT_USERID + " = ? ";
        String[] selectionArgs = {DBParams.USER_ID};
        String sortOrder = "";
        Cursor c = mDb.query(TableContract.TABLE_FREQSHIFT, projection, selection, selectionArgs, null, null, sortOrder);
        c.moveToFirst();

        return c.getCount();
    }
}
