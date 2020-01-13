package com.smile.box.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.smile.box.R;
import java.util.List;
import android.widget.ImageView;
import android.widget.TextView;

public class WebAdapter extends ArrayAdapter<Web>
{
	public WebAdapter(Context context, List<Web> objects)
	{
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Web tool = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(R.layout.web_item, parent, false);
		ImageView img = view.findViewById(R.id.icon);
		img.setImageBitmap(tool.getIcon());
		TextView title = view.findViewById(R.id.title);
		title.setText(tool.getTitle());
		return view;
	}
}
