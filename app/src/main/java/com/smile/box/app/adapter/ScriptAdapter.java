package com.smile.box.app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.smile.box.R;
import com.smile.box.app.ScriptItem;
import java.util.List;

public class ScriptAdapter extends ArrayAdapter<ScriptItem>
{
	public ScriptAdapter(Context context, List<ScriptItem> objects)
	{
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ScriptItem tool = getItem(position);
		View view = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
		view.setTag(tool.getName());
		Button title = view.findViewById(R.id.title);
		title.setText(tool.getName());
		title.setTag(position);
		title.setBackground(null);
		title.setFocusable(false);
		title.setClickable(false);
		return view;
	}
}
