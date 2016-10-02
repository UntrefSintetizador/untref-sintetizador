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
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity implements OnEditorActionListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private FragmentTabHost tabHost;

    //esto es de test
    private static final String TAG = "XUL";
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
    private TextView logs;
    private PdService pdService = null;
    private Toast toast = null;
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
        initializePureData();
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

}