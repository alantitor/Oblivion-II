package ntou.cs.lab505.oblivionii.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import ntou.cs.lab505.oblivionii.database.helper.DBHelper;

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
    public void seaveData(int semitone) {

    }

    public int getData() {
        int semitone = 0;


        return semitone;
    }
}
