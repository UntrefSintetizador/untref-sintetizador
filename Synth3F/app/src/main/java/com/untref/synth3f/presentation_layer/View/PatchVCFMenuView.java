package com.untref.synth3f.presentation_layer.View;

import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCFPatch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

import org.puredata.core.PdBase;


public class PatchVCFMenuView extends PatchMenuView {

    public PatchVCFMenuView(MainActivity container, int layout, String name, PatchPresenter patchPresenter, Patch patch) {
        super(container, layout, name, patchPresenter, patch);
    }

    @Override
    public void initializeModule(String title, View view) {

        VCFPatch vcfPatch = (VCFPatch) patch;

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBarVCF1_0, R.id.labelVCF1_0, "on-off", title, 1.0f, 0.0f, 1.0f, 1.0f, view, vcfPatch.on_off, MenuScale.linear);
        createSeekBarComponent(R.id.seekBarVCF1_1, R.id.labelVCF1_1, "att_signal", title, 1000.0f, 0.0f, 0.01f, 0.01f, view, vcfPatch.att_signal, MenuScale.exponential_left);
        createSeekBarComponent(R.id.seekBarVCF1_2, R.id.labelVCF1_2, "att_freq", title, 1000.0f, 0.0f, 0.01f, 0.01f, view, vcfPatch.att_freq, MenuScale.exponential_left);
        createSeekBarMode(title, view, vcfPatch.mode);
        createSeekBarComponent(R.id.seekBarVCF1_4, R.id.labelVCF1_4, "freq", title, 15000.0f, 0.0f, 15.0f, 15.0f, view, vcfPatch.freq, MenuScale.exponential_left);
        createSeekBarComponent(R.id.seekBarVCF1_5, R.id.labelVCF1_5, "q", title, 100.0f, 0.0f, 1.0f, 1.0f, view, vcfPatch.q, MenuScale.linear);
    }

    private void createSeekBarMode(String title, View view, float value) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);
        final String name = title;

        SeekBar seekBarVCF = (SeekBar) view.findViewById(R.id.seekBarVCF1_3);
        final TextView labelVCF = (TextView) view.findViewById(R.id.labelVCF1_3);
        float multiplicadorVCF = 1.0f;
        float maxVCF1_3 = 2.0f;
        float minVCF1_3 = 0.0f;
        seekBarVCF.setMax((int) ((maxVCF1_3 - minVCF1_3) / multiplicadorVCF));
        seekBarVCF.setProgress((int) ((value - minVCF1_3) / multiplicadorVCF));
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
        labelVCF.setText("mode : " + tipoDeModo);
        seekBarVCF.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "x_" + name + "_mode";
                String labelVCF1_3text = "mode";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
//                Log.i("Mensaje seek"+name+"_3", msj);
//                Log.i("Valor   seek"+name+"_3", String.valueOf(value));
                //container.masterConfig.sendValue(msj , value);
                patchPresenter.setValue(msj, value);
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
                labelVCF.setText(labelVCF1_3text + ": " + tipoDeModo);
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
