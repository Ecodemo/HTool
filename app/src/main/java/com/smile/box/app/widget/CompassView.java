package com.smile.box.app.widget;
import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import com.smile.box.R;
import com.smile.box.app.utils.DisplayUtil;
public class CompassView extends View implements SensorEventListener {

    private int mWidth;
    private int mHeigth;
    private Sensor sensor;
    private SensorManager sensorManager;
    private int toDegrees;
    private Paint mPaint;

    public void setCureentAngla(int mCureentAngla) {
        this.mCureentAngla = mCureentAngla;
        if (sensorManager == null) {
            sensorManager = (SensorManager) context.getSystemService(Service.SENSOR_SERVICE);
            //通过 getDefaultSensor 获得指南针传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
            //为传感器管理者注册监听器，第三个参数指获取速度正常
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    private int mCureentAngla=181;
    private Paint mBitmapPaint;
    private Paint mTextPaint;
    private Context context;
    public CompassView(Context context) {
        this(context, null);
    }

    public CompassView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mPaint = new Paint();
        mPaint.setDither(true);
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DisplayUtil.sp2px(getContext(),1));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mBitmapPaint = new Paint();
        mBitmapPaint.setDither(true);
        mBitmapPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setDither(true);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.GRAY);
		mTextPaint.setTypeface(Typeface.SERIF);
        mTextPaint.setTextSize(DisplayUtil.sp2px(getContext(),12));

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec);
        mHeigth = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeigth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int x = mWidth / 2;
        int y = mHeigth / 2;
        int r = (int) (mWidth * 0.42);
        canvas.save();
        for (int i = 0; i < 120; i++) {
            if (i == 0) {
                mPaint.setColor(Color.RED);
                drawText("北",canvas);
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.1), mPaint);
            } else if (i == 30) {
                mPaint.setColor(Color.RED);
                drawText("东", canvas);
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.1), mPaint);
            } else if (i == 60) {
                mPaint.setColor(Color.RED);
                drawText("南", canvas);
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.1), mPaint);
            } else if (i == 90) {
                mPaint.setColor(Color.RED);
                drawText("西", canvas);
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.1), mPaint);
            } else if (i == 5 || i == 10 || i == 20 || i == 25 || i == 15 || i == 35 || i == 40 || i == 45 || i == 50 || i == 55 || i == 65 || i == 70 || i == 75 || i == 80 || i == 85 || i == 95 || i == 100 || i == 105 || i == 110 || i == 115) {
				TypedValue frame = new TypedValue();
				getContext().getTheme().resolveAttribute(R.attr.FrameColor, frame, true);
                mPaint.setColor(getContext().getResources().getColor(frame.resourceId));
                drawText(i * 3 + "", canvas);
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.1), mPaint);
            } else {
                mPaint.setColor(Color.GRAY);
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.1), mPaint);
            }
            canvas.rotate(360 / 120, x, y);
        }
        for (int i = 0; i < 360; i++) {
            if (mCureentAngla == i) {
                canvas.drawLine(x, y - r, x, (float) (y - r + x * 0.2), mPaint);
            }
            canvas.rotate(360 / 360, x, y);
        }
		Paint paint = new Paint();
		paint.setStrokeWidth((mWidth/2)-50);
		TypedValue typedValue = new TypedValue();
		getContext().getTheme().resolveAttribute(android.R.attr.windowBackground, typedValue, true);
		paint.setColor(getContext().getResources().getColor(typedValue.resourceId));
		canvas.drawCircle(mWidth/2, mHeigth/2, (mWidth/2)-65, paint);
        canvas.restore();
    }
	
    public void drawText(String string, Canvas canvas) {
		mTextPaint.setColor(Color.GRAY);
		mTextPaint.setTextSize(DisplayUtil.sp2px(getContext(),12));
        //画中间文字
        String text = string;
        Rect textRect=new Rect();
        mTextPaint.getTextBounds(text, 0, text.length(), textRect);
        int startX=getWidth() / 2 - textRect.width() / 2;
        int textHeight=textRect.height();
        int baseLine=0;
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy=(fontMetricsInt.bottom - fontMetricsInt.top) / 2 - fontMetricsInt.bottom;
        baseLine = textHeight / 2 + dy;
		if(string.equals("北")||string.equals("南")||string.equals("西")||string.equals("东"))
		{
			mTextPaint.setColor(getResources().getColor(R.color.colorAccent));
			mTextPaint.setTextSize(DisplayUtil.sp2px(getContext(),12));
		}
        canvas.drawText(text, startX, baseLine, mTextPaint);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        switch (sensorEvent.sensor.getType()) {
            case Sensor.TYPE_ORIENTATION:
                //顺时针转动为正，故手机顺时针转动时，图片得逆时针转动
                toDegrees = (int) -sensorEvent.values[0];
                if (this.onAnglaChanged != null) {
                    onAnglaChanged.onChanged(toDegrees);
                }
                break;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    public interface OnAnglaChanged {
        void onChanged(int angla);
    }
    public OnAnglaChanged onAnglaChanged;
    public void setOnAnglaChanged(OnAnglaChanged onAnglaChanged) {
        this.onAnglaChanged = onAnglaChanged;
    }
}
