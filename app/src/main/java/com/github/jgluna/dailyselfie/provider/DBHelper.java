package com.github.jgluna.dailyselfie.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String SELFIES_TABLE = "selfies";
    public static final String SELFIES_ID_COLUMN = "id";
    public static final String SELFIES_CREATION_DATE_COLUMN = "creation_date";
    public static final String SELFIES_ORIGINAL_IMAGE_PATH_COLUMN = "original_image_path";
    public static final String SELFIES_IS_MODIFIED_COLUMN = "is_modified";
    public static final String SELFIES_MODIFIED_IMAGE_PATH_COLUMN = "modified_image_path";
    public static final String SELFIES_LAST_MODIFICATION_DATE_COLUMN = "modification_date";
    private static final String DATABASE_NAME = "selfies_db";
    private static final String CREATE_SELFIES_TABLE = " CREATE TABLE " + SELFIES_TABLE
            + " (" + SELFIES_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SELFIES_CREATION_DATE_COLUMN + " TEXT NOT NULL, "
            + SELFIES_ORIGINAL_IMAGE_PATH_COLUMN + " TEXT NOT NULL, "
            + SELFIES_IS_MODIFIED_COLUMN + " INTEGER NOT NULL DEFAULT 0, "
            + SELFIES_MODIFIED_IMAGE_PATH_COLUMN + " TEXT, "
            + SELFIES_LAST_MODIFICATION_DATE_COLUMN + " TEXT);";
    private static int DATABASE_VERSION = 1;


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SELFIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SELFIES_TABLE);
        onCreate(db);
    }

}
