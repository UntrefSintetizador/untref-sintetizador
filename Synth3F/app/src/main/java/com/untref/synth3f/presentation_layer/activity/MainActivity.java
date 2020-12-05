package com.untref.synth3f.presentation_layer.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.helpers.Config;
import com.untref.synth3f.domain_layer.helpers.ConfigFactory;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Config config;
    private PatchGraphFragment patchGraphFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        patchGraphFragment = new PatchGraphFragment();
        transaction.add(R.id.graph, patchGraphFragment);
        transaction.commit();
        config = ConfigFactory.create();
        config.setContext(this);
        patchGraphFragment.setProcessor(config.getProcessor());
        patchGraphFragment.setContext(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        View decorView = getWindow().getDecorView();
//        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (config.isServiceRunning()) {
            config.startAudio();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_exit) {
            config.stopAudio();
            config.cleanup();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        config.cleanup();
    }

    @Override
    public void onBackPressed() {
        patchGraphFragment.handleBackPressedCallback();
    }
}
