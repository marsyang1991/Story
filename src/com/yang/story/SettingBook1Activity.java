package com.yang.story;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.ListMapWheelAdapter;
import cn.smssdk.gui.CommonDialog;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class SettingBook1Activity extends Activity {
	String provinceId;
	HashMap<String, String> cityMap, gradeMap, subjectMap, versionMap;
	List<Map<String, String>> cityList, gradeList, subjectList, versionList;
	WheelView cityWheelView, gradeWheelView, subjectWheelView,
			versionWheelView;
	Button btNext;
	Dialog pd;
	String urlBase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		urlBase = getResources().getString(R.string.db_url_base);
		setContentView(R.layout.activity_settextbook1);
		Intent intent = getIntent();
		provinceId = intent.getExtras().getString("id");
		cityWheelView = (WheelView) findViewById(R.id.wheel_city);
		
		cityWheelView.setDrawShadows(false);
		gradeWheelView = (WheelView) findViewById(R.id.wheel_grade);
		gradeWheelView.setDrawShadows(false);
		subjectWheelView = (WheelView) findViewById(R.id.wheel_course);
		subjectWheelView.setDrawShadows(false);
		versionWheelView = (WheelView) findViewById(R.id.wheel_version);
		versionWheelView.setDrawShadows(false);
		getKemu();
		getCity();
		getVersion();
		getGrade();
		btNext = (Button) findViewById(R.id.settextbook1_okbt);
		btNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String cityId = cityList.get(cityWheelView.getCurrentItem())
						.get("id");
				String gradeId = gradeList.get(gradeWheelView.getCurrentItem())
						.get("id");
				String subjectId = subjectList.get(
						subjectWheelView.getCurrentItem()).get("id");
				String versionId = versionList.get(
						versionWheelView.getCurrentItem()).get("id");
				Intent intent = new Intent(SettingBook1Activity.this,
						SettingBook2Activity.class);
				intent.putExtra("provinceId", provinceId);
				intent.putExtra("cityId", cityId);
				intent.putExtra("gradeId", gradeId);
				intent.putExtra("subjectId", subjectId);
				intent.putExtra("versionId", versionId);
				startActivity(intent);
			}
		});
	}

	private void getKemu() {
		// 获取科目信息
		subjectList = new ArrayList<Map<String, String>>();
		subjectMap = new HashMap<String, String>();
		HttpUtils httpSubject = new HttpUtils();
		httpSubject.send(HttpMethod.GET, urlBase + "list_subject_api.do", null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (pd != null && pd.isShowing()) {
							pd.dismiss();
						}
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

									if (tagName.equals("subjectID")) {
										id = reader.nextString();
									} else if (tagName.equals("subjectName")) {
										name = reader.nextString();
									} else if (tagName.equals("idDeleted")) {
										reader.nextInt();
									}

								}
								reader.endObject();
								subjectMap = new HashMap<String, String>();
								subjectMap.put("name", name);
								subjectMap.put("id", id);
								subjectList.add(subjectMap);
							}
							Message msg = new Message();
							msg.what = 1;
							handler.handleMessage(msg);
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
						Toast.makeText(SettingBook1Activity.this, "获取科目失败",
								Toast.LENGTH_SHORT).show();
						finish();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						showDialog();
					}

				});

	}

	private void getVersion() {
		// 获取版本信息
		versionList = new ArrayList<Map<String, String>>();
		versionMap = new HashMap<String, String>();
		HttpUtils httpVersion = new HttpUtils();
		httpVersion.send(HttpMethod.GET, urlBase + "list_version_api.do", null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (pd != null && pd.isShowing()) {
							pd.dismiss();
						}
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

									if (tagName.equals("versionID")) {
										id = reader.nextString();
									} else if (tagName.equals("versionName")) {
										name = reader.nextString();
									} else if (tagName.equals("idDeleted")) {
										reader.nextInt();
									}

								}
								reader.endObject();
								versionMap = new HashMap<String, String>();
								versionMap.put("name", name);
								versionMap.put("id", id);
								versionList.add(versionMap);
							}

							Message msg = new Message();
							msg.what = 2;
							handler.handleMessage(msg);

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
						Toast.makeText(SettingBook1Activity.this, "获取版本失败",
								Toast.LENGTH_SHORT).show();
						finish();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						showDialog();
					}
				});
	}

	private void getGrade() {
		// 获取年级信息
		gradeList = new ArrayList<Map<String, String>>();
		gradeMap = new HashMap<String, String>();
		HttpUtils httpGrade = new HttpUtils();
		httpGrade.send(HttpMethod.GET, urlBase + "list_grade_api.do", null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (pd != null && pd.isShowing()) {
							pd.dismiss();
						}
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

									if (tagName.equals("gradeID")) {
										id = reader.nextString();
									} else if (tagName.equals("gradeName")) {
										name = reader.nextString();
									} else if (tagName.equals("idDeleted")) {
										reader.nextInt();
									}

								}
								reader.endObject();
								gradeMap = new HashMap<String, String>();
								gradeMap.put("name", name);
								gradeMap.put("id", id);
								gradeList.add(gradeMap);
							}
							Message msg = new Message();
							msg.what = 3;
							handler.handleMessage(msg);

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
						Toast.makeText(SettingBook1Activity.this, "获取年级失败",
								Toast.LENGTH_SHORT).show();
						finish();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						showDialog();
					}
				});

	}

	private void getCity() {
		cityList = new ArrayList<Map<String, String>>();
		cityMap = new HashMap<String, String>();
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase + "list_city_api.do?provinceId="
				+ provinceId, null, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				String string = responseInfo.result;
				JsonReader reader = new JsonReader(new StringReader(string));
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
								name = reader.nextString();
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
					Message msg = new Message();
					msg.what = 4;
					handler.handleMessage(msg);
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
				Toast.makeText(SettingBook1Activity.this, "获取城市失败",
						Toast.LENGTH_SHORT).show();
				finish();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				showDialog();
			}
		});

	}

	private void showDialog() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(this);
		pd.show();
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int wheelIndex = msg.what;
			switch (wheelIndex) {
			case 1:
				subjectWheelView.setViewAdapter(new ListMapWheelAdapter(
						SettingBook1Activity.this, subjectList));
				subjectWheelView.setCurrentItem(0);
				break;
			case 2:
				versionWheelView.setViewAdapter(new ListMapWheelAdapter(
						SettingBook1Activity.this, versionList));
				versionWheelView.setCurrentItem(0);
				break;
			case 3:
				gradeWheelView.setViewAdapter(new ListMapWheelAdapter(
						SettingBook1Activity.this, gradeList));
				gradeWheelView.setCurrentItem(0);
				break;
			case 4:
				cityWheelView.setViewAdapter(new ListMapWheelAdapter(
						SettingBook1Activity.this, cityList));
				cityWheelView.setCurrentItem(0);
				break;
			default:
				break;
			}
		}

	};
}
