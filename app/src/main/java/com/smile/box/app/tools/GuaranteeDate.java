package com.smile.box.app.tools;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.smile.box.R;
import com.smile.box.app.MxActivity;
import java.text.ParseException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GuaranteeDate extends MxActivity
{
	private Button calculation;
	private EditText day;
	private EditText date;
	private Spinner company;
	private SimpleDateFormat sdf;
	private TextView message;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guarantee_date);
		date = findViewById(R.id.date);
		day = findViewById(R.id.day);
		calculation = findViewById(R.id.calculation);
		company = findViewById(R.id.company);
		message = findViewById(R.id.message);
		company.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new String[]{"天","月","年"}));
		calculation.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					try
					{
						sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date time = sdf.parse(date.getText().toString());
						Calendar c = Calendar.getInstance();
						c.setTime(time);
						switch(company.getSelectedItemPosition())
						{
							case 0:
								c.add(Calendar.DATE, Integer.parseInt(day.getText().toString()));
								break;
							case 1:
								c.add(Calendar.MONTH, Integer.parseInt(day.getText().toString()));
								break;
							case 2:
								c.add(Calendar.YEAR, Integer.parseInt(day.getText().toString()));
								break;
						}
						message.setText(String.format("过期时间：%s", sdf.format(c.getTime())));
					}
					catch (ParseException e)
					{}
				}
			});
	}
}
