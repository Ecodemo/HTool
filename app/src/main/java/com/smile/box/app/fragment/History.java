package com.smile.box.app.fragment;
import com.google.gson.annotations.SerializedName;

public class History
{
	@SerializedName("id")
	private int id;
	@SerializedName("lunar")
	private String lunar;
	@SerializedName("pic")
	private String pic;
	@SerializedName("title")
	private String title;
	@SerializedName("year")
	private String year;


	public void setId(int id)
	{
		this.id = id;
	}

	public int getId()
	{
		return id;
	}

	public void setLunar(String lunar)
	{
		this.lunar = lunar;
	}

	public String getLunar()
	{
		return lunar;
	}

	public void setPic(String pic)
	{
		this.pic = pic;
	}

	public String getPic()
	{
		return pic;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return title;
	}

	public void setYear(String year)
	{
		this.year = year;
	}

	public String getYear()
	{
		return year;
	}
}
