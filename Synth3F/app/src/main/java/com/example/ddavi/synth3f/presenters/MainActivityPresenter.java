package com.example.ddavi.synth3f.presenters;

import android.content.res.AssetManager;
import android.view.View;

import com.example.ddavi.synth3f.helpers.MasterConfig;
import com.example.ddavi.synth3f.ModulesPopupWindow.EGPopupWindow;
import com.example.ddavi.synth3f.ModulesPopupWindow.MIXPopupWindow;
import com.example.ddavi.synth3f.ModulesPopupWindow.ModulePopupWindow;
import com.example.ddavi.synth3f.ModulesPopupWindow.SHPopupWindow;
import com.example.ddavi.synth3f.ModulesPopupWindow.VCAPopupWindow;
import com.example.ddavi.synth3f.ModulesPopupWindow.VCFPopupWindow;
import com.example.ddavi.synth3f.ModulesPopupWindow.VCOPopupWindow;
import com.example.ddavi.synth3f.R;
import com.example.ddavi.synth3f.activity.MainActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ddavi on 26/2/2017.
 */

public class MainActivityPresenter {

    private MainActivity context;
    Map<String,ModulePopupWindow> sliders;
    private MasterConfig masterConfig;

    public MainActivityPresenter(MainActivity context){
        this.context = context;
        this.sliders = new HashMap<>();
        this.createPopUpSliders();
    }

    public Map<String, ModulePopupWindow> getSliders() {
        return sliders;
    }

    /*****************************************************************************
     *
     *                  INICIALIZACION DE MODELO DE NEGOCIO
     *
     *****************************************************************************/

    public void initializeMasterConfig(){
        try{

            JSONObject configJson = new JSONObject(this.loadConfigFile());
            masterConfig = new MasterConfig(configJson);
            masterConfig.setProcessorActivity(context);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
        Para este metodo verificar que las rutas en archivo que esta en
         ..\app\src\main\assets\app.json sean las correctas. Si el archivo no existe crearlo
     */
    public String loadConfigFile(){
        String json = null;
        AssetManager manager = context.getAssets();
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

    public boolean isServiceRunning(){
        return masterConfig.config.isServiceRunning();
    }

    public void startService(){

        if (isServiceRunning()) {
            masterConfig.config.startAudio();
        }
    }

    public void stopService(){
        masterConfig.config.stopAudio();
    }

    public void unBindService(){
        masterConfig.config.cleanup();
    }

    public void resetPresets(){
        masterConfig.resetPresets();
    }

    public void setPreset(String preset){
        masterConfig.setPreset(preset);
    }

    /********************************************************************************************
     *
                                   CREACION DE POPUP WINDOWS
     *
     * ******************************************************************************************/
    private ModulePopupWindow createPopUpWindow(String name, String name_popUp){
        ModulePopupWindow popupWindow = null;

        switch (name) {
            case "VCO": popupWindow = new VCOPopupWindow(context, R.layout.popup_vco,name_popUp); break;
            case "VCA": popupWindow = new VCAPopupWindow(context, R.layout.popup_vca,name_popUp); break;
            case "VCF": popupWindow = new VCFPopupWindow(context, R.layout.popup_vcf,name_popUp); break;
            case "EG":  popupWindow = new EGPopupWindow(context, R.layout.popup_eg,name_popUp); break;
            case "SH":  popupWindow = new SHPopupWindow(context, R.layout.popup_sh,name_popUp); break;
            case "MIX": popupWindow = new MIXPopupWindow(context, R.layout.popup_mix,name_popUp); break;
        }

        sliders.put(name_popUp,popupWindow);
        return popupWindow;
    }

    public ArrayList<String> getNameSliders(){
        ArrayList<String> slids = new ArrayList<String>(sliders.keySet());

        Collections.sort(slids, new Comparator<String>() {
            @Override
            public int compare(String b2, String b1) {
                return (b2.compareTo(b1));
            }
        });
        ArrayList<String> sliders2 = new ArrayList<>();
        sliders2.add("Seleccionar Modulo");
        sliders2.addAll(slids);

        return sliders2;
    }

    public void createPopUpSliders(){
        String[] VCOSliders = {"VCO1","VCO2","VCO3"};
        String[] VCASliders = {"VCA1","VCA2"};
        String[] MIXSliders = {"MIX"};
        String[] VCFSliders = {"VCF1","VCF2"};
        String[] EGSliders = {"EG1","EG2"};
        String[] SHSliders = {"SH"};

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

    public void onClickSelected(View v, String name_modulo){
            ModulePopupWindow popup = getSliders().get(name_modulo.replace(" ",""));
            popup.showAsDropDown(v, 150,-500);
            popup.setButton(v);

    }
}
