package com.yang.story;

import org.json.JSONObject;
import org.json.JSONTokener;

import cn.smssdk.gui.CommonDialog;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class XiugaimimaActivity extends Activity {
	LinearLayout xiugai_back;
	EditText xiugai_yanzhengma, xiugai_pw;
	Button xiugai_getyanzhengma, xiugai_wancheng;
	TextView xiugai_dianhua;
	String dianhua;
	Dialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiugaimima);
		Intent intent = getIntent();
		dianhua = intent.getExtras().getString("phonenum");
		init();
	}

	private void init() {
		/*********** 本地文件读取账号 *********/
		xiugai_dianhua = (TextView) findViewById(R.id.xiugai_dianhua);
		xiugai_dianhua.setText(dianhua);
		xiugai_back = (LinearLayout) findViewById(R.id.xiugai_back);
		xiugai_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		xiugai_getyanzhengma = (Button) findViewById(R.id.xiugai_getSMSbt);
		xiugai_getyanzhengma.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/*********** 发送电话到服务器，发送验证码 ***********/

			}
		});
		xiugai_yanzhengma = (EditText) findViewById(R.id.xiugai_sms);
		xiugai_pw = (EditText) findViewById(R.id.xiugai_pw);
		xiugai_wancheng = (Button) findViewById(R.id.xiugai_bt_wancheng);
		xiugai_wancheng.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// String yanzhengmaString =
				// xiugai_yanzhengma.getText().toString();
				String pwString = xiugai_pw.getText().toString();
				String urlBase = getResources().getString(R.string.db_url_base);

				RequestParams params = new RequestParams();
				HttpUtils http = new HttpUtils();
				http.send(HttpMethod.GET, urlBase + "api_update_user.do?"
						+ "userName=" + dianhua + "&passwd=" + pwString, null,
						new RequestCallBack<String>() {

							@Override
							public void onSuccess(
									ResponseInfo<String> responseInfo) {
								if (pd != null && pd.isShowing())
									pd.dismiss();
								String string = responseInfo.result;
								JSONTokener jsonParser = new JSONTokener(string);
								JSONObject jsonObject = (JSONObject) jsonParser
										.nextValue();
								boolean result = Boolean
										.parseBoolean(jsonObject
												.getString("rst"));
								if (result) {
									Toast.makeText(XiugaimimaActivity.this,
											"修改成功,请返回登录", Toast.LENGTH_SHORT)
											.show();
									SharedPreferences preferences = getSharedPreferences(
											"userInfo", MODE_PRIVATE);
									Editor editor = preferences.edit();
									editor.putBoolean("flag", false);
									startActivity(new Intent(
											XiugaimimaActivity.this,
											FrameActivity.class));
									finish();
								} else {
									Toast.makeText(XiugaimimaActivity.this,
											"修改失败", Toast.LENGTH_SHORT).show();
								}

							}

							@Override
							public void onFailure(HttpException error,
									String msg) {
								Toast.makeText(XiugaimimaActivity.this,
										"服务器错误", Toast.LENGTH_SHORT).show();
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								showDialog();
							}
						});

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

}