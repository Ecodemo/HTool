package com.smile.box.app.tools;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import com.smile.box.R;
import com.smile.box.app.MxActivity;
import com.smile.box.app.widget.CompassView;

public class CompassActivity extends MxActivity implements SensorEventListener
{
	private CompassView cv;
    private int fromDegrees = 0;
    private TextView mTvAngla;
	private SensorManager mSensorManager;
    private Sensor acc_sensor;
    private Sensor mag_sensor;

    private float[] accValues = new float[3];
    private float[] magValues = new float[3];
    // 旋转矩阵，用来保存磁场和加速度的数据
    private float r[] = new float[9];
    // 模拟方向传感器的数据（原始数据为弧度）
    private float values[] = new float[3];
	private TextView tvVert;
	private TextView tvHorz;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.compass);
        mSensorManager = (SensorManager)getApplication().getSystemService(SENSOR_SERVICE);
		cv = (CompassView) findViewById(R.id.cv);
        mTvAngla = (TextView) findViewById(R.id.tv_angla);
		tvVert = (TextView)findViewById(R.id.tvv_vertical);
        tvHorz = (TextView)findViewById(R.id.tvv_horz);
        cv.setCureentAngla((int) gps2d(31.1237f, 121.3536f, 31.2036f, 121.6012f));
        cv.setOnAnglaChanged(new CompassView.OnAnglaChanged() {
				@Override
				public void onChanged(int toDegrees) {
					mTvAngla.setText(Math.abs(toDegrees) + "°");
					//让图片相对自身中心点转动，开始角度默认为0；此后开始角度等于上一次结束角度
					RotateAnimation ra = new RotateAnimation(fromDegrees, 
															 toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
					ra.setInterpolator(new LinearInterpolator());
					//动画时间200毫秒
					ra.setDuration(Math.abs(fromDegrees-toDegrees));
					ra.setFillAfter(true);
					cv.startAnimation(ra);
					fromDegrees = toDegrees;
				}
			});
	}
	
	private float gps2d(float lat_a, float lng_a, float lat_b, float lng_b) {
        float d = 0;
        lat_a = (float) (lat_a * Math.PI / 180);
        lng_a = (float) (lng_a * Math.PI / 180);
        lat_b = (float) (lat_b * Math.PI / 180);
        lng_b = (float) (lng_b * Math.PI / 180);
        d = (float) (Math.sin(lat_a) * Math.sin(lat_b) + 
			Math.cos(lat_a) * Math.cos(lat_b) * Math.cos(lng_b - lng_a));
        d = (float) Math.sqrt(1 - d * d);
        d = (float) (Math.cos(lat_b) * Math.sin(lng_b - lng_a) / d);
        d = (float) (Math.asin(d) * 180 / Math.PI);
        return d;
	}
    
	@Override
    public void onResume() {
        super.onResume();
        acc_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag_sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // 给传感器注册监听：
        mSensorManager.registerListener(this, acc_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mag_sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
	@Override
    protected void onPause() {
        // 取消方向传感器的监听
        mSensorManager.unregisterListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        // 取消方向传感器的监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 获取手机触发event的传感器的类型
        int sensorType = event.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                accValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magValues = event.values.clone();
                break;
        }

        SensorManager.getRotationMatrix(r, null, accValues, magValues);
        SensorManager.getOrientation(r, values);
        // 获取　沿着Z轴转过的角度
        float azimuth = values[0];
        // 获取　沿着X轴倾斜时　与Y轴的夹角
        float pitchAngle = values[1];
        // 获取　沿着Y轴的滚动时　与X轴的角度
        //此处与官方文档描述不一致，所在加了符号（https://developer.android.google.cn/reference/android/hardware/SensorManager.html#getOrientation(float[], float[])）
        float rollAngle = - values[2];
        onAngleChanged(rollAngle, pitchAngle, azimuth);
    }
	
    private void onAngleChanged(float rollAngle, float pitchAngle, float azimuth){
        tvHorz.setText(String.valueOf((int)Math.toDegrees(rollAngle)) + "°");
        tvVert.setText(String.valueOf((int)Math.toDegrees(pitchAngle)) + "°");
    }
}
