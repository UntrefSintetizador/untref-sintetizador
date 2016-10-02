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

public class EGPopupWindow extends ModulePopupWindow {

    public EGPopupWindow(MainActivity container, int layout, String name){
        super(container,layout,name);
    }

    @Override
    public void initializeModule(String title, View view) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);
        final String name = title;

        //TODOS LOS SLIDERS EG1
        //COMIENZO DE SEEKBAR EG1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarEG1_1 = (SeekBar) view.findViewById(R.id.seekBarEG1_1);
        final TextView labelEG1_1 = (TextView) view.findViewById(R.id.labelEG1_1);
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
                String msj = "X_"+name+"_attack";
                String labelEG1_1text = "attack";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_1", msj);
                Log.i("Valor   seek"+name+"_1", String.valueOf(value));
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
        SeekBar seekBarEG1_2 = (SeekBar) view.findViewById(R.id.seekBarEG1_2);
        final TextView labelEG1_2 = (TextView) view.findViewById(R.id.labelEG1_2);
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
                String msj = "X_"+name+"_decay";
                String labelEG1_2text = "decay";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_2", msj);
                Log.i("Valor   seek"+name+"_2", String.valueOf(value));
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
        SeekBar seekBarEG1_3 = (SeekBar) view.findViewById(R.id.seekBarEG1_3);
        final TextView labelEG1_3 = (TextView) view.findViewById(R.id.labelEG1_3);
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
                String msj = "X_"+name+"_sustain";
                String labelEG1_3text = "sustain";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_3", msj);
                Log.i("Valor   seek"+name+"_3", String.valueOf(value));
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
        SeekBar seekBarEG1_4 = (SeekBar) view.findViewById(R.id.seekBarEG1_4);
        final TextView labelEG1_4 = (TextView) view.findViewById(R.id.labelEG1_4);
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
                String msj = "X_"+name+"_release";
                String labelEG1_4text = "release";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_4", msj);
                Log.i("Valor   seek"+name+"_4", String.valueOf(value));
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
        SeekBar seekBarEG1_5 = (SeekBar) view.findViewById(R.id.seekBarEG1_5);
        final TextView labelEG1_5 = (TextView) view.findViewById(R.id.labelEG1_5);
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
                String msj = "X_"+name+"_gate";
                String labelEG1_5text = "gate";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_5", msj);
                Log.i("Valor   seek"+name+"_5", String.valueOf(value));
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

    }
}
