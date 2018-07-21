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
import com.untref.synth3f.presentation_layer.fragment.FragmentMatriz;
import com.untref.synth3f.presentation_layer.fragment.FragmentOrgano;
import com.untref.synth3f.presentation_layer.fragment.PatchGraphFragment;
import com.untref.synth3f.presentation_layer.presenters.MainActivityPresenter;

public class MainActivity extends AppCompatActivity implements ActionBar.OnNavigationListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private MainActivityPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this.initializeActionBar();

        presenter = new MainActivityPresenter(this);

        FragmentManager fmanager = getFragmentManager();
        FragmentTransaction transaction = fmanager.beginTransaction();
        //transaction.add(R.id.container_matriz, new FragmentMatriz());
        //transaction.add(R.id.container_piano, new FragmentOrgano());
        PatchGraphFragment patchGraphFragment = new PatchGraphFragment();
        transaction.add(R.id.graph, patchGraphFragment);
        transaction.commit();

        this.presenter.initializeMasterConfig();
        this.initializeNavigationDrawer();
        patchGraphFragment.setProcessor(this.presenter.getProcessor());
        patchGraphFragment.setContext(this);
    }

    private void initializeNavigationDrawer() {
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final NavigationView navView = (NavigationView) findViewById(R.id.navview);

        Menu menu = navView.getMenu();
        MenuItem modulos = menu.findItem(R.id.menu_seccion_sliders);
        SpannableString s = new SpannableString(modulos.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.MenuTextTheme), 0, s.length(), 0);
        modulos.setTitle(s);

        navView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        presenter.onClickSelected(navView, menuItem.getTitle().toString());
                        drawerLayout.closeDrawers();

                        return true;
                    }
                });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        presenter.startService();
        /*
        if (pdConfig.getService().isRunning()) {
            pdConfig.startAudio();
        }*/
    }

    private void initializeActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        //actionBar.setIcon(R.drawable.icon);

        //crear el spinner
        SpinnerAdapter adapter = ArrayAdapter.createFromResource(this, R.array.Presets, R.layout.item_spinner);
        actionBar.setListNavigationCallbacks(adapter, this);

        //mostrar el spinner
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

    }

    @Override
    public boolean onNavigationItemSelected(int i, long l) {
        presenter.resetPresets();
        switch (i) {
            case 0:
                presenter.setPreset("sq_bass1");
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;

            case 1:
                presenter.setPreset("filter_tone");
                Log.i("Preset Elegido", "Preset 2 (Filter Tone)");
                break;

            case 2:
                presenter.setPreset("herbie");
                Log.i("Preset Elegido", "Preset 3 (Herbie)");
                break;

            case 3:
                presenter.setPreset("extasis");
                Log.i("Preset Elegido", "Preset 4 (Extasis)");
                break;

            case 4:
                presenter.setPreset("fm");
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;

            case 5:
                presenter.setPreset("saw_seq");
                Log.i("Preset Elegido", "Preset 6 (Saw Seq)");
                break;

            case 6:
                presenter.setPreset("bell");
                Log.i("Preset Elegido", "Preset 7 (Bell)");
                break;

            case 7:
                presenter.setPreset("sq_bass");
                Log.i("Preset Elegido", "Preset 8 (Sq Bass)");
                break;

            case 8:
                presenter.setPreset("chord_pad");
                Log.i("Preset Elegido", "Preset 1 (Chord Pad)");
                break;

            default:
                presenter.setPreset("sq_bass1");
                Log.i("Preset Elegido", "Preset 1 (Chord Pad)");
                break;

        }
        return false;
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
                presenter.stopService();
                presenter.unBindService();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.unBindService();
    }

}
