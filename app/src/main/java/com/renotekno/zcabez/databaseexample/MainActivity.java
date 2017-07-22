package com.renotekno.zcabez.databaseexample;

import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.renotekno.zcabez.databaseexample.adapter.PetCursorAdapter;
import com.renotekno.zcabez.databaseexample.data.PetContract.PetEntry;
import com.renotekno.zcabez.databaseexample.model.Pet;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton addNewPetFAB;
    private List<Pet> pets;
    private ListView listView;
    private PetCursorAdapter petCursorAdapter;
    private RelativeLayout emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        listView.setEmptyView(emptyView);

        addNewPetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        pets = new ArrayList<>();

        petCursorAdapter = new PetCursorAdapter(this, null, true);
        listView.setAdapter(petCursorAdapter);

        getLoaderManager().initLoader(0, null, this);
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
                if (uri != null) {
                    Toast.makeText(this, "Dummy pet data inserted", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.delete_all_pet:
                int totalRowDeleted = getContentResolver().delete(PetEntry.CONTENT_URI, null, null);
                if (totalRowDeleted > 0) {
                    pets.clear();

                    Toast.makeText(this, "All pet data deleted", Toast.LENGTH_SHORT).show();
                }
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
//        petRV = (RecyclerView) findViewById(R.id.petRecyclerView);
        listView = (ListView) findViewById(R.id.listView);
        emptyView = (RelativeLayout) findViewById(R.id.emptyView);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d("LOADER_WAY", "onCreateLoader");
        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
        };

        return new CursorLoader(
                this,
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d("LOADER_WAY", "onLoadFinished");
        petCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d("LOADER_WAY", "onLoaderReset");
        petCursorAdapter.swapCursor(null);
    }
}
