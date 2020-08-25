package com.ecodemo.magic.box.app.tools.Color.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.View;

class TestView extends View {
    private final AlphaDrawable alphaDrawable;
    private int color;
    private final Paint paint;
    private final Rect rect;

    TestView(Context context) {
        this(context, (AttributeSet) null);
    }

    TestView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.color = 0;
        this.alphaDrawable = new AlphaDrawable(8);
        this.paint = new Paint();
        this.rect = new Rect();
    }

    @Override
    protected void onSizeChanged(int i, int i2, int i3, int i4) {
        int i5 = i / 10;
        int i6 = i2 / 20;
        this.rect.set(i5, i6, i - i5, i2 - i6);
        this.alphaDrawable.setBounds(this.rect);
        super.onSizeChanged(i, i2, i3, i4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.alphaDrawable.draw(canvas);
        canvas.drawRect(this.rect, this.paint);
    }

    public void setColor(int i) {
        int width = getWidth() / 2;
        this.paint.setShader(new LinearGradient((float) (width - 1), (float) 0, (float) (width + 1), (float) 0, this.color, i, TileMode.CLAMP));
        invalidate();
    }

    public void setInitColor(int i) {
        this.color = i;
        int width = getWidth() / 2;
        this.paint.setShader(new LinearGradient((float) (width - 1), (float) 0, (float) (width + 1), (float) 0, i, i, TileMode.CLAMP));
        invalidate();
    }
}

