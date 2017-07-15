package com.renotekno.zcabez.databaseexample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.renotekno.zcabez.databaseexample.R;
import com.renotekno.zcabez.databaseexample.model.Pet;

import java.util.List;

/**
 * Created by zcabez on 16/07/2017.
 */
public class PetListAdapter extends ArrayAdapter<Pet> {


    public PetListAdapter(Context context, List<Pet> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.pet_list_view, parent, false);

            viewHolder.petName = (TextView) convertView.findViewById(R.id.petName);
            viewHolder.petBreed = (TextView) convertView.findViewById(R.id.petBreed);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Pet pet = getItem(position);

        viewHolder.petName.setText(pet.getName());
        viewHolder.petBreed.setText(pet.getBreed());

        return convertView;
    }

    static class ViewHolder {
        TextView petName;
        TextView petBreed;
    }

}
