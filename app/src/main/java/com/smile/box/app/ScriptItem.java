package com.smile.box.app;
import com.google.gson.annotations.SerializedName;

public class ScriptItem
{
	@SerializedName("name")
	private String name;
	@SerializedName("url")
	private String url;

	public void setName(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setUrl(String url)
	{
		this.url = url;
	}

	public String getUrl()
	{
		return url;
	}
}
