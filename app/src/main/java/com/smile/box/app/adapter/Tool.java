package com.smile.box.app.adapter;
import android.view.View.OnClickListener;

public class Tool
{
	private String name;
	private ToolType type;
	
	public Tool(String name, ToolType type)
	{
		this.name = name;
		this.type = type;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setType(ToolType type)
	{
		this.type = type;
	}

	public ToolType getType()
	{
		return type;
	}
}
