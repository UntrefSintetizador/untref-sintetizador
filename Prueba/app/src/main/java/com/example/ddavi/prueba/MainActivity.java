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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.evilduck.piano.views.instrument.PianoView;
import com.example.ddavi.prueba.Listeners.ModuleListener;
import com.example.ddavi.prueba.ModulesPopupWindow.EGPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.MIXPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.ModulePopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.SHPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.VCAPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.VCFPopupWindow;
import com.example.ddavi.prueba.ModulesPopupWindow.VCOPopupWindow;
import com.example.ddavi.prueba.MyGridView.GridViewCustomAdapter;
import com.example.ddavi.prueba.Tabs.TabMatriz;
import com.example.ddavi.prueba.Tabs.TabPiano;

import org.puredata.core.PdBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

//Import Sinte Adapter

public class MainActivity extends AppCompatActivity implements OnEditorActionListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentTabHost tabHost;

    private static final int MIN_OCTIVE = 5;
    private static final int MAX_OCTIVE = 8;

    GridViewCustomAdapter gridViewAdapter;
    private static LayoutInflater inflater = null;
    Map<String,Button> modulos_matriz;

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
    //PureDataConfig pdConfig;

    MasterConfig masterConfig;
    private String processor = "Pd"; //Asca se cambiaria por el tipo de processor utilizado

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

        //Consultar por el literal Pd se podria hacer algo en un metodo anterior
        masterConfig = new MasterConfig(this.processor);
        masterConfig.setProcessorActivity(this);

        //pdConfig = new PureDataConfig(this);

        //initializeGridViewAdapter();
        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        modulos_matriz = new HashMap<>();
        gridViewAdapter = new GridViewCustomAdapter(this, modulos_matriz);

        //initializeModulesPopWindow();
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
        //gridViewAdapter = new GridViewCustomAdapter(this, data);
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

                masterConfig.config.stopAudio();
                masterConfig.config.cleanup();

                //pdConfig.stopAudio();
                //pdConfig.cleanup();
                finish();
                break;
            case R.id.action_clear:
                PianoView piano = (PianoView) findViewById(R.id.pianito);
                piano.clear();
                break;
            case R.id.action_chordPad:
                masterConfig.resetPresets();
                masterConfig.setPreset("chord_pad");

                //PdBase.sendFloat("reset_presets",1);
                //PdBase.sendFloat("chord_pad",1);
                Log.i("Preset Elegido", "Preset 1 (Chord Pad)");
                break;

            case R.id.action_filterTone:
                masterConfig.resetPresets();
                masterConfig.setPreset("filter_tone");
                //PdBase.sendFloat("reset_presets",1);
                //PdBase.sendFloat("filter_tone",1);
                Log.i("Preset Elegido", "Preset 2 (Filter Tone)");
                break;

            case R.id.action_herbie:
                masterConfig.resetPresets();
                masterConfig.setPreset("herbie");
                PdBase.sendFloat("reset_presets",1);
                //PdBase.sendFloat("herbie",1);
                Log.i("Preset Elegido", "Preset 3 (Herbie)");
                break;

            case R.id.action_extasis:
                masterConfig.resetPresets();
                masterConfig.setPreset("extasis");
                //PdBase.sendFloat("reset_presets",1);
                //PdBase.sendFloat("extasis",1);
                Log.i("Preset Elegido", "Preset 4 (Extasis)");
                break;

            case R.id.action_reset:
                masterConfig.resetPresets();
                masterConfig.setPreset("sq_bass1");
                //PdBase.sendFloat("reset_presets",1);
                //PdBase.sendFloat("sq_bass1",1);
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;

            case R.id.action_sawSeq:
                masterConfig.resetPresets();
                masterConfig.setPreset("saw_seq");
                //PdBase.sendFloat("reset_presets",1);
                //PdBase.sendFloat("saw_seq",1);
                Log.i("Preset Elegido", "Preset 6 (Saw Seq)");
                break;

            case R.id.action_bell:
                masterConfig.resetPresets();
                masterConfig.setPreset("bell");
                //PdBase.sendFloat("reset_presets",1);
                //PdBase.sendFloat("bell",1);
                Log.i("Preset Elegido", "Preset 7 (Bell)");
                break;

            case R.id.action_sqBass:
                masterConfig.resetPresets();
                masterConfig.setPreset("sq_bass");
                //PdBase.sendFloat("reset_presets",1);
                //PdBase.sendFloat("sq_bass",1);
                Log.i("Preset Elegido", "Preset 8 (Sq Bass)");
                break;

            case R.id.action_sqBass1:
                masterConfig.resetPresets();
                masterConfig.setPreset("sq_bass1");
                //PdBase.sendFloat("reset_presets",1);
                //PdBase.sendFloat("sq_bass1",1);
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;
            case R.id.action_fm:
                masterConfig.resetPresets();
                masterConfig.setPreset("fm");
                //PdBase.sendFloat("reset_presets",1);
                //PdBase.sendFloat("fm",1);
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

    private Button createButtonBasic(ModulePopupWindow popup, String name_module) {
        View view = null;
        view = inflater.inflate(R.layout.item, null);
        Button tv = (Button) view.findViewById(R.id.button);
        tv.setText(name_module);
        popup.setButton(tv);
        tv.setOnClickListener(new ModuleListener(popup,gridViewAdapter));
        return tv;
    }

    private Button createbtnVCO(String name){
        VCOPopupWindow popup =  new VCOPopupWindow(this, R.layout.popup_vco,name);
        return createButtonBasic(popup,name);
    }

    private Button createbtnVCA(String name){
        VCAPopupWindow popup =  new VCAPopupWindow(this, R.layout.popup_vca,name);
        return createButtonBasic(popup,name);
    }

    private Button createbtnVCF(String name){
        VCFPopupWindow popup =  new VCFPopupWindow(this, R.layout.popup_vcf,name);
        return createButtonBasic(popup,name);
    }

    private Button createbtnEG(String name){
        EGPopupWindow popup =  new EGPopupWindow(this, R.layout.popup_eg,name);
        return createButtonBasic(popup,name);
    }

    private Button createbtnSH(String name){
        SHPopupWindow popup =  new SHPopupWindow(this, R.layout.popup_sh,name);
        return createButtonBasic(popup,name);
    }

    private Button createbtnMIX(String name){
        MIXPopupWindow popup =  new MIXPopupWindow(this, R.layout.popup_mix,name);
        return createButtonBasic(popup,name);
    }

    private Button createButtonOut(String name_button,final String id_msg){
        View view = null;
        view = inflater.inflate(R.layout.item, null);
        final Button tv = (Button) view.findViewById(R.id.button);
        tv.setText(name_button);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "connect-"+ id_msg;
                if (tv.getCurrentTextColor() == Color.BLACK) {
                    Float value = 1.0f;
                    PdBase.sendFloat(msg, value);
                    tv.setBackgroundColor(Color.parseColor("#FF9800"));
                    Log.i("MSJ A PD ", msg);
                    tv.setTextColor(Color.WHITE);
                    tv.setWidth(80);
                    gridViewAdapter.getItemsPressed().add(tv);

                } else {
                    Float value = 0.0f;
                    PdBase.sendFloat(msg, value);
                    tv.setBackgroundColor(Color.parseColor("#607D8B"));
                    Log.i("MSJ A PD ", msg);
                    tv.setTextColor(Color.BLACK);
                    tv.setWidth(81);
                    gridViewAdapter.getItemsPressed().remove(gridViewAdapter.getItemsPressed().indexOf(tv));
                }
            }
        });

        return tv;
    }

    private Button createButtonModule(String name, String name_button, String id_msg){
        Button button = null;

        switch (name) {
            case "VCO": button = createbtnVCO(name_button); break;
            case "VCA": button = createbtnVCA(name_button); break;
            case "VCF": button = createbtnVCF(name_button); break;
            case "EG":  button = createbtnEG(name_button); break;
            case "SH":  button = createbtnSH(name_button); break;
            case "MIX": button = createbtnMIX(name_button); break;
            case "out": button = createButtonOut(name_button, id_msg); break;
        }

        return button;
    }

    private void actionToCheckBoxModule(int id_check, int id_cant_modulos, String name_module){
        CheckBox c_button = (CheckBox) findViewById(id_check);
        EditText cant_module = (EditText) findViewById(id_cant_modulos);
        String c_cant = cant_module.getText().toString();
        int cant = (c_cant.isEmpty())?0:Integer.parseInt(c_cant);
        int i;
        String name,id_msg;

        if (c_button.isChecked() && cant > 0) {
            for (i=0; i< cant; i++) {
                name = name_module +"_"+ String.valueOf(i);
                id_msg = "0_"+ String.valueOf(i);
                modulos_matriz.put(name, createButtonModule(name_module,name,id_msg));
            }

        }else if(!modulos_matriz.isEmpty()) {
            for (i=0; i< cant; i++) {
                name = name_module + String.valueOf(i+1);
                modulos_matriz.remove(name);
            }
        }
    }

    public void onClickRadioButtonVCO(View v) {
        String name = "VCO";
        actionToCheckBoxModule(R.id.option_VCO, R.id.cant_modulos_VCO, name);
    }

    public void onClickRadioButtonVCA(View v) {
        String name = "VCA";
        actionToCheckBoxModule(R.id.option_VCA, R.id.cant_modulos_VCA, name);
    }

    public void onClickRadioButtonVCF(View v) {
        String name = "VCF";
        actionToCheckBoxModule(R.id.option_VCF, R.id.cant_modulos_VCF, name);
    }

    public void onClickRadioButtonSH(View v) {
        String name = "SH";
        actionToCheckBoxModule(R.id.option_SH, R.id.cant_modulos_SH, name);
    }

    public void onClickRadioButtonEG(View v) {
        String name = "EG";
        actionToCheckBoxModule(R.id.option_EG, R.id.cant_modulos_EG, name);
    }

    public void onClickRadioButtonMIX(View v) {
        String name = "MIX";
        actionToCheckBoxModule(R.id.option_MIX, R.id.cant_modulos_MIX, name);
    }

    public void onClickAccept(View v){
        tabHost.getCurrentTabTag();
        TabMatriz tab = ((TabMatriz)getSupportFragmentManager().findFragmentByTag("tab2"));

        modulos_matriz.put("out0",createButtonModule("out","out0","0-25"));
        modulos_matriz.put("out1",createButtonModule("out","out1","1-25"));
        modulos_matriz.put("out2",createButtonModule("out","out2","2-25"));

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(tab);
        ft.attach(tab);
        ft.commit();
    }



    public Map<String,Button> getButtonsModulesMatriz(){
        return modulos_matriz;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        masterConfig.config.cleanup();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (masterConfig.config.isServiceRunning()) {
            masterConfig.config.startAudio();
        }
        /*
        if (pdConfig.getService().isRunning()) {
            pdConfig.startAudio();
        }*/
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        masterConfig.config.evaluateMessage(msg.getText().toString());
        return true;
    }

}