package ntou.cs.lab505.oblivionii.activities;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ntou.cs.lab505.oblivionii.R;
import ntou.cs.lab505.oblivionii.database.helper.DBHelper;

public class DatabaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        String data = showDatabase();

        TextView tv = (TextView) findViewById(R.id.drawarea_activity_database);
        //Resources res = getResources();
        tv.setText(data);
    }

    private String showDatabase() {
        String data = "";
        String tableName = "";

        DBHelper mDbHelper = new DBHelper(this.getApplicationContext());
        SQLiteDatabase mDb = mDbHelper.getWritableDatabase();

        Cursor cTable = mDb.rawQuery("SELECT NAME FROM sqlite_master WHERE TYPE='table'", null);

        if (cTable.moveToFirst()) {
            while (!cTable.isAfterLast()) {
                // table name
                tableName = cTable.getString(0);
                data += tableName + "\n==========\n";

                // column name
                List<String> columnName = new ArrayList<String>();
                Cursor cColumn = mDb.rawQuery("PRAGMA table_info(" + tableName + ")", null);
                if (cColumn.moveToFirst()) {
                    while (!cColumn.isAfterLast()) {
                        columnName.add(cColumn.getString(1));
                        cColumn.moveToNext();
                    }

                    for (int i = 0; i < columnName.size(); i++) {
                        data += columnName.get(i) + ", ";
                    }
                }
                data += "\n";

                // column data
                Cursor cData = mDb.rawQuery("SELECT * FROM " + tableName, null);
                if (cData.moveToFirst()) {
                    while (!cData.isAfterLast()) {
                        for (int i = 0; i < cData.getColumnCount(); i++) {
                            data += cData.getString(i) + ", ";
                        }
                        data += "\n";
                        cData.moveToNext();
                    }
                }

                data += "\n\n";  //
                cTable.moveToNext();
            }
        }


        return data;
    }
}
