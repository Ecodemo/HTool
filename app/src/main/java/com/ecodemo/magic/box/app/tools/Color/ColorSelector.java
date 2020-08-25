package com.ecodemo.magic.box.app.tools.Color;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import com.ecodemo.magic.box.app.tools.Color.view.ColorPickerView;
import com.ecodemo.magic.box.app.tools.Color.view.OnColorChangedListener;
public class ColorSelector extends MxActivity implements OnColorChangedListener,TextWatcher
{
	private static final String hint_fmt = "Integer:0x%08X\nARGB:(%d,%d,%d,%d)\nHSV:(%d,%.2f,%.2f)";
	private ColorPickerView cpv;
	private boolean auto;
	private EditText hex;
	private TextView hint_text;
	private View hint;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.color_choose);
		//锁定竖屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		cpv = findViewById(R.id.colorpicker);
		cpv.setOnColorChangedListener(this);
		hex = findViewById(R.id.hex);
		hint = findViewById(R.id.hint);
		hint_text = findViewById(R.id.hint_text);
		hint_text.setTextIsSelectable(true);
		hex.addTextChangedListener(this);
	}


	@Override
    protected void onResume()
	{
        super.onResume();
        onColorChanged(-65536);
       	cpv.setColor(-65536);
    }

	@Override
    public void onColorChanged(int n)
	{
        if (auto)
		{
            hex.setText((CharSequence)String.format("#%08X", new Integer(n)));
        }
		else
		{
            cpv.setColor(n);
        }
        hint.setBackgroundColor(n);
       	int a = Color.alpha(n);
        int r = Color.red(n);
        int g = Color.green(n);
        int b = Color.blue(n);
        float[] arrf = new float[3];

        Color.RGBToHSV(r, g, b, arrf);
        hint_text.setText((CharSequence)String.format(hint_fmt,
													  new Integer(n),
													  new Integer(a),
													  new Integer(r),
													  new Integer(g),
													  new Integer(b),
													  new Integer((int)(arrf[0] + 0.5f)),
													  new Float(arrf[1]),
													  new Float(arrf[2])));
        hint_text.setTextColor(~ n | -16777216);
    }

	@Override
    public void afterColorChanged()
	{
        this.auto = false;
    }

    @Override
    public void afterTextChanged(Editable editable)
	{
        if (this.auto)
		{
            return;
        }
        int n = editable.length();
        if (n < 7) return;
        if (n == 8) return;
        onColorChanged(Color.parseColor(editable.toString()));
    }

    @Override
    public void beforeColorChanged()
	{
        this.auto = true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int n, int n2, int n3)
	{
    }

	@Override
    public void onTextChanged(CharSequence charSequence, int n, int n2, int n3)
	{
    }
}

