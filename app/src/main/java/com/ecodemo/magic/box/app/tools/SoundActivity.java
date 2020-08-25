package com.ecodemo.magic.box.app.tools;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
public class SoundActivity extends MxActivity
{
	private static final int SAMPLE_RATE_IN_HZ = 8000;
	private static final int BUFFER_SIZE = AudioRecord.getMinBufferSize(SAMPLE_RATE_IN_HZ,
																		AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
	private AudioRecord mAudioRecord;
	private boolean isGetVoiceRun;
	private TextView dB;
	private TextView tv;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sound);
		dB = findViewById(R.id.dB);
		tv = findViewById(R.id.tv);
		getNoiseLevel();
	}
	private void getNoiseLevel()
	{
		if (isGetVoiceRun)
		{
			return;
		}
		mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
									   SAMPLE_RATE_IN_HZ, AudioFormat.CHANNEL_IN_DEFAULT,
									   AudioFormat.ENCODING_PCM_16BIT, BUFFER_SIZE);
		if (mAudioRecord == null)
		{
			Log.e("sound", "mAudioRecord初始化失败");
		}
		isGetVoiceRun = true;
		new Thread(new Runnable() {
				@Override
				public void run()
				{
					mAudioRecord.startRecording();
					short[] buffer = new short[BUFFER_SIZE];
					while (isGetVoiceRun)
					{
						//r是实际读取的数据长度，一般而言r会小于buffersize
						int r = mAudioRecord.read(buffer, 0, BUFFER_SIZE);
						long v = 0;
						// 将 buffer 内容取出，进行平方和运算
						for (int i = 0; i < buffer.length; i++)
						{
							v += buffer[i] * buffer[i];
						}
						// 平方和除以数据总长度，得到音量大小。
						double mean = v / (double) r;
						final double volume = 10 * Math.log10(mean);
						runOnUiThread(new Runnable(){
								@Override
								public void run()
								{
									dB.setText(String.valueOf((int)volume));
									int db = (int)volume;
									if (db > 0 && db < 45)
									{
										tv.setText(Html.fromHtml("<font color=\"#32CD32\">非常安静的环境<font>"));
									}
									else if (db > 45 && db < 60)
									{
										tv.setText(Html.fromHtml("<font color=\"#7FFF00\">普通的室内谈话<font>"));
									}
									else if (db > 60 && db < 80)
									{
										tv.setText(Html.fromHtml("<font color=\"#7FFF00\">吵闹的工作环境<font>"));
									}
									else if (db > 80 && db < 115)
									{
										tv.setText(Html.fromHtml("<font color=\"#FF8C00\">神经细胞受损坏<font>"));
									}
									else if (db > 115)
									{
										tv.setText(Html.fromHtml("<font color=\"#FF0000\">你最好赶紧离开<font>"));
									}
								}
							});
					}
					mAudioRecord.stop();
					mAudioRecord.release();
					mAudioRecord = null;
				}
			}).start();
	}

	@Override
	protected void onPause()
	{
		isGetVoiceRun = false;
		super.onPause();
	}
}


