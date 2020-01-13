package com.smile.box.app.tools.Color.view;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

class AlphaDrawable extends Drawable {
    private Bitmap bitmap;
    private int numRectanglesHorizontal;
    private int numRectanglesVertical;
    private Paint paint = new Paint();
    private Paint paintGray = new Paint();
    private Paint paintWhite = new Paint();
    private int rectangleSize = 10;

    AlphaDrawable(int i) {
        this.rectangleSize = i;
        this.paintWhite.setColor(-1);
        this.paintGray.setColor(-3421237);
    }

    @Override
    public void draw(Canvas canvas) {
        if (this.bitmap != null && !this.bitmap.isRecycled()) {
            canvas.drawBitmap(this.bitmap, (Rect) null, getBounds(), this.paint);
        }
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    public void setAlpha(int i) {
        throw new UnsupportedOperationException("Alpha is not supported by this drawable.");
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        throw new UnsupportedOperationException("ColorFilter is not supported by this drawable.");
    }

    @Override
    protected void onBoundsChange(Rect rect) {
        super.onBoundsChange(rect);
        int height = rect.height();
        this.numRectanglesHorizontal = (int) Math.ceil((double) (rect.width() / this.rectangleSize));
        this.numRectanglesVertical = (int) Math.ceil((double) (height / this.rectangleSize));
        generatePatternBitmap();
    }

    private void generatePatternBitmap() {
        if (getBounds().width() > 0 && getBounds().height() > 0) {
            this.bitmap = Bitmap.createBitmap(getBounds().width(), getBounds().height(), Config.ARGB_8888);
            Canvas canvas = new Canvas(this.bitmap);
            Rect rect = new Rect();
            Object obj = 1;
            for (int i = 0; i <= this.numRectanglesVertical; i++) {
                Object obj2 = obj;
                for (int i2 = 0; i2 <= this.numRectanglesHorizontal; i2++) {
                    rect.top = this.rectangleSize * i;
                    rect.left = this.rectangleSize * i2;
                    rect.bottom = rect.top + this.rectangleSize;
                    rect.right = rect.left + this.rectangleSize;
                    canvas.drawRect(rect, obj2 != null ? this.paintWhite : this.paintGray);
                    if (obj2 != null) {
                        obj2 = null;
                    } else {
                        int i3 = 1;
                    }
                }
                if (obj != null) {
                    obj = null;
                } else {
                    int i4 = 1;
                }
            }
        }
    }
}

