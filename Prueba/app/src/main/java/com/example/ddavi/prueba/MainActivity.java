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

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import android.support.v4.app.FragmentTabHost;

import com.evilduck.piano.views.instrument.PianoView;
import com.example.ddavi.prueba.Tabs.TabMatriz;
import com.example.ddavi.prueba.Tabs.TabPiano;
import com.twobard.pianoview.Piano;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.service.PdPreferences;
import org.puredata.android.service.PdService;
import org.puredata.core.PdBase;
import org.puredata.core.PdReceiver;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

//quitado cuando comence con controles finales
//import android.support.v7.widget.ListPopupWindow;

public class MainActivity extends AppCompatActivity /**Activity**/ implements OnClickListener, OnEditorActionListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentTabHost tabHost;

    //esto es de test
    private static final String TAG = "XUL";
    private static final int MIN_OCTIVE = 5;
    private static final int MAX_OCTIVE = 8;

    MyGridView matriz_modulos;
    GridViewCustomAdapter gridViewAdapter;

    private EditText msg;
    private TextView logs;
    private PdService pdService = null;
    private Toast toast = null;
    private int octava;

    public int getOctava(){
        return octava;
    }

    public void setMatriz(MyGridView matriz){
        matriz_modulos = matriz;
    }

    public MyGridView getMatriz_modulos(){
        return matriz_modulos;
    }

    public void setOctava(int valor){
        this.octava = valor;
    }
    private void toast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
                }
                toast.setText(TAG + ": " + msg);
                toast.show();
            }
        });
    }

    private void post(final String s) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                logs.append(s + ((s.endsWith("\n")) ? "" : "\n"));
            }
        });
    }

    private PdReceiver receiver = new PdReceiver() {

        private void pdPost(String msg) {
            toast("Pure Data says, \"" + msg + "\"");
        }

        @Override
        public void print(String s) {
            post(s);
        }

        @Override
        public void receiveBang(String source) {
            pdPost("bang");
        }

        @Override
        public void receiveFloat(String source, float x) {
            pdPost("float: " + x);
        }

        @Override
        public void receiveList(String source, Object... args) {
            pdPost("list: " + Arrays.toString(args));
        }

        @Override
        public void receiveMessage(String source, String symbol, Object... args) {
            pdPost("message: " + Arrays.toString(args));
        }

        @Override
        public void receiveSymbol(String source, String symbol) {
            pdPost("symbol: " + symbol);
        }
    };

    private final ServiceConnection pdConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            pdService = ((PdService.PdBinder)service).getService();
            initPd();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // this method will never be called
        }
    };

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

    private void initializePureData(){

        AudioParameters.init(this);
        PdPreferences.initPreferences(getApplicationContext());
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).registerOnSharedPreferenceChangeListener(this);

        logs = (TextView) findViewById(R.id.log_box);
        logs.setMovementMethod(new ScrollingMovementMethod());

        bindService(new Intent(this, PdService.class), pdConnection, BIND_AUTO_CREATE);
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
        initializePureData();
        initializeGridViewAdapter();

        createTabs();
        setOctava(5);
    }

    public GridViewCustomAdapter getGridViewAdapter(){
        return gridViewAdapter;
    }

    private void initializeGridViewAdapter() {
        int CantColumnas = 17; //SIN LOS TITULOS CANTIDAD ERA 12 (CON PD TEST5 ERA 13, ahora 17 porque piden 16)
        int CantFilas = 27; // SIN LOS TITULOS CANTIDAD ERA 23 (CON PD TEST5 ERA 24, ahora 27 porque piden 26)
        //int CantColumnas = 3;
        //int CantFilas = 5;
        ArrayList<String> data = new ArrayList<>();
        //CON ESTE CODIGO LLENA TODA LA MATRIZ CON SU POSICION
        /*
        for (int i = 0; i < CantFilas; i++) {
            for (int j = 0; j < CantColumnas; j++)
                data.add(j + "-" + i);
        }
        */
        //CON ESTE CODIGO PONE PRIMERA COLUMNA NOMBRES
        for (int i = -1; i < CantFilas - 1; i++) {
            for (int j = -1; j < CantColumnas - 1; j++)
                //PONGO NOMBRES A PRIMERA COLUMNA
                if (j == -1 & i == -1) {
                    data.add("");
                } else if (j == -1 & i == 0) {
                    data.add("0");
                } else if (j == -1 & i == 1) {
                    data.add("1");
                } else if (j == -1 & i == 2) {
                    data.add("2");
                } else if (j == -1 & i == 3) {
                    data.add("3");
                } else if (j == -1 & i == 4) {
                    data.add("4");
                } else if (j == -1 & i == 5) {
                    data.add("5");
                } else if (j == -1 & i == 6) {
                    data.add("6");
                } else if (j == -1 & i == 7) {
                    data.add("7");
                } else if (j == -1 & i == 8) {
                    data.add("8");
                } else if (j == -1 & i == 9) {
                    data.add("9");
                } else if (j == -1 & i == 10) {
                    data.add("10");
                } else if (j == -1 & i == 11) {
                    data.add("11");
                } else if (j == -1 & i == 12) {
                    data.add("12");
                } else if (j == -1 & i == 13) {
                    data.add("13");
                } else if (j == -1 & i == 14) {
                    data.add("14");
                } else if (j == -1 & i == 15) {
                    data.add("15");
                } else if (j == -1 & i == 16) {
                    data.add("16");
                } else if (j == -1 & i == 17) {
                    data.add("17");
                } else if (j == -1 & i == 18) {
                    data.add("18");
                } else if (j == -1 & i == 19) {
                    data.add("19");
                } else if (j == -1 & i == 20) {
                    data.add("20");
                } else if (j == -1 & i == 21) {
                    data.add("21");
                } else if (j == -1 & i == 22) {
                    data.add("22");
                } else if (j == -1 & i == 23) {
                    data.add("23");
                } else if (j == -1 & i == 24) {
                    data.add("24");
                } else if (j == -1 & i == 25) {
                    data.add("25");
                }
                //PONGO NOMBRES A PRIMERA FILA
                else if (i == -1 & j == 0) {
                    data.add("0");
                } else if (i == -1 & j == 1) {
                    data.add("1");
                } else if (i == -1 & j == 2) {
                    data.add("2");
                } else if (i == -1 & j == 3) {
                    data.add("3");
                } else if (i == -1 & j == 4) {
                    data.add("4");
                } else if (i == -1 & j == 5) {
                    data.add("5");
                } else if (i == -1 & j == 6) {
                    data.add("6");
                } else if (i == -1 & j == 7) {
                    data.add("7");
                } else if (i == -1 & j == 8) {
                    data.add("8");
                } else if (i == -1 & j == 9) {
                    data.add("9");
                } else if (i == -1 & j == 10) {
                    data.add("10");
                } else if (i == -1 & j == 11) {
                    data.add("11");
                } else if (i == -1 & j == 12) {
                    data.add("12");
                } else if (i == -1 & j == 13) {
                    data.add("13");
                } else if (i == -1 & j == 14) {
                    data.add("14");
                } else if (i == -1 & j == 15) {
                    data.add("15");
                }
                //EL RESTO DE LAS POSICIONES
                else {
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_exit:
                stopAudio();
                cleanup();
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
        cleanup();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (pdService.isRunning()) {
            startAudio();
        }
    }

    private void initGui() {

        setContentView(R.layout.main);

        Button solapa1 = (Button) findViewById(R.id.solapa1);
        solapa1.setOnClickListener(this);
        Button solapa2 = (Button) findViewById(R.id.solapa2);
        solapa2.setOnClickListener(this);
        Button solapa3 = (Button) findViewById(R.id.solapa3);
        solapa3.setOnClickListener(this);
        Button solapa4 = (Button) findViewById(R.id.solapa4);
        solapa4.setOnClickListener(this);
        Button solapa5 = (Button) findViewById(R.id.solapa5);
        solapa5.setOnClickListener(this);
        Button solapa6 = (Button) findViewById(R.id.solapa6);
        solapa6.setOnClickListener(this);

        //OCULTA SOLAPA DE SECUENCIADOR
        solapa2.setVisibility(View.GONE);

        Button botonVCO1 = (Button) findViewById(R.id.botonVCO1);
        botonVCO1.setOnClickListener(this);
        Button botonVCO2 = (Button) findViewById(R.id.botonVCO2);
        botonVCO2.setOnClickListener(this);
        Button botonVCO3 = (Button) findViewById(R.id.botonVCO3);
        botonVCO3.setOnClickListener(this);
        Button botonVCA1 = (Button) findViewById(R.id.botonVCA1);
        botonVCA1.setOnClickListener(this);
        Button botonVCA2 = (Button) findViewById(R.id.botonVCA2);
        botonVCA2.setOnClickListener(this);
        Button botonMIXER1 = (Button) findViewById(R.id.botonMIXER1);
        botonMIXER1.setOnClickListener(this);
        Button botonVCF1 = (Button) findViewById(R.id.botonVCF1);
        botonVCF1.setOnClickListener(this);
        Button botonVCF2 = (Button) findViewById(R.id.botonVCF2);
        botonVCF2.setOnClickListener(this);
        Button botonEG1 = (Button) findViewById(R.id.botonEG1);
        botonEG1.setOnClickListener(this);
        Button botonEG2 = (Button) findViewById(R.id.botonEG2);
        botonEG2.setOnClickListener(this);
        Button botonSH1 = (Button) findViewById(R.id.botonSH1);
        botonSH1.setOnClickListener(this);

        /**
         Button botonOctavaMenos = (Button) findViewById(R.id.botonOctavaMenos);
         botonOctavaMenos.setOnClickListener(this);
         Button botonOctavaMas = (Button) findViewById(R.id.botonOctavaMas);
         botonOctavaMas.setOnClickListener(this);

        Button botonPreset1 = (Button) findViewById(R.id.botonPreset1);
        botonPreset1.setOnClickListener(this);
        Button botonPreset2 = (Button) findViewById(R.id.botonPreset2);
        botonPreset2.setOnClickListener(this);
        Button botonPreset3 = (Button) findViewById(R.id.botonPreset3);
        botonPreset3.setOnClickListener(this);
        Button botonPreset4 = (Button) findViewById(R.id.botonPreset4);
        botonPreset4.setOnClickListener(this);
        Button botonPreset5 = (Button) findViewById(R.id.botonPreset5);
        botonPreset5.setOnClickListener(this);

        Button botonPreset6 = (Button) findViewById(R.id.botonPreset6);
        botonPreset6.setOnClickListener(this);
        Button botonPreset7 = (Button) findViewById(R.id.botonPreset7);
        botonPreset7.setOnClickListener(this);
        Button botonPreset8 = (Button) findViewById(R.id.botonPreset8);
        botonPreset8.setOnClickListener(this);
        Button botonPreset9 = (Button) findViewById(R.id.botonPreset9);
        botonPreset9.setOnClickListener(this);
        Button botonPreset10 = (Button) findViewById(R.id.botonPreset10);
        botonPreset10.setOnClickListener(this);**/

        //color a botones, primera vista
        solapa1.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        solapa2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        solapa3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        solapa4.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        solapa5.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        solapa6.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);

        botonVCO1.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);

        logs = (TextView) findViewById(R.id.log_box);
        logs.setMovementMethod(new ScrollingMovementMethod());

        initGUIPD4();
    }

    private void initPd() {
        Resources res = getResources();
        File patchFile = null;
        try {
            PdBase.setReceiver(receiver);
            PdBase.subscribe("android");
            //agregada apertura de todos los PD que SYNTH necesita

            //24-07-16 PRUEBA DE HACER 1 UNICO INPUTSTREAM, TODOS SE LLAMARAN IN EN LUGAR DE IN1, IN2...
            InputStream in = res.openRawResource(R.raw.x_vco1);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_vco1 =
            IoUtils.extractResource(in, "x_vco1.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_vco1);
            in = res.openRawResource(R.raw.x_vco2);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_vco2 =
            IoUtils.extractResource(in, "x_vco2.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_vco2);
            in = res.openRawResource(R.raw.x_vco3);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_vco3 =
            IoUtils.extractResource(in, "x_vco3.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_vco3);
            in = res.openRawResource(R.raw.cell);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_cell =
            IoUtils.extractResource(in, "cell.pd", getCacheDir());


            in = res.openRawResource(R.raw.x_eg1);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_eg1 =
            IoUtils.extractResource(in, "x_eg1.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_eg1);
            in = res.openRawResource(R.raw.x_eg2);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_eg2 =
            IoUtils.extractResource(in, "x_eg2.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_eg2);
            in = res.openRawResource(R.raw.x_mix);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_mix =
            IoUtils.extractResource(in, "x_mix.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_mix);
            in = res.openRawResource(R.raw.x_mtx);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_mtx =
            IoUtils.extractResource(in, "x_mtx.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_mtx);
            in = res.openRawResource(R.raw.x_ng);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_ng =
            IoUtils.extractResource(in, "x_ng.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_ng);
            in = res.openRawResource(R.raw.x_sh);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_sh =
            IoUtils.extractResource(in, "x_sh.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_sh);
            in = res.openRawResource(R.raw.x_vca1);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_vca1 =
            IoUtils.extractResource(in, "x_vca1.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_vca1);
            in = res.openRawResource(R.raw.x_vca2);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_vca2 =
            IoUtils.extractResource(in, "x_vca2.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_vca2);
            in = res.openRawResource(R.raw.x_vcf1);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_vcf1 =
            IoUtils.extractResource(in, "x_vcf1.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_vcf1);
            in = res.openRawResource(R.raw.x_vcf2);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_vcf2 =
            IoUtils.extractResource(in, "x_vcf2.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_vcf2);
            in = res.openRawResource(R.raw.x_kb);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_kb =
            IoUtils.extractResource(in, "x_kb.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_kb);
            in = res.openRawResource(R.raw.x_seq);
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: File patchFile_x_seq =
            IoUtils.extractResource(in, "x_seq.pd", getCacheDir());
            // 18-07-16 COMENTADA APERTURA POR RECURSOS: PdBase.openPatch(patchFile_x_seq);
            /* */
            in = res.openRawResource(R.raw.presets);
            File patchFile_presets =IoUtils.extractResource(in, "presets.pd", getCacheDir());
            PdBase.openPatch(patchFile_presets);
            //apertura original de 1 PD
            in = res.openRawResource(R.raw.synth);
            patchFile = IoUtils.extractResource(in, "synth.pd", getCacheDir());
            PdBase.openPatch(patchFile);

            //ESTO INICIA EL SONIDO, ANTES ESTABA AL PRESIONAR PLAY
            if (pdService.isRunning()) {
                stopAudio();
            } else {
                startAudio();
                //PUSE MEJOR UN PRESET QUE NO QUEDE SONANDO
                PdBase.sendFloat("sq_bass1",1);
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());
            finish();
        } finally {
            if (patchFile != null) patchFile.delete();
        }
    }

    private void startAudio() {
        String name = getResources().getString(R.string.app_name);
        try {
            pdService.initAudio(-1, -1, -1, -1);   // negative values will be replaced with defaults/preferences
            pdService.startAudio(new Intent(this, MainActivity.class), R.drawable.icon, name, "Return to " + name + ".");
        } catch (IOException e) {
            toast(e.toString());
        }
    }

    private void stopAudio() {
        pdService.stopAudio();
    }

    private void cleanup() {
        try {
            unbindService(pdConnection);
        } catch (IllegalArgumentException e) {
            // already unbound
            pdService = null;
        }
    }

    @Override
    public void onClick(View v) {

        Button botonVCO1 = (Button) findViewById(R.id.botonVCO1);
        Button botonVCO2 = (Button) findViewById(R.id.botonVCO2);
        Button botonVCO3 = (Button) findViewById(R.id.botonVCO3);
        Button botonVCA1 = (Button) findViewById(R.id.botonVCA1);
        Button botonVCA2 = (Button) findViewById(R.id.botonVCA2);
        Button botonMIXER1 = (Button) findViewById(R.id.botonMIXER1);
        Button botonVCF1 = (Button) findViewById(R.id.botonVCF1);
        Button botonVCF2 = (Button) findViewById(R.id.botonVCF2);
        Button botonEG1 = (Button) findViewById(R.id.botonEG1);
        Button botonEG2 = (Button) findViewById(R.id.botonEG2);
        Button botonSH1 = (Button) findViewById(R.id.botonSH1);
        TableLayout tablaVCO1 = (TableLayout) findViewById(R.id.tablaVCO1);
        TableLayout tablaVCO2 = (TableLayout) findViewById(R.id.tablaVCO2);
        TableLayout tablaVCO3 = (TableLayout) findViewById(R.id.tablaVCO3);
        TableLayout tablaVCA1 = (TableLayout) findViewById(R.id.tablaVCA1);
        TableLayout tablaVCA2 = (TableLayout) findViewById(R.id.tablaVCA2);
        TableLayout tablaMIXER1 = (TableLayout) findViewById(R.id.tablaMIXER1);
        TableLayout tablaVCF1 = (TableLayout) findViewById(R.id.tablaVCF1);
        TableLayout tablaVCF2 = (TableLayout) findViewById(R.id.tablaVCF2);
        TableLayout tablaEG1 = (TableLayout) findViewById(R.id.tablaEG1);
        TableLayout tablaEG2 = (TableLayout) findViewById(R.id.tablaEG2);
        TableLayout tablaSH1 = (TableLayout) findViewById(R.id.tablaSH1);

        /**TextView labelOctava = (TextView) findViewById(R.id.labelOctava);
         String labelOctava1 = String.valueOf(labelOctava.getText());
         int labelOctava2= Integer.parseInt(labelOctava1);**/

        switch (v.getId()) {

            case R.id.solapa1:
                mostrarPiano();
                //esconderSecuenciador();
                esconderControles();
                //esconderPresets();
                //esconderMatriz();
                break;

            case R.id.solapa2:
                //esconderPiano();
                //mostrarSecuenciador();
                esconderControles();
                //esconderPresets();
                //esconderMatriz();
                break;

            case R.id.solapa3:
                //esconderPiano();
                //esconderSecuenciador();
                mostrarControles();
                //esconderPresets();
                //esconderMatriz();
                break;

            case R.id.solapa4:
                //esconderPiano();
                //esconderSecuenciador();
                esconderControles();
                mostrarPresets();
                //esconderMatriz();
                break;

            case R.id.solapa5:
                //esconderPiano();
                //esconderSecuenciador();
                esconderControles();
                //esconderPresets();
                //mostrarMatriz();
                break;

            case R.id.solapa6:
                stopAudio();
                cleanup();
                finish();
                break;

            /**case R.id.botonOctavaMenos:
             labelOctava2--;
             labelOctava.setText("" + labelOctava2);
             break;
             case R.id.botonOctavaMas:
             labelOctava2++;
             labelOctava.setText("" + labelOctava2);
             break;

            case R.id.botonPreset1:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("chord_pad",1);
                Log.i("Preset Elegido", "Preset 1 (Chord Pad)");
                break;

            case R.id.botonPreset2:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("filter_tone",1);
                Log.i("Preset Elegido", "Preset 2 (Filter Tone)");
                break;

            case R.id.botonPreset3:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("herbie",1);
                Log.i("Preset Elegido", "Preset 3 (Herbie)");
                break;

            case R.id.botonPreset4:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("extasis",1);
                Log.i("Preset Elegido", "Preset 4 (Extasis)");
                break;

            case R.id.botonPreset5:
                PdBase.sendFloat("reset_presets",1);
                Log.i("Preset Elegido", "Preset 5 (Reset)");
                break;

            case R.id.botonPreset6:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("saw_seq",1);
                Log.i("Preset Elegido", "Preset 6 (Saw Seq)");
                break;

            case R.id.botonPreset7:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("bell",1);
                Log.i("Preset Elegido", "Preset 7 (Bell)");
                break;

            case R.id.botonPreset8:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("sq_bass",1);
                Log.i("Preset Elegido", "Preset 8 (Sq Bass)");
                break;

            case R.id.botonPreset9:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("sq_bass1",1);
                Log.i("Preset Elegido", "Preset 9 (Sq Bass1)");
                break;
            case R.id.botonPreset10:
                PdBase.sendFloat("reset_presets",1);
                PdBase.sendFloat("fm",1);
                Log.i("Preset Elegido", "Preset 10 (Fm)");
                break;**/

            case R.id.botonVCO1:
                botonVCO1.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.VISIBLE);
                tablaVCO2.setVisibility(View.GONE);
                tablaVCO3.setVisibility(View.GONE);
                tablaVCA1.setVisibility(View.GONE);
                tablaVCA2.setVisibility(View.GONE);
                tablaMIXER1.setVisibility(View.GONE);
                tablaVCF1.setVisibility(View.GONE);
                tablaVCF2.setVisibility(View.GONE);
                tablaEG1.setVisibility(View.GONE);
                tablaEG2.setVisibility(View.GONE);
                tablaSH1.setVisibility(View.GONE);
                break;

            case R.id.botonVCO2:
                botonVCO1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.GONE);
                tablaVCO2.setVisibility(View.VISIBLE);
                tablaVCO3.setVisibility(View.GONE);
                tablaVCA1.setVisibility(View.GONE);
                tablaVCA2.setVisibility(View.GONE);
                tablaMIXER1.setVisibility(View.GONE);
                tablaVCF1.setVisibility(View.GONE);
                tablaVCF2.setVisibility(View.GONE);
                tablaEG1.setVisibility(View.GONE);
                tablaEG2.setVisibility(View.GONE);
                tablaSH1.setVisibility(View.GONE);
                break;

            case R.id.botonVCO3:
                botonVCO1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.GONE);
                tablaVCO2.setVisibility(View.GONE);
                tablaVCO3.setVisibility(View.VISIBLE);
                tablaVCA1.setVisibility(View.GONE);
                tablaVCA2.setVisibility(View.GONE);
                tablaMIXER1.setVisibility(View.GONE);
                tablaVCF1.setVisibility(View.GONE);
                tablaVCF2.setVisibility(View.GONE);
                tablaEG1.setVisibility(View.GONE);
                tablaEG2.setVisibility(View.GONE);
                tablaSH1.setVisibility(View.GONE);
                break;

            case R.id.botonVCA1:
                botonVCO1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.GONE);
                tablaVCO2.setVisibility(View.GONE);
                tablaVCO3.setVisibility(View.GONE);
                tablaVCA1.setVisibility(View.VISIBLE);
                tablaVCA2.setVisibility(View.GONE);
                tablaMIXER1.setVisibility(View.GONE);
                tablaVCF1.setVisibility(View.GONE);
                tablaVCF2.setVisibility(View.GONE);
                tablaEG1.setVisibility(View.GONE);
                tablaEG2.setVisibility(View.GONE);
                tablaSH1.setVisibility(View.GONE);
                break;

            case R.id.botonVCA2:
                botonVCO1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.GONE);
                tablaVCO2.setVisibility(View.GONE);
                tablaVCO3.setVisibility(View.GONE);
                tablaVCA1.setVisibility(View.GONE);
                tablaVCA2.setVisibility(View.VISIBLE);
                tablaMIXER1.setVisibility(View.GONE);
                tablaVCF1.setVisibility(View.GONE);
                tablaVCF2.setVisibility(View.GONE);
                tablaEG1.setVisibility(View.GONE);
                tablaEG2.setVisibility(View.GONE);
                tablaSH1.setVisibility(View.GONE);
                break;

            case R.id.botonMIXER1:
                botonVCO1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.GONE);
                tablaVCO2.setVisibility(View.GONE);
                tablaVCO3.setVisibility(View.GONE);
                tablaVCA1.setVisibility(View.GONE);
                tablaVCA2.setVisibility(View.GONE);
                tablaMIXER1.setVisibility(View.VISIBLE);
                tablaVCF1.setVisibility(View.GONE);
                tablaVCF2.setVisibility(View.GONE);
                tablaEG1.setVisibility(View.GONE);
                tablaEG2.setVisibility(View.GONE);
                tablaSH1.setVisibility(View.GONE);
                break;

            case R.id.botonVCF1:
                botonVCO1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.GONE);
                tablaVCO2.setVisibility(View.GONE);
                tablaVCO3.setVisibility(View.GONE);
                tablaVCA1.setVisibility(View.GONE);
                tablaVCA2.setVisibility(View.GONE);
                tablaMIXER1.setVisibility(View.GONE);
                tablaVCF1.setVisibility(View.VISIBLE);
                tablaVCF2.setVisibility(View.GONE);
                tablaEG1.setVisibility(View.GONE);
                tablaEG2.setVisibility(View.GONE);
                tablaSH1.setVisibility(View.GONE);
                break;

            case R.id.botonVCF2:
                botonVCO1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.GONE);
                tablaVCO2.setVisibility(View.GONE);
                tablaVCO3.setVisibility(View.GONE);
                tablaVCA1.setVisibility(View.GONE);
                tablaVCA2.setVisibility(View.GONE);
                tablaMIXER1.setVisibility(View.GONE);
                tablaVCF1.setVisibility(View.GONE);
                tablaVCF2.setVisibility(View.VISIBLE);
                tablaEG1.setVisibility(View.GONE);
                tablaEG2.setVisibility(View.GONE);
                tablaSH1.setVisibility(View.GONE);
                break;

            case R.id.botonEG1:
                botonVCO1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.GONE);
                tablaVCO2.setVisibility(View.GONE);
                tablaVCO3.setVisibility(View.GONE);
                tablaVCA1.setVisibility(View.GONE);
                tablaVCA2.setVisibility(View.GONE);
                tablaMIXER1.setVisibility(View.GONE);
                tablaVCF1.setVisibility(View.GONE);
                tablaVCF2.setVisibility(View.GONE);
                tablaEG1.setVisibility(View.VISIBLE);
                tablaEG2.setVisibility(View.GONE);
                tablaSH1.setVisibility(View.GONE);
                break;

            case R.id.botonEG2:
                botonVCO1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.GONE);
                tablaVCO2.setVisibility(View.GONE);
                tablaVCO3.setVisibility(View.GONE);
                tablaVCA1.setVisibility(View.GONE);
                tablaVCA2.setVisibility(View.GONE);
                tablaMIXER1.setVisibility(View.GONE);
                tablaVCF1.setVisibility(View.GONE);
                tablaVCF2.setVisibility(View.GONE);
                tablaEG1.setVisibility(View.GONE);
                tablaEG2.setVisibility(View.VISIBLE);
                tablaSH1.setVisibility(View.GONE);
                break;

            case R.id.botonSH1:
                botonVCO1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
                botonSH1.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
                tablaVCO1.setVisibility(View.GONE);
                tablaVCO2.setVisibility(View.GONE);
                tablaVCO3.setVisibility(View.GONE);
                tablaVCA1.setVisibility(View.GONE);
                tablaVCA2.setVisibility(View.GONE);
                tablaMIXER1.setVisibility(View.GONE);
                tablaVCF1.setVisibility(View.GONE);
                tablaVCF2.setVisibility(View.GONE);
                tablaEG1.setVisibility(View.GONE);
                tablaEG2.setVisibility(View.GONE);
                tablaSH1.setVisibility(View.VISIBLE);
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        evaluateMessage(msg.getText().toString());
        return true;
    }

    private void evaluateMessage(String s) {
        String dest = "test", symbol = null;
        boolean isAny = s.length() > 0 && s.charAt(0) == ';';
        Scanner sc = new Scanner(isAny ? s.substring(1) : s);
        if (isAny) {
            if (sc.hasNext()) dest = sc.next();
            else {
                toast("Message not sent (empty recipient)");
                return;
            }
            if (sc.hasNext()) symbol = sc.next();
            else {
                toast("Message not sent (empty symbol)");
            }
        }
        List<Object> list = new ArrayList<Object>();
        while (sc.hasNext()) {
            if (sc.hasNextInt()) {
                list.add(Float.valueOf(sc.nextInt()));
            } else if (sc.hasNextFloat()) {
                list.add(sc.nextFloat());
            } else {
                list.add(sc.next());
            }
        }
        if (isAny) {
            PdBase.sendMessage(dest, symbol, list.toArray());
        } else {
            switch (list.size()) {
                case 0:
                    PdBase.sendBang(dest);
                    break;
                case 1:
                    Object x = list.get(0);
                    if (x instanceof String) {
                        PdBase.sendSymbol(dest, (String) x);
                    } else {
                        PdBase.sendFloat(dest, (Float) x);
                    }
                    break;
                default:
                    PdBase.sendList(dest, list.toArray());
                    break;
            }
        }
    }


    private void initGUIPD4() {

        //ANTES DE PRESENTACION COMENTADO METODO ESCONDER APERTURA ARCHIVOS, DE PD4
        //esconderControlesAperturaArchivos();
        //esconderMatriz();
        esconderControles();
        //esconderPresets();
        //esconderLog();

        //setTamanioGrilla();

        //ACA COMIENZAN LOS CONTROLES FINALES
        //TODOS LOS SLIDERS VCO1
        //COMIENZO DE SEEKBAR VCO 1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO1_1 = (SeekBar) findViewById(R.id.seekBarVCO1_1);
        final TextView labelVCO1_1 = (TextView) findViewById(R.id.labelVCO1_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO1_1 = 0.01f;
        float maxVCO1_1 = 1.0f;
        float minVCO1_1 = 0.0f;
        seekBarVCO1_1.setMax((int) ((maxVCO1_1 - minVCO1_1) / multiplicadorVCO1_1));
        seekBarVCO1_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO1_att_freq0";
                String labelVCO1_1text = "att_freq0";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO1_1", msj);
                Log.i("Valor   seekVCO1_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_1.setText(labelVCO1_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO1_1
        //COMIENZO DE SEEKBAR VCO 1_1b
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO1_1b = (SeekBar) findViewById(R.id.seekBarVCO1_1b);
        final TextView labelVCO1_1b = (TextView) findViewById(R.id.labelVCO1_1b);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO1_1b = 0.01f;
        float maxVCO1_1b = 1.0f;
        float minVCO1_1b = 0.0f;
        seekBarVCO1_1b.setMax((int) ((maxVCO1_1b - minVCO1_1b) / multiplicadorVCO1_1b));
        seekBarVCO1_1b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO1_att_freq1";
                String labelVCO1_1btext = "att_freq1";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO1_1b", msj);
                Log.i("Valor   seekVCO1_1b", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_1b.setText(labelVCO1_1btext + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO1_1b

        //COMIENZO DE SEEKBAR VCO 1_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO1_2 = (SeekBar) findViewById(R.id.seekBarVCO1_2);
        final TextView labelVCO1_2 = (TextView) findViewById(R.id.labelVCO1_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO1_2 = 0.01f;
        float maxVCO1_2 = 1.0f;
        float minVCO1_2 = 0.0f;
        seekBarVCO1_2.setMax((int) ((maxVCO1_2 - minVCO1_2) / multiplicadorVCO1_2));
        seekBarVCO1_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO1_att_pw";
                String labelVCO1_2text = "att_pw";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO1_2", msj);
                Log.i("Valor   seekVCO1_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_2.setText(labelVCO1_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO1_2
        //COMIENZO DE SEEKBAR VCO 1_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO1_3 = (SeekBar) findViewById(R.id.seekBarVCO1_3);
        final TextView labelVCO1_3 = (TextView) findViewById(R.id.labelVCO1_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO1_3 = 1.0f;
        float maxVCO1_3 = 4.0f;
        float minVCO1_3 = 0.0f;
        seekBarVCO1_3.setMax((int) ((maxVCO1_3 - minVCO1_3) / multiplicadorVCO1_3));
        seekBarVCO1_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO1_shape";
                String labelVCO1_3text = "waveform";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO1_3", msj);
                Log.i("Valor   seekVCO1_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                String tipoDeOnda = "sine";
                if (value == 0.0) {
                    tipoDeOnda = "sine";
                }
                if (value == 1.0) {
                    tipoDeOnda = "ramp";
                }
                if (value == 2.0) {
                    tipoDeOnda = "saw";
                }
                if (value == 3.0) {
                    tipoDeOnda = "trig";
                }
                if (value == 4.0) {
                    tipoDeOnda = "pulse";
                }
                labelVCO1_3.setText(labelVCO1_3text + ": " + tipoDeOnda);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO1_3
        //COMIENZO DE SEEKBAR VCO 1_4
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO1_4 = (SeekBar) findViewById(R.id.seekBarVCO1_4);
        final TextView labelVCO1_4 = (TextView) findViewById(R.id.labelVCO1_4);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO1_4 = 20.0f;
        float maxVCO1_4 = 20000.0f;
        float minVCO1_4 = 0.0f;
        seekBarVCO1_4.setMax((int) ((maxVCO1_4 - minVCO1_4) / multiplicadorVCO1_4));
        seekBarVCO1_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO1_freq";
                String labelVCO1_4text = "freq";
                float multiplicador = 20.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO1_4", msj);
                Log.i("Valor   seekVCO1_4", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_4.setText(labelVCO1_4text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO1_4

        //COMIENZO DE SEEKBAR VCO1_4b
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO1_4b = (SeekBar) findViewById(R.id.seekBarVCO1_4b);
        final TextView labelVCO1_4b = (TextView) findViewById(R.id.labelVCO1_4b);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO1_4b = 1.0f;
        float maxVCO1_4b = 63.0f;
        float minVCO1_4b = -64.0f;
        seekBarVCO1_4b.setMax((int) ((maxVCO1_4b - minVCO1_4b) / multiplicadorVCO1_4b));
        seekBarVCO1_4b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO1_offset";
                String labelVCO1_4btext = "offset";
                float multiplicador = 1.0f;
                float valorInicial = -64.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO1_4b", msj);
                Log.i("Valor   seekVCO1_4b", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_4b.setText(labelVCO1_4btext + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO1_4b

        //COMIENZO DE SEEKBAR VCO 1_5
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO1_5 = (SeekBar) findViewById(R.id.seekBarVCO1_5);
        final TextView labelVCO1_5 = (TextView) findViewById(R.id.labelVCO1_5);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO1_5 = 1.0f;
        float maxVCO1_5 = 100.0f;
        float minVCO1_5 = 0.0f;
        seekBarVCO1_5.setMax((int) ((maxVCO1_5 - minVCO1_5) / multiplicadorVCO1_5));
        seekBarVCO1_5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO1_pw";
                String labelVCO1_5text = "pw";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO1_5", msj);
                Log.i("Valor   seekVCO1_5", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_5.setText(labelVCO1_5text + ": " + value + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO1_5

        //TODOS LOS SLIDERS VCO2
        //COMIENZO DE SEEKBAR VCO 2_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO2_1 = (SeekBar) findViewById(R.id.seekBarVCO2_1);
        final TextView labelVCO2_1 = (TextView) findViewById(R.id.labelVCO2_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO2_1 = 0.01f;
        float maxVCO2_1 = 1.0f;
        float minVCO2_1 = 0.0f;
        seekBarVCO2_1.setMax((int) ((maxVCO2_1 - minVCO2_1) / multiplicadorVCO2_1));
        seekBarVCO2_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO2_att_freq0";
                String labelVCO2_1text = "att_freq0";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO2_1", msj);
                Log.i("Valor   seekVCO2_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO2_1.setText(labelVCO2_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO2_1

        //COMIENZO DE SEEKBAR VCO2_1b
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO2_1b = (SeekBar) findViewById(R.id.seekBarVCO2_1b);
        final TextView labelVCO2_1b = (TextView) findViewById(R.id.labelVCO2_1b);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO2_1b = 0.01f;
        float maxVCO2_1b = 1.0f;
        float minVCO2_1b = 0.0f;
        seekBarVCO2_1b.setMax((int) ((maxVCO2_1b - minVCO2_1b) / multiplicadorVCO2_1b));
        seekBarVCO2_1b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO2_att_freq1";
                String labelVCO2_1btext = "att_freq1";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO2_1b", msj);
                Log.i("Valor   seekVCO2_1b", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO2_1b.setText(labelVCO2_1btext + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO2_1b

        //COMIENZO DE SEEKBAR VCO 2_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO2_2 = (SeekBar) findViewById(R.id.seekBarVCO2_2);
        final TextView labelVCO2_2 = (TextView) findViewById(R.id.labelVCO2_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO2_2 = 0.01f;
        float maxVCO2_2 = 1.0f;
        float minVCO2_2 = 0.0f;
        seekBarVCO2_2.setMax((int) ((maxVCO2_2 - minVCO2_2) / multiplicadorVCO2_2));
        seekBarVCO2_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO2_att_pw";
                String labelVCO2_2text = "att_pw";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO2_2", msj);
                Log.i("Valor   seekVCO2_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO2_2.setText(labelVCO2_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO2_2
        //COMIENZO DE SEEKBAR VCO 2_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO2_3 = (SeekBar) findViewById(R.id.seekBarVCO2_3);
        final TextView labelVCO2_3 = (TextView) findViewById(R.id.labelVCO2_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO2_3 = 1.0f;
        float maxVCO2_3 = 3.0f;
        float minVCO2_3 = 0.0f;
        seekBarVCO2_3.setMax((int) ((maxVCO2_3 - minVCO2_3) / multiplicadorVCO2_3));
        seekBarVCO2_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO2_shape";
                String labelVCO2_3text = "waveform";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO2_3", msj);
                Log.i("Valor   seekVCO2_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                String tipoDeOnda = "sine";
                if (value == 0.0) {
                    tipoDeOnda = "sine";
                }
                if (value == 1.0) {
                    tipoDeOnda = "ramp";
                }
                if (value == 2.0) {
                    tipoDeOnda = "saw";
                }
                if (value == 3.0) {
                    tipoDeOnda = "trig";
                }
                if (value == 4.0) {
                    tipoDeOnda = "pulse";
                }
                labelVCO2_3.setText(labelVCO2_3text + ": " + tipoDeOnda);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO2_3
        //COMIENZO DE SEEKBAR VCO 2_4
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO2_4 = (SeekBar) findViewById(R.id.seekBarVCO2_4);
        final TextView labelVCO2_4 = (TextView) findViewById(R.id.labelVCO2_4);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO2_4 = 20.0f;
        float maxVCO2_4 = 20000.0f;
        float minVCO2_4 = 0.0f;
        seekBarVCO2_4.setMax((int) ((maxVCO2_4 - minVCO2_4) / multiplicadorVCO2_4));
        seekBarVCO2_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO2_freq";
                String labelVCO2_4text = "freq";
                float multiplicador = 20.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO2_4", msj);
                Log.i("Valor   seekVCO2_4", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO2_4.setText(labelVCO2_4text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO2_4

        //COMIENZO DE SEEKBAR VCO2_4b
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO2_4b = (SeekBar) findViewById(R.id.seekBarVCO2_4b);
        final TextView labelVCO2_4b = (TextView) findViewById(R.id.labelVCO2_4b);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO2_4b = 1.0f;
        float maxVCO2_4b = 63.0f;
        float minVCO2_4b = -64.0f;
        seekBarVCO2_4b.setMax((int) ((maxVCO2_4b - minVCO2_4b) / multiplicadorVCO2_4b));
        seekBarVCO2_4b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO2_offset";
                String labelVCO2_4btext = "offset";
                float multiplicador = 1.0f;
                float valorInicial = -64.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO2_4b", msj);
                Log.i("Valor   seekVCO2_4b", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO2_4b.setText(labelVCO2_4btext + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO2_4b

        //COMIENZO DE SEEKBAR VCO 2_5
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO2_5 = (SeekBar) findViewById(R.id.seekBarVCO2_5);
        final TextView labelVCO2_5 = (TextView) findViewById(R.id.labelVCO2_5);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO2_5 = 1.0f;
        float maxVCO2_5 = 100.0f;
        float minVCO2_5 = 0.0f;
        seekBarVCO2_5.setMax((int) ((maxVCO2_5 - minVCO2_5) / multiplicadorVCO2_5));
        seekBarVCO2_5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO2_pw";
                String labelVCO2_5text = "pw";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO2_5", msj);
                Log.i("Valor   seekVCO2_5", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO2_5.setText(labelVCO2_5text + ": " + value + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO2_5


        //TODOS LOS SLIDERS VCO3
        //COMIENZO DE SEEKBAR VCO 3_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO3_1 = (SeekBar) findViewById(R.id.seekBarVCO3_1);
        final TextView labelVCO3_1 = (TextView) findViewById(R.id.labelVCO3_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO3_1 = 0.01f;
        float maxVCO3_1 = 1.0f;
        float minVCO3_1 = 0.0f;
        seekBarVCO3_1.setMax((int) ((maxVCO3_1 - minVCO3_1) / multiplicadorVCO3_1));
        seekBarVCO3_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO3_att_freq0";
                String labelVCO3_1text = "att_freq0";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO3_1", msj);
                Log.i("Valor   seekVCO3_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO3_1.setText(labelVCO3_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO3_1

        //COMIENZO DE SEEKBAR VCO3_1b
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO3_1b = (SeekBar) findViewById(R.id.seekBarVCO3_1b);
        final TextView labelVCO3_1b = (TextView) findViewById(R.id.labelVCO3_1b);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO3_1b = 0.01f;
        float maxVCO3_1b = 1.0f;
        float minVCO3_1b = 0.0f;
        seekBarVCO3_1b.setMax((int) ((maxVCO3_1b - minVCO3_1b) / multiplicadorVCO3_1b));
        seekBarVCO3_1b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO3_att_freq1";
                String labelVCO3_1btext = "att_freq1";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO3_1b", msj);
                Log.i("Valor   seekVCO3_1b", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO3_1b.setText(labelVCO3_1btext + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO3_1b

        //COMIENZO DE SEEKBAR VCO 3_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO3_2 = (SeekBar) findViewById(R.id.seekBarVCO3_2);
        final TextView labelVCO3_2 = (TextView) findViewById(R.id.labelVCO3_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO3_2 = 0.01f;
        float maxVCO3_2 = 1.0f;
        float minVCO3_2 = 0.0f;
        seekBarVCO3_2.setMax((int) ((maxVCO3_2 - minVCO3_2) / multiplicadorVCO3_2));
        seekBarVCO3_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO3_att_pw";
                String labelVCO3_2text = "att_pw";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO3_2", msj);
                Log.i("Valor   seekVCO3_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO3_2.setText(labelVCO3_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO3_2
        //COMIENZO DE SEEKBAR VCO 3_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO3_3 = (SeekBar) findViewById(R.id.seekBarVCO3_3);
        final TextView labelVCO3_3 = (TextView) findViewById(R.id.labelVCO3_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO3_3 = 1.0f;
        float maxVCO3_3 = 3.0f;
        float minVCO3_3 = 0.0f;
        seekBarVCO3_3.setMax((int) ((maxVCO3_3 - minVCO3_3) / multiplicadorVCO3_3));
        seekBarVCO3_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO3_shape";
                String labelVCO3_3text = "waveform";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO3_3", msj);
                Log.i("Valor   seekVCO3_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                String tipoDeOnda = "sine";
                if (value == 0.0) {
                    tipoDeOnda = "sine";
                }
                if (value == 1.0) {
                    tipoDeOnda = "ramp";
                }
                if (value == 2.0) {
                    tipoDeOnda = "saw";
                }
                if (value == 3.0) {
                    tipoDeOnda = "trig";
                }
                if (value == 4.0) {
                    tipoDeOnda = "pulse";
                }
                labelVCO3_3.setText(labelVCO3_3text + ": " + tipoDeOnda);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO3_3
        //COMIENZO DE SEEKBAR VCO 3_4
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO3_4 = (SeekBar) findViewById(R.id.seekBarVCO3_4);
        final TextView labelVCO3_4 = (TextView) findViewById(R.id.labelVCO3_4);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO3_4 = 20.0f;
        float maxVCO3_4 = 20000.0f;
        float minVCO3_4 = 0.0f;
        seekBarVCO3_4.setMax((int) ((maxVCO3_4 - minVCO3_4) / multiplicadorVCO3_4));
        seekBarVCO3_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO3_freq";
                String labelVCO3_4text = "freq";
                float multiplicador = 20.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO3_4", msj);
                Log.i("Valor   seekVCO3_4", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO3_4.setText(labelVCO3_4text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO3_4

        //COMIENZO DE SEEKBAR VCO3_4b
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO3_4b = (SeekBar) findViewById(R.id.seekBarVCO3_4b);
        final TextView labelVCO3_4b = (TextView) findViewById(R.id.labelVCO3_4b);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO3_4b = 1.0f;
        float maxVCO3_4b = 63.0f;
        float minVCO3_4b = -64.0f;
        seekBarVCO3_4b.setMax((int) ((maxVCO3_4b - minVCO3_4b) / multiplicadorVCO3_4b));
        seekBarVCO3_4b.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO3_offset";
                String labelVCO3_4btext = "offset";
                float multiplicador = 1.0f;
                float valorInicial = -64.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO3_4b", msj);
                Log.i("Valor   seekVCO3_4b", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO3_4b.setText(labelVCO3_4btext + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO3_4b

        //COMIENZO DE SEEKBAR VCO 3_5
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCO3_5 = (SeekBar) findViewById(R.id.seekBarVCO3_5);
        final TextView labelVCO3_5 = (TextView) findViewById(R.id.labelVCO3_5);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCO3_5 = 1.0f;
        float maxVCO3_5 = 100.0f;
        float minVCO3_5 = 0.0f;
        seekBarVCO3_5.setMax((int) ((maxVCO3_5 - minVCO3_5) / multiplicadorVCO3_5));
        seekBarVCO3_5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCO3_pw";
                String labelVCO3_5text = "pw";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCO3_5", msj);
                Log.i("Valor   seekVCO3_5", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO3_5.setText(labelVCO3_5text + ": " + value + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO3_5


        //TODOS LOS SLIDERS VCA1
        //COMIENZO DE SEEKBAR VCA 1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCA1_1 = (SeekBar) findViewById(R.id.seekBarVCA1_1);
        final TextView labelVCA1_1 = (TextView) findViewById(R.id.labelVCA1_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCA1_1 = 0.01f;
        float maxVCA1_1 = 1.0f;
        float minVCA1_1 = 0.0f;
        seekBarVCA1_1.setMax((int) ((maxVCA1_1 - minVCA1_1) / multiplicadorVCA1_1));
        seekBarVCA1_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCA1_att_control";
                String labelVCA1_1text = "att_control";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCA1_1", msj);
                Log.i("Valor   seekVCA1_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCA1_1.setText(labelVCA1_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCA1_1
        //COMIENZO DE SEEKBAR VCA 1_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCA1_2 = (SeekBar) findViewById(R.id.seekBarVCA1_2);
        final TextView labelVCA1_2 = (TextView) findViewById(R.id.labelVCA1_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCA1_2 = 0.01f;
        float maxVCA1_2 = 1.0f;
        float minVCA1_2 = 0.0f;
        seekBarVCA1_2.setMax((int) ((maxVCA1_2 - minVCA1_2) / multiplicadorVCA1_2));
        seekBarVCA1_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCA1_base";
                String labelVCA1_2text = "base";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCA1_2", msj);
                Log.i("Valor   seekVCA1_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCA1_2.setText(labelVCA1_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCA1_2
        //COMIENZO DE SEEKBAR VCA 1_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCA1_3 = (SeekBar) findViewById(R.id.seekBarVCA1_3);
        final TextView labelVCA1_3 = (TextView) findViewById(R.id.labelVCA1_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCA1_3 = 1.0f;
        float maxVCA1_3 = 1.0f;
        float minVCA1_3 = 0.0f;
        seekBarVCA1_3.setMax((int) ((maxVCA1_3 - minVCA1_3) / multiplicadorVCA1_3));
        seekBarVCA1_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCA1_clip";
                String labelVCA1_3text = "clip";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCA1_3", msj);
                Log.i("Valor   seekVCA1_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCA1_3.setText(labelVCA1_3text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCA1_3


        //TODOS LOS SLIDERS VCA2
        //COMIENZO DE SEEKBAR VCA 2_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCA2_1 = (SeekBar) findViewById(R.id.seekBarVCA2_1);
        final TextView labelVCA2_1 = (TextView) findViewById(R.id.labelVCA2_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCA2_1 = 0.01f;
        float maxVCA2_1 = 1.0f;
        float minVCA2_1 = 0.0f;
        seekBarVCA2_1.setMax((int) ((maxVCA2_1 - minVCA2_1) / multiplicadorVCA2_1));
        seekBarVCA2_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCA2_att_control";
                String labelVCA2_1text = "att_control";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCA2_1", msj);
                Log.i("Valor   seekVCA2_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCA2_1.setText(labelVCA2_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCA2_1
        //COMIENZO DE SEEKBAR VCA 2_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCA2_2 = (SeekBar) findViewById(R.id.seekBarVCA2_2);
        final TextView labelVCA2_2 = (TextView) findViewById(R.id.labelVCA2_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCA2_2 = 0.01f;
        float maxVCA2_2 = 1.0f;
        float minVCA2_2 = 0.0f;
        seekBarVCA2_2.setMax((int) ((maxVCA2_2 - minVCA2_2) / multiplicadorVCA2_2));
        seekBarVCA2_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCA2_base";
                String labelVCA2_2text = "base";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCA2_2", msj);
                Log.i("Valor   seekVCA2_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCA2_2.setText(labelVCA2_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCA2_2
        //COMIENZO DE SEEKBAR VCA 2_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCA2_3 = (SeekBar) findViewById(R.id.seekBarVCA2_3);
        final TextView labelVCA2_3 = (TextView) findViewById(R.id.labelVCA2_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCA2_3 = 1.0f;
        float maxVCA2_3 = 1.0f;
        float minVCA2_3 = 0.0f;
        seekBarVCA2_3.setMax((int) ((maxVCA2_3 - minVCA2_3) / multiplicadorVCA2_3));
        seekBarVCA2_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCA2_clip";
                String labelVCA2_3text = "clip";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCA2_3", msj);
                Log.i("Valor   seekVCA2_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCA2_3.setText(labelVCA2_3text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCA2_3


        //TODOS LOS SLIDERS MIXER1
        //COMIENZO DE SEEKBAR MIXER1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarMIXER1_1 = (SeekBar) findViewById(R.id.seekBarMIXER1_1);
        final TextView labelMIXER1_1 = (TextView) findViewById(R.id.labelMIXER1_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorMIXER1_1 = 0.01f;
        float maxMIXER1_1 = 1.0f;
        float minMIXER1_1 = 0.0f;
        seekBarMIXER1_1.setMax((int) ((maxMIXER1_1 - minMIXER1_1) / multiplicadorMIXER1_1));
        seekBarMIXER1_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_MIX_ch1";
                String labelMIXER1_1text = "ch1";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekMIXER1_1", msj);
                Log.i("Valor   seekMIXER1_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelMIXER1_1.setText(labelMIXER1_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR MIXER1_1
        //COMIENZO DE SEEKBAR MIXER1_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarMIXER1_2 = (SeekBar) findViewById(R.id.seekBarMIXER1_2);
        final TextView labelMIXER1_2 = (TextView) findViewById(R.id.labelMIXER1_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorMIXER1_2 = 0.01f;
        float maxMIXER1_2 = 1.0f;
        float minMIXER1_2 = 0.0f;
        seekBarMIXER1_2.setMax((int) ((maxMIXER1_2 - minMIXER1_2) / multiplicadorMIXER1_2));
        seekBarMIXER1_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_MIX_ch2";
                String labelMIXER1_2text = "ch2";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekMIXER1_2", msj);
                Log.i("Valor   seekMIXER1_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelMIXER1_2.setText(labelMIXER1_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR MIXER1_2
        //COMIENZO DE SEEKBAR MIXER1_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarMIXER1_3 = (SeekBar) findViewById(R.id.seekBarMIXER1_3);
        final TextView labelMIXER1_3 = (TextView) findViewById(R.id.labelMIXER1_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorMIXER1_3 = 0.01f;
        float maxMIXER1_3 = 1.0f;
        float minMIXER1_3 = 0.0f;
        seekBarMIXER1_3.setMax((int) ((maxMIXER1_3 - minMIXER1_3) / multiplicadorMIXER1_3));
        seekBarMIXER1_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_MIX_ch3";
                String labelMIXER1_3text = "ch3";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekMIXER1_3", msj);
                Log.i("Valor   seekMIXER1_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelMIXER1_3.setText(labelMIXER1_3text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR MIXER1_3
        //COMIENZO DE SEEKBAR MIXER1_4
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarMIXER1_4 = (SeekBar) findViewById(R.id.seekBarMIXER1_4);
        final TextView labelMIXER1_4 = (TextView) findViewById(R.id.labelMIXER1_4);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorMIXER1_4 = 0.01f;
        float maxMIXER1_4 = 1.0f;
        float minMIXER1_4 = 0.0f;
        seekBarMIXER1_4.setMax((int) ((maxMIXER1_4 - minMIXER1_4) / multiplicadorMIXER1_4));
        seekBarMIXER1_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_MIX_ch4";
                String labelMIXER1_4text = "ch4";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekMIXER1_4", msj);
                Log.i("Valor   seekMIXER1_4", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelMIXER1_4.setText(labelMIXER1_4text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR MIXER1_4
        //COMIENZO DE SEEKBAR MIXER1_5
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarMIXER1_5 = (SeekBar) findViewById(R.id.seekBarMIXER1_5);
        final TextView labelMIXER1_5 = (TextView) findViewById(R.id.labelMIXER1_5);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorMIXER1_5 = 0.1f;
        float maxMIXER1_5 = 1.0f;
        float minMIXER1_5 = 0.0f;
        seekBarMIXER1_5.setMax((int) ((maxMIXER1_5 - minMIXER1_5) / multiplicadorMIXER1_5));
        seekBarMIXER1_5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_MIX_master";
                String labelMIXER1_5text = "master";
                float multiplicador = 0.1f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekMIXER1_5", msj);
                Log.i("Valor   seekMIXER1_5", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelMIXER1_5.setText(labelMIXER1_5text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR MIXER1_5


        //TODOS LOS SLIDERS VCF1
        //COMIENZO DE SEEKBAR VCF1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF1_1 = (SeekBar) findViewById(R.id.seekBarVCF1_1);
        final TextView labelVCF1_1 = (TextView) findViewById(R.id.labelVCF1_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCF1_1 = 0.01f;
        float maxVCF1_1 = 1.0f;
        float minVCF1_1 = 0.0f;
        seekBarVCF1_1.setMax((int) ((maxVCF1_1 - minVCF1_1) / multiplicadorVCF1_1));
        seekBarVCF1_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCF1_att_signal";
                String labelVCF1_1text = "att_signal";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCF1_1", msj);
                Log.i("Valor   seekVCF1_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCF1_1.setText(labelVCF1_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCF1_1
        //COMIENZO DE SEEKBAR VCF1_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF1_2 = (SeekBar) findViewById(R.id.seekBarVCF1_2);
        final TextView labelVCF1_2 = (TextView) findViewById(R.id.labelVCF1_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCF1_2 = 0.01f;
        float maxVCF1_2 = 1.0f;
        float minVCF1_2 = 0.0f;
        seekBarVCF1_2.setMax((int) ((maxVCF1_2 - minVCF1_2) / multiplicadorVCF1_2));
        seekBarVCF1_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCF1_att_freq";
                String labelVCF1_2text = "att_freq";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCF1_2", msj);
                Log.i("Valor   seekVCF1_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCF1_2.setText(labelVCF1_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCF1_2
        //COMIENZO DE SEEKBAR VCF1_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF1_3 = (SeekBar) findViewById(R.id.seekBarVCF1_3);
        final TextView labelVCF1_3 = (TextView) findViewById(R.id.labelVCF1_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCF1_3 = 1.0f;
        float maxVCF1_3 = 2.0f;
        float minVCF1_3 = 0.0f;
        seekBarVCF1_3.setMax((int) ((maxVCF1_3 - minVCF1_3) / multiplicadorVCF1_3));
        seekBarVCF1_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCF1_mode";
                String labelVCF1_3text = "mode";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCF1_3", msj);
                Log.i("Valor   seekVCF1_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                String tipoDeModo = "bandpass";
                if (value == 0.0) {
                    tipoDeModo = "bandpass";
                }
                if (value == 1.0) {
                    tipoDeModo = "lowpass";
                }
                if (value == 2.0) {
                    tipoDeModo = "highpass";
                }
                labelVCF1_3.setText(labelVCF1_3text + ": " + tipoDeModo);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCF1_3
        //COMIENZO DE SEEKBAR VCF1_4
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF1_4 = (SeekBar) findViewById(R.id.seekBarVCF1_4);
        final TextView labelVCF1_4 = (TextView) findViewById(R.id.labelVCF1_4);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCF1_4 = 15.0f;
        float maxVCF1_4 = 15000.0f;
        float minVCF1_4 = 0.0f;
        seekBarVCF1_4.setMax((int) ((maxVCF1_4 - minVCF1_4) / multiplicadorVCF1_4));
        seekBarVCF1_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCF1_freq";
                String labelVCF1_4text = "freq";
                float multiplicador = 15.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCF1_4", msj);
                Log.i("Valor   seekVCF1_4", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCF1_4.setText(labelVCF1_4text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCF1_4
        //COMIENZO DE SEEKBAR VCF1_5
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF1_5 = (SeekBar) findViewById(R.id.seekBarVCF1_5);
        final TextView labelVCF1_5 = (TextView) findViewById(R.id.labelVCF1_5);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCF1_5 = 1.0f;
        float maxVCF1_5 = 100.0f;
        float minVCF1_5 = 0.0f;
        seekBarVCF1_5.setMax((int) ((maxVCF1_5 - minVCF1_5) / multiplicadorVCF1_5));
        seekBarVCF1_5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCF1_q";
                String labelVCF1_5text = "q";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCF1_5", msj);
                Log.i("Valor   seekVCF1_5", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCF1_5.setText(labelVCF1_5text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCF1_5


        //TODOS LOS SLIDERS VCF2
        //COMIENZO DE SEEKBAR VCF2_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF2_1 = (SeekBar) findViewById(R.id.seekBarVCF2_1);
        final TextView labelVCF2_1 = (TextView) findViewById(R.id.labelVCF2_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCF2_1 = 0.01f;
        float maxVCF2_1 = 1.0f;
        float minVCF2_1 = 0.0f;
        seekBarVCF2_1.setMax((int) ((maxVCF2_1 - minVCF2_1) / multiplicadorVCF2_1));
        seekBarVCF2_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCF2_att_signal";
                String labelVCF2_1text = "att_signal";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCF2_1", msj);
                Log.i("Valor   seekVCF2_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCF2_1.setText(labelVCF2_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCF2_1
        //COMIENZO DE SEEKBAR VCF2_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF2_2 = (SeekBar) findViewById(R.id.seekBarVCF2_2);
        final TextView labelVCF2_2 = (TextView) findViewById(R.id.labelVCF2_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCF2_2 = 0.01f;
        float maxVCF2_2 = 1.0f;
        float minVCF2_2 = 0.0f;
        seekBarVCF2_2.setMax((int) ((maxVCF2_2 - minVCF2_2) / multiplicadorVCF2_2));
        seekBarVCF2_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCF2_att_freq";
                String labelVCF2_2text = "att_freq";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCF2_2", msj);
                Log.i("Valor   seekVCF2_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCF2_2.setText(labelVCF2_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCF2_2
        //COMIENZO DE SEEKBAR VCF2_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF2_3 = (SeekBar) findViewById(R.id.seekBarVCF2_3);
        final TextView labelVCF2_3 = (TextView) findViewById(R.id.labelVCF2_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCF2_3 = 1.0f;
        float maxVCF2_3 = 2.0f;
        float minVCF2_3 = 0.0f;
        seekBarVCF2_3.setMax((int) ((maxVCF2_3 - minVCF2_3) / multiplicadorVCF2_3));
        seekBarVCF2_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCF2_mode";
                String labelVCF2_3text = "mode";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCF2_3", msj);
                Log.i("Valor   seekVCF2_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                String tipoDeModo = "bandpass";
                if (value == 0.0) {
                    tipoDeModo = "bandpass";
                }
                if (value == 1.0) {
                    tipoDeModo = "lowpass";
                }
                if (value == 2.0) {
                    tipoDeModo = "highpass";
                }
                labelVCF2_3.setText(labelVCF2_3text + ": " + tipoDeModo);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCF2_3
        //COMIENZO DE SEEKBAR VCF2_4
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF2_4 = (SeekBar) findViewById(R.id.seekBarVCF2_4);
        final TextView labelVCF2_4 = (TextView) findViewById(R.id.labelVCF2_4);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCF2_4 = 15.0f;
        float maxVCF2_4 = 15000.0f;
        float minVCF2_4 = 0.0f;
        seekBarVCF2_4.setMax((int) ((maxVCF2_4 - minVCF2_4) / multiplicadorVCF2_4));
        seekBarVCF2_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCF2_freq";
                String labelVCF2_4text = "freq";
                float multiplicador = 15.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCF2_4", msj);
                Log.i("Valor   seekVCF2_4", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCF2_4.setText(labelVCF2_4text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCF2_4
        //COMIENZO DE SEEKBAR VCF2_5
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF2_5 = (SeekBar) findViewById(R.id.seekBarVCF2_5);
        final TextView labelVCF2_5 = (TextView) findViewById(R.id.labelVCF2_5);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCF2_5 = 1.0f;
        float maxVCF2_5 = 100.0f;
        float minVCF2_5 = 0.0f;
        seekBarVCF2_5.setMax((int) ((maxVCF2_5 - minVCF2_5) / multiplicadorVCF2_5));
        seekBarVCF2_5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_VCF2_q";
                String labelVCF2_5text = "q";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekVCF2_5", msj);
                Log.i("Valor   seekVCF2_5", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCF2_5.setText(labelVCF2_5text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCF2_5


        //TODOS LOS SLIDERS EG1
        //COMIENZO DE SEEKBAR EG1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG1_1 = (SeekBar) findViewById(R.id.seekBarEG1_1);
        final TextView labelEG1_1 = (TextView) findViewById(R.id.labelEG1_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorEG1_1 = 1.0f;
        float maxEG1_1 = 5000.0f;
        float minEG1_1 = 0.0f;
        seekBarEG1_1.setMax((int) ((maxEG1_1 - minEG1_1) / multiplicadorEG1_1));
        seekBarEG1_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_EG1_attack";
                String labelEG1_1text = "attack";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekEG1_1", msj);
                Log.i("Valor   seekEG1_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelEG1_1.setText(labelEG1_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR EG1_1
        //COMIENZO DE SEEKBAR EG1_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG1_2 = (SeekBar) findViewById(R.id.seekBarEG1_2);
        final TextView labelEG1_2 = (TextView) findViewById(R.id.labelEG1_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorEG1_2 = 1.0f;
        float maxEG1_2 = 5000.0f;
        float minEG1_2 = 0.0f;
        seekBarEG1_2.setMax((int) ((maxEG1_2 - minEG1_2) / multiplicadorEG1_2));
        seekBarEG1_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_EG1_decay";
                String labelEG1_2text = "decay";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekEG1_2", msj);
                Log.i("Valor   seekEG1_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelEG1_2.setText(labelEG1_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR EG1_2
        //COMIENZO DE SEEKBAR EG1_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG1_3 = (SeekBar) findViewById(R.id.seekBarEG1_3);
        final TextView labelEG1_3 = (TextView) findViewById(R.id.labelEG1_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorEG1_3 = 0.01f;
        float maxEG1_3 = 1.0f;
        float minEG1_3 = 0.0f;
        seekBarEG1_3.setMax((int) ((maxEG1_3 - minEG1_3) / multiplicadorEG1_3));
        seekBarEG1_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_EG1_sustain";
                String labelEG1_3text = "sustain";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekEG1_3", msj);
                Log.i("Valor   seekEG1_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelEG1_3.setText(labelEG1_3text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR EG1_3
        //COMIENZO DE SEEKBAR EG1_4
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG1_4 = (SeekBar) findViewById(R.id.seekBarEG1_4);
        final TextView labelEG1_4 = (TextView) findViewById(R.id.labelEG1_4);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorEG1_4 = 1.0f;
        float maxEG1_4 = 5000.0f;
        float minEG1_4 = 0.0f;
        seekBarEG1_4.setMax((int) ((maxEG1_4 - minEG1_4) / multiplicadorEG1_4));
        seekBarEG1_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_EG1_release";
                String labelEG1_4text = "release";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekEG1_4", msj);
                Log.i("Valor   seekEG1_4", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelEG1_4.setText(labelEG1_4text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR EG1_4
        //COMIENZO DE SEEKBAR EG1_5
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG1_5 = (SeekBar) findViewById(R.id.seekBarEG1_5);
        final TextView labelEG1_5 = (TextView) findViewById(R.id.labelEG1_5);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorEG1_5 = 1.0f;
        float maxEG1_5 = 1.0f;
        float minEG1_5 = 0.0f;
        seekBarEG1_5.setMax((int) ((maxEG1_5 - minEG1_5) / multiplicadorEG1_5));
        seekBarEG1_5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_EG1_gate";
                String labelEG1_5text = "gate";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekEG1_5", msj);
                Log.i("Valor   seekEG1_5", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelEG1_5.setText(labelEG1_5text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR EG1_5


        //TODOS LOS SLIDERS EG2
        //COMIENZO DE SEEKBAR EG2_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG2_1 = (SeekBar) findViewById(R.id.seekBarEG2_1);
        final TextView labelEG2_1 = (TextView) findViewById(R.id.labelEG2_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorEG2_1 = 1.0f;
        float maxEG2_1 = 5000.0f;
        float minEG2_1 = 0.0f;
        seekBarEG2_1.setMax((int) ((maxEG2_1 - minEG2_1) / multiplicadorEG2_1));
        seekBarEG2_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_EG2_attack";
                String labelEG2_1text = "attack";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekEG2_1", msj);
                Log.i("Valor   seekEG2_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelEG2_1.setText(labelEG2_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR EG2_1
        //COMIENZO DE SEEKBAR EG2_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG2_2 = (SeekBar) findViewById(R.id.seekBarEG2_2);
        final TextView labelEG2_2 = (TextView) findViewById(R.id.labelEG2_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorEG2_2 = 1.0f;
        float maxEG2_2 = 5000.0f;
        float minEG2_2 = 0.0f;
        seekBarEG2_2.setMax((int) ((maxEG2_2 - minEG2_2) / multiplicadorEG2_2));
        seekBarEG2_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_EG2_decay";
                String labelEG2_2text = "decay";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekEG2_2", msj);
                Log.i("Valor   seekEG2_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelEG2_2.setText(labelEG2_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR EG2_2
        //COMIENZO DE SEEKBAR EG2_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG2_3 = (SeekBar) findViewById(R.id.seekBarEG2_3);
        final TextView labelEG2_3 = (TextView) findViewById(R.id.labelEG2_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorEG2_3 = 0.01f;
        float maxEG2_3 = 1.0f;
        float minEG2_3 = 0.0f;
        seekBarEG2_3.setMax((int) ((maxEG2_3 - minEG2_3) / multiplicadorEG2_3));
        seekBarEG2_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_EG2_sustain";
                String labelEG2_3text = "sustain";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekEG2_3", msj);
                Log.i("Valor   seekEG2_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelEG2_3.setText(labelEG2_3text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR EG2_3
        //COMIENZO DE SEEKBAR EG2_4
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG2_4 = (SeekBar) findViewById(R.id.seekBarEG2_4);
        final TextView labelEG2_4 = (TextView) findViewById(R.id.labelEG2_4);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorEG2_4 = 1.0f;
        float maxEG2_4 = 5000.0f;
        float minEG2_4 = 0.0f;
        seekBarEG2_4.setMax((int) ((maxEG2_4 - minEG2_4) / multiplicadorEG2_4));
        seekBarEG2_4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_EG2_release";
                String labelEG2_4text = "release";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekEG2_4", msj);
                Log.i("Valor   seekEG2_4", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelEG2_4.setText(labelEG2_4text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR EG2_4
        //COMIENZO DE SEEKBAR EG2_5
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG2_5 = (SeekBar) findViewById(R.id.seekBarEG2_5);
        final TextView labelEG2_5 = (TextView) findViewById(R.id.labelEG2_5);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorEG2_5 = 1.0f;
        float maxEG2_5 = 1.0f;
        float minEG2_5 = 0.0f;
        seekBarEG2_5.setMax((int) ((maxEG2_5 - minEG2_5) / multiplicadorEG2_5));
        seekBarEG2_5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_EG2_gate";
                String labelEG2_5text = "gate";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekEG2_5", msj);
                Log.i("Valor   seekEG2_5", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelEG2_5.setText(labelEG2_5text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR EG2_5


        //TODOS LOS SLIDERS SH1
        //COMIENZO DE SEEKBAR SH1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarSH1_1 = (SeekBar) findViewById(R.id.seekBarSH1_1);
        final TextView labelSH1_1 = (TextView) findViewById(R.id.labelSH1_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorSH1_1 = 0.01f;
        float maxSH1_1 = 1.0f;
        float minSH1_1 = 0.0f;
        seekBarSH1_1.setMax((int) ((maxSH1_1 - minSH1_1) / multiplicadorSH1_1));
        seekBarSH1_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_SH_att_signal";
                String labelSH1_1text = "att_signal";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seekSH1_1", msj);
                Log.i("Valor   seekSH1_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelSH1_1.setText(labelSH1_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR SH1_1
    }
 /**   private void esconderMatriz() {

        MyGridView grid_view = (MyGridView) findViewById(R.id.grid_view);
        grid_view.setVisibility(View.GONE);
        View espacioBlancoMatriz1 = findViewById(R.id.espacioBlancoMatriz1);
        espacioBlancoMatriz1.setVisibility(View.GONE);
        View lineaNegraMatriz1 = findViewById(R.id.lineaNegraMatriz1);
        lineaNegraMatriz1.setVisibility(View.GONE);
        View lineaNegraMatriz2 = findViewById(R.id.lineaNegraMatriz2);
        lineaNegraMatriz2.setVisibility(View.GONE);
        View espacioBlancoMatriz2 = findViewById(R.id.espacioBlancoMatriz2);
        espacioBlancoMatriz2.setVisibility(View.GONE);
    }**/
/**
    private void esconderPresets(){
        TableLayout tablaPresets = (TableLayout) findViewById(R.id.tablaPresets);
        tablaPresets.setVisibility(View.GONE);
    }
**/
    private void esconderControles() {
        //ESCONDER TODOS LOS CONTROLES HASTA ABRIR ARCHIVO
        TableLayout tablaComponentes = (TableLayout) findViewById(R.id.tablaComponentes);
        tablaComponentes.setVisibility(View.GONE);
        TableLayout tablaVCO1 = (TableLayout) findViewById(R.id.tablaVCO1);
        tablaVCO1.setVisibility(View.GONE);
        TableLayout tablaVCO2 = (TableLayout) findViewById(R.id.tablaVCO2);
        tablaVCO2.setVisibility(View.GONE);
        TableLayout tablaVCO3 = (TableLayout) findViewById(R.id.tablaVCO3);
        tablaVCO3.setVisibility(View.GONE);
        TableLayout tablaVCA1 = (TableLayout) findViewById(R.id.tablaVCA1);
        tablaVCA1.setVisibility(View.GONE);
        TableLayout tablaVCA2 = (TableLayout) findViewById(R.id.tablaVCA2);
        tablaVCA2.setVisibility(View.GONE);
        TableLayout tablaMIXER1 = (TableLayout) findViewById(R.id.tablaMIXER1);
        tablaMIXER1.setVisibility(View.GONE);
        TableLayout tablaVCF1 = (TableLayout) findViewById(R.id.tablaVCF1);
        tablaVCF1.setVisibility(View.GONE);
        TableLayout tablaVCF2 = (TableLayout) findViewById(R.id.tablaVCF2);
        tablaVCF2.setVisibility(View.GONE);
        TableLayout tablaEG1 = (TableLayout) findViewById(R.id.tablaEG1);
        tablaEG1.setVisibility(View.GONE);
        TableLayout tablaEG2 = (TableLayout) findViewById(R.id.tablaEG2);
        tablaEG2.setVisibility(View.GONE);
        TableLayout tablaSH1 = (TableLayout) findViewById(R.id.tablaSH1);
        tablaSH1.setVisibility(View.GONE);

    }
/**
    private void esconderPiano() {
        Piano pianito = (Piano) findViewById(R.id.pianito);
        pianito.setVisibility(View.GONE);
        TextView log_box = (TextView) findViewById(R.id.log_box);
        log_box.setVisibility(View.GONE);**/

        /**TableLayout tablaBotonesOctavas = (TableLayout) findViewById(R.id.tablaBotonesOctavas);
         tablaBotonesOctavas.setVisibility(View.GONE);**/
    //}

    private void mostrarPiano() {
        Button solapa1 = (Button) findViewById(R.id.solapa1);
        solapa1.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        Button solapa2 = (Button) findViewById(R.id.solapa2);
        solapa2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa3 = (Button) findViewById(R.id.solapa3);
        solapa3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa4 = (Button) findViewById(R.id.solapa4);
        solapa4.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa5 = (Button) findViewById(R.id.solapa5);
        solapa5.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa6 = (Button) findViewById(R.id.solapa6);
        solapa6.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Piano pianito = (Piano) findViewById(R.id.pianito);
        pianito.setVisibility(View.VISIBLE);
        TextView log_box = (TextView) findViewById(R.id.log_box);
        log_box.setVisibility(View.VISIBLE);

        /**TableLayout tablaBotonesOctavas = (TableLayout) findViewById(R.id.tablaBotonesOctavas);
         tablaBotonesOctavas.setVisibility(View.VISIBLE);**/
    }

    private void mostrarPresets(){
        Button solapa1 = (Button) findViewById(R.id.solapa1);
        solapa1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa2 = (Button) findViewById(R.id.solapa2);
        solapa2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa3 = (Button) findViewById(R.id.solapa3);
        solapa3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa4 = (Button) findViewById(R.id.solapa4);
        solapa4.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        Button solapa5 = (Button) findViewById(R.id.solapa5);
        solapa5.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa6 = (Button) findViewById(R.id.solapa6);
        solapa6.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        TableLayout tablaPresets = (TableLayout) findViewById(R.id.tablaPresets);
        tablaPresets.setVisibility(View.VISIBLE);
    }

    private void mostrarControles() {
        //MOSTRAR TODOS LOS CONTROLES LUEGO DE ABRIR ARCHIVO
        Button solapa1 = (Button) findViewById(R.id.solapa1);
        solapa1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa2 = (Button) findViewById(R.id.solapa2);
        solapa2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa3 = (Button) findViewById(R.id.solapa3);
        solapa3.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        Button solapa4 = (Button) findViewById(R.id.solapa4);
        solapa4.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa5 = (Button) findViewById(R.id.solapa5);
        solapa5.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa6 = (Button) findViewById(R.id.solapa6);
        solapa6.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        TableLayout tablaComponentes = (TableLayout) findViewById(R.id.tablaComponentes);
        tablaComponentes.setVisibility(View.VISIBLE);

        TableLayout tablaVCO1 = (TableLayout) findViewById(R.id.tablaVCO1);
        tablaVCO1.setVisibility(View.VISIBLE);
        TableLayout tablaVCO2 = (TableLayout) findViewById(R.id.tablaVCO2);
        tablaVCO2.setVisibility(View.GONE);
        TableLayout tablaVCO3 = (TableLayout) findViewById(R.id.tablaVCO3);
        tablaVCO3.setVisibility(View.GONE);
        TableLayout tablaVCA1 = (TableLayout) findViewById(R.id.tablaVCA1);
        tablaVCA1.setVisibility(View.GONE);
        TableLayout tablaVCA2 = (TableLayout) findViewById(R.id.tablaVCA2);
        tablaVCA2.setVisibility(View.GONE);
        TableLayout tablaMIXER1 = (TableLayout) findViewById(R.id.tablaMIXER1);
        tablaMIXER1.setVisibility(View.GONE);
        TableLayout tablaVCF1 = (TableLayout) findViewById(R.id.tablaVCF1);
        tablaVCF1.setVisibility(View.GONE);
        TableLayout tablaVCF2 = (TableLayout) findViewById(R.id.tablaVCF2);
        tablaVCF2.setVisibility(View.GONE);
        TableLayout tablaEG1 = (TableLayout) findViewById(R.id.tablaEG1);
        tablaEG1.setVisibility(View.GONE);
        TableLayout tablaEG2 = (TableLayout) findViewById(R.id.tablaEG2);
        tablaEG2.setVisibility(View.GONE);
        TableLayout tablaSH1 = (TableLayout) findViewById(R.id.tablaSH1);
        tablaSH1.setVisibility(View.GONE);

        Button botonVCO1 = (Button) findViewById(R.id.botonVCO1);
        Button botonVCO2 = (Button) findViewById(R.id.botonVCO2);
        Button botonVCO3 = (Button) findViewById(R.id.botonVCO3);
        Button botonVCA1 = (Button) findViewById(R.id.botonVCA1);
        Button botonVCA2 = (Button) findViewById(R.id.botonVCA2);
        Button botonMIXER1 = (Button) findViewById(R.id.botonMIXER1);
        Button botonVCF1 = (Button) findViewById(R.id.botonVCF1);
        Button botonVCF2 = (Button) findViewById(R.id.botonVCF2);
        Button botonEG1 = (Button) findViewById(R.id.botonEG1);
        Button botonEG2 = (Button) findViewById(R.id.botonEG2);
        Button botonSH1 = (Button) findViewById(R.id.botonSH1);
        botonVCO1.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        botonVCO2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonVCO3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonVCA1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonVCA2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonMIXER1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonVCF1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonVCF2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonEG1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonEG2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        botonSH1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);

    }
/**
    private void mostrarMatriz() {
        Button solapa1 = (Button) findViewById(R.id.solapa1);
        solapa1.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa2 = (Button) findViewById(R.id.solapa2);
        solapa2.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa3 = (Button) findViewById(R.id.solapa3);
        solapa3.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa4 = (Button) findViewById(R.id.solapa4);
        solapa4.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);
        Button solapa5 = (Button) findViewById(R.id.solapa5);
        solapa5.getBackground().setColorFilter(0xFF00FF00, PorterDuff.Mode.MULTIPLY);
        Button solapa6 = (Button) findViewById(R.id.solapa6);
        solapa6.getBackground().setColorFilter(0x33999999, PorterDuff.Mode.MULTIPLY);

        MyGridView grid_view = (MyGridView) findViewById(R.id.grid_view);
        grid_view.setVisibility(View.VISIBLE);

    }**/

/**
    public void setTamanioGrilla() {
        int CantColumnas = 17; //SIN LOS TITULOS CANTIDAD ERA 12 (CON PD TEST5 ERA 13, ahora 17 porque piden 16)
        int CantFilas = 27; // SIN LOS TITULOS CANTIDAD ERA 23 (CON PD TEST5 ERA 24, ahora 27 porque piden 26)
        //int CantColumnas = 3;
        //int CantFilas = 5;
        //MyGridView list;
        ArrayList<String> data = new ArrayList<>();**/
        //CON ESTE CODIGO LLENA TODA LA MATRIZ CON SU POSICION
        /*
        for (int i = 0; i < CantFilas; i++) {
            for (int j = 0; j < CantColumnas; j++)
                data.add(j + "-" + i);
        }
        */
        //CON ESTE CODIGO PONE PRIMERA COLUMNA NOMBRES
     /**   for (int i = -1; i < CantFilas-1; i++) {
            for (int j = -1; j < CantColumnas-1; j++)
                //PONGO NOMBRES A PRIMERA COLUMNA
                if (j == -1 & i == -1){data.add("");}
                else if (j == -1 & i == 0){data.add("0");}
                else if (j == -1 & i == 1){data.add("1");}
                else if (j == -1 & i == 2){data.add("2");}
                else if (j == -1 & i == 3){data.add("3");}
                else if (j == -1 & i == 4){data.add("4");}
                else if (j == -1 & i == 5){data.add("5");}
                else if (j == -1 & i == 6){data.add("6");}
                else if (j == -1 & i == 7){data.add("7");}
                else if (j == -1 & i == 8){data.add("8");}
                else if (j == -1 & i == 9){data.add("9");}
                else if (j == -1 & i == 10){data.add("10");}
                else if (j == -1 & i == 11){data.add("11");}
                else if (j == -1 & i == 12){data.add("12");}
                else if (j == -1 & i == 13){data.add("13");}
                else if (j == -1 & i == 14){data.add("14");}
                else if (j == -1 & i == 15){data.add("15");}
                else if (j == -1 & i == 16){data.add("16");}
                else if (j == -1 & i == 17){data.add("17");}
                else if (j == -1 & i == 18){data.add("18");}
                else if (j == -1 & i == 19){data.add("19");}
                else if (j == -1 & i == 20){data.add("20");}
                else if (j == -1 & i == 21){data.add("21");}
                else if (j == -1 & i == 22){data.add("22");}
                else if (j == -1 & i == 23){data.add("23");}
                else if (j == -1 & i == 24){data.add("24");}
                else if (j == -1 & i == 25){data.add("25");}
                //PONGO NOMBRES A PRIMERA FILA
                else if (i == -1 & j == 0){data.add("0");}
                else if (i == -1 & j == 1){data.add("1");}
                else if (i == -1 & j == 2){data.add("2");}
                else if (i == -1 & j == 3){data.add("3");}
                else if (i == -1 & j == 4){data.add("4");}
                else if (i == -1 & j == 5){data.add("5");}
                else if (i == -1 & j == 6){data.add("6");}
                else if (i == -1 & j == 7){data.add("7");}
                else if (i == -1 & j == 8){data.add("8");}
                else if (i == -1 & j == 9){data.add("9");}
                else if (i == -1 & j == 10){data.add("10");}
                else if (i == -1 & j == 11){data.add("11");}
                else if (i == -1 & j == 12){data.add("12");}
                else if (i == -1 & j == 13){data.add("13");}
                else if (i == -1 & j == 14){data.add("14");}
                else if (i == -1 & j == 15){data.add("15");}
                //EL RESTO DE LAS POSICIONES
                else {data.add(j + "-" + i);}
        }

        GridViewCustomAdapter adapter = new GridViewCustomAdapter(this, data);
        //agregado por tema de scroll
        //list = (MyGridView) findViewById(R.id.grid_view);
        if (matriz_modulos != null) {
            matriz_modulos.setNumColumns(CantColumnas);
            matriz_modulos.setAdapter(adapter);
        }
    }
**/
}