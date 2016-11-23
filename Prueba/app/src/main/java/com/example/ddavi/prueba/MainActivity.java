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
import android.content.res.AssetManager;
import android.graphics.Color;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements ActionBar.OnNavigationListener, OnEditorActionListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentTabHost tabHost;

    private static final int MIN_OCTIVE = 5;
    private static final int MAX_OCTIVE = 8;

    GridViewCustomAdapter gridViewAdapter;
    private static LayoutInflater inflater = null;
    Map<String,Button> modulos_matriz;
    Map<String,ModulePopupWindow> sliders;

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

    public Map<String,ModulePopupWindow> getSliders(){
        return sliders;
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

    /*****************************************************************************************************************
     *
     * Configuracion de Action Bar
     *
     *****************************************************************************************************************/
    private void initializeActionBar(){
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
        masterConfig.resetPresets();
        switch (i) {
            case 0:
                masterConfig.setPreset("sq_bass1");
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;

            case 1:
                masterConfig.setPreset("filter_tone");
                Log.i("Preset Elegido", "Preset 2 (Filter Tone)");
                break;

            case 2:
                masterConfig.setPreset("herbie");
                Log.i("Preset Elegido", "Preset 3 (Herbie)");
                break;

            case 3:
                masterConfig.setPreset("extasis");
                Log.i("Preset Elegido", "Preset 4 (Extasis)");
                break;

            case 4:
                masterConfig.setPreset("fm");
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;

            case 5:
                masterConfig.setPreset("saw_seq");
                Log.i("Preset Elegido", "Preset 6 (Saw Seq)");
                break;

            case 6:
                masterConfig.setPreset("bell");
                Log.i("Preset Elegido", "Preset 7 (Bell)");
                break;

            case 7:
                masterConfig.setPreset("sq_bass");
                Log.i("Preset Elegido", "Preset 8 (Sq Bass)");
                break;

            case 8:
                masterConfig.setPreset("chord_pad");
                Log.i("Preset Elegido", "Preset 1 (Chord Pad)");
                break;

            default:
                masterConfig.setPreset("sq_bass1");
                Log.i("Preset Elegido", "Preset 1 (Chord Pad)");
                break;

        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                masterConfig.config.stopAudio();
                masterConfig.config.cleanup();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

/***********************************************************************************************************/

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

        inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        modulos_matriz = new HashMap<>();
        gridViewAdapter = new GridViewCustomAdapter(this, modulos_matriz);
        createTabs();

        sliders = new HashMap<>();
        createPopUpSliders();

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
    /***********************************************************************************************
     *
     * Construccion de popSliders
     *
     **********************************************************************************************/
    private ModulePopupWindow createPopUpWindow(String name, String name_popUp){
        ModulePopupWindow popupWindow = null;

        switch (name) {
            case "VCO": popupWindow = new VCOPopupWindow(this, R.layout.popup_vco,name_popUp); break;
            case "VCA": popupWindow = new VCAPopupWindow(this, R.layout.popup_vca,name_popUp); break;
            case "VCF": popupWindow = new VCFPopupWindow(this, R.layout.popup_vcf,name_popUp); break;
            case "EG":  popupWindow = new EGPopupWindow(this, R.layout.popup_eg,name_popUp); break;
            case "SH":  popupWindow = new SHPopupWindow(this, R.layout.popup_sh,name_popUp); break;
            case "MIX": popupWindow = new MIXPopupWindow(this, R.layout.popup_mix,name_popUp); break;
        }

        sliders.put(name_popUp,popupWindow);
        return popupWindow;
    }

    private void createPopUpSliders(){
        String[] VCOSliders = {"VCO1","VCO2","VCO3"};
        String[] VCASliders = {"VCA1","VCA2"};
        String[] MIXSliders = {"MIX"};
        String[] VCFSliders = {"VCF1","VCF2"};
        String[] EGSliders = {"EG1","EG2"};
        String[] SHSliders = {"S&H"};

        Map<String,String[]> sliders = new HashMap<>();
        sliders.put("VCO",VCOSliders);
        sliders.put("VCA",VCASliders);
        sliders.put("VCF",VCFSliders);
        sliders.put("MIX",MIXSliders);
        sliders.put("EG",EGSliders);
        sliders.put("SH",SHSliders);

        int i, j;
        String[] names = {};
        ArrayList<String> sl = new ArrayList<String>(sliders.keySet());
        Collections.sort(sl, new Comparator<String>() {
            @Override
            public int compare(String b2, String b1) {
                return (b2.compareTo(b1));
            }
        });

        for (i = 0; i < sl.size(); i++) {
            names = sliders.get(sl.get(i));
            for (j = 0; j < names.length; j++)
                createPopUpWindow(sl.get(i), names[j]);
        }
    }

    /***********************************************************************************************
     *
     * Construccion de modulos de la matriz
     *
     **********************************************************************************************/
    private Button createButtonBasic(String name_module) {
        View view = null;
        view = inflater.inflate(R.layout.item, null);
        Button tv = (Button) view.findViewById(R.id.button);
        tv.setText(name_module);
        tv.setOnClickListener(new ModuleListener(gridViewAdapter));
        return tv;
    }
/*
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
*/
    private Button createButtonModule(String name, String name_button){
        Button button = null;

        switch (name) {
            case "VCO": button = createButtonBasic(name_button); break;
            case "VCA": button = createButtonBasic(name_button); break;
            case "VCF": button = createButtonBasic(name_button); break;
            case "EG":  button = createButtonBasic(name_button); break;
            case "SH":  button = createButtonBasic(name_button); break;
            case "MIX": button = createButtonBasic(name_button); break;
            case "out": button = createButtonBasic(name_button); break;
        }

        return button;
    }

    private void actionToCheckBoxModule(CheckBox check, int cant_modulos, String name_module){
        int i;
        String name_button;

        if (check.isChecked()) {
            for (i=0; i< cant_modulos; i++) {
                name_button = name_module + String.valueOf(i+1);
                modulos_matriz.put(name_button, createButtonModule(name_module,name_button));
            }

        }else if(!modulos_matriz.isEmpty()) {
            for (i=0; i< cant_modulos; i++) {
                name_button = name_module + String.valueOf(i+1);
                modulos_matriz.remove(name_button);
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
/*
        //Esto carga todos los elementos como la matriz en la primera version del aplicativo
        int CantColumnas = 17; //SIN LOS TITULOS CANTIDAD ERA 12 (CON PD TEST5 ERA 13, ahora 17 porque piden 16)
        int CantFilas = 27; // SIN LOS TITULOS CANTIDAD ERA 23 (CON PD TEST5 ERA 24, ahora 27 porque piden 26)
        String name;
        for (i = 0; i < CantFilas-1; i++) {
            for (int j = 0; j < CantColumnas-1; j++){
                name = String.valueOf(j) + "-" + String.valueOf(i);
                modulos_matriz.put(name, createButtonOut(name));
            }
        }*/

        //Ejemplo de carga de matriz, con esto no necesita cargar ningun modulo. Es para probar la conexiones de modulos
        //if (!modulos_matriz.isEmpty()) {
            modulos_matriz.put("VCO1", createButtonBasic("0-0"));
            modulos_matriz.put("VCO2", createButtonBasic("0-1"));
            modulos_matriz.put("VCO3", createButtonBasic("0-2"));
            modulos_matriz.put("out0", createButtonBasic("0-25"));
            modulos_matriz.put("out1", createButtonBasic("1-25"));
            modulos_matriz.put("out2", createButtonBasic("2-25"));
        //}

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