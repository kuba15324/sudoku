/*
package com.jakub.sudoku;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.appcompat.widget.AppCompatButton;

public class NumberButton extends AppCompatButton {

    public NumberButton(Context context) {
        super(context);
        setBackgroundColor(getResources().getColor(R.color.backgroundFieldColor, getContext().getTheme()));
        setPadding(0, 0, 0, 0);
        setTextColor(getResources().getColor(R.color.numberColor, getContext().getTheme()));
        setTextSize(20);
    }

    public void setWidth(int screenWidth) {
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = screenWidth / 10 - 20;
        params.height = screenWidth / 10 - 20;
        setLayoutParams(params);
    }

    public void setNumber(int number) {
        setText(String.valueOf(number));
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (isEnabled())
            setBackgroundColor(getResources().getColor((selected ? R.color.selectedBackgroundFieldColor : R.color.backgroundFieldColor), getContext().getTheme()));
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setBackgroundColor(getResources().getColor((enabled ? R.color.backgroundFieldColor : R.color.disabledBackgroundFieldColor), getContext().getTheme()));
    }
}
*/
