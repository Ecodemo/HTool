package com.smile.box.app.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.smile.box.R;
import java.util.List;

public class GridAdapter extends ArrayAdapter<Tool>
{
	public GridAdapter(Context context, List<Tool> objects)
	{
		super(context, 0, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Tool tool = getItem(position);
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
