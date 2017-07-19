package com.renotekno.zcabez.databaseexample;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.renotekno.zcabez.databaseexample.adapter.PetRVAdapter;
import com.renotekno.zcabez.databaseexample.data.DBConnection;
import com.renotekno.zcabez.databaseexample.data.PetContract.PetEntry;
import com.renotekno.zcabez.databaseexample.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addNewPetFAB;
    private List<Pet> pets;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView petRV;
    private PetRVAdapter petRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        addNewPetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        pets = new ArrayList<>();
        getPetDataFromDB();

        petRVAdapter = new PetRVAdapter(pets);
        linearLayoutManager = new LinearLayoutManager(this);
        petRV.setHasFixedSize(true);
        petRV.setLayoutManager(linearLayoutManager);
        petRV.setAdapter(petRVAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.catalog_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.insert_dummy_data:

                ContentValues contentValues = new ContentValues();
                contentValues.put(PetEntry.COLUMN_PET_NAME, "Toto");
                contentValues.put(PetEntry.COLUMN_PET_BREED, "Terrier");
                contentValues.put(PetEntry.COLUMN_PET_GENDER, 1);
                contentValues.put(PetEntry.COLUMN_PET_WEIGHT, 7);

                Uri uri = getContentResolver().insert(PetEntry.CONTENT_URI, contentValues);
                if (uri != null){
                    int validPosition = pets.size() > 1 ? 1 : 0;
                    pets.add(validPosition, new Pet("Toto", "Terrier", 1, 7));
                    petRVAdapter.notifyItemInserted(validPosition);
                    Toast.makeText(this, "Dummy pet data inserted", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.delete_all_pet:
                int totalRowDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
                if (totalRowDeleted > 0) {
                    pets.clear();
                    petRVAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "All pet data deleted", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        getPetDataFromDB();
        super.onStart();
    }

    private void initView() {
        addNewPetFAB = (FloatingActionButton) findViewById(R.id.addNewPetFAB);
        petRV = (RecyclerView) findViewById(R.id.petRecyclerView);
    }

    public void getPetDataFromDB() {
        pets.clear();
        Cursor c = getContentResolver().query(PetEntry.CONTENT_URI, null, null, null, null);
        while (c != null && c.moveToNext()) {
            Pet pet = new Pet(
                    c.getString(c.getColumnIndex(PetEntry.COLUMN_PET_NAME)),
                    c.getString(c.getColumnIndex(PetEntry.COLUMN_PET_BREED)),
                    c.getInt(c.getColumnIndex(PetEntry.COLUMN_PET_GENDER)),
                    c.getInt(c.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT))
            );

            pets.add(pet);
        }

        if (c != null) {
            c.close();
        }

        if (petRVAdapter != null) {
            petRVAdapter.notifyDataSetChanged();
        }

    }
}
