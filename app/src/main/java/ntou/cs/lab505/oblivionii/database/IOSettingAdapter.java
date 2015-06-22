package ntou.cs.lab505.oblivionii.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import ntou.cs.lab505.oblivionii.database.helper.DBHelper;
import ntou.cs.lab505.oblivionii.database.helper.DbParams;
import ntou.cs.lab505.oblivionii.database.helper.TableContract;
import ntou.cs.lab505.oblivionii.datastructure.IOSetUnit;

/**
 * Created by alan on 6/22/15.
 */
public class IOSettingAdapter {

    Context mCtx;
    DBHelper mDbHelper;
    SQLiteDatabase mDb;


    public IOSettingAdapter(Context contenxt) {
        this.mCtx = contenxt;
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
     */
    public void saveData(IOSetUnit ioSetUnit) {

        String[] projection = {TableContract._ID,
                                    TableContract.T_IO_USERID};
        String selection = TableContract.T_IO_USERID + " = ?";
        String[] selectionArgs = {DbParams.USER_ID};
        String sortOrder = "";
        Cursor c = mDb.query(TableContract.TABLE_IO, projection, selection, selectionArgs, null, null, sortOrder);
        c.moveToFirst();

        Log.d("IOSettingAdapter", "in saveData. count: " + c.getCount());
        if (c.getCount() != 1) {
            // delete old data.
            // insert new data.
            ContentValues insertValues = new ContentValues();
            insertValues.put(TableContract.T_IO_USERID, DbParams.USER_ID);
            insertValues.put(TableContract.T_IO_CHANNEL, 0);
            insertValues.put(TableContract.T_IO_INPUT, 0);
            insertValues.put(TableContract.T_IO_OUTPUT, 0);
            insertValues.put(TableContract.T_IO_STATE, 1);
            mDb.insert(TableContract.TABLE_IO, null, insertValues);
        } else {
            // update old data.
            long db_id = Long.parseLong(c.getString(c.getColumnIndex(TableContract._ID)));
            //mDb.execSQL("UPDATE " + TableContract.TABLE_IO +
              //              " SET " + TableContract.T_IO_CHANNEL);
        }
    }

    /**
     *
     */
    public void deleteData() {

    }

    /**
     *
     */
    public void getData() {

    }
}
