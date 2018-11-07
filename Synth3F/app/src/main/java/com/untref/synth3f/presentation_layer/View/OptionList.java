package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class OptionList extends LinearLayout {

    private PatchMenuView patchMenuView;
    private String parameterName;
    private OptionItem[] options;

    private int selectedValue;
    private int buttonSize;
    private int activeColor;
    private int inactiveColor;

    public OptionList(Context context) {
        super(context);
    }

    public OptionList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(PatchMenuView patchMenuView, int buttonSize) {
        this.patchMenuView = patchMenuView;
        this.buttonSize = buttonSize;
    }

    public void setValues(String parameterName, int[] imagesIds, int selectedValue, int color) {
        this.activeColor = color;
        this.inactiveColor = darken(color);
        this.parameterName = parameterName;
        this.selectedValue = selectedValue;
        options = new OptionItem[imagesIds.length];
        OptionItem optionItem;
        for (int i = 0; i < imagesIds.length; i++) {
            optionItem = new OptionItem(getContext(), i);
            addView(optionItem);
            options[i] = optionItem;
            optionItem.init(imagesIds[i], buttonSize, inactiveColor);
        }
        options[selectedValue].setColor(activeColor);
    }

    public void clear() {
        parameterName = null;
        options = null;
        removeAllViews();
    }

    public void selectValue(int selectedValue) {
        options[this.selectedValue].setColor(inactiveColor);
        this.selectedValue = selectedValue;
        options[selectedValue].setColor(activeColor);
        patchMenuView.setParameterToEdit(parameterName, selectedValue);
        patchMenuView.setValue(parameterName, selectedValue, true);
    }

    private int darken(int color) {
        //El color esta representado como hecadecimal ARGB
        //al realizar un shift hacia la derecha se dividen todos los valores por dos
        //el and con 0x007F7F7F evita que se pase el resto de un valor a otro
        //el or con (color & 0xFF000000) devuelve la opacidad a su estado anterior
        return ((color >> 1) & 0x007F7F7F) | (color & 0xFF000000);
    }

    private class OptionItem extends AppCompatButton implements View.OnClickListener {

        private int id;

        public OptionItem(Context context, int id) {
            super(context);
            this.id = id;
        }

        public void init(int imageId, int size, int color) {
            getLayoutParams().width = size;
            getLayoutParams().height = size;
            setBackgroundResource(imageId);
            this.setColor(color);
            this.setClickable(true);
            this.setOnClickListener(this);
        }

        public void setColor(int color) {
            getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }

        @Override
        public void onClick(View view) {
            ((OptionList) getParent()).selectValue(id);
        }
    }
}
