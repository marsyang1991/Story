package com.yang.story;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ListMapWheelAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.JsonReader;
import android.widget.Toast;

public class SettingBook2Activity extends Activity{
	WheelView bixuanxiuWheelView,shangxiaceWheelView,zhangWheelView,jieWheelView;
	String provinceId,cityId,subjectId,versionId;
	String urlBase;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		urlBase = getResources().getString(R.string.db_url_base);
		setContentView(R.layout.activity_settextbook2);
		bixuanxiuWheelView = (WheelView)findViewById(R.id.wheel_suanbixiu);
		shangxiaceWheelView = (WheelView)findViewById(R.id.wheel_shangxiace);
		zhangWheelView = (WheelView)findViewById(R.id.wheel_zhang);
		jieWheelView = (WheelView)findViewById(R.id.wheel_jie);
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		provinceId = data.getString("provinceId");
		cityId = data.getString("cityId");
		subjectId = data.getString("subjectId");
		versionId = data.getString("versionId");

	}
	private void getContent()
	{
		
	}
	
	
	
	
}
