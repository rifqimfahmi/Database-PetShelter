package com.renotekno.zcabez.databaseexample.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.renotekno.zcabez.databaseexample.R;
import com.renotekno.zcabez.databaseexample.model.Pet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zcabez on 7/17/17.
 */

public class PetRVAdapter extends RecyclerView.Adapter<PetRVAdapter.PetVH> {

    List<Pet> pets;

    public PetRVAdapter (List<Pet> pets) {
        this.pets = pets;
    }

    @Override
    public PetVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pet_list_view, parent, false);
        return new PetVH(view);
    }

    @Override
    public void onBindViewHolder(PetVH holder, int position) {
        Pet pet = pets.get(position);
        holder.attachDataToView(pet);
    }

    @Override
    public int getItemCount() {
        return pets.size();
    }

    static class PetVH extends RecyclerView.ViewHolder {

        private TextView petName;
        private TextView petBreed;

        public PetVH(View itemView) {
            super(itemView);
            petName = (TextView) itemView.findViewById(R.id.petName);
            petBreed = (TextView) itemView.findViewById(R.id.petBreed);
        }

        public void attachDataToView(Pet pet) {
            petName.setText(pet.getName());
            petBreed.setText(pet.getBreed());
        }
    }
}
