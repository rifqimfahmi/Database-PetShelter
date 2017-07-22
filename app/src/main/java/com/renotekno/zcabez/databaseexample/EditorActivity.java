package com.renotekno.zcabez.databaseexample;

import android.app.LoaderManager;
import android.content.*;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.renotekno.zcabez.databaseexample.data.PetContract;
import com.renotekno.zcabez.databaseexample.data.PetContract.PetEntry;

import java.util.ArrayList;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private EditText editPetName;
    private EditText editPetBreed;
    private Spinner spinnerGender;
    private EditText editPetWeight;
    private ArrayList<EditText> editTexts = new ArrayList<>();
    private Uri mUriToEdit;

    private boolean isEdited = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            isEdited = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        initView();

        Intent intent = getIntent();
        mUriToEdit = intent.getData();

        if (mUriToEdit != null) {
            setTitle("Edit Pet  ");
            getLoaderManager().initLoader(1, null, this);
        } else {
            invalidateOptionsMenu();
        }

        editTexts.add(editPetBreed);
        editTexts.add(editPetName);
        editTexts.add(editPetWeight);

        editPetName.setOnTouchListener(touchListener);
        editPetBreed.setOnTouchListener(touchListener);
        editPetWeight.setOnTouchListener(touchListener);
        spinnerGender.setOnTouchListener(touchListener);
    }

    @Override
    public void onBackPressed() {
        if (!isEdited) {
            super.onBackPressed();
            return;
        }

        DialogInterface.OnClickListener discardListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        };

        showUnsavedChangesDialog(discardListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (mUriToEdit == null) {
            MenuItem menuItem = menu.findItem(R.id.deletePetBtn);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemID = item.getItemId();

        switch (itemID) {
            case R.id.finishAddBtn:
                savePet();
                return true;
            case R.id.deletePetBtn:
                deletePet();
                return true;
            case android.R.id.home:
                if (!isEdited) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };

                showUnsavedChangesDialog(discardListener);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(
                EditorActivity.this,
                mUriToEdit,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToNext()){
            editPetName.setText(data.getString(data.getColumnIndex(PetEntry.COLUMN_PET_NAME)));
            editPetBreed.setText(data.getString(data.getColumnIndex(PetEntry.COLUMN_PET_BREED)));
            editPetWeight.setText(data.getString(data.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT)));

            spinnerGender.setSelection(data.getInt(data.getColumnIndex(PetEntry.COLUMN_PET_GENDER)));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        // If the loader is invalidated, clear out all the data from the input fields.
        editPetBreed.setText("");
        editPetWeight.setText("");
        editPetName.setText("");
        spinnerGender.setSelection(0); // Select "Unknown" gender
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard Your changes and quit editing?");
        builder.setPositiveButton("DISCARD", discardButtonClickListener);
        builder.setNegativeButton("KEEP EDITING", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deletePet() {
        int rowDeleted = getContentResolver().delete(mUriToEdit, null, null);
        if (rowDeleted > 0) {
            finish();
        } else {
            Toast.makeText(this, "Error deleting pet", Toast.LENGTH_SHORT).show();
        }
    }

    private void savePet() {
        // Add new pet
        if (mUriToEdit == null) {
            addNewPetToDB();
        } else {
            // Update pet
            updatePet();
        }
    }

    private void updatePet() {
        boolean isValid = true;

        isValid = verifyEmptyValue(isValid);

        if (isValid) {
            int totalRowsUpdated = getContentResolver().update(mUriToEdit, putDataToContentValues(), null, null);

            if (totalRowsUpdated > 0) {
                finish();
            }
        } else {
            Toast.makeText(this, "Please fill in the blank!", Toast.LENGTH_SHORT).show();
        }
    }

    private void addNewPetToDB() {
        boolean isValid = true;

        isValid = verifyEmptyValue(isValid);

        if (isValid) {
            Uri uri = getContentResolver().insert(PetEntry.CONTENT_URI, putDataToContentValues());

            if (uri != null) {
                finish();
            }
        } else {
            Toast.makeText(this, "Please fill in the blank!", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verifyEmptyValue(boolean isValid) {
        for (EditText editText : editTexts) {
            if (editText.getText().toString().trim().equals("")) {
                isValid = false;
            }
        }
        return isValid;
    }

    private ContentValues putDataToContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PetEntry.COLUMN_PET_NAME, getEditTextString(editPetName));
        contentValues.put(PetEntry.COLUMN_PET_BREED, getEditTextString(editPetBreed));
        contentValues.put(PetEntry.COLUMN_PET_WEIGHT, getEditTextString(editPetWeight));

        String breed = getGender();
        int petGender = 0;
        if (breed.equals(getString(R.string.gender_unknown))) {
            petGender = 0;
        } else if (breed.equals(getString(R.string.gender_male))) {
            petGender = 1;
        } else if (breed.equals(getString(R.string.gender_female))) {
            petGender = 2;
        }
        contentValues.put(PetEntry.COLUMN_PET_GENDER, petGender);

        return contentValues;
    }

    private String getEditTextString(EditText editText) {
        return editText.getText().toString();
    }

    private String getGender() {
        return spinnerGender.getSelectedItem().toString();
    }

    private void initView() {
        editPetName = (EditText) findViewById(R.id.edit_pet_name);
        editPetBreed = (EditText) findViewById(R.id.edit_pet_breed);
        spinnerGender = (Spinner) findViewById(R.id.spinner_gender);
        editPetWeight = (EditText) findViewById(R.id.edit_pet_weight);
    }
}
