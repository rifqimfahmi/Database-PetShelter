package com.renotekno.zcabez.databaseexample.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import com.renotekno.zcabez.databaseexample.R;
import com.renotekno.zcabez.databaseexample.data.PetContract.PetEntry;

/**
 * Created by zcabez on 20/07/2017.
 */
public class PetCursorAdapter extends CursorAdapter {

    public PetCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        Log.d("LOADER_WAY", "newView on Adapter");
        View view = LayoutInflater.from(context).inflate(R.layout.pet_list_view, parent, false);
        PetVH vh = new PetVH();

        vh.petName = (TextView) view.findViewById(R.id.petName);
        vh.petBreed = (TextView) view.findViewById(R.id.petBreed);

        view.setTag(vh);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.d("LOADER_WAY", "bindView on Adapter");
        PetVH vh = (PetVH) view.getTag();

        String name = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME));
        String breed = cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED));

        if (TextUtils.isEmpty(breed)) {
            breed = "Unknown";
        }

        vh.petName.setText(name);
        vh.petBreed.setText(breed);
    }

    static class PetVH {
        TextView petName;
        TextView petBreed;
    }
}
