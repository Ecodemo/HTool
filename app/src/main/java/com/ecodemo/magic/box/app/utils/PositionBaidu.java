package com.ecodemo.magic.box.app.utils;

import com.google.gson.annotations.SerializedName;
import com.google.gson.JsonObject;
import java.util.List;
public class PositionBaidu
{
	@SerializedName("result")
	private Result result;

	public void setResult(Result result)
	{
		this.result = result;
	}

	public Result getResult()
	{
		return result;
	}
	
	public class Result
	{
		@SerializedName("formatted_address")
		private String addressComponent;

		public void setAddressComponent(String addressComponent)
		{
			this.addressComponent = addressComponent;
		}

		public String getAddressComponent()
		{
			return addressComponent;
		}
	}
}
