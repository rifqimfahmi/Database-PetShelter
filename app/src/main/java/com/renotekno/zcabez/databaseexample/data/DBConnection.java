package com.renotekno.zcabez.databaseexample.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by zcabez on 14/07/2017.
 */
public class DBConnection {
    public static PetDbHelper petDbHelper = null;
    public static SQLiteDatabase writeAbleDB = null;
    public static SQLiteDatabase readAbleDB = null;

    public static SQLiteDatabase getWriteAbleDB(Context context) {
        if (petDbHelper == null) {
            petDbHelper = new PetDbHelper(context);
        }

        if (writeAbleDB != null && writeAbleDB.isOpen()) {
            return writeAbleDB;
        }
        writeAbleDB = petDbHelper.getWritableDatabase();
        return writeAbleDB;
    }

    public static SQLiteDatabase getReadAbleDB(Context context) {
        if (petDbHelper == null) {
            petDbHelper = new PetDbHelper(context);
        }

        if (readAbleDB != null && readAbleDB.isOpen()) {
            return readAbleDB;
        }
        readAbleDB = petDbHelper.getReadableDatabase();
        return readAbleDB;
    }
}
