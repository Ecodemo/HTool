package com.smile.box.app.tools;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Spinner;
import com.smile.box.R;
import com.smile.box.app.MxActivity;
import com.smile.box.app.tools.Translate;
import com.smile.box.app.utils.TranslateUtil;
import android.widget.Button;

public class Translate extends MxActivity {
	private EditText enter;
	private EditText exit;
	private Spinner translateSpinner1;
	private Spinner translateSpinner2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.translate);
		enter = findViewById(R.id.input);//输入编辑框
		exit = findViewById(R.id.output);//输出编辑框
		translateSpinner1 = findViewById(R.id.translateSpinner);//输入语言
		translateSpinner2 = findViewById(R.id.translateSpinner2);//输出语言
		findViewById(R.id.translate).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					TranslateUtil.TranslateCallback translateCallback = new TranslateUtil.TranslateCallback() {
						@Override
						public void onTranslateDone(String result) {
							if (TextUtils.isEmpty(result) && !TextUtils.isEmpty(enter.getText())) {
								exit.setText("字数超出限制");
								return;
							}
							exit.setText(result);
						}
					};
					String from = getResources().getStringArray(R.array.language_id)[translateSpinner1.getSelectedItemPosition()];
					String to = getResources().getStringArray(R.array.language_id)[translateSpinner2.getSelectedItemPosition()];
					new TranslateUtil().translate(Translate.this, from, to, enter.getText().toString(), translateCallback);
				}
			});
	}
}

