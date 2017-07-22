package com.renotekno.zcabez.databaseexample.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.renotekno.zcabez.databaseexample.data.PetContract.PetEntry;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by zcabez on 7/19/17.
 */

public class PetProvider extends ContentProvider {

    public static final int PETS = 100;
    public static final int PETS_ID = 101;

    private static UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetEntry.TABLE_NAME, PETS);
        uriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetEntry.TABLE_NAME + "/#", PETS_ID);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;

        int match = uriMatcher.match(uri);
        switch (match) {
            case PETS:
                cursor = DBConnection
                        .getWriteAbleDB(getContext())
                        .query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case PETS_ID:
                selection = PetEntry._ID + " = ?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = DBConnection
                        .getWriteAbleDB(getContext())
                        .query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Invalid parameter query");
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertData(uri, values);
            default:
                throw new IllegalArgumentException("Invalid uri argument");
        }
    }

    private Uri insertData(Uri uri, ContentValues values) {

        // TODO : Perform sanity check
        final String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
        final String breed = values.getAsString(PetEntry.COLUMN_PET_BREED);
        final int gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        final int weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);

        long id = DBConnection
                .getWriteAbleDB(getContext())
                .insert(PetEntry.TABLE_NAME, null, values);

        if (id == -1){
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final int match = uriMatcher.match(uri);
        switch (match) {
            case PETS:
                int totalDataDeleted = DBConnection
                        .getWriteAbleDB(getContext())
                        .delete(PetEntry.TABLE_NAME, selection, selectionArgs);
                if (totalDataDeleted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return totalDataDeleted;
            default:
                throw new IllegalArgumentException("Invalid uri argument");
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = uriMatcher.match(uri);

        switch (match) {
            case PETS:
                return PetEntry.CONTENT_FULL_TYPE;
            case PETS_ID:
                return PetEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException("Invalid getType uri argument");
        }
    }
}
