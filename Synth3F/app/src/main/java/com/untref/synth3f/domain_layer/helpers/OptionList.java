package com.untref.synth3f.domain_layer.helpers;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.untref.synth3f.presentation_layer.View.PatchMenuView;

public class OptionList extends LinearLayout {

    private PatchMenuView patchMenuView;
    private String parameterName;
    private OptionItem[] options;

    private int selectedValue;
    private int height;
    private int width;
    private int spacing;

    public OptionList(Context context) {
        super(context);
    }

    public OptionList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void init(PatchMenuView patchMenuView, int span, int width, int height, int spacing) {
        this.patchMenuView = patchMenuView;
        ((TableRow.LayoutParams) getLayoutParams()).span = span;
        this.width = width;
        this.height = height;
        this.spacing = spacing;
        setMinimumHeight(height);
    }

    public void setValues(String parameterName, int color, int[] iconOffIds, int[] iconOnIds,
                          int selectedValue) {
        this.parameterName = parameterName;
        this.selectedValue = selectedValue;
        int buttonWidth = width / iconOffIds.length - spacing * 2;
        options = new OptionItem[iconOffIds.length];
        OptionItem optionItem;
        for (int i = 0; i < iconOffIds.length; i++) {
            optionItem = new OptionItem(getContext(), i, color, iconOffIds[i], iconOnIds[i]);
            addView(optionItem);
            options[i] = optionItem;
            optionItem.init(buttonWidth, height, spacing);
        }
        options[selectedValue].setSelected(true);
    }

    public void clear() {
        parameterName = null;
        options = null;
        removeAllViews();
    }

    public void selectValue(int selectedValue) {
        options[this.selectedValue].setSelected(false);
        this.selectedValue = selectedValue;
        options[selectedValue].setSelected(true);
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
        private final int color;

        public OptionItem(Context context, int id, int color, int iconOffId, int iconOnId) {
            super(context);
            this.iconOffId = iconOffId;
            this.iconOnId = iconOnId;
            this.id = id;
            this.color = color;
        }

        public void init(int width, int height, int spacing) {
            LayoutParams layoutParams = (LayoutParams) getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = height;
            layoutParams.leftMargin = spacing;
            setScaleType(ScaleType.CENTER);
            setAdjustViewBounds(true);
            setSelected(false);
            setClickable(true);
            setOnClickListener(this);
        }

        @Override
        public void setSelected(boolean selected) {
            super.setSelected(selected);
            setImageResource(selected ? iconOnId : iconOffId);
            GradientDrawable drawable = new GradientDrawable();
            drawable.setCornerRadius(15);
            if (selected) {
                drawable.setColor(color);
            } else {
                drawable.setStroke(2, color);
            }
            setBackground(drawable);
        }

        @Override
        public void onClick(View view) {
            ((OptionList) getParent()).selectValue(id);
        }
    }
}
