package com.example.ddavi.prueba.ModulesPopupWindow;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ddavi.prueba.MainActivity;
import com.example.ddavi.prueba.R;

import org.puredata.core.PdBase;

import java.text.DecimalFormat;

/**
 * Created by ddavi on 1/10/2016.
 */

public class VCOPopupWindow extends ModulePopupWindow {

    MainActivity container;

    public VCOPopupWindow(MainActivity container, int layout, String name){
        super(container,layout,name);
        this.container = container;
    }

    @Override
    public void initializeModule(String title, View view) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBar_att_freq0,R.id.label_att_freq0,"att_freq0",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBar_att_freq1,R.id.label_att_freq1,"att_freq1",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarComponent(R.id.seekBar_att_pw,R.id.label_att_pw,"att_pw",title,1.0f,0.0f,0.01f,0.01f,view);
        createSeekBarShape(title,view);
        createSeekBarComponent(R.id.seekBar_freq,R.id.label_freq,"freq",title,20000.0f,0.0f,20.0f,20.0f,view);
        createSeekBarComponent(R.id.seekBar_offset,R.id.label_offset,"offset",title,63.0f,-64.0f,1.0f,1.0f,view);
        createSeekBarComponent(R.id.seekBar_pw,R.id.label_pw,"pw",title,100.0f,0.0f,1.0f,1.0f,view);
    }

    private void createSeekBarShape(final String name, View view){
        SeekBar seekBarVCO = (SeekBar) view.findViewById(R.id.seekBar_waveform);
        final TextView labelVCO = (TextView) view.findViewById(R.id.label_waveform);
        float multiplicadorVCO = 1.0f;
        float maxVCO = 4.0f;
        float minVCO = 0.0f;
        seekBarVCO.setMax((int) ((maxVCO - minVCO) / multiplicadorVCO));
        seekBarVCO.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_"+name+"_shape";
                String labelVCO1_3text = "waveform";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;

                DecimalFormat decimales = new DecimalFormat("0.000");
                float value = (float) (valorInicial + (progress * multiplicador));
                Log.i("Mensaje seek"+name+"_3", msj);
                Log.i("Valor   seek"+name+"_3", decimales.format(value));

                container.masterConfig.sendValue(msj , value);
                String tipo_onda[] = {"sine","ramp","saw","trig","pulse"};

                labelVCO.setText(labelVCO1_3text + ": " + tipo_onda[(int)value]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
    }

}