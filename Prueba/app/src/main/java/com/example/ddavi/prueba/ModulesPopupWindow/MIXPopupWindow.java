package com.example.ddavi.prueba.ModulesPopupWindow;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ddavi.prueba.MainActivity;
import com.example.ddavi.prueba.ModulesPopupWindow.ModulePopupWindow;
import com.example.ddavi.prueba.R;

import org.puredata.core.PdBase;

/**
 * Created by ddavi on 2/10/2016.
 */

public class MIXPopupWindow extends ModulePopupWindow {

    public MIXPopupWindow(MainActivity container, int layout, String name){
        super(container,layout,name);
    }

    @Override
    public void initializeModule(String title, View view) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);
        final String name = title;
        //TODOS LOS SLIDERS MIXER1
        //COMIENZO DE SEEKBAR MIXER1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarMIXER1_1 = (SeekBar) view.findViewById(R.id.seekBarMIXER1_1);
        final TextView labelMIXER1_1 = (TextView) view.findViewById(R.id.labelMIXER1_1);
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
                String msj = "X_"+name+"_ch1";
                String labelMIXER1_1text = "ch1";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"ER1_1", msj);
                Log.i("Valor   seek"+name+"ER1_1", String.valueOf(value));
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
        SeekBar seekBarMIXER1_2 = (SeekBar) view.findViewById(R.id.seekBarMIXER1_2);
        final TextView labelMIXER1_2 = (TextView) view.findViewById(R.id.labelMIXER1_2);
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
                String msj = "X_"+name+"_ch2";
                String labelMIXER1_2text = "ch2";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"ER1_2", msj);
                Log.i("Valor   seek"+name+"ER1_2", String.valueOf(value));
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
        SeekBar seekBarMIXER1_3 = (SeekBar) view.findViewById(R.id.seekBarMIXER1_3);
        final TextView labelMIXER1_3 = (TextView) view.findViewById(R.id.labelMIXER1_3);
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
                String msj = "X_"+name+"_ch3";
                String labelMIXER1_3text = "ch3";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"ER1_3", msj);
                Log.i("Valor   seek"+name+"ER1_3", String.valueOf(value));
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
        SeekBar seekBarMIXER1_4 = (SeekBar) view.findViewById(R.id.seekBarMIXER1_4);
        final TextView labelMIXER1_4 = (TextView) view.findViewById(R.id.labelMIXER1_4);
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
                String msj = "X_"+name+"_ch4";
                String labelMIXER1_4text = "ch4";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"ER1_4", msj);
                Log.i("Valor   seek"+name+"ER1_4", String.valueOf(value));
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
        SeekBar seekBarMIXER1_5 = (SeekBar) view.findViewById(R.id.seekBarMIXER1_5);
        final TextView labelMIXER1_5 = (TextView) view.findViewById(R.id.labelMIXER1_5);
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
                String msj = "X_"+name+"_master";
                String labelMIXER1_5text = "master";
                float multiplicador = 0.1f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"ER1_5", msj);
                Log.i("Valor   seek"+name+"ER1_5", String.valueOf(value));
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
    }
}
