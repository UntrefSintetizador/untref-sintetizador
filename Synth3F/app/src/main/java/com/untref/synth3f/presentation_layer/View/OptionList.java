package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class OptionList extends LinearLayout {

    private PatchMenuView patchMenuView;
    private String parameterName;
    private OptionItem[] options;

    private int selectedValue;
    private int buttonSize;

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

    public void setValues(String parameterName, int[] iconOffIds, int[] iconOnIds,
                          int selectedValue) {
        this.parameterName = parameterName;
        this.selectedValue = selectedValue;
        options = new OptionItem[iconOffIds.length];
        OptionItem optionItem;
        for (int i = 0; i < iconOffIds.length; i++) {
            optionItem = new OptionItem(getContext(), i, iconOffIds[i], iconOnIds[i]);
            addView(optionItem);
            options[i] = optionItem;
            optionItem.init(buttonSize);
        }
        options[selectedValue].setIcon(true);
    }

    public void clear() {
        parameterName = null;
        options = null;
        removeAllViews();
    }

    public void selectValue(int selectedValue) {
        options[this.selectedValue].setIcon(false);
        this.selectedValue = selectedValue;
        options[selectedValue].setIcon(true);
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

    private static class OptionItem extends AppCompatImageButton implements View.OnClickListener {

        private final int iconOffId;
        private final int iconOnId;
        private final int id;

        public OptionItem(Context context, int id, int iconOffId, int iconOnId) {
            super(context);
            this.id = id;
            this.iconOffId = iconOffId;
            this.iconOnId = iconOnId;
        }

        public void init(int size) {
            getLayoutParams().width = size;
            getLayoutParams().height = size;
            setIcon(false);
            setClickable(true);
            setOnClickListener(this);
        }

        public void setIcon(boolean active) {
            setImageResource(active ? iconOnId : iconOffId);
        }

        @Override
        public void onClick(View view) {
            ((OptionList) getParent()).selectValue(id);
        }
    }
}
