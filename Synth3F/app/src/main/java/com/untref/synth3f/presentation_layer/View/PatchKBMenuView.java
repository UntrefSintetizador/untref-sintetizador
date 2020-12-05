package com.untref.synth3f.presentation_layer.View;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.untref.synth3f.R;
import com.untref.synth3f.entities.KBPatch;
import com.untref.synth3f.entities.Patch;
import com.untref.synth3f.presentation_layer.activity.MainActivity;
import com.untref.synth3f.presentation_layer.presenters.PatchKBPresenter;
import com.untref.synth3f.presentation_layer.presenters.PatchPresenter;

import java.lang.reflect.Field;
import java.text.DecimalFormat;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PatchKBMenuView extends PopupWindow {

    protected PatchPresenter patchPresenter;
    protected Patch patch;
    private View button;
    private int layoutId;
    private String title;
    private View popupView;

    public PatchKBMenuView(MainActivity context, int layoutId, PatchPresenter patchPresenter,
                           Patch patch) {
        super(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);

        this.layoutId = layoutId;
        this.patchPresenter = patchPresenter;
        this.patch = patch;
        title = patch.getTypeName() + "_" + patch.getId();

        initializePopupView(
                (LayoutInflater) context.getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE)
        );

        setContentView(popupView);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
    }

    public View getButton() {
        return this.button;
    }

    public void setButton(View button) {
        this.button = button;
    }

    private void initializePopupView(LayoutInflater layoutInflater) {
        popupView = layoutInflater.inflate(layoutId, null);

        // Behavior of XML items
        initializeModule(title, popupView);

        // Close button behavior
        Button closeButton = popupView.findViewById(R.id.id_close);
        closeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            private int dx = 0;
            private int dy = 0;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        dx = (int) motionEvent.getX();
                        dy = (int) motionEvent.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int xp = (int) motionEvent.getRawX();
                        int yp = (int) motionEvent.getRawY();
                        int sides = (xp - dx);
                        int topBot = (yp - dy);
                        update(sides, topBot, -1, -1, true);
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    public void initializeModule(String title, View view) {
        KBPatch kbPatch = (KBPatch) patch;

        TextView titleView = view.findViewById(R.id.title);
        titleView.setText(title);

        PianoView pianoView = view.findViewById(R.id.pianito);
        pianoView.setPresenter((PatchKBPresenter) patchPresenter);
        pianoView.init(view, kbPatch);

        createSeekBarComponent(R.id.seekBar_KB0, R.id.label_KB0, view.getResources().getString(R.string.parameter_on_off), 1.0f, view,
                               kbPatch.on_off, MenuScale.LINEAR);
        createSeekBarComponent(R.id.seekBar_KB1, R.id.label_KB1, view.getResources().getString(R.string.parameter_glide),5000.0f, view,
                               kbPatch.glide, MenuScale.EXPONENTIAL_LEFT);
    }

    private void createSeekBarComponent(int seekBarId, int seekBarLabelId, final String moduleId,
                                        final float maxModule, View view, float value,
                                        final MenuScale scale) {
        /*SET MAX VALUE TO SEEK BAR
          If you want values from 3 to 5 with a step of 0.1 (3, 3.1, 3.2, ..., 5)
          this means that you have 21 possible values in the seekBar.
          So the range of the seek bar will be [0 ; (5-3)/0.1 = 20].*/
        SeekBar seekBar = view.findViewById(seekBarId);
        seekBar.setMax((int) maxModule);

        setSeekBarProgress(maxModule, value, scale, seekBar);

        final TextView moduleLabel = view.findViewById(seekBarLabelId);
        final DecimalFormat decFormat = new DecimalFormat("0.00");
        moduleLabel.setText(String.format("%s: %s", moduleId, decFormat.format(value)));

        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar1, int progress,
                                                  boolean fromUser) {
                        float value = calculateValue(progress);

                        try {
                            Class<?> c = patch.getClass();
                            Field f = c.getDeclaredField(moduleId.replace("-",
                                                                          "_"));
                            f.setAccessible(true);
                            f.setFloat(patch, value);
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        patchPresenter.setValue(moduleId, value);
                        moduleLabel.setText(
                                String.format("%s: %s", moduleId, decFormat.format(value))
                        );
                    }

                    private float calculateValue(int progress) {
                        float value = 0;
                        switch (scale) {
                            case LINEAR:
                                value = (float) (progress);
                                break;
                            case EXPONENTIAL_LEFT:
                                value = (float) (Math.pow(maxModule + 1, progress / maxModule) -
                                                 1);
                                break;
                            case EXPONENTIAL_CENTER:
                                double aux = Math.abs((progress - maxModule / 2) * 2 / maxModule);
                                if (progress > maxModule / 2) {
                                    value = (float) Math.pow(maxModule + 1, aux) - 1;
                                } else {
                                    value = (float) -aux + 1;
                                }
                                break;
                            default:
                                break;
                        }
                        return value;
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

    private void setSeekBarProgress(float maxModule, float value, MenuScale scale,
                                    SeekBar seekBar) {
        switch (scale) {
            case LINEAR:
                seekBar.setProgress((int) value);
                break;
            case EXPONENTIAL_LEFT:
                seekBar.setProgress((int) (maxModule * Math.log(value - 1) /
                                           Math.log(maxModule + 1)));
                break;
            case EXPONENTIAL_CENTER:
                double aux;
                if (value > 0) {
                    aux = Math.log(value + 1) / Math.log(maxModule + 1);
                } else {
                    aux = -Math.log(-value + 1);
                }
                seekBar.setProgress((int) ((aux + 1) * maxModule / 2));
                break;
            default:
                break;
        }
    }

    private enum MenuScale {
        LINEAR, EXPONENTIAL_LEFT, EXPONENTIAL_CENTER
    }
}