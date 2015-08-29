package com.yang.story;

import org.json.JSONObject;
import org.json.JSONTokener;

import cn.smssdk.gui.CommonDialog;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Activity implements OnClickListener {
	LinearLayout llback;
	Button loginButton;
	EditText username;
	EditText password;
	ImageView disanfangQQ;
	ImageView disanfangWeixin;
	ImageView disanfangWeibo;
	TextView wangjimima, zhuce;
	boolean flag;
	Dialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	private void init() {
		llback = (LinearLayout) findViewById(R.id.titlebacklinearlayout);
		llback.setOnClickListener(this);
		loginButton = (Button) findViewById(R.id.Login_OK);
		loginButton.setOnClickListener(this);
		username = (EditText) findViewById(R.id.Login_user);
		password = (EditText) findViewById(R.id.Login_password);
		disanfangQQ = (ImageView) findViewById(R.id.login_disanfangQQ);
		disanfangWeibo = (ImageView) findViewById(R.id.login_disanfangWeibo);
		disanfangWeixin = (ImageView) findViewById(R.id.login_disanfangWeixin);
		wangjimima = (TextView) findViewById(R.id.Login_wangjimima);
		zhuce = (TextView) findViewById(R.id.Login_zhuce);
		zhuce.setOnClickListener(this);
		wangjimima.setOnClickListener(this);
		disanfangQQ.setOnClickListener(this);
		disanfangWeibo.setOnClickListener(this);
		disanfangWeixin.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.titlebacklinearlayout:
			finish();
			break;
		/*************** 与服务器交互 ******************************************/
		case R.id.Login_OK:
			String usernameString = username.getText().toString();
			String passwordString = password.getText().toString();
			login(usernameString, passwordString);

			break;
		case R.id.login_disanfangQQ:
			break;
		case R.id.login_disanfangWeibo:
			break;
		case R.id.login_disanfangWeixin:
			break;
		case R.id.Login_wangjimima:
			startActivity(new Intent(LoginActivity.this,
					ZhaohuimimaActivity.class));
			break;
		/**********************************************************************/
		case R.id.Login_zhuce:
			startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
			break;
		}

	}

	private boolean login(final String username, final String password) {
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase + "api_login.do?" + "username="
				+ username + "&passwd=" + password, null,
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
							// 更新用户资料，更新本地数据
							/*
							 * myInfo 标题 设置book
							 */
							Toast.makeText(LoginActivity.this, "登录成功",
									Toast.LENGTH_SHORT).show();
							
							int uid = jsonObject.getInt("uid");
							FrameActivity.user.setUid(uid);
							FrameActivity.user.setUsername(username);
							FrameActivity.user.setPassword(password);
							SharedPreferences preferences = getSharedPreferences(
									"userInfo", MODE_PRIVATE);
							Editor editor = preferences.edit();
							editor.putBoolean("flag", true);
							editor.putString("username", username);
							editor.putString("password", password);
							editor.putString("uid", uid + "");
							editor.commit();
							finish();
							Intent intent = new Intent(FrameActivity.ACTION_LOG);
							sendBroadcast(intent);
						} else {
							// setResult(RESULT_CANCELED);
							Toast.makeText(LoginActivity.this, "登录失败",
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(LoginActivity.this, "服务器错误",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						showDialog();
					}

				});
		return flag;
	}

	private void showDialog() {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(this);
		pd.show();
	}
	
	
	class OnImageViewClickListener implements OnClickListener
	{

		@Override
		public void onClick(View arg0) {
			int id = arg0.getId();
			if(id==R.id.login_disanfangWeibo)
			{
				
				return;
			}
			
		}
		
	}

}
