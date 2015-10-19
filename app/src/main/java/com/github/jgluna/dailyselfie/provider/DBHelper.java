package com.github.jgluna.dailyselfie.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "selfies_db";
    private static int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "selfies";
    public static final String ID_COLUMN = "id";
    public static final String CREATION_DATE_COLUMN = "creation_date";
    public static final String ORIGINAL_IMAGE_PATH_COLUMN = "original_image_path";
    public static final String IS_MODIFIED_COLUMN = "is_modified";
    public static final String MODIFIED_IMAGE_PATH_COLUMN = "modified_image_path";
    public static final String LAST_MODIFICATION_DATE_COLUMN = "modification_date";
    private static final String CREATE_DB_TABLE = " CREATE TABLE " + TABLE_NAME
            + " (" + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CREATION_DATE_COLUMN + " TEXT NOT NULL, "
            + ORIGINAL_IMAGE_PATH_COLUMN + " TEXT NOT NULL, "
            + IS_MODIFIED_COLUMN + " INTEGER NOT NULL DEFAULT 0, "
            + MODIFIED_IMAGE_PATH_COLUMN + " TEXT, "
            + LAST_MODIFICATION_DATE_COLUMN + " TEXT);";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
