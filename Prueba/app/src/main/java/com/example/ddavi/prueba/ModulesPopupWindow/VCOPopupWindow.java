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


    public VCOPopupWindow(MainActivity container, int layout, String name){
        super(container,layout,name);
    }

    @Override
    public void initializeModule(String title, View view) {
        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);
        //ACA COMIENZAN LOS CONTROLES FINALES
        //TODOS LOS SLIDERS VCO1
        //COMIENZO DE SEEKBAR VCO 1_1
        //1) MOSTRAR SEEKBAR

        SeekBar seekBarVCO1_1 = (SeekBar) view.findViewById(R.id.seekBar_att_freq0);
        final TextView labelVCO1_1 = (TextView) view.findViewById(R.id.label_att_freq0);
        final String name = title;

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
                String msj = "X_"+name+"_att_freq0";
                String labelVCO1_1text = "att_freq0";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;

                DecimalFormat decimales = new DecimalFormat("0.000");
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_1", msj);
                Log.i("Valor   seek"+name+"_1", decimales.format(value));//String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_1.setText(labelVCO1_1text + ": " + decimales.format(value));
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
        SeekBar seekBarVCO1_1b = (SeekBar) view.findViewById(R.id.seekBar_att_freq1);
        final TextView labelVCO1_1b = (TextView) view.findViewById(R.id.label_att_freq1);
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
                String msj = "X_"+name+"_att_freq1";
                String labelVCO1_1btext = "att_freq1";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;

                DecimalFormat decimales = new DecimalFormat("0.000");
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_1b", msj);
                Log.i("Valor   seek"+name+"_1b", decimales.format(value));//String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_1b.setText(labelVCO1_1btext + ": " + decimales.format(value));
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
        SeekBar seekBarVCO1_2 = (SeekBar) view.findViewById(R.id.seekBar_att_pw);
        final TextView labelVCO1_2 = (TextView) view.findViewById(R.id.label_att_pw);
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
                String msj = "X_"+name+"_att_pw";
                String labelVCO1_2text = "att_pw";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;

                DecimalFormat decimales = new DecimalFormat("0.000");
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_2", msj);
                Log.i("Valor   seek"+name+"_2",decimales.format(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_2.setText(labelVCO1_2text + ": " + decimales.format(value));
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
        SeekBar seekBarVCO1_3 = (SeekBar) view.findViewById(R.id.seekBar_waveform);
        final TextView labelVCO1_3 = (TextView) view.findViewById(R.id.label_waveform);
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
                String msj = "X_"+name+"_shape";
                String labelVCO1_3text = "waveform";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;

                DecimalFormat decimales = new DecimalFormat("0.000");
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_3", msj);
                Log.i("Valor   seek"+name+"_3", decimales.format(value));
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
        SeekBar seekBarVCO1_4 = (SeekBar) view.findViewById(R.id.seekBar_freq);
        final TextView labelVCO1_4 = (TextView) view.findViewById(R.id.label_freq);
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
                String msj = "X_"+name+"_freq";
                String labelVCO1_4text = "freq";
                float multiplicador = 20.0f;
                float valorInicial = 0.0f;

                DecimalFormat decimales = new DecimalFormat("0.000");
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_4", msj);
                Log.i("Valor   seek"+name+"_4", decimales.format(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_4.setText(labelVCO1_4text + ": " + decimales.format(value));
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
        SeekBar seekBarVCO1_4b = (SeekBar) view.findViewById(R.id.seekBar_offset);
        final TextView labelVCO1_4b = (TextView) view.findViewById(R.id.label_offset);
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
                String msj = "X_"+name+"_offset";
                String labelVCO1_4btext = "offset";
                float multiplicador = 1.0f;
                float valorInicial = -64.0f;

                DecimalFormat decimales = new DecimalFormat("0.000");
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_4b", msj);
                Log.i("Valor   seek"+name+"_4b", decimales.format(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_4b.setText(labelVCO1_4btext + ": " + decimales.format(value));
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
        SeekBar seekBarVCO1_5 = (SeekBar) view.findViewById(R.id.seekBar_pw);
        final TextView labelVCO1_5 = (TextView) view.findViewById(R.id.label_pw);
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
                String msj = "X_"+name+"_pw";
                String labelVCO1_5text = "pw";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;

                DecimalFormat decimales = new DecimalFormat("0.000");
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_5", msj);
                Log.i("Valor   seek"+name+"_5", decimales.format(value));
                PdBase.sendFloat(msj, value);
                labelVCO1_5.setText(labelVCO1_5text + ": " + decimales.format(value) + "%");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCO1_5
    }
}