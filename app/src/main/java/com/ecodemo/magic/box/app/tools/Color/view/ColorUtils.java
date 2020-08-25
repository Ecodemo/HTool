package com.ecodemo.magic.box.app.tools.Color.view;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.NumberKeyListener;

public class ColorUtils extends NumberKeyListener implements TextWatcher {
    private final String chars = "0123456789abcdefABCDEF#";
    private final ColorPickerLayout picker;

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    public ColorUtils(ColorPickerLayout colorPickerLayout) {
        this.picker = colorPickerLayout;
    }

    @Override
    public void afterTextChanged(Editable editable) {
        String toLowerCase = editable.toString().toLowerCase();
        int length = toLowerCase.length();
        if (!toLowerCase.startsWith("#")) {
            editable.insert(0, "#");
        } else if (length == 7 || length == 9) {
            this.picker.findColor(toLowerCase);
        }
    }

    @Override
    protected char[] getAcceptedChars() {
        return this.chars.toCharArray();
    }

    @Override
    public int getInputType() {
        return 2;
    }
}

