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

import java.text.DecimalFormat;

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

    protected void createSeekBarComponent(int id_seekBar, int id_label_seekBar,final String identificador_modulo,final String name_modulo,float max_module,final float min_module,float multiplicador_modulo, final float multiplicador_seekBar, View view){
        SeekBar seekBar = (SeekBar) view.findViewById(id_seekBar);
        final TextView label_module = (TextView) view.findViewById(id_label_seekBar);
        //final String indetificador = identificador_modulo;
        /*ESTABLECER MAXIMO PARA SEEKBAR
         If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
         this means that you have 21 possible values in the seekbar.
        So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].*/

        seekBar.setMax((int) ((max_module - min_module) / multiplicador_modulo));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                //3) MANDAR PARAMETROS MSJ
                String msj = "X_"+name_modulo+"_" + identificador_modulo;
                String label_moduel_text = identificador_modulo;
                float multiplicador = multiplicador_seekBar;
                float valorInicial = min_module;
                DecimalFormat decimales = new DecimalFormat("0.00");
                float value = (float) (valorInicial + (progress * multiplicador));
                //float value = (float) (valorInicial + progress * multiplicador);
                Log.i("Mensaje seek"+name_modulo+"ER1_" + identificador_modulo, msj);
                Log.i("Valor   seek"+name_modulo+"ER1_" + identificador_modulo, decimales.format(value));
                PdBase.sendFloat(msj, value);
                label_module.setText(label_moduel_text + ": " + decimales.format(value));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar1) {
            }
        });
    }

    public abstract void initializeModule(String title, View view);

}
