package com.ecodemo.magic.box.app.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import com.ecodemo.magic.box.R;
import java.util.List;
import android.widget.TextView;

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
		TextView title = view.findViewById(R.id.title);
		title.setText(tool.getName());
		title.setTag(position);
		return view;
	}
}
