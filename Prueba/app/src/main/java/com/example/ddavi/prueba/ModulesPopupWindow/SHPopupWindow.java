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

public class SHPopupWindow  extends ModulePopupWindow{

    public SHPopupWindow(MainActivity container, int layout, String name){
        super(container,layout,name);
    }

    @Override
    public void initializeModule(String title, View view) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);
        final String name = title;

        //TODOS LOS SLIDERS SH1
        //COMIENZO DE SEEKBAR SH1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarSH1_1 = (SeekBar) view.findViewById(R.id.seekBarSH1_1);
        final TextView labelSH1_1 = (TextView) view.findViewById(R.id.labelSH1_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorSH1_1 = 0.01f;
        float maxSH1_1 = 1.0f;
        float minSH1_1 = 0.0f;
        seekBarSH1_1.setMax((int) ((maxSH1_1 - minSH1_1) / multiplicadorSH1_1));
        seekBarSH1_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_"+name+"_att_signal";
                String labelSH1_1text = "att_signal";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"1_1", msj);
                Log.i("Valor   seek"+name+"1_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelSH1_1.setText(labelSH1_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR SH1_1
    }
}
