package com.Dasuni.mydiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
public class DBManager {
    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String date, String time, String title,String description, String image) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.DATE, date);
        contentValue.put(DatabaseHelper.TIME, time);
        contentValue.put(DatabaseHelper.TITLE, title);
        contentValue.put(DatabaseHelper.DESCRIPTION, description);
        contentValue.put(DatabaseHelper.IMAGE, image);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.DATE, DatabaseHelper.TIME, DatabaseHelper.TITLE,DatabaseHelper.DESCRIPTION, DatabaseHelper.IMAGE };
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, "_ID desc");
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String date, String time, String title,String description, String image) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.DATE, date);
        contentValues.put(DatabaseHelper.TIME, time);
        contentValues.put(DatabaseHelper.TITLE, title);
        contentValues.put(DatabaseHelper.DESCRIPTION, description);
        contentValues.put(DatabaseHelper.IMAGE, image);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }
}
