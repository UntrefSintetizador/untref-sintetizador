package com.untref.synth3f.domain_layer.helpers;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Pair;
import android.widget.TextView;
import android.widget.Toast;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Connection;
import com.untref.synth3f.entities.Patch;
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
    private String mainPatchName = "pd-empty.pd";
    private HashMap<Integer, Integer> PDids;
    private PdService service = null;
    private MainActivity context;
    private TextView logs;
    private Toast toast = null;
    private static final String TAG = "XUL";
    private final Handler handler = new Handler();
    private List<Pair<String, Integer>> toDelete = new ArrayList<>();
    private final PatchCollector patchCollector = new PatchCollector();

    private ServiceConnection pdConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PureDataProcessor.this.service = ((PdService.PdBinder) service).getService();
            PDids = new HashMap<>();
            initPd();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // this method will never be called
        }
    };

    @Override
    public boolean isServiceRunning() {
        return this.getService().isRunning();
    }

    @Override
    public void setContext(MainActivity activity) {
        this.context = activity;
        this.initializePureData();
    }

    public PdService getService() {
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

    private Map<String, Integer> getDictionaryFilesPD() {
        Map<String, Integer> dictionary = new HashMap<String, Integer>();
        dictionary.put("x_vco.pd", R.raw.x_vco);
        dictionary.put("cell.pd", R.raw.cell);
        dictionary.put("x_eg.pd", R.raw.x_eg);
        dictionary.put("x_mix.pd", R.raw.x_mix);
        dictionary.put("x_mtx.pd", R.raw.x_mtx);
        dictionary.put("x_ng.pd", R.raw.x_ng);
        dictionary.put("x_sh.pd", R.raw.x_sh);
        dictionary.put("x_vca.pd", R.raw.x_vca);
        dictionary.put("x_vcf.pd", R.raw.x_vcf);
        dictionary.put("x_kb.pd", R.raw.x_kb);
        dictionary.put("x_kb_.pd", R.raw.x_kb_);
        dictionary.put("x_seq.pd", R.raw.x_seq);
        dictionary.put("x_dac.pd", R.raw.x_dac);
        dictionary.put("param.pd", R.raw.param);
        dictionary.put("x_fade.pd", R.raw.x_fade);
        dictionary.put("attenuator.pd", R.raw.attenuator);

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

    private void initializePureData() {
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
            Log.e(TAG, e.toString());
            context.finish();

        } finally {

            if (patchFile != null) {
                patchFile.delete();
            }
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
    public void resetPresets() {
        PdBase.sendFloat("reset_presets", (float) 1);
    }

    @Override
    public void setPreset(String name, Float f) {
        this.sendValue(name, f);
    }

    @Override
    public void sendValue(String name, Float value) {
        Log.i(name, Float.toString(value));
        PdBase.sendFloat(name, value);
    }

    @Override
    public void createPatch(String type, int patchId) {
        int newId = PDids.size();
        String name = "x_" + type + "_" + Integer.toString(patchId);
        PDids.put(patchId, newId);
        Object[] array = {10, 10, "x_" + type, name};
        Log.i("Create", mainPatchName + " " + "obj" + " 10 10 " + "x_" + type + " " + name);
        PdBase.sendMessage(mainPatchName, "obj", array);
    }

    @Override
    public void connect(Connection connection) {
        int newId = PDids.size();
        int sourcePatch = PDids.get(connection.getSourcePatch());
        int targetPatch = PDids.get(connection.getTargetPatch());
        String fadeName = "x_fade_" + Integer.toString(connection.getId());
        Object[] array = {10, 10, "x_fade", fadeName};
        PDids.put(connection.getId(), newId);
        PdBase.sendMessage(mainPatchName, "obj", array);
        Log.i("Create Fade", mainPatchName + " " + "obj" + " 10 10 x_fade " + fadeName);
        array = new Object[]{sourcePatch, connection.getSourceOutlet(), newId, 0};
        PdBase.sendMessage(mainPatchName, "connect", array);
        Log.i("Connect", mainPatchName + " " + "connect " + sourcePatch + " " + connection.getSourceOutlet() + " " + newId + " 0");
        array = new Object[]{newId, 0, targetPatch, connection.getTargetInlet()};
        PdBase.sendMessage(mainPatchName, "connect", array);
        Log.i("Connect", mainPatchName + " " + "connect " + newId + " 0 " + targetPatch + " " + connection.getTargetInlet());
        PdBase.sendFloat(fadeName, 1);
    }

    @Override
    public void delete(Patch patch) {

        for (Connection connection : patch.getOutputConnections()) {
            PdBase.sendFloat("x_fade_" + Integer.toString(connection.getId()), 0);
            toDelete.add(new Pair<>("x_fade_" + Integer.toString(connection.getId()), connection.getId()));
        }

        for (Connection connection : patch.getInputConnections()) {
            PdBase.sendFloat("x_fade_" + Integer.toString(connection.getId()), 0);
            toDelete.add(new Pair<>("x_fade_" + Integer.toString(connection.getId()), connection.getId()));
        }

        toDelete.add(new Pair<>("x_" + patch.getTypeName() + "_" + patch.getId(), patch.getId()));
        handler.postDelayed(patchCollector, 100);
    }

    @Override
    public void clear(Patch[] patches) {

        Object[] array = {"", 1};

        for (Patch patch : patches) {

            for (Connection connection : patch.getOutputConnections()) {
                array[0] = "x_fade_" + Integer.toString(connection.getId());
                PdBase.sendMessage(mainPatchName, "find", array);
                PdBase.sendMessage(mainPatchName, "cut", array);
            }

            for (Connection connection : patch.getInputConnections()) {
                array[0] = "x_fade_" + Integer.toString(connection.getId());
                PdBase.sendMessage(mainPatchName, "find", array);
                PdBase.sendMessage(mainPatchName, "cut", array);
            }

            array[0] = "x_" + patch.getTypeName() + "_" + patch.getId();
            PdBase.sendMessage(mainPatchName, "find", array);
            PdBase.sendMessage(mainPatchName, "cut", array);

        }

        toDelete.clear();
        PDids.clear();
    }

    @Override
    public void evaluateMessage(String s) {
        String dest = "test", symbol = null;
        boolean isAny = s.length() > 0 && s.charAt(0) == ';';
        Scanner sc = new Scanner(isAny ? s.substring(1) : s);

        if (isAny) {

            if (sc.hasNext()) {
                dest = sc.next();

            } else {
                toast("Message not sent (empty recipient)");
                return;
            }

            if (sc.hasNext()) {
                symbol = sc.next();

            } else {
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

    private class PatchCollector implements Runnable {

        @Override
        public void run() {
            Object[] array = {"", 1};

            for (Pair<String, Integer> patch : toDelete) {
                Integer position = PDids.remove(patch.second);

                for (Integer key : PDids.keySet()) {
                    Integer value = PDids.get(key);

                    if (value > position) {
                        PDids.put(key, value - 1);
                    }
                }

                array[0] = patch.first;
                PdBase.sendMessage(mainPatchName, "find", array);
                PdBase.sendMessage(mainPatchName, "cut", array);
            }

            toDelete.clear();
        }
    }
}
