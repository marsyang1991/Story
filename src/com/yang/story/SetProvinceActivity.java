package com.yang.story;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class SetProvinceActivity extends Activity {
	ListView provinceList;
	List<Map<String, String>> cityList;
	Map<String, String> cityMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settingprovince);
		provinceList = (ListView) findViewById(R.id.settingProvince_list);
		initWidgeContent();
		provinceList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String id = cityList.get(arg2).get("id");
				Intent intent = new Intent(SetProvinceActivity.this,
						SettingBook1Activity.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});

	}

	private void initWidgeContent() {
		cityList = new ArrayList<Map<String, String>>();
		cityMap = new HashMap<String, String>();
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase + "list_province_api.do?countryId=2",
				null, new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String string = responseInfo.result;
						JsonReader reader = new JsonReader(new StringReader(
								string));
						try {
							reader.beginArray();
							while (reader.hasNext()) {
								reader.beginObject();
								String name = "";
								String id = "";
								while (reader.hasNext()) {
									String tagName = reader.nextName();

									if (tagName.equals("id")) {
										id = reader.nextString();
									} else if (tagName.equals("text")) {
										name = reader.nextString().trim();
									} else if (tagName.equals("leaf")) {
										reader.nextBoolean();
									}

								}
								reader.endObject();
								cityMap = new HashMap<String, String>();
								cityMap.put("name", name);
								cityMap.put("id", id);
								cityList.add(cityMap);
							}
							String[] from = { "name" };
							int[] to = { R.id.txt };
							SimpleAdapter simpleAdapter = new SimpleAdapter(
									SetProvinceActivity.this, cityList,
									R.layout.item_provincelist, from, to);
							provinceList.setAdapter(simpleAdapter);

						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							try {
								reader.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub

					}
				});
	}

}
