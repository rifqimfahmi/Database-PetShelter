package com.renotekno.zcabez.databaseexample;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import com.renotekno.zcabez.databaseexample.adapter.PetListAdapter;
import com.renotekno.zcabez.databaseexample.data.DBConnection;
import com.renotekno.zcabez.databaseexample.data.PetContract.PetEntry;
import com.renotekno.zcabez.databaseexample.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addNewPetFAB;
    private ListView petListView;
    List<Pet> pets;
    private Object petDataFromDB;

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

        PetListAdapter petListAdapter = new PetListAdapter(this, pets);
        petListView.setAdapter(petListAdapter);

//        displayDummyDataFromDB();
//        checkDatabaseConnection();
    }

//    private void displayDummyDataFromDB() {
//        Cursor c = DBConnection.getReadAbleDB(this).query(PetEntry.TABLE_NAME, null, null, null, null, null, null);
//        String name = "";
//        if (c.moveToLast()) {
//            name = "Name = " + c.getString(c.getColumnIndex(PetEntry.COLUMN_PET_NAME)) + "\nWeight = " + c.getInt(c.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT));
//        }
//
//        try {
//            databaseDummy.setText(name);
//        } finally {
//            c.close();
//        }
//    }

//    private void checkDatabaseConnection() {
//        Cursor c = DBConnection.getReadAbleDB(this).rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);
//        try {
//            testDbConnection.setText("Row count on database: " + c.getCount());
//        } finally {
//            c.close();
//        }
//    }

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

                long id = DBConnection.getWriteAbleDB(this).insert(PetEntry.TABLE_NAME, null, contentValues);

                break;
            case R.id.delete_all_pet:
                Toast.makeText(this, "Action menu 2 clicked", Toast.LENGTH_SHORT).show();


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initView() {
        addNewPetFAB = (FloatingActionButton) findViewById(R.id.addNewPetFAB);
        petListView = (ListView) findViewById(R.id.pet_list_view);
    }

    public void getPetDataFromDB() {
        Cursor c = DBConnection.getReadAbleDB(this).query(PetEntry.TABLE_NAME, null, null, null, null, null, null);
        while (c.moveToNext()) {
            Pet pet = new Pet(
                    c.getString(c.getColumnIndex(PetEntry.COLUMN_PET_NAME)),
                    c.getString(c.getColumnIndex(PetEntry.COLUMN_PET_BREED)),
                    c.getInt(c.getColumnIndex(PetEntry.COLUMN_PET_GENDER)),
                    c.getInt(c.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT))
            );

            pets.add(pet);
        }

        c.close();
    }
}
