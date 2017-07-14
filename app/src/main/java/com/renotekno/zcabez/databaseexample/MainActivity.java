package com.renotekno.zcabez.databaseexample;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.renotekno.zcabez.databaseexample.data.PetContract.PetEntry;
import com.renotekno.zcabez.databaseexample.data.PetDbHelper;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton addNewPetFAB;
    private TextView testDbConnection;
    private PetDbHelper petDbHelper;
    private SQLiteDatabase writeAbleDB = null;
    private SQLiteDatabase readAbleDB = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

        petDbHelper = new PetDbHelper(this);

        addNewPetFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


//        SQLiteDatabase petDB = petDbHelper.getReadableDatabase();
         checkDatabaseConnection();
    }

    private void checkDatabaseConnection() {
        Cursor c = getReadAbleDB().rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);
        try {
            testDbConnection.setText( "Row count on database: "+ c.getCount());
        } finally {
            c.close();
        }
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

                long id = getWriteAbleDB().insert(PetEntry.TABLE_NAME, null, contentValues);
                checkDatabaseConnection();

                break;
            case R.id.delete_all_pet:
                Toast.makeText(this, "Action menu 2 clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private SQLiteDatabase getWriteAbleDB() {
        if (writeAbleDB != null && writeAbleDB.isOpen()) {
            return writeAbleDB;
        }
        writeAbleDB = petDbHelper.getWritableDatabase();
        return writeAbleDB;
    }

    private SQLiteDatabase getReadAbleDB() {
        if (readAbleDB != null && readAbleDB.isOpen()) {
            return readAbleDB;
        }
        readAbleDB = petDbHelper.getReadableDatabase();
        return readAbleDB;
    }

    private void initView() {
        testDbConnection = (TextView) findViewById(R.id.database_connection);
        addNewPetFAB = (FloatingActionButton) findViewById(R.id.addNewPetFAB);
    }
}
