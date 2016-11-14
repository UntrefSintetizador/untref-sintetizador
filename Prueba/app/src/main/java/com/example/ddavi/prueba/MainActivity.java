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
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.puredata.core.PdBase;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
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

    private EditText msg;
    //PureDataConfig pdConfig;

    public MasterConfig masterConfig;
    private String processor; //Aca se cambiaria por el tipo de processor utilizado
    private JSONObject configJson;
    private JSONArray processor_list;
    private int octava;

    public int getOctava(){
        return octava;
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


    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initialConfigurationWindow();
        setContentView(R.layout.tabs_view);
        initializeActionBar();

        //Consultar por el literal Pd se podria hacer algo en un metodo anterior
        try {
            this.configJson = new JSONObject(this.loadConfigFile());
            //this.processor_list = this.configJson.getJSONArray("processors");
            this.processor = this.configJson.getString("processor");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        masterConfig = new MasterConfig(this.configJson);
        masterConfig.setProcessorActivity(this);

        //pdConfig = new PureDataConfig(this);

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        modulos_matriz = new HashMap<>();
        gridViewAdapter = new GridViewCustomAdapter(this, modulos_matriz);
        createTabs();

        setOctava(5);
    }

    public GridViewCustomAdapter getGridViewAdapter(){
        return gridViewAdapter;
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
                //PdBase.sendFloat("reset_presets",1);
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

    /*************************************************************************************************
     *
     * Construccion de modulos de la matriz
     *
     **********************************************************************************************/
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

    private Button createButtonOut(String name_button){
        View view = null;
        view = inflater.inflate(R.layout.item, null);
        final Button tv = (Button) view.findViewById(R.id.button);
        tv.setText(name_button);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = "connect-"+ tv.getText().toString();
                if (tv.getCurrentTextColor() == Color.BLACK) {
                    Float value = 1.0f;
                    masterConfig.sendValue(msg , value);
                    //PdBase.sendFloat(msg, value);
                    tv.setBackgroundColor(Color.parseColor("#FF9800"));
                    Log.i("MSJ A PD ", msg);
                    tv.setTextColor(Color.WHITE);
                    tv.setWidth(80);
                    gridViewAdapter.getItemsPressed().add(tv);

                } else {
                    Float value = 0.0f;
                    masterConfig.sendValue(msg , value);
                    //PdBase.sendFloat(msg, value);
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

    private Button createButtonModule(String name, String name_button){
        Button button = null;

        switch (name) {
            case "VCO": button = createbtnVCO(name_button); break;
            case "VCA": button = createbtnVCA(name_button); break;
            case "VCF": button = createbtnVCF(name_button); break;
            case "EG":  button = createbtnEG(name_button); break;
            case "SH":  button = createbtnSH(name_button); break;
            case "MIX": button = createbtnMIX(name_button); break;
            case "out": button = createButtonOut(name_button); break;
        }

        return button;
    }

    private void actionToCheckBoxModule(CheckBox check, int cant_modulos, String name_module){

        int i;
        String name;

        if (check.isChecked()) {
            for (i=0; i< cant_modulos; i++) {
                name = name_module + String.valueOf(i+1);
                modulos_matriz.put(name, createButtonModule(name_module,name));
            }

        }else if(!modulos_matriz.isEmpty()) {
            for (i=0; i< cant_modulos; i++) {
                name = name_module + String.valueOf(i+1);
                modulos_matriz.remove(name);
            }
        }
    }

    private void markCheckBox(View v,int posInRow){
        TableRow row = (TableRow)v.getParent();
        CheckBox chk = (CheckBox) row.getChildAt(posInRow);
        chk.setChecked(true);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public void onClickEditCantFirstColumn(View v){
        markCheckBox(v,0);
    }

    public void onClickEditCantSecondColumn(View v){
        markCheckBox(v,2);
    }

    private void chargeModules(TableRow row, int pos_check, int pos_edit){
        int cant;
        String c_cant;
        CheckBox chk;
        EditText text;

        chk = (CheckBox) row.getChildAt(pos_check);
        text = (EditText) row.getChildAt(pos_edit);
        c_cant = text.getText().toString();
        cant = (c_cant.isEmpty())?0:Integer.parseInt(c_cant);
        if (cant > 0)
            actionToCheckBoxModule(chk, cant, chk.getText().toString());
    }

    public void onClickAccept(View v){
        TabMatriz tab = ((TabMatriz)getSupportFragmentManager().findFragmentByTag("tab2"));
        TableLayout table = (TableLayout) findViewById(R.id.tabla_modulos);
        TableRow row;
        int i;

        for (i=1; i< table.getChildCount()-1; i++){
            row = (TableRow) table.getChildAt(i);
            chargeModules(row,0,1);
            chargeModules(row,2,3);
        }

        if (!modulos_matriz.isEmpty()) {
            modulos_matriz.put("out0", createButtonModule("out", "0-25"));
            modulos_matriz.put("out1", createButtonModule("out", "1-25"));
            modulos_matriz.put("out2", createButtonModule("out", "2-25"));
        }

        tab.setAddModules(false);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.detach(tab);
        ft.attach(tab);
        ft.commit();
    }

/****************************************************************************************************/

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

    public String loadConfigFile(){
        String json = null;
        AssetManager manager = getAssets();
        try {
            InputStream is = manager.open("app.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}