package com.smile.box.app.tools;
import android.os.Bundle;
import com.smile.box.app.MxActivity;
import com.smile.box.R;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.app.Activity;
import android.net.Uri;
import android.database.Cursor;
import android.provider.MediaStore;
import java.io.File;
import android.content.Context;
import android.content.ContentResolver;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;
import com.smile.box.app.utils.Base64Util;
import com.smile.box.app.utils.Util;
import android.app.ProgressDialog;
import android.os.Looper;
import com.smile.box.app.utils.IToast;
import android.util.Log;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

public class ImageToBase64 extends MxActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_to_base64);
		findViewById(R.id.selectBase64).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("*/*");
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					startActivityForResult(intent, 2);
				}
			});

		findViewById(R.id.selectImage).setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View p1)
				{
					Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					startActivityForResult(intent, 1);
				}
			});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK)
		{
			if (requestCode == 1)
			{
				final String path = uriToFile(data.getData(), ImageToBase64.this).toString();
				final ProgressDialog dialog = ProgressDialog.show(this, "正在转换", null, true, false);
				new Thread(new Runnable(){
						@Override
						public void run()
						{
							try
							{
								byte[] bytes = getContent(path);
								String base64 = Base64Util.encode(bytes);
								Util.WriterTxt(new File(path.substring(0, path.indexOf("."))+".txt"), base64);
							}
							catch (IOException e)
							{}
							Looper.prepare();
							IToast.makeText(ImageToBase64.this, "转换完成", IToast.LENGTH_SHORT).show();
							dialog.dismiss();
						}
					}).start();
				IToast.makeText(ImageToBase64.this, "转换完成", IToast.LENGTH_SHORT).show();
			}else if(requestCode==2){
				final String path = uriToFile(data.getData(), ImageToBase64.this).toString();
				final ProgressDialog dialog = ProgressDialog.show(this, "正在转换", null, true, false);
				new Thread(new Runnable(){
						StringBuilder sb = new StringBuilder();
						@Override
						public void run()
						{
							String base64 = Util.ReadTxt(new File(path), "UTF-8");
							byte[] bytes = Base64Util.decode(base64);
							sb.append(Integer.toHexString(bytes[0]).substring(6).toUpperCase());
							sb.append(Integer.toHexString(bytes[1]).substring(6).toUpperCase());
							sb.append(Integer.toHexString(bytes[2]).substring(6).toUpperCase());
							sb.append(Integer.toHexString(bytes[3]).substring(6).toUpperCase());
							if(sb.toString().equals("89504E47"))
							{
								getFile(bytes,path.substring(0,path.indexOf("."))+".png");
							}else if(sb.toString().equals("FFD8FFE1")){
								getFile(bytes,path.substring(0,path.indexOf("."))+".jpg");
							}else if(sb.toString().equals("47494638")){
								getFile(bytes,path.substring(0,path.indexOf("."))+".gif");
							}else if(sb.toString().equals("52494646")){
								getFile(bytes,path.substring(0,path.indexOf("."))+".webp");
							}else{
								getFile(bytes,path.substring(0,path.indexOf(".")));
							}
							Looper.prepare();
							dialog.dismiss();
						}
					}).start();
				IToast.makeText(ImageToBase64.this, "转换完成", IToast.LENGTH_SHORT).show();
			}
		}
	}

	public static void getFile(byte[] bfile, String filePath) {  
        BufferedOutputStream bos = null;  
        FileOutputStream fos = null;  
        File file = null;  
        try {  
            file = new File(filePath);  
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (bos != null) {  
                try {  
                    bos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
            if (fos != null) {  
                try {  
                    fos.close();  
                } catch (IOException e1) {  
                    e1.printStackTrace();  
                }  
            }  
        }  
    }
	
	public static byte[] getContent(String filePath) throws IOException
	{
        File file = new File(filePath);
        long fileSize = file.length();
        if (fileSize > Integer.MAX_VALUE)
		{
            System.out.println("file too big...");
            return null;
        }
        FileInputStream fi = new FileInputStream(file);
        byte[] buffer = new byte[(int) fileSize];
        int offset = 0;
        int numRead = 0;
        while (offset < buffer.length
			   && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0)
		{
            offset += numRead;
        }
        if (offset != buffer.length)
		{
            throw new IOException("Could not completely read file " + file.getName());
        }
        fi.close();
        return buffer;
    }

	public static File uriToFile(Uri uri, Context context)
	{
        String path = null;
        if ("file".equals(uri.getScheme()))
		{
            path = uri.getEncodedPath();
            if (path != null)
			{
                path = Uri.decode(path);
                ContentResolver cr = context.getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=").append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[] { MediaStore.Images.ImageColumns._ID, MediaStore.Images.ImageColumns.DATA }, buff.toString(), null, null);
                int index = 0;
                int dataIdx = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext())
				{
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    index = cur.getInt(index);
                    dataIdx = cur.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    path = cur.getString(dataIdx);
                }
                cur.close();
                if (index == 0)
				{
                }
            }
            if (path != null)
			{
                return new File(path);
            }
        }
		else if ("content".equals(uri.getScheme()))
		{
            // 4.2.2以后
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
            if (cursor.moveToFirst())
			{
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
            cursor.close();

            return new File(path);
        }
        return null;
    }
}
