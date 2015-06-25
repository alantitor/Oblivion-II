package ntou.cs.lab505.oblivionii.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import ntou.cs.lab505.oblivionii.database.helper.DBHelper;
import ntou.cs.lab505.oblivionii.database.helper.DBParams;
import ntou.cs.lab505.oblivionii.database.helper.TableContract;
import ntou.cs.lab505.oblivionii.datastructure.BandGainSetUnit;

/**
 * Created by alan on 6/22/15.
 */
public class BandSettingAdapter {

    Context mCtx;
    DBHelper mDbHelper;
    SQLiteDatabase mDb;

    public BandSettingAdapter(Context context) {
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

    public void saveData(ArrayList<BandGainSetUnit> dataUnit) {

        String[] projection = {TableContract._ID,
                                TableContract.T_BAND_USERID};
        String selection = TableContract.T_BAND_USERID + " = ? ";
        String[] selectionArgs = {DBParams.USER_ID};
        String sortOrder = "";
        Cursor c = mDb.query(TableContract.TABLE_BAND, projection, selection, selectionArgs, null, null, sortOrder);
        c.moveToFirst();

        if (c.getCount() != 0) {
            deleteData();
        } else {
            // save valuesto database.
            ContentValues insertValues = null;

            for (int count = 0; count < dataUnit.size(); count++) {
                insertValues = new ContentValues();
                insertValues.put(TableContract.T_BAND_USERID, DBParams.USER_ID);
                insertValues.put(TableContract.T_BAND_LR, dataUnit.get(count).getLr());
                insertValues.put(TableContract.T_BAND_LOWBAND, dataUnit.get(count).getLowBand());
                insertValues.put(TableContract.T_BAND_HIGHBAND, dataUnit.get(count).getHighBand());
                insertValues.put(TableContract.T_BAND_GAIN40, dataUnit.get(count).getGain40());
                insertValues.put(TableContract.T_BAND_GAIN60, dataUnit.get(count).getGain60());
                insertValues.put(TableContract.T_BAND_GAIN80, dataUnit.get(count).getGain80());
                insertValues.put(TableContract.T_BAND_STATE, "1");
                mDb.insert(TableContract.TABLE_BAND, null, insertValues);
            }
        }
    }

    public ArrayList<BandGainSetUnit> getData() {

        String[] projection = {TableContract.T_BAND_USERID,
                                TableContract.T_BAND_LR,
                                TableContract.T_BAND_LOWBAND,
                                TableContract.T_BAND_HIGHBAND,
                                TableContract.T_BAND_GAIN40,
                                TableContract.T_BAND_GAIN60,
                                TableContract.T_BAND_GAIN80,
                                TableContract.T_BAND_STATE};
        String selection = TableContract.T_BAND_USERID + " = ? ";
        String[] selectionArgs = {DBParams.USER_ID};
        String sortOrder = "";
        Cursor c = mDb.query(TableContract.TABLE_BAND, projection, selection, selectionArgs, null, null, sortOrder);
        c.moveToFirst();

        ArrayList<BandGainSetUnit> bandGainSetUnitArrayList = new ArrayList<>();
        BandGainSetUnit bandGainSetUnit = null;
        int lr, lowBand, highBand, gain40, gain60, gain80;


        while (!c.isAfterLast()) {
            lr = Integer.parseInt(c.getString(c.getColumnIndex(TableContract.T_BAND_LR)));
            lowBand = Integer.parseInt(c.getString(c.getColumnIndex(TableContract.T_BAND_LOWBAND)));
            highBand = Integer.parseInt(c.getString(c.getColumnIndex(TableContract.T_BAND_HIGHBAND)));
            gain40 = Integer.parseInt(c.getString(c.getColumnIndex(TableContract.T_BAND_GAIN40)));
            gain60 = Integer.parseInt(c.getString(c.getColumnIndex(TableContract.T_BAND_GAIN60)));
            gain80 = Integer.parseInt(c.getString(c.getColumnIndex(TableContract.T_BAND_GAIN80)));
            bandGainSetUnit = new BandGainSetUnit(lr, lowBand, highBand, gain40, gain60, gain80);
            bandGainSetUnitArrayList.add(bandGainSetUnit);

            c.moveToNext();  // go to next.
        }

        return bandGainSetUnitArrayList;
    }

    public void deleteData() {
        //Log.d("BandSettingAdapter", "in deleteData. delete data.");
        mDb.delete(TableContract.TABLE_BAND, TableContract.T_BAND_USERID + " = " + DBParams.USER_ID, null);
    }
}
