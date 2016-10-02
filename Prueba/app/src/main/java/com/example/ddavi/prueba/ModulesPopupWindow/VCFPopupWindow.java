package com.example.ddavi.prueba.ModulesPopupWindow;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ddavi.prueba.MainActivity;
import com.example.ddavi.prueba.R;

import org.puredata.core.PdBase;

/**
 * Created by ddavi on 2/10/2016.
 */

public class VCFPopupWindow extends ModulePopupWindow {

    public VCFPopupWindow(MainActivity container, int layout, String name){
        super(container,layout,name);
    }

    @Override
    public void initializeModule(String title, View view) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);
        final String name = title;

        //TODOS LOS SLIDERS VCF1
        //COMIENZO DE SEEKBAR VCF1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCF1_1 = (SeekBar) view.findViewById(R.id.seekBarVCF1_1);
        final TextView labelVCF1_1 = (TextView) view.findViewById(R.id.labelVCF1_1);
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
                String msj = "X_"+name+"_att_signal";
                String labelVCF1_1text = "att_signal";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_1", msj);
                Log.i("Valor   seek"+name+"_1", String.valueOf(value));
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
        SeekBar seekBarVCF1_2 = (SeekBar) view.findViewById(R.id.seekBarVCF1_2);
        final TextView labelVCF1_2 = (TextView) view.findViewById(R.id.labelVCF1_2);
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
                String msj = "X_"+name+"_att_freq";
                String labelVCF1_2text = "att_freq";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_2", msj);
                Log.i("Valor   seek"+name+"_2", String.valueOf(value));
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
        SeekBar seekBarVCF1_3 = (SeekBar) view.findViewById(R.id.seekBarVCF1_3);
        final TextView labelVCF1_3 = (TextView) view.findViewById(R.id.labelVCF1_3);
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
                String msj = "X_"+name+"_mode";
                String labelVCF1_3text = "mode";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_3", msj);
                Log.i("Valor   seek"+name+"_3", String.valueOf(value));
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
        SeekBar seekBarVCF1_4 = (SeekBar) view.findViewById(R.id.seekBarVCF1_4);
        final TextView labelVCF1_4 = (TextView) view.findViewById(R.id.labelVCF1_4);
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
                String msj = "X_"+name+"_freq";
                String labelVCF1_4text = "freq";
                float multiplicador = 15.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_4", msj);
                Log.i("Valor   seek"+name+"_4", String.valueOf(value));
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
        SeekBar seekBarVCF1_5 = (SeekBar) view.findViewById(R.id.seekBarVCF1_5);
        final TextView labelVCF1_5 = (TextView) view.findViewById(R.id.labelVCF1_5);
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
                String msj = "X_"+name+"_q";
                String labelVCF1_5text = "q";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_5", msj);
                Log.i("Valor   seek"+name+"_5", String.valueOf(value));
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

    }
}
