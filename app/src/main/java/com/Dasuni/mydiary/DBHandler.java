package com.Dasuni.mydiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper {

    //We use version to update database.

    private static final int VERSION = 1;
    private static final String DB_NAME = "MYDiarry";
    private static final String TABLE_NAME = "my_diarry";

    //Column Names
    private static final String ID = "page_number";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";

    private static final String IMAGE_PATH = "image_path";
    private static final String DATE = "date";
    private static final String TIME = "time";

    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {  //database table
        String TABLE_CREATE_QUERY = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITLE + " TEXT," +
                DESCRIPTION + " TEXT," +
                DATE + " TEXT," +
                TIME + " TEXT," +
                IMAGE_PATH + " TEXT );";

        sqLiteDatabase.execSQL(TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    //datainsert
    public void dataInsert(ModelMyDiary model){ //[obj1, obj2, obj3 ....]

        SQLiteDatabase sqlLiteDatabase = getWritableDatabase();

        ContentValues contentValues = new ContentValues(); //We use this to make organize data before send it to the database.

        contentValues.put(TITLE, model.getTitle());
        contentValues.put(DESCRIPTION, model.getDescription());
        contentValues.put(DATE, model.getDate());
        contentValues.put(TIME, model.getDate());
        contentValues.put(IMAGE_PATH, model.getDirectory());


        //saving data to database
        sqlLiteDatabase.insert(TABLE_NAME, null, contentValues);

        //optional : then we can close the database
        sqlLiteDatabase.close();
    }
}
