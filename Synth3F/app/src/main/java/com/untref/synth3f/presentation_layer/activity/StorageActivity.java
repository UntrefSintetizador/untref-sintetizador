package com.untref.synth3f.presentation_layer.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;

import java.util.List;

import static com.untref.synth3f.domain_layer.serializers.FileManager.getFilenameList;

public class StorageActivity extends AppCompatActivity {

    private boolean closeApp;
    private boolean saveMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_activity);
        populateList();
        setMode(getIntent().getIntExtra("mode", 0));
        closeApp = getIntent().getBooleanExtra("closeApp", false);
        createClickEvents();
    }

    private void populateList() {
        List<String> list = getFilenameList(getBaseContext());
        ListView listview = findViewById(R.id.filenameList);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }

    private void setMode(int mode) {
        saveMode = mode == PatchGraphFragment.REQUEST_SAVE;
        TextView inputFileNameView = findViewById(R.id.inputFilename);
        inputFileNameView.setVisibility(saveMode ? View.VISIBLE : View.INVISIBLE);
        inputFileNameView.setHint(R.string.save_hint);
        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setText(getString(saveMode ? R.string.dialog_save : R.string.dialog_load));
    }

    private void createClickEvents() {
        ((ListView) findViewById(R.id.filenameList)).setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((EditText) findViewById(R.id.inputFilename)).setText(((TextView) view).getText());
                    }
                });
        findViewById(R.id.buttonSave).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String filename = ((EditText) findViewById(R.id.inputFilename)).getText().toString();
                        List<String> filenameList = getFilenameList(getBaseContext());
                        if (saveMode && filenameList.contains(filename)) {
                            handleFileOverwriting(filename);
                        } else {
                            returnToFragment(filename);
                        }
                    }
                });
        findViewById(R.id.buttonCancel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent returnIntent = new Intent();

                        setResult(PatchGraphFragment.RESULT_CANCEL, returnIntent);
                        finish();
                    }
                });
    }

    private void handleFileOverwriting(final String filename) {
        int dialogStyle = R.style.Theme_AppCompat_Dialog_Alert;
        AlertDialog dialog = new AlertDialog.Builder(this, dialogStyle).create();
        dialog.setTitle(getString(R.string.overwrite_dialog_title));
        dialog.setMessage(getString(R.string.overwrite_dialog_message));
        dialog.setButton(android.app.AlertDialog.BUTTON_NEGATIVE,
                getString(R.string.dialog_cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        dialog.setButton(android.app.AlertDialog.BUTTON_POSITIVE,
                getString(R.string.dialog_accept),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        returnToFragment(filename);
                    }
                });
        dialog.show();
    }

    private void returnToFragment(String filename) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("filename", filename);
        returnIntent.putExtra("closeApp", closeApp);

        setResult(PatchGraphFragment.RESULT_OK, returnIntent);
        finish();
    }
}
