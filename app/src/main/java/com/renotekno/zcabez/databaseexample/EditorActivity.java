package com.renotekno.zcabez.databaseexample;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.renotekno.zcabez.databaseexample.data.DBConnection;
import com.renotekno.zcabez.databaseexample.data.PetContract;

import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity {

    private EditText editPetName;
    private EditText editPetBreed;
    private Spinner spinnerGender;
    private EditText editPetWeight;
    private ArrayList<EditText> editTexts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initView();

        editTexts.add(editPetBreed);
        editTexts.add(editPetName);
        editTexts.add(editPetWeight);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.finishAddBtn) {
            boolean isValid = true;

            for (EditText editText : editTexts) {
                if (editText.getText().toString().trim().equals("")) {
                    isValid = false;
                }
            }

            if (isValid) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(PetContract.PetEntry.COLUMN_PET_NAME, getEditTextString(editPetName));
                contentValues.put(PetContract.PetEntry.COLUMN_PET_BREED, getEditTextString(editPetBreed));
                contentValues.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, getEditTextString(editPetWeight));

                String breed = getGender();
                int petGender = 0;
                if ( breed.equals(getString(R.string.gender_unknown))) {
                    petGender = 0;
                } else if (breed.equals(getString(R.string.gender_male))) {
                    petGender = 1;
                } else if (breed.equals(getString(R.string.gender_female))) {
                    petGender = 2;
                }
                contentValues.put(PetContract.PetEntry.COLUMN_PET_GENDER, petGender);

                Uri uri = getContentResolver().insert(PetContract.PetEntry.CONTENT_URI, contentValues);

                if (uri != null) {
                    finish();
                }
            } else {
                Toast.makeText(this, "Please fill in the blank!", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private String getEditTextString(EditText editText) {
        return editText.getText().toString();
    }

    private String getGender(){
        return spinnerGender.getSelectedItem().toString();
    }

    private void initView() {
        editPetName = (EditText) findViewById(R.id.edit_pet_name);
        editPetBreed = (EditText) findViewById(R.id.edit_pet_breed);
        spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
        editPetWeight = (EditText) findViewById(R.id.edit_pet_weight);
    }
}
