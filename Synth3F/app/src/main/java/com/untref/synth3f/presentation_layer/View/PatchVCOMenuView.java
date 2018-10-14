package com.untref.synth3f.presentation_layer.View;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.entities.VCOPatch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

import java.text.DecimalFormat;

public class PatchVCOMenuView extends PatchMenuView {

    public PatchVCOMenuView(MainActivity container, int layout, PatchPresenter patchPresenter, Patch patch) {
        super(container, layout, patchPresenter, patch);
    }

    @Override
    public void initializeModule(String title, View view) {

        VCOPatch vcoPatch = (VCOPatch) patch;

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarComponent(R.id.seekBar_VCO0, R.id.label_VCO0, "on-off", title, 1.0f, 0.0f, 1.0f, 1.0f, view, vcoPatch.on_off, MenuScale.LINEAR);
        createSeekBarComponent(R.id.seekBar_att_freq0, R.id.label_att_freq0, "att_freq0", title, 100.0f, -100.0f, 0.01f, 0.01f, view, vcoPatch.att_freq0, MenuScale.EXPONENTIAL_CENTER);
        createSeekBarComponent(R.id.seekBar_att_freq1, R.id.label_att_freq1, "att_freq1", title, 100.0f, -100.0f, 0.01f, 0.01f, view, vcoPatch.att_freq1, MenuScale.EXPONENTIAL_CENTER);
        createSeekBarComponent(R.id.seekBar_att_pw, R.id.label_att_pw, "att_pw", title, 100.0f, -100.0f, 1.0f, 1.0f, view, vcoPatch.att_pw, MenuScale.EXPONENTIAL_CENTER);
        createSeekBarShape(title, view, vcoPatch.shape);
        createSeekBarComponent(R.id.seekBar_freq, R.id.label_freq, "freq", title, 20000.0f, 0.0f, 20.0f, 20.0f, view, vcoPatch.freq, MenuScale.EXPONENTIAL_LEFT);
        createSeekBarComponent(R.id.seekBar_offset, R.id.label_offset, "offset", title, 63.0f, -64.0f, 1.0f, 1.0f, view, vcoPatch.offset, MenuScale.LINEAR);
        createSeekBarComponent(R.id.seekBar_pw, R.id.label_pw, "pw", title, 100.0f, 0.0f, 1.0f, 1.0f, view, vcoPatch.pw, MenuScale.LINEAR);
    }

    private void createSeekBarShape(final String name, View view, float value) {
        SeekBar seekBarVCO = (SeekBar) view.findViewById(R.id.seekBar_waveform);
        final TextView labelVCO = (TextView) view.findViewById(R.id.label_waveform);
        float multiplicadorVCO = 1.0f;
        float maxVCO = 4.0f;
        float minVCO = 0.0f;
        seekBarVCO.setMax((int) ((maxVCO - minVCO) / multiplicadorVCO));
        seekBarVCO.setProgress((int) ((value - minVCO) / multiplicadorVCO));
        String tipo_onda[] = {"sine", "ramp", "saw", "trig", "pulse"};
        labelVCO.setText("waveform : " + tipo_onda[(int) value]);
        seekBarVCO.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "x_" + name + "_shape";
                String labelVCO1_3text = "waveform";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;

                DecimalFormat decimales = new DecimalFormat("0.000");
                float value = (float) (valorInicial + (progress * multiplicador));
//                Log.i("Mensaje seek"+name+"_3", msj);
//                Log.i("Valor   seek"+name+"_3", decimales.format(value));

                //container.masterConfig.sendValue(msj , value);
                String tipo_onda[] = {"sine", "ramp", "saw", "trig", "pulse"};

                ((VCOPatch) patch).shape = value;
                patchPresenter.setValue(msj, value);

                labelVCO.setText(labelVCO1_3text + ": " + tipo_onda[(int) value]);
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