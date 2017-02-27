package com.example.ddavi.synth3f.helpers;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ddavi.synth3f.R;
import com.example.ddavi.synth3f.activity.MainActivity;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by oargueyo on 31/10/16.
 */

public class PureDataProcessor extends BaseProcessor {
    private PdService service = null;

    private MainActivity context;
    private TextView logs;
    private Toast toast = null;

    private static final String TAG = "XUL";

    private ServiceConnection pdConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PureDataProcessor.this.service = ((PdService.PdBinder)service).getService();
            initPd();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // this method will never be called
        }
    };

    @Override
    public boolean isServiceRunning(){
        return this.getService().isRunning();
    }

    @Override
    public void setContext(MainActivity activity) {
        this.context = activity;
        this.initializePureData();

    }

    public PdService getService(){
        return service;
    }
    public void toast(final String msg) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
                }
                toast.setText(TAG + ": " + msg);
                toast.show();
            }
        });
    }

    private Map<String,Integer> getDictionaryFilesPD(){
        Map<String,Integer> dictionary = new HashMap<String,Integer>();
        dictionary.put("x_vco1.pd", R.raw.x_vco1);
        dictionary.put("x_vco2.pd",R.raw.x_vco2);
        dictionary.put("x_vco3.pd",R.raw.x_vco3);
        dictionary.put("cell.pd",R.raw.cell);
        dictionary.put("x_eg1.pd",R.raw.x_eg1);
        dictionary.put("x_eg2.pd",R.raw.x_eg2);
        dictionary.put("x_mix.pd",R.raw.x_mix);
        dictionary.put("x_mtx.pd",R.raw.x_mtx);
        dictionary.put("x_ng.pd",R.raw.x_ng);
        dictionary.put("x_sh.pd",R.raw.x_sh);
        dictionary.put("x_vca1.pd",R.raw.x_vca1);
        dictionary.put("x_vca2.pd",R.raw.x_vca2);
        dictionary.put("x_vcf1.pd",R.raw.x_vcf1);
        dictionary.put("x_vcf2.pd",R.raw.x_vcf2);
        dictionary.put("x_kb.pd",R.raw.x_kb);
        dictionary.put("x_seq.pd",R.raw.x_seq);

        return dictionary;
    }

    private void post(final String s) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //logs.append(s + ((s.endsWith("\n")) ? "" : "\n"));
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

    private void initializePureData(){

        AudioParameters.init(context);
        PdPreferences.initPreferences(context.getApplicationContext());
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).registerOnSharedPreferenceChangeListener(context);

        //logs = (TextView) context.findViewById(R.id.log_box);
        //logs.setMovementMethod(new ScrollingMovementMethod());

        context.bindService(new Intent(context, PdService.class), pdConnection, BIND_AUTO_CREATE);
    }

    private void initPd() {
        Resources res = context.getResources();
        File patchFile = null;
        InputStream in;

        try {
            PdBase.setReceiver(receiver);
            PdBase.subscribe("android");
            //agregada apertura de todos los PD que SYNTH necesita
            Map<String,Integer> dictionary = getDictionaryFilesPD();
            Iterator it = dictionary.keySet().iterator();
            while(it.hasNext()) {
                String key = (String)it.next();
                in = res.openRawResource(dictionary.get(key));
                IoUtils.extractResource(in, key, context.getCacheDir());
            }
            in = res.openRawResource(R.raw.presets);
            File patchFile_presets = IoUtils.extractResource(in, "presets.pd", context.getCacheDir());
            PdBase.openPatch(patchFile_presets);
            //apertura original de 1 PD
            in = res.openRawResource(R.raw.synth);
            patchFile = IoUtils.extractResource(in, "synth.pd", context.getCacheDir());
            PdBase.openPatch(patchFile);

            //ESTO INICIA EL SONIDO, ANTES ESTABA AL PRESIONAR PLAY
            if (service.isRunning()) {
                stopAudio();
            } else {
                startAudio();
                //PUSE MEJOR UN PRESET QUE NO QUEDE SONANDO
                PdBase.sendFloat("sq_bass1",1);
            }

        } catch (IOException e) {
            Log.e(TAG, e.toString());
            context.finish();
        } finally {
            if (patchFile != null) patchFile.delete();
        }
    }

    @Override
    public void startAudio() {
        String name = context.getResources().getString(R.string.app_name);
        try {
            service.initAudio(-1, -1, -1, -1);   // negative values will be replaced with defaults/preferences
            service.startAudio(new Intent(context, MainActivity.class), R.drawable.icon_synth, name, "Return to " + name + ".");
        } catch (IOException e) {
            toast(e.toString());
        }
    }

    @Override
    public void stopAudio() {
        service.stopAudio();
    }

    @Override
    public void cleanup() {
        try {
            context.unbindService(pdConnection);
        } catch (IllegalArgumentException e) {
            // already unbound
            service = null;
        }
    }

    @Override
    public void resetPresets(){
        PdBase.sendFloat("reset_presets",(float)1);
    }

    @Override
    public void setPreset(String name , Float f){
        this.sendValue(name , f);
    }

    @Override
    public void sendValue(String name , Float value){
        PdBase.sendFloat(name , value);
    }

    @Override
    public void evaluateMessage(String s) {
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
