package com.untref.synth3f.presentation_layer.View;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.LFOPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

import java.text.DecimalFormat;

public class PatchLFOMenuView extends PatchMenuView {

    public PatchLFOMenuView(MainActivity container, int layout, PatchPresenter patchPresenter, Patch patch) {
        super(container, layout, patchPresenter, patch);
    }

    @Override
    public void initializeModule(String title, View view) {

        LFOPatch lfoPatch = (LFOPatch) patch;

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBarLFO1_1, R.id.labelLFO1_1, "on-off", title, 1.0f, 0.0f, 1.0f, 1.0f, view, lfoPatch.on_off, MenuScale.LINEAR);
        createSeekBarComponent(R.id.seekBarLFO1_2, R.id.labelLFO1_2, "att_freq0", title, 100.0f, -100.0f, 1.0f, 1.0f, view, lfoPatch.att_freq0, MenuScale.EXPONENTIAL_CENTER);
        createSeekBarComponent(R.id.seekBarLFO1_3, R.id.labelLFO1_3, "att_pw", title, 100.0f, -100.0f, 1.0f, 1.0f, view, lfoPatch.att_pw, MenuScale.EXPONENTIAL_CENTER);
        createSeekBarShape(title, view, lfoPatch.shape);
        createSeekBarComponent(R.id.seekBarLFO1_5, R.id.labelLFO1_5, "freq", title, 100.0f, 0.0f, 1.00f, 1.00f, view, lfoPatch.freq, MenuScale.EXPONENTIAL_LEFT);
        createSeekBarComponent(R.id.seekBarLFO1_6, R.id.labelLFO1_6, "BPM", title, 6000.0f, 0.0f, 1.00f, 1.00f, view, lfoPatch.BPM, MenuScale.EXPONENTIAL_LEFT);
        createSeekBarComponent(R.id.seekBarLFO1_7, R.id.labelLFO1_7, "pw", title, 100.0f, 0.0f, 1.0f, 1.0f, view, lfoPatch.pw, MenuScale.LINEAR);
    }

    private void createSeekBarShape(final String name, View view, float value) {
        SeekBar seekBarLFO = (SeekBar) view.findViewById(R.id.seekBarLFO1_4);
        final TextView labelLFO = (TextView) view.findViewById(R.id.labelLFO1_4);
        float multiplicadorLFO = 1.0f;
        float maxLFO = 4.0f;
        float minLFO = 0.0f;
        seekBarLFO.setMax((int) ((maxLFO - minLFO) / multiplicadorLFO));
        seekBarLFO.setProgress((int) ((value - minLFO) / multiplicadorLFO));
        String tipo_onda[] = {"sine", "ramp", "saw", "trig", "pulse"};
        labelLFO.setText("waveform : " + tipo_onda[(int) value]);
        seekBarLFO.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "x_" + name + "_shape";
                String labelLFO1_3text = "waveform";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;

                DecimalFormat decimales = new DecimalFormat("0.000");
                float value = (float) (valorInicial + (progress * multiplicador));
//                Log.i("Mensaje seek"+name+"_3", msj);
//                Log.i("Valor   seek"+name+"_3", decimales.format(value));

                //container.masterConfig.sendValue(msj , value);
                String tipo_onda[] = {"sine", "ramp", "saw", "trig", "pulse"};

                ((LFOPatch) patch).shape = value;
                patchPresenter.setValue(msj, value);

                labelLFO.setText(labelLFO1_3text + ": " + tipo_onda[(int) value]);
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
