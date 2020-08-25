package com.ecodemo.magic.box.app.update;
import com.google.gson.annotations.SerializedName;

public class Update
{
	@SerializedName("version_code")
	private int versionCode;
	
	@SerializedName("version_name")
	private String versionName;
	
	@SerializedName("force")
	private boolean force;
	
	@SerializedName("download")
	private String download;
	
	@SerializedName("log")
	private String log;
	
	public void setVersionCode(int versionCode)
	{
		this.versionCode = versionCode;
	}

	public int getVersionCode()
	{
		return versionCode;
	}

	public void setVersionName(String versionName)
	{
		this.versionName = versionName;
	}

	public String getVersionName()
	{
		return versionName;
	}

	public void setForce(boolean force)
	{
		this.force = force;
	}

	public boolean isForce()
	{
		return force;
	}

	public void setDownload(String download)
	{
		this.download = download;
	}

	public String getDownload()
	{
		return download;
	}

	public void setLog(String log)
	{
		this.log = log;
	}

	public String getLog()
	{
		return log;
	}
}
