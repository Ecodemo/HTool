package com.ecodemo.magic.box.app.tools.Color.view;

import android.content.Context;
import android.graphics.Color;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

public class ColorPickerLayout extends ViewGroup implements OnColorChangedListener {
    private static final InputFilter[] filters = new InputFilter[]{new LengthFilter(9)};
    private boolean autoset;
    private int color;
    private final EditText hint;
    private final LinearLayout hintLayout;
    private final ColorPickerView picker;
    private final TestView testView;

    public ColorPickerLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public ColorPickerLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.autoset = false;
        this.picker = new ColorPickerView(context, attributeSet);
        this.testView = new TestView(context, attributeSet);
        this.hintLayout = new LinearLayout(context, attributeSet);
        this.hint = new EditText(context, attributeSet);
        this.hintLayout.setOrientation(1);
        this.hint.setGravity(17);
        this.hint.setFilters(filters);
        ColorUtils colorUtils = new ColorUtils(this);
        this.hint.addTextChangedListener(colorUtils);
        this.hint.setKeyListener(colorUtils);
        this.hintLayout.addView(this.hint);
        this.picker.setOnColorChangedListener(this);
        addView(this.picker);
        addView(this.testView);
        addView(this.hintLayout);
    }

    @Override
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        View view = this.picker;
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        view.layout(i, i2, i + measuredWidth, i2 + measuredHeight);
        view = this.hintLayout;
        int measuredHeight2 = view.getMeasuredHeight();
        view.layout((measuredWidth / 2) + i, i2 + measuredHeight, i + measuredWidth, (i2 + measuredHeight) + measuredHeight2);
        this.testView.layout(i, i2 + measuredHeight, (measuredWidth / 2) + i, (measuredHeight + i2) + measuredHeight2);
    }

    public void setColor(int i) {
        this.picker.setColor(i);
        this.testView.setInitColor(i);
        onColorChanged(i);
    }

    @Override
    protected void onMeasure(int i, int i2) {
        View view = this.picker;
        view.measure(i, i2);
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        int size = ((MeasureSpec.getSize(i2) - getPaddingBottom()) - getPaddingTop()) - measuredHeight;
        int makeMeasureSpec = MeasureSpec.makeMeasureSpec(measuredWidth / 2, 1073741824);
        size = MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
        View view2 = this.hintLayout;
        view2.measure(makeMeasureSpec, size);
        setMeasuredDimension(measuredWidth, measuredHeight + view2.getMeasuredHeight());
    }

    @Override
    public void beforeColorChanged() {
        this.autoset = true;
    }

    @Override
    public void afterColorChanged() {
        this.autoset = false;
    }

    void findColor(String str) {
        if (!this.autoset) {
            int parseColor = Color.parseColor(str);
            this.picker.setColor(parseColor);
            this.testView.setColor(parseColor);
            this.color = parseColor;
        }
    }

    @Override
    public void onColorChanged(int i) {
        this.color = i;
        this.testView.setColor(i);
        this.hint.setText(String.format("#%08X", new Object[]{new Integer(i)}));
    }

    public int getColor() {
        return this.color;
    }
}

