package com.untref.synth3f.presentation_layer.ModulesPopupWindow;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.presentation_layer.activity.MainActivity;

import org.puredata.core.PdBase;

import java.sql.Timestamp;

/**
 * Created by ddavi on 2/10/2016.
 */

public class SequencerPopupWindow extends ModulePopupWindow {

    private View view;
    private Handler handler;
    private Runnable runnable;
    private int delay = 0;
    private int noteLength = 0;
    private boolean keyDown = false;

    //Valores en milisegundos
    private final String textDelay = "Periodo";
    private final int minDelay = 0;
    private final int stepDelay = 20;
    private final int maxDelay = 2000;

    private final String textNoteLength = "Duracion";
    private final int minNoteLength = 20;
    private final int stepNoteLength = 20;
    private final int maxNoteLength = 2000;


    public SequencerPopupWindow(MainActivity container, int layout, String name) {
        super(container, layout, name);

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                keyDown = !keyDown;
                if (keyDown) {
                    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                    Log.i("Time", timestamp.toString());
                    Log.i("X_KB_gate", Integer.toString(1));
                    PdBase.sendFloat("X_KB_gate", 1);
                    handler.postDelayed(runnable, noteLength);
                } else {
                    Log.i("X_KB_gate", Integer.toString(0));
                    PdBase.sendFloat("X_KB_gate", 0);
                    handler.postDelayed(runnable, delay - noteLength);
                }
            }
        };
    }

    @Override
    public void initializeModule(String title, View view) {

        this.view = view;
        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);

        createSeekBarSequencer();
        createSeekBarSoundLength();
    }

    protected void createSeekBarSequencer() {
        SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBarSequencer1_1);
        final TextView label_module = (TextView) view.findViewById(R.id.labelSequencer1_1);
        label_module.setText(textDelay + ": Apagado");

        seekBar.setMax((maxDelay - minDelay) / stepDelay);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                handler.removeCallbacks(runnable);
                delay = minDelay + progress * stepDelay;
                keyDown = false;
                PdBase.sendFloat("X_KB_gate", 0);
                if (delay == maxDelay){
                    keyDown = true;
                    PdBase.sendFloat("X_KB_gate", 1);
                    label_module.setText(textDelay + ": Constante");
                } else if(delay == 0){
                    label_module.setText(textDelay + ": Apagado");
                } else {
                    handler.postDelayed(runnable, delay);
                    label_module.setText(textDelay + ": " + Integer.toString(delay) + " ms");
                    if (noteLength >= delay) {
                        noteLength = delay - stepNoteLength;
                        SeekBar seekBar2 = (SeekBar) view.findViewById(R.id.seekBarSequencer1_2);
                        seekBar2.setProgress((noteLength - minNoteLength) / stepNoteLength);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
    }

    protected void createSeekBarSoundLength() {
        final SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekBarSequencer1_2);
        final TextView label_module = (TextView) view.findViewById(R.id.labelSequencer1_2);

        label_module.setText(textNoteLength + ": " + Integer.toString(minNoteLength) + " ms");
        seekBar.setMax((maxNoteLength - minNoteLength) / stepNoteLength);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                noteLength = minNoteLength + progress * stepNoteLength;
                if (noteLength >= delay && delay != 0) {
                    delay = noteLength + stepDelay;
                    SeekBar seekBar2 = (SeekBar) view.findViewById(R.id.seekBarSequencer1_1);
                    seekBar2.setProgress((delay - minDelay) / stepDelay);
                }
                label_module.setText(textNoteLength + ": " + Integer.toString(noteLength) + " ms");
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
