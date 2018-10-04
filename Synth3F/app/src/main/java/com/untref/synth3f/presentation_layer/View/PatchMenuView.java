package com.untref.synth3f.presentation_layer.View;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public abstract class PatchMenuView extends PopupWindow {

    private View button;
    private int id_layout;
    private String title;
    private LayoutInflater layoutInflater;
    private View popupView;
    protected PatchPresenter patchPresenter;
    protected Patch patch;

    public PatchMenuView(MainActivity context, int layout, PatchPresenter patchPresenter, Patch patch) {
        super(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

        this.id_layout = layout;
        this.title = patch.getTypeName() + "_" + patch.getId();
        this.patchPresenter = patchPresenter;
        this.patch = patch;

        layoutInflater = (LayoutInflater) context.getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        initializePopupView();

        this.setContentView(popupView);
    }

    public View getButton() {
        return this.button;
    }

    public void setButton(View boton) {
        this.button = boton;
    }

    private void initializePopupView() {

        popupView = layoutInflater.inflate(id_layout, null);
        //defino comportamiento de elementos del XML
        initializeModule(title, popupView);

        //Defino comportamiento del boton que cierrar el popup
        Button btn_Cerrar = (Button) popupView.findViewById(R.id.id_cerrar);
        btn_Cerrar.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
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

    protected void createSeekBarComponent(int id_seekBar, int id_label_seekBar, final String identificador_modulo, final String name_modulo, final float max_module, final float min_module, float multiplicador_modulo, final float multiplicador_seekBar, View view, float value, final MenuScale scale) {
        SeekBar seekBar = (SeekBar) view.findViewById(id_seekBar);
        final TextView label_module = (TextView) view.findViewById(id_label_seekBar);
        //final String indetificador = identificador_modulo;
        /*ESTABLECER MAXIMO PARA SEEKBAR
         If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
         this means that you have 21 possible values in the seekbar.
        So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].*/

        seekBar.setMax((int) ((max_module - min_module) / multiplicador_modulo));
        final float maxValue = max_module - min_module;
        final float steps = (max_module - min_module) / multiplicador_modulo;
        if (scale == MenuScale.linear) {
            seekBar.setProgress((int) ((value - min_module) / multiplicador_seekBar));
        } else if (scale == MenuScale.exponential_left) {
            seekBar.setProgress((int) (steps * Math.log(value - min_module - 1) / Math.log(maxValue + 1)));
        } else if (scale == MenuScale.exponential_center) {
            double aux = 0;
            if (value > 0) {
                aux = Math.log(value + 1) / Math.log(max_module + 1);
            } else {
                aux = -Math.log(-value + 1) / Math.log(-min_module + 1);
            }
            seekBar.setProgress((int) ((aux + 1) * steps / 2));
        }
        DecimalFormat decimales = new DecimalFormat("0.00");
        label_module.setText(identificador_modulo + ": " + decimales.format(value));
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar1, int progress, boolean fromUser) {
                        //3) MANDAR PARAMETROS MSJ
                        String msj = "x_" + name_modulo + "_" + identificador_modulo;
                        String label_moduel_text = identificador_modulo;
                        float multiplicador = multiplicador_seekBar;
                        float valorInicial = min_module;
                        DecimalFormat decimales = new DecimalFormat("0.00");
                        float value = 0;
                        if (scale == MenuScale.linear) {
                            value = (float) (valorInicial + (progress * multiplicador));
                        } else if (scale == MenuScale.exponential_left) {
                            value = (float) (valorInicial + Math.pow(maxValue + 1, progress / steps) - 1);
                        } else if (scale == MenuScale.exponential_center) {
                            double aux = Math.abs((progress - steps / 2) * 2 / steps);
                            if (progress > steps / 2) {
                                value = (float) Math.pow(max_module + 1, aux) - 1;
                            } else {
                                value = (float) -Math.pow(-min_module + 1, aux) + 1;
                            }
                        }
                        //float value = (float) (valorInicial + progress * multiplicador);
//                        Log.i("Mensaje seek" + name_modulo + "ER1_" + identificador_modulo, msj);
//                        Log.i("Valor   seek" + name_modulo + "ER1_" + identificador_modulo, decimales.format(value));

                        //context.masterConfig.config.sendValue(msj.replace(" ","") , value);

                        try {
                            Class<?> c = patch.getClass();
                            Field f = c.getDeclaredField(identificador_modulo.replace("-", "_"));
                            f.setAccessible(true);
                            f.setFloat(patch, value);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        patchPresenter.setValue(msj, value);
                        label_module.setText(label_moduel_text + ": " + decimales.format(value));
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar1) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar1) {
                    }
                }
        );
    }

    public abstract void initializeModule(String title, View view);

    public enum MenuScale {
        linear, exponential_left, exponential_center
    }

}
