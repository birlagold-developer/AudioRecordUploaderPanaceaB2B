package com.kss.AudioRecordUploader.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kss.AudioRecordUploader.model.MissedCallLog;

import java.util.ArrayList;

public class DataBaseAdapter {
    public static final String DATABASE_NAME = "skycinex.db";
    public static final int DATABASE_VERSION = 1;
    // SQL Statement to create a new database.

    // Table Name "Login"
    static final String DATABASE_CREATE_MISSED_CALL_LOG = "create table IF NOT EXISTS " + "MISSED_CALL_LOG"
            + "(ID integer primary key autoincrement, MobileNumber  text); ";

    private Context context;
    private SQLiteDatabase db;
    private DataBaseHelper dbHelper;

    public DataBaseAdapter(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public DataBaseAdapter open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public Long insertMissedCallLog(String mobileNumber) {
        ContentValues values = new ContentValues();
        values.put("MobileNumber", mobileNumber);
        return db.insert("MISSED_CALL_LOG", null, values);
    }

    public ArrayList<MissedCallLog> getAllMissedCallLog() {
        ArrayList<MissedCallLog> missedCallLogs = new ArrayList<>();
        Cursor cursor = db.query("MISSED_CALL_LOG", null, null, null, null, null, " ID ASC");
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                String mobileNumber = cursor.getString(cursor.getColumnIndex("MobileNumber"));

                missedCallLogs.add(new MissedCallLog(id, mobileNumber));
            } while (cursor.moveToNext());
        }

        return missedCallLogs.size() == 0 ? null : missedCallLogs;
    }

    public int deleteMissedCallLog(String ID) {
        return db.delete("MISSED_CALL_LOG", "ID=" + ID, null);
    }
}