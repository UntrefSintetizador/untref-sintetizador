/**
 *
 * @author Peter Brinkmann (peter.brinkmann@gmail.com)
 *
 * For information on usage and redistribution, and for a DISCLAIMER OF ALL
 * WARRANTIES, see the file, "LICENSE.txt," in this distribution.
 *
 * simple test case for {@link PdService}
 *
 */
package com.example.ddavi.prueba;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.support.v4.app.FragmentTabHost;

import com.evilduck.piano.views.instrument.PianoView;
import com.example.ddavi.prueba.ModulesPopupWindow.EGPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.MIXPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.SHPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.VCAPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.VCFPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.VCOPopupWindow;
import com.example.ddavi.prueba.MyGridView.GridViewCustomAdapter;
import com.example.ddavi.prueba.Tabs.TabMatriz;
import com.example.ddavi.prueba.Tabs.TabPiano;

import org.puredata.core.PdBase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnEditorActionListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentTabHost tabHost;

    private static final int MIN_OCTIVE = 5;
    private static final int MAX_OCTIVE = 8;

    GridViewCustomAdapter gridViewAdapter;

    //Defino ventanas de sliders
    VCOPopupWindow vco1Window;
    VCOPopupWindow vco2Window;
    VCOPopupWindow vco3Window;
    VCAPopupWindow vca1Window;
    VCAPopupWindow vca2Window;
    MIXPopupWindow mixWindow;
    VCFPopupWindow vcf1Window;
    VCFPopupWindow vcf2Window;
    EGPopupWindow eg1Window;
    EGPopupWindow eg2Window;
    SHPopupWindow shWindow;

    private EditText msg;
    PureDataConfig pdConfig;
    private int octava;

    public int getOctava(){
        return octava;
    }

    public VCOPopupWindow getVco1Window(){
        return vco1Window;
    }
    public VCOPopupWindow getVco2Window(){
        return vco2Window;
    }
    public VCOPopupWindow getVco3Window(){
        return vco3Window;
    }
    public VCAPopupWindow getVca1Window(){
        return vca1Window;
    }
    public VCAPopupWindow getVca2Window(){
        return vca2Window;
    }
    public VCFPopupWindow getVcf1Window(){
        return vcf1Window;
    }
    public VCFPopupWindow getVcf2Window(){
        return vcf2Window;
    }
    public MIXPopupWindow getMixWindow(){
        return mixWindow;
    }
    public EGPopupWindow getEg1Window(){
        return eg1Window;
    }
    public EGPopupWindow getEg2Window(){
        return eg2Window;
    }
    public SHPopupWindow getShWindow(){
        return shWindow;
    }

    public void setOctava(int valor){
        this.octava = valor;
    }

    private void createTabs(){

        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Piano"),
                TabPiano.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Matriz"),
                TabMatriz.class, null);

        //cambio altura de tabs
        for (int i = 0; i < tabHost.getTabWidget().getTabCount(); i++) {
            tabHost.getTabWidget().getChildAt(i).getLayoutParams().height = 60;
            tabHost.getTabWidget().setBackgroundColor(Color.WHITE);
        }
    }

    private void initialConfigurationWindow(){

        //Esto metodo debe llamarse OBLIGATORIAMENTE antes seterar el XML asociado al activity

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initializeActionBar(){
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setIcon(R.drawable.icon);
    }

    /*
    *inicializo la ventanas de presets
    */
    private void initializeModulesPopWindow(){
        vco1Window = new VCOPopupWindow(this,R.layout.popup_vco,"VCO1");
        vco2Window = new VCOPopupWindow(this,R.layout.popup_vco,"VCO2");
        vco3Window = new VCOPopupWindow(this,R.layout.popup_vco,"VCO3");
        vca1Window = new VCAPopupWindow(this,R.layout.popup_vca,"VCA1");
        vca2Window = new VCAPopupWindow(this,R.layout.popup_vca,"VCA2");
        mixWindow = new MIXPopupWindow(this,R.layout.popup_mix,"MIX");
        vcf1Window = new VCFPopupWindow(this,R.layout.popup_vcf,"VCF1");
        vcf2Window = new VCFPopupWindow(this,R.layout.popup_vcf,"VCF2");
        eg1Window = new EGPopupWindow(this,R.layout.popup_eg,"EG1");
        eg2Window = new EGPopupWindow(this,R.layout.popup_eg,"EG1");
        shWindow = new SHPopupWindow(this,R.layout.popup_sh,"S\u0026H");
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("preferencias", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("labelOctava", "5");
        editor.commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("octava", "5");
    }

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialConfigurationWindow();
        setContentView(R.layout.tabs_view);
        initializeActionBar();
        pdConfig = new PureDataConfig(this);
        initializeGridViewAdapter();
        initializeModulesPopWindow();
        createTabs();
        setOctava(5);
    }

    public GridViewCustomAdapter getGridViewAdapter(){
        return gridViewAdapter;
    }

    private void initializeGridViewAdapter() {
        int CantColumnas = 17; //SIN LOS TITULOS CANTIDAD ERA 12 (CON PD TEST5 ERA 13, ahora 17 porque piden 16)
        int CantFilas = 27; // SIN LOS TITULOS CANTIDAD ERA 23 (CON PD TEST5 ERA 24, ahora 27 porque piden 26)
        ArrayList<String> data = new ArrayList<>();

        for (int i = -1; i < CantFilas - 1; i++) {
            for (int j = -1; j < CantColumnas - 1; j++) {
                //PONGO NOMBRES A PRIMERA COLUMNA
                if (j == -1 & i == -1) {
                    data.add("");
                } else if (j == -1)
                    /*Pongo nombres a filas*/
                    data.add(String.valueOf(i));
                else if (i == -1)
                    /*Pongo nombre a columnas*/
                    data.add(String.valueOf(j));
                else
                    data.add(j + "-" + i);
            }
        }
        gridViewAdapter = new GridViewCustomAdapter(this, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    /*
    * Presets en menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                pdConfig.stopAudio();
                pdConfig.cleanup();
                finish();
                break;
            case R.id.action_clear:
                PianoView piano = (PianoView) findViewById(R.id.pianito);
                piano.clear();
                break;
            case R.id.action_chordPad:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("chord_pad",1);
                Log.i("Preset Elegido", "Preset 1 (Chord Pad)");
                break;

            case R.id.action_filterTone:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("filter_tone",1);
                Log.i("Preset Elegido", "Preset 2 (Filter Tone)");
                break;

            case R.id.action_herbie:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("herbie",1);
                Log.i("Preset Elegido", "Preset 3 (Herbie)");
                break;

            case R.id.action_extasis:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("extasis",1);
                Log.i("Preset Elegido", "Preset 4 (Extasis)");
                break;

            case R.id.action_reset:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("sq_bass1",1);
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;

            case R.id.action_sawSeq:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("saw_seq",1);
                Log.i("Preset Elegido", "Preset 6 (Saw Seq)");
                break;

            case R.id.action_bell:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("bell",1);
                Log.i("Preset Elegido", "Preset 7 (Bell)");
                break;

            case R.id.action_sqBass:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("sq_bass",1);
                Log.i("Preset Elegido", "Preset 8 (Sq Bass)");
                break;

            case R.id.action_sqBass1:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("sq_bass1",1);
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;
            case R.id.action_fm:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("fm",1);
                Log.i("Preset Elegido", "Preset 10 (Fm)");
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickMenosOctava(View v) {

        if (octava > MIN_OCTIVE) {
            octava--;

            PianoView piano = (PianoView) findViewById(R.id.pianito);
            piano.setINITIAL_OCTIVE(octava);

            TextView label = (TextView) findViewById(R.id.labelOctava);
            label.setText(String.valueOf(octava));
        }
    }

    public void onClickMasOctava(View v) {

        if (octava < MAX_OCTIVE) {
            octava++;

            PianoView piano = (PianoView) findViewById(R.id.pianito);
            piano.setINITIAL_OCTIVE(octava);

            TextView label = (TextView) findViewById(R.id.labelOctava);
            label.setText(String.valueOf(octava));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pdConfig.cleanup();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (pdConfig.getService().isRunning()) {
            pdConfig.startAudio();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        pdConfig.evaluateMessage(msg.getText().toString());
        return true;
    }

}