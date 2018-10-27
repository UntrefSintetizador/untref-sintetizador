package com.untref.synth3f.presentation_layer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.serializers.JSONSerializer;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class StorageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.storage_activity);
        populateList();
        setMode(getIntent().getIntExtra("mode", 0));
        createClickEvents();
    }

    private void populateList() {
        File filesDir = new File(getBaseContext().getFilesDir(), JSONSerializer.FILE_FOLDER);
        filesDir.mkdirs();
        String name;
        List<String> list = new ArrayList<>();
        for (File file : filesDir.listFiles()) {
            if (file.isFile()) {
                name = file.getName();
                list.add(name);
            }
        }
        ListView listview = findViewById(R.id.filenameList);
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);
    }

    private void setMode(int mode) {
        if (mode == PatchGraphFragment.REQUEST_LOAD) {
            findViewById(R.id.inputFilename).setEnabled(false);
        }
    }

    private void createClickEvents() {
        ((ListView) findViewById(R.id.filenameList)).setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ((EditText) findViewById(R.id.inputFilename)).setText(((TextView) view).getText());
                    }
                });
        findViewById(R.id.buttonOK).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent returnIntent = new Intent();
                        String filename = ((EditText) findViewById(R.id.inputFilename)).getText().toString();
                        returnIntent.putExtra("filename", filename);

                        setResult(PatchGraphFragment.RESULT_OK, returnIntent);
                        finish();
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
}
