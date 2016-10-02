package com.example.ddavi.prueba.ModulesPopupWindow;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.ddavi.prueba.MainActivity;
import com.example.ddavi.prueba.R;

import org.puredata.core.PdBase;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by ddavi on 2/10/2016.
 */

public abstract class ModulePopupWindow extends PopupWindow {

    private MainActivity activity;
    private Button button;
    private int id_layout;
    private String title;
    private LayoutInflater layoutInflater;
    private View popupView;

    public ModulePopupWindow(MainActivity container, int layout, String name){
        super(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

        activity = container;
        id_layout = layout;
        title = name;

        layoutInflater =(LayoutInflater)activity.getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        initializePopupView();

        this.setContentView(popupView);
    }

    public ModulePopupWindow(MainActivity container, Button view, int layout, String name){

        super(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

        activity = container;
        button = view;
        id_layout = layout;
        title = name;

        layoutInflater =(LayoutInflater)activity.getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        initializePopupView();

        this.setContentView(popupView);

    }

    public Button getButton(){
        return this.button;
    }
    public void setButton(Button boton){
        this.button = boton;
    }

    private void initializePopupView(){

        popupView = layoutInflater.inflate(id_layout, null);
        //defino comportamiento de elementos del XML
        initializeModule(title,popupView);

        //Defino comportamiento del boton que cierrar el popup
        Button btn_Cerrar = (Button)popupView.findViewById(R.id.id_cerrar);
        btn_Cerrar.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                dismiss();
                button.setEnabled(true);
            }});

        popupView.setOnTouchListener(new View.OnTouchListener(){
            private int dx = 0;
            private int dy = 0;

            private int xp = 0;
            private int yp = 0;

            private int sides = 0;
            private int topBot = 0;
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = (int) motionEvent.getX();
                        dy = (int) motionEvent.getY();
                        break;

                    case MotionEvent.ACTION_MOVE:
                        xp = (int) motionEvent.getRawX();
                        yp = (int) motionEvent.getRawY();
                        sides = (xp - dx);
                        topBot = (yp - dy);
                        update(sides, topBot, -1, -1, true);
                        break;
                }
                return true;
            }
        });
    }

    public abstract void initializeModule(String title, View view);

/**
    public void initializeModule(String title, View view) {

        TextView label_title = (TextView) view.findViewById(R.id.title);
        label_title.setText(title);
        final String name = title;
        //TODOS LOS SLIDERS VCA1
        //COMIENZO DE SEEKBAR VCA 1_1
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCA1_1 = (SeekBar) view.findViewById(R.id.seekBarVCA1_1);
        final TextView labelVCA1_1 = (TextView) view.findViewById(R.id.labelVCA1_1);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCA1_1 = 0.01f;
        float maxVCA1_1 = 1.0f;
        float minVCA1_1 = 0.0f;
        seekBarVCA1_1.setMax((int) ((maxVCA1_1 - minVCA1_1) / multiplicadorVCA1_1));
        seekBarVCA1_1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_"+name+"_att_control";
                String labelVCA1_1text = "att_control";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_1", msj);
                Log.i("Valor   seek"+name+"_1", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCA1_1.setText(labelVCA1_1text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCA1_1
        //COMIENZO DE SEEKBAR VCA 1_2
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCA1_2 = (SeekBar) view.findViewById(R.id.seekBarVCA1_2);
        final TextView labelVCA1_2 = (TextView) view.findViewById(R.id.labelVCA1_2);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCA1_2 = 0.01f;
        float maxVCA1_2 = 1.0f;
        float minVCA1_2 = 0.0f;
        seekBarVCA1_2.setMax((int) ((maxVCA1_2 - minVCA1_2) / multiplicadorVCA1_2));
        seekBarVCA1_2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_"+name+"_base";
                String labelVCA1_2text = "base";
                float multiplicador = 0.01f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_2", msj);
                Log.i("Valor   seek"+name+"_2", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCA1_2.setText(labelVCA1_2text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCA1_2
        //COMIENZO DE SEEKBAR VCA 1_3
        //1) MOSTRAR SEEKBAR
        SeekBar seekBarVCA1_3 = (SeekBar) view.findViewById(R.id.seekBarVCA1_3);
        final TextView labelVCA1_3 = (TextView) view.findViewById(R.id.labelVCA1_3);
        //2) ESTABLECER MAXIMO PARA SEEKBAR
        // If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
        // this means that you have 21 possible values in the seekbar.
        // So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].
        float multiplicadorVCA1_3 = 1.0f;
        float maxVCA1_3 = 1.0f;
        float minVCA1_3 = 0.0f;
        seekBarVCA1_3.setMax((int) ((maxVCA1_3 - minVCA1_3) / multiplicadorVCA1_3));
        seekBarVCA1_3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_"+name+"_clip";
                String labelVCA1_3text = "clip";
                float multiplicador = 1.0f;
                float valorInicial = 0.0f;
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name+"_3", msj);
                Log.i("Valor   seek"+name+"_3", String.valueOf(value));
                PdBase.sendFloat(msj, value);
                labelVCA1_3.setText(labelVCA1_3text + ": " + value);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
        //FIN SEEKBAR VCA1_3
    }**/
}
