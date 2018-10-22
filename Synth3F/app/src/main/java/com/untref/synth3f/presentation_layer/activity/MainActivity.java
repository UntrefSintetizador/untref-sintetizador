package com.untref.synth3f.presentation_layer.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;

import com.untref.synth3f.R;
import com.untref.synth3f.domain_layer.helpers.ConfigFactory;
import com.untref.synth3f.domain_layer.helpers.IConfig;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    //private MainActivityPresenter presenter;
    private IConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //presenter = new MainActivityPresenter(this);

        FragmentManager fmanager = getFragmentManager();
        FragmentTransaction transaction = fmanager.beginTransaction();
        //transaction.add(R.id.container_matriz, new FragmentMatriz());
        //transaction.add(R.id.container_piano, new FragmentOrgano());
        PatchGraphFragment patchGraphFragment = new PatchGraphFragment();
        transaction.add(R.id.graph, patchGraphFragment);
        transaction.commit();

        config = ConfigFactory.create("PureData"); //this.presenter.initializeMasterConfig();
        config.setContext(this);
        patchGraphFragment.setProcessor(config.getProcessor());
        patchGraphFragment.setContext(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (config.isServiceRunning()) {
            config.startAudio(); //presenter.startService();
        }
        /*
        if (pdConfig.getService().isRunning()) {
            pdConfig.startAudio();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                config.stopAudio();
                config.cleanup();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        config.cleanup();
    }

}
