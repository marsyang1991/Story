package com.yang.story;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.JSONTokener;

import cn.smssdk.gui.CommonDialog;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yang.MYModel.Textbook;
import com.yang.MyAdapter.TextbookAdapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class SetTextbookActivity extends Activity {
	private Button setTextbookOK;
	private ListView textbookList;
	private TextView skipTextView;
	List<Map<String, Object>> textbooks;
	String currentCity = "";
	String currentGrade = "";
	String currentCourse = "";
	String currentVersion = "";
	Dialog pd;
	Boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settextbook);
		init();
	}

	private Textbook getSttedTextbook() {
		SharedPreferences sharedPreferences = getSharedPreferences("textbook",
				Context.MODE_PRIVATE);
		boolean flag = sharedPreferences.getBoolean("flag", false);
		if (flag) {
			String city = sharedPreferences.getString("city", "");
			String course = sharedPreferences.getString("course", "");
			String grade = sharedPreferences.getString("grade", "");
			String version = sharedPreferences.getString("version", "");
			Textbook textbook = new Textbook();
			return textbook;
		}
		return null;
	}

	private void init() {
		/*
		 * Textbook settedTextbook = getSttedTextbook();
		 * if(settedTextbook!=null) { currentCity = settedTextbook.getCity();
		 * currentCourse = settedTextbook.getCourse(); currentGrade =
		 * settedTextbook.getGrade(); currentVersion =
		 * settedTextbook.getVersion(); }
		 */
		setTextbookOK = (Button) findViewById(R.id.settextbook_okbt);
		setTextbookOK.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				updataToDb();
				SharedPreferences sharedPreferences = getSharedPreferences(
						"userInfo", Context.MODE_PRIVATE); // 私有数据
				Editor editor = sharedPreferences.edit();// 获取编辑器
				editor.putBoolean("flag", true);
				editor.putString("city", currentCity);
				editor.putString("grade", currentGrade);
				editor.putString("course", currentCourse);
				editor.putString("version", currentVersion);
				editor.commit();// 提交修改
				setResult(RESULT_OK);
				finish();
			}
		});
		textbookList = (ListView) findViewById(R.id.settextbook_listview);
		skipTextView = (TextView) findViewById(R.id.settextbook_skip);
		skipTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				setResult(RESULT_CANCELED);
				startActivity(new Intent(SetTextbookActivity.this,
						FrameActivity.class));
				SetTextbookActivity.this.finish();
			}
		});
		textbooks = new ArrayList<Map<String, Object>>();
		TextbookAdapter adapter = null;

		textbookList.setAdapter(adapter);

		textbookList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				for (int i = 0; i < arg0.getCount(); i++) {
					View view = arg0.getChildAt(i);
					LinearLayout ll = (LinearLayout) view
							.findViewById(R.id.textbookListItemLayout);
					ll.setBackgroundColor(Color.parseColor("#ffffff"));
					TextView cityTextView = (TextView) view
							.findViewById(R.id.city);
					TextView courseTextView = (TextView) view
							.findViewById(R.id.course);
					TextView gradeTextView = (TextView) view
							.findViewById(R.id.grade);
					TextView versionTextView = (TextView) view
							.findViewById(R.id.version);
					cityTextView.setTextColor(Color.parseColor("#848484"));
					gradeTextView.setTextColor(Color.parseColor("#848484"));
					courseTextView.setTextColor(Color.parseColor("#848484"));
					versionTextView.setTextColor(Color.parseColor("#848484"));
				}
				LinearLayout ll = (LinearLayout) arg1
						.findViewById(R.id.textbookListItemLayout);
				ll.setBackgroundColor(Color.parseColor("#F8E0CD"));
				TextView cityTextView = (TextView) arg1.findViewById(R.id.city);
				TextView courseTextView = (TextView) arg1
						.findViewById(R.id.course);
				TextView gradeTextView = (TextView) arg1
						.findViewById(R.id.grade);
				TextView versionTextView = (TextView) arg1
						.findViewById(R.id.version);
				cityTextView.setTextColor(Color.parseColor("#F68720"));
				gradeTextView.setTextColor(Color.parseColor("#F68720"));
				courseTextView.setTextColor(Color.parseColor("#F68720"));
				versionTextView.setTextColor(Color.parseColor("#F68720"));
				currentCity = cityTextView.getText().toString();
				currentCourse = courseTextView.getText().toString();
				currentGrade = gradeTextView.getText().toString();
				currentVersion = versionTextView.getText().toString();
			}
		});
	}

	private void updataToDb() {
		SharedPreferences preferences = getSharedPreferences("userInfo",
				MODE_PRIVATE);
		String username = preferences.getString("username", null);
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		String data = "userName=" + username + "&=";
		http.send(HttpMethod.GET, urlBase + "api_update_user.do?" + data, null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						if (pd != null && pd.isShowing()) {
							pd.dismiss();
						}
						String string = responseInfo.result;
						JSONTokener jsonParser = new JSONTokener(string);
						JSONObject jsonObject = (JSONObject) jsonParser
								.nextValue();
						flag = Boolean.parseBoolean(jsonObject.getString("rst"));
						if (flag) {
							Toast.makeText(SetTextbookActivity.this, "设置成功",
									Toast.LENGTH_SHORT).show();
							finish();
						} else {
							setResult(RESULT_CANCELED);
							Toast.makeText(SetTextbookActivity.this, "设置失败",
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(SetTextbookActivity.this, "服务器错误",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
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

	// 按下后退键效果
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

		}
		return true;
	}

	/*
	 * @Override public void onChanged(WheelView wheel, int oldValue, int
	 * newValue) { if (wheel == cityWheelView) { updateGrade(); } else if (wheel
	 * == gradeWheelView) { updateVersion(); }else if(wheel==versionWheelView) {
	 * updateCourse(); } else if (wheel == courseWheelView) { currentCourse =
	 * courseDatas.get(currentVersion)[newValue]; }
	 * 
	 * }
	 * 
	 * private void updateGrade() { int pCurrent =
	 * cityWheelView.getCurrentItem(); currentGrade =
	 * gradeDatas.get(currentCity)[pCurrent]; String[] grades =
	 * gradeDatas.get(currentGrade);
	 * 
	 * if (grades == null) { grades = new String[] { "" }; }
	 * gradeWheelView.setViewAdapter(new ArrayWheelAdapter<String>(this,
	 * grades)); gradeWheelView.setCurrentItem(0); updateVersion(); } private
	 * void updateVersion() { int pCurrent = gradeWheelView.getCurrentItem();
	 * currentGrade = versionDatas.get(currentCity)[pCurrent]; String[] versions
	 * = gradeDatas.get(currentGrade);
	 * 
	 * if (versions == null) { versions = new String[] { "" }; }
	 * versionWheelView.setViewAdapter(new ArrayWheelAdapter<String>(this,
	 * versions)); versionWheelView.setCurrentItem(0); updateCourse(); } private
	 * void updateCourse() { int pCurrent = versionWheelView.getCurrentItem();
	 * currentVersion = versionDatas.get(currentGrade)[pCurrent]; String[]
	 * courses = versionDatas.get(currentVersion);
	 * 
	 * if (courses == null) { courses = new String[] { "" }; }
	 * courseWheelView.setViewAdapter(new ArrayWheelAdapter<String>(this,
	 * courses)); courseWheelView.setCurrentItem(0); }
	 */

}
