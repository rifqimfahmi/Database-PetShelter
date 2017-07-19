package com.renotekno.zcabez.databaseexample.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by zcabez on 7/13/17.
 */

public final class PetContract {

    private PetContract () {}

    public static final String CONTENT_AUTHORITY = "com.renotekno.zcabez.shelter";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);


    public final static class PetEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PetEntry.TABLE_NAME);

        public static final String CONTENT_FULL_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PetEntry.TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PetEntry.TABLE_NAME;

        public static final String SQL_CREATE_PETS_TABLE = "CREATE TABLE " + PetEntry.TABLE_NAME + "(" +
                PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, " +
                PetEntry.COLUMN_PET_BREED + " TEXT, " +
                PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, " +
                PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);"
                ;

        public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + PetEntry.TABLE_NAME;

        public static final String TABLE_NAME = "pets";
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";

        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 1;
    }

}
