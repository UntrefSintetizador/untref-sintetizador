package com.untref.synth3f.domain_layer.helpers;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

import org.puredata.android.io.AudioParameters;
import org.puredata.android.service.PdPreferences;
import org.puredata.android.service.PdService;
import org.puredata.core.PdBase;
import org.puredata.core.PdReceiver;
import org.puredata.core.utils.IoUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.BIND_AUTO_CREATE;

public class PureDataConfig implements IConfig {
    private PdService service = null;
    private MainActivity context;
    private Toast toast = null;
    private PureDataProcessor processor;

    private ServiceConnection pdConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PureDataConfig.this.service = ((PdService.PdBinder) service).getService();
            initPd();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // this method will never be called
        }
    };

    private PdReceiver receiver = new PdReceiver() {

        private void pdPost(String msg) {
            toast("Pure Data says, \"" + msg + "\"");
        }

        @Override
        public void print(String s) {
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

    @Override
    public void startAudio() {
        String name = context.getResources().getString(R.string.app_name);

        try {
            service.initAudio(-1, 0, -1, -1);   // negative values will be replaced with defaults/preferences
            service.startAudio();
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
            this.initializePureData();
        }
    }

    @Override
    public void setContext(MainActivity context) {
        this.context = context;
        this.initializePureData();
        processor = new PureDataProcessor();
    }

    @Override
    public boolean isServiceRunning() {
        return service.isRunning();
    }

    @Override
    public IProcessor getProcessor() {
        return processor;
    }

    public void toast(final String msg) {

        context.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (toast == null) {
                    toast = Toast.makeText(context.getApplicationContext(), "", Toast.LENGTH_SHORT);
                }
                toast.setText("Synth3F" + ": " + msg);
                toast.show();
            }
        });
    }

    private Map<String, Integer> getDictionaryFilesPD() {
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        dictionary.put("attenuator.pd", R.raw.attenuator);
        dictionary.put("cell.pd", R.raw.cell);
        dictionary.put("param.pd", R.raw.param);
        dictionary.put("ramp.pd", R.raw.ramp);
        dictionary.put("saw.pd", R.raw.saw);
        dictionary.put("sine.pd", R.raw.sine);
        dictionary.put("square.pd", R.raw.square);
        dictionary.put("triangle.pd", R.raw.triangle);
        dictionary.put("x_dac.pd", R.raw.x_dac);
        dictionary.put("x_eg.pd", R.raw.x_eg);
        dictionary.put("x_fade.pd", R.raw.x_fade);
        dictionary.put("x_kb.pd", R.raw.x_kb);
        dictionary.put("x_lfo.pd", R.raw.x_lfo);
        dictionary.put("x_mix.pd", R.raw.x_mix);
        dictionary.put("x_ng.pd", R.raw.x_ng);
        dictionary.put("x_seq.pd", R.raw.x_seq);
        dictionary.put("x_sh.pd", R.raw.x_sh);
        dictionary.put("x_vca.pd", R.raw.x_vca);
        dictionary.put("x_vcf.pd", R.raw.x_vcf);
        dictionary.put("x_vco.pd", R.raw.x_vco);

        return dictionary;
    }

    private void initializePureData() {
        AudioParameters.init(context);
        PdPreferences.initPreferences(context.getApplicationContext());
        PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext()).registerOnSharedPreferenceChangeListener(context);

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
            Map<String, Integer> dictionary = getDictionaryFilesPD();
            Iterator it = dictionary.keySet().iterator();

            while (it.hasNext()) {
                String key = (String) it.next();
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
            in = res.openRawResource(R.raw.empty);
            patchFile = IoUtils.extractResource(in, "empty.pd", context.getCacheDir());
            PdBase.openPatch(patchFile);

            //ESTO INICIA EL SONIDO, ANTES ESTABA AL PRESIONAR PLAY
            if (service.isRunning()) {
                stopAudio();

            } else {
                startAudio();
                //PUSE MEJOR UN PRESET QUE NO QUEDE SONANDO
                PdBase.sendFloat("sq_bass1", 1);
            }

        } catch (IOException e) {
            Log.e("Synth3F", e.toString());
            context.finish();

        } finally {

            if (patchFile != null) {
                patchFile.delete();
            }
        }
    }
}
