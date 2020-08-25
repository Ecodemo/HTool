package com.ecodemo.magic.box.app.tools.Color.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;

public class ColorPickerView extends View {
    private static final int[] hueColors = new int[]{-65536, -256, -16711936, -16711681, -16776961, -65281, -65536};
    private static final int labelPaintSize = 3;
    private int a;
    private final AlphaDrawable alpha;
    private final Paint alphaPaint;
    private final Rect alphaRect;
    private float alphax;
    private float h;
    private final Paint huePaint;
    private final Rect hueRect;
    private float huey;
    private final Paint labelPaint;
    private OnColorChangedListener listener;
    private float s;
    private final Paint satPaint;
    private final Rect satRect;
    private Shader satShader;
    private float satx;
    private float saty;
    private boolean touch_alpha;
    private boolean touch_hue;
    private boolean touch_sat;
    private float v;
    private final Paint valPaint;
    private Shader valShader;

    public ColorPickerView(Context context) {
        this(context, (AttributeSet) null);
    }

    public ColorPickerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.huePaint = new Paint();
        this.satPaint = new Paint();
        this.alphaPaint = new Paint();
        this.alpha = new AlphaDrawable(8);
        this.satRect = new Rect();
        this.hueRect = new Rect();
        this.alphaRect = new Rect();
        this.valPaint = new Paint();
        this.listener = (OnColorChangedListener) null;
        this.valPaint.setXfermode(new PorterDuffXfermode(Mode.MULTIPLY));
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth((float) labelPaintSize);
        paint.setStyle(Style.STROKE);
        this.labelPaint = paint;
        this.h = 0.0f;
        this.s = 0.0f;
        this.v = 1.0f;
        this.a = 255;
        float f = (float) 0;
        this.alphax = f;
        this.saty = f;
        this.satx = f;
        this.huey = f;
    }

    public void setOnColorChangedListener(OnColorChangedListener onColorChangedListener) {
        this.listener = onColorChangedListener;
    }

    public void setColor(int i) {
        setColor(Color.alpha(i), Color.red(i), Color.green(i), Color.blue(i));
    }

    public void setColor(int i, int i2, int i3) {
        setColor(255, i, i2, i3);
    }

    public void setColor(int i, int i2, int i3, int i4) {
        this.a = i;
        float[] fArr = new float[labelPaintSize];
        Color.RGBToHSV(i2, i3, i4, fArr);
        this.h = fArr[0];
        this.s = fArr[1];
        this.v = fArr[2];
        a();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        int min = Math.min(i, i2) / 20;
        int i5 = min * 17;
        int i6 = min * 18;
        int i7 = min * 19;
        this.satRect.set(min, min, i5, i5);
        this.hueRect.set(i6, min, i7, i5);
        this.alphaRect.set(min, i6, i7, i7);
        this.alpha.setBounds(this.alphaRect);
        setupHue();
        setupVal();
        a();
    }

    private void a() {
        Rect rect = this.hueRect;
        if (rect.height() != 0) {
            this.huey = (((float) rect.height()) * (this.h / 360.0f)) + ((float) rect.top);
            rect = this.alphaRect;
            this.alphax = ((float) rect.right) - (((float) rect.width()) * (((float) this.a) / ((float) 255)));
            rect = this.satRect;
            this.satx = ((float) rect.left) + (this.s * ((float) rect.width()));
            this.saty = ((float) rect.bottom) - (((float) rect.height()) * this.v);
            refresh(true, false);
        }
    }

    private void setupVal() {
        Rect rect = this.satRect;
        this.valShader = new LinearGradient((float) 0, (float) 0, (float) 0, (float) rect.height(), -1, -16777216, TileMode.CLAMP);
        this.satShader = new LinearGradient((float) 0, (float) 0, (float) rect.width(), (float) 0, -1, -16777216, TileMode.CLAMP);
        this.valPaint.setShader(this.valShader);
    }

    private void setupHue() {
        Rect rect = this.hueRect;
        this.huePaint.setShader(new LinearGradient((float) rect.left, (float) rect.top, (float) rect.left, (float) rect.bottom, hueColors, (float[]) null, TileMode.CLAMP));
    }

    @Override
    protected void onMeasure(int i, int i2) {
        int min = Math.min((MeasureSpec.getSize(i) - getPaddingLeft()) - getPaddingRight(), (MeasureSpec.getSize(i2) - getPaddingBottom()) - getPaddingTop());
        setMeasuredDimension(min, min);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Rect rect = this.satRect;
        Paint paint = this.labelPaint;
        canvas.drawRect(rect, this.satPaint);
        canvas.drawRect(rect, this.valPaint);
        paint.setStrokeWidth((float) 1);
        canvas.drawRect(rect, paint);
        paint.setStrokeWidth((float) labelPaintSize);
        float[] fArr = new float[labelPaintSize];
        fArr[0] = this.h;
        fArr[1] = 0.5f;
        fArr[2] = ((float) 1) - this.v;
        paint.setColor(Color.HSVToColor(fArr));
        canvas.drawCircle(this.satx, this.saty, (float) 6, paint);
        paint.setColor(-16777216);
        rect = this.hueRect;
        canvas.drawRect(rect, this.huePaint);
        canvas.drawRect((float) (rect.left - labelPaintSize), this.huey - ((float) labelPaintSize), (float) (rect.right + labelPaintSize), ((float) labelPaintSize) + this.huey, paint);
        this.alpha.draw(canvas);
        rect = this.alphaRect;
        canvas.drawRect(rect, this.alphaPaint);
        canvas.drawRect(this.alphax - ((float) labelPaintSize), (float) (rect.top - labelPaintSize), this.alphax + ((float) labelPaintSize), (float) (rect.bottom + labelPaintSize), paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        switch (motionEvent.getAction()) {
            case 0:
                if (this.listener != null) {
                    this.listener.beforeColorChanged();
                }
                if (!this.hueRect.contains(x, y)) {
                    if (!this.satRect.contains(x, y)) {
                        if (this.alphaRect.contains(x, y)) {
                            processAlpha((float) x);
                            break;
                        }
                    }
                    processSat((float) x, (float) y);
                    break;
                }
                processHue((float) y);
                break;
            case 1:
                this.touch_hue = false;
                this.touch_sat = false;
                this.touch_alpha = false;
                if (this.listener != null) {
                    this.listener.afterColorChanged();
                    break;
                }
                break;
            case 2:
                if (!this.touch_hue) {
                    if (!this.touch_sat) {
                        if (this.touch_alpha) {
                            processAlpha((float) x);
                            break;
                        }
                    }
                    processSat((float) x, (float) y);
                    break;
                }
                processHue((float) y);
                break;
        }
        refresh(false, true);
        invalidate();
        return true;
    }

    private void processAlpha(float f) {
        this.touch_alpha = true;
        Rect rect = this.alphaRect;
        if (f < ((float) rect.left)) {
            f = (float) rect.left;
        }
        if (f > ((float) rect.right)) {
            f = (float) rect.right;
        }
        this.alphax = f;
        this.a = (int) ((((float) 1) - ((f - ((float) rect.left)) / ((float) rect.width()))) * ((float) 255));
    }

    private void processSat(float f, float f2) {
        this.touch_sat = true;
        Rect rect = this.satRect;
        if (f2 < ((float) rect.top)) {
            f2 = (float) rect.top;
        }
        if (f2 > ((float) rect.bottom)) {
            f2 = (float) rect.bottom;
        }
        if (f < ((float) rect.left)) {
            f = (float) rect.left;
        }
        if (f > ((float) rect.right)) {
            f = (float) rect.right;
        }
        this.satx = f;
        this.saty = f2;
        float f3 = f2 - ((float) rect.top);
        this.s = (f - ((float) rect.left)) / ((float) rect.width());
        this.v = ((float) 1) - (f3 / ((float) rect.height()));
    }

    private void processHue(float f) {
        this.touch_hue = true;
        Rect rect = this.hueRect;
        if (f < ((float) rect.top)) {
            f = (float) rect.top;
        }
        if (f > ((float) rect.bottom)) {
            f = (float) rect.bottom;
        }
        this.huey = f;
        this.h = ((f - ((float) rect.top)) / ((float) rect.height())) * 360.0f;
    }

    private void refresh(boolean z, boolean z2) {
        float[] fArr = new float[labelPaintSize];
        fArr[0] = this.h;
        fArr[1] = 1.0f;
        fArr[2] = 1.0f;
        if (z || this.touch_hue) {
            this.satShader = new LinearGradient((float) 0, (float) 0, (float) this.satRect.width(), (float) 0, -1, Color.HSVToColor(fArr), TileMode.CLAMP);
            this.satPaint.setShader(this.satShader);
        }
        fArr[1] = this.s;
        fArr[2] = this.v;
        if (z || this.touch_hue || this.touch_sat) {
            Rect rect = this.alphaRect;
            int HSVToColor = Color.HSVToColor(fArr);
            this.alphaPaint.setShader(new LinearGradient((float) rect.left, (float) rect.top, (float) rect.right, (float) rect.top, HSVToColor, HSVToColor & 16777215, TileMode.CLAMP));
        }
        if (z2 && this.listener != null) {
            this.listener.onColorChanged(Color.HSVToColor(this.a, fArr));
        }
    }
}

