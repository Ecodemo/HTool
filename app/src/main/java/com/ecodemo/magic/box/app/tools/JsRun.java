package com.ecodemo.magic.box.app.tools;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.ecodemo.magic.box.R;
import com.ecodemo.magic.box.app.MxActivity;
import android.app.Activity;
import com.ecodemo.magic.box.app.utils.RunScriptApp;

public class JsRun extends MxActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jsrun);
		findViewById(R.id.choice).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("*/*");
					intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);//打开多个文件
					intent.addCategory(Intent.CATEGORY_DEFAULT);
					intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
					try{
						startActivityForResult(Intent.createChooser(intent,"请选择文件"),1);
					}catch (ActivityNotFoundException e){
						e.printStackTrace();
					}
				}
		});
	}
	
	@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
			Intent intent = new Intent(this, RunScriptApp.class);
			intent.putExtra("url", data.getData().toString());
			intent.putExtra("title", "Test");
			startActivity(intent);
        }
    }
}
