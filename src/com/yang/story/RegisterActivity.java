package com.yang.story;
import java.util.Random;

import org.json.JSONObject;
import org.json.JSONTokener;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
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
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends Activity implements OnClickListener, Callback{
	LinearLayout llback;
	Button yanzhengmaButton,submitButton;
	EditText phonenum,yanzhengma,pw;
	CheckBox cb;
	TextView tiaokuan;
	boolean result = false;
	EventHandler eventHandler; 
	CountDownHanlder countDownHandler;
	Dialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		init();
	}
	private void initSMSSDK()
	{
		SMSSDK.initSDK(this, getResources().getString(R.string.appkey), getResources().getString(R.string.appsecret));
		final Handler handler = new Handler(this);
		eventHandler= new EventHandler() {
			@Override
			public void afterEvent(int event, int result, Object data) {
				Message msg = new Message();
				msg.arg1 = event;
				msg.arg2 = result;
				msg.obj = data;
				handler.sendMessage(msg);
			}
		};
		// 注册回调监听接口
		SMSSDK.registerEventHandler(eventHandler);
		
	}
	class CountDownHanlder extends Handler{

		@Override
		public void handleMessage(Message msg) {
			Button bt = (Button)RegisterActivity.this.findViewById(R.id.register_getSMSbt);
			int countDown = msg.arg1;
			if(countDown!=0){
				bt.setEnabled(false);
				bt.setTextColor(Color.GRAY);
				bt.setText(countDown + "秒");	
			}else {
				bt.setEnabled(true);
				bt.setTextColor(Color.parseColor("#FD8914"));
				bt.setText("获取验证码");
			}
			
		}
		
	}
	@Override
	protected void onDestroy() {
		SMSSDK.unregisterEventHandler(eventHandler);
		super.onDestroy();
	}
	private void init()
	{
		initSMSSDK();
		countDownHandler = new CountDownHanlder();
		llback = (LinearLayout)findViewById(R.id.titlebacklinearlayout);
		llback.setOnClickListener(this);
		yanzhengma = (EditText)findViewById(R.id.register_sms);
		yanzhengma.setOnClickListener(this);
		phonenum = (EditText)findViewById(R.id.register_user);
		pw = (EditText)findViewById(R.id.register_password);
		yanzhengmaButton = (Button)findViewById(R.id.register_getSMSbt);
		yanzhengmaButton.setOnClickListener(this);
		submitButton= (Button)findViewById(R.id.register_okbt);
		submitButton.setOnClickListener(this);
		cb = (CheckBox)findViewById(R.id.register_cb);
		tiaokuan = (TextView)findViewById(R.id.register_tiaokuan);		
		tiaokuan.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId())
		{
		case R.id.titlebacklinearlayout:
			finish();
			break;
		case R.id.register_tiaokuan:
			startActivity(new Intent(RegisterActivity.this,TiaokuanActivity.class));
			break;
		case R.id.register_getSMSbt:
			String phoneString = phonenum.getText().toString();
			//checkPhoneNum(phoneString, "+86")
			SMSSDK.getVerificationCode("86",phoneString.trim());
			Thread countDownThread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					int countDown = 60;
					while(countDown>=0){
						Message msg = new Message();
						msg.arg1 = countDown;
						countDownHandler.sendMessage(msg);
						countDown--;
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					
				}
			});
			countDownThread.start();
			break;
		case R.id.register_okbt:
			if(cb.isChecked())
			{
				
				String phonenumString = phonenum.getText().toString();
				String yanzhengmaString = yanzhengma.getText().toString();
				String passwordString = pw.getText().toString();
				if(phonenumString.trim().isEmpty())
				{
					Toast.makeText(RegisterActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(passwordString.trim().isEmpty())
				{
					Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(passwordString.trim().length()<6||passwordString.trim().length()>20)
				{
					Toast.makeText(RegisterActivity.this, "密码长度为6-20", Toast.LENGTH_SHORT).show();
					return;
				}
				
				/**********与服务器交互发送注册信息，返回成功后自动登录，失败Toast*******************/
				SMSSDK.submitVerificationCode("86", phonenumString, yanzhengmaString);
				
					
				
				/*************************************************************************/
			}else {
				Toast.makeText(RegisterActivity.this, "请接受使用条款和隐私政策", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		
	}
	//提交用户信息
	private void registerUser(String country, String phone) {
		Random rnd = new Random();
		int id = Math.abs(rnd.nextInt());
		String uid = String.valueOf(id);
		
	}
	@Override
	public boolean handleMessage(Message msg) {
		int event = msg.arg1;
		int result = msg.arg2;
		if(result==SMSSDK.RESULT_COMPLETE)
		{
			switch(event)
			{
			case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
				//提交验证码成功
				registerUserToMyDB();
				break;
			case SMSSDK.EVENT_GET_VERIFICATION_CODE:
				//获取验证码事件返回
				Toast.makeText(this, R.string.yanzhengmaSent, Toast.LENGTH_SHORT).show();
				break;
			
			}
		}else{
			Toast.makeText(this, "验证码注册失败", Toast.LENGTH_SHORT).show();
		}
		
	
		return false;
	}
	private boolean registerUserToMyDB()
	{
		String urlBase = getResources().getString(R.string.db_url_base);
		String username = phonenum.getText().toString();
		String password = pw.getText().toString();
		RequestParams params = new RequestParams();
		params.addHeader("userName", username);
		params.addHeader("passwd",password);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase+"api_register.do?"+"userName="+username+"&passwd="+password, null, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if(pd!=null&&pd.isShowing())
					pd.dismiss();
				String string  = responseInfo.result;
				JSONTokener jsonParser = new JSONTokener(string);
				JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
				result = Boolean.parseBoolean(jsonObject.getString("rst"));
				if(result)
				{
					Toast.makeText(RegisterActivity.this, "注册成功,请登录", Toast.LENGTH_SHORT).show();
					finish();
				}
				else {
					Toast.makeText(RegisterActivity.this, "注册失败,请重试", Toast.LENGTH_SHORT).show();
				}
			
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(RegisterActivity.this, "服务器错误", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				showDialog();
			}
			
		});

		return result;
	}
	private void showDialog(){
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(this);
		pd.show();
	}
}
