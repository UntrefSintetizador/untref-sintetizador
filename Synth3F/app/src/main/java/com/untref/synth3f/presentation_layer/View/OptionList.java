package com.untref.synth3f.presentation_layer.View;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.untref.synth3f.R;

public class OptionList extends LinearLayout {

    private PatchMenuView2 patchMenuView2;
    private String parameterName;

    private int buttonSize;
    private ColorStateList activeColor;
    private ColorStateList inactiveColor;

    public OptionList(Context context) {
        super(context);
    }

    public OptionList(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public OptionList(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(PatchMenuView2 patchMenuView2, int buttonSize, int activeColor, int inactiveColor) {
        this.patchMenuView2 = patchMenuView2;
        this.buttonSize = buttonSize;
        this.activeColor = getContext().getResources().getColorStateList(activeColor);
        this.inactiveColor = getContext().getResources().getColorStateList(inactiveColor);
    }

    public void setValues(String parameterName, int[] imagesIds, int selectedValue) {
        this.parameterName = parameterName;
        OptionItem optionItem;
        for (int i = 0; i < imagesIds.length; i++) {
            optionItem = new OptionItem(getContext(), i);
            addView(optionItem);
            optionItem.init(imagesIds[i], buttonSize);
            ((OptionItem) getChildAt(i)).setColor(inactiveColor);
        }
        ((OptionItem) getChildAt(selectedValue)).setColor(activeColor);
    }

    public void clear() {
        removeAllViews();
    }

    public void selectValue(int id) {
        for (int i = 0; i < getChildCount(); i++) {
            if (i == id) {
                ((OptionItem) getChildAt(i)).setColor(activeColor);
            } else {
                ((OptionItem) getChildAt(i)).setColor(inactiveColor);
            }
        }
        patchMenuView2.setParameterToEdit(parameterName, id);
        patchMenuView2.setValue(parameterName, id);
    }

    private class OptionItem extends AppCompatButton implements View.OnClickListener {

        private int id;

        public OptionItem(Context context, int id) {
            super(context);
            this.id = id;
        }

        public void init(int imageId, int size) {
            getLayoutParams().width = size;
            getLayoutParams().height = size;
            setBackgroundResource(imageId);
            setSupportBackgroundTintMode(PorterDuff.Mode.MULTIPLY);
            setSupportBackgroundTintList(getContext().getResources().getColorStateList(R.color.colorPrimaryDark));
            this.setClickable(true);
            this.setOnClickListener(this);
        }

        public void setColor(ColorStateList color) {
            this.setSupportBackgroundTintList(color);
        }

        @Override
        public void onClick(View view) {
            ((OptionList) getParent()).selectValue(id);
        }
    }
}
