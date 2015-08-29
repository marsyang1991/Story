package com.yang.story;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ZhaohuimimaActivity extends Activity implements Callback {
	LinearLayout zhaohui_back;
	EditText zhaohui_yanzhengma, zhaohui_shoujihao;
	Button zhaohui_getyanzhengma, zhaohui_next;
	EventHandler eventHandler;
	CountDownHanlder countDownHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zhaohuimima);
		init();
	}

	private void initSMSSDK() {
		SMSSDK.initSDK(this, getResources().getString(R.string.appkey),
				getResources().getString(R.string.appsecret));
		final Handler handler = new Handler(this);
		eventHandler = new EventHandler() {
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

	class CountDownHanlder extends Handler {

		@Override
		public void handleMessage(Message msg) {
			Button bt = (Button) ZhaohuimimaActivity.this
					.findViewById(R.id.zhaohui_getSMSbt);
			int countDown = msg.arg1;
			if (countDown != 0) {
				bt.setEnabled(false);
				bt.setTextColor(Color.GRAY);
				bt.setText(countDown + "秒");
			} else {
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

	private void init() {
		initSMSSDK();
		zhaohui_back = (LinearLayout) findViewById(R.id.zhaohui_back);
		zhaohui_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		zhaohui_getyanzhengma = (Button) findViewById(R.id.zhaohui_getSMSbt);
		zhaohui_getyanzhengma.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String phoneString = zhaohui_shoujihao.getText().toString();
				// checkPhoneNum(phoneString, "+86")
				SMSSDK.getVerificationCode("86", phoneString.trim());
				Thread countDownThread = new Thread(new Runnable() {

					@Override
					public void run() {
						int countDown = 60;
						while (countDown >= 0) {
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
				// countDownThread.start();

			}
		});
		zhaohui_yanzhengma = (EditText) findViewById(R.id.zhaohui_sms);
		zhaohui_shoujihao = (EditText) findViewById(R.id.zhaohui_shoujihao);
		zhaohui_next = (Button) findViewById(R.id.zhaohui_bt_next);
		zhaohui_next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/***************** 发送信息到服务器，等待返回 *******************/
				String yanzhengmaString = zhaohui_yanzhengma.getText()
						.toString();
				String phoneString = zhaohui_shoujihao.getText().toString();
				SMSSDK.submitVerificationCode("86", phoneString,
						yanzhengmaString);
			}
		});
	}

	@Override
	public boolean handleMessage(Message msg) {
		int event = msg.arg1;
		int result = msg.arg2;
		if (result == SMSSDK.RESULT_COMPLETE) {
			switch (event) {
			case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
				// 提交验证码成功
				Intent intent = new Intent(ZhaohuimimaActivity.this,
						XiugaimimaActivity.class);
				intent.putExtra("phonenum", zhaohui_shoujihao.getText()
						.toString());
				startActivity(intent);
				break;
			case SMSSDK.EVENT_GET_VERIFICATION_CODE:
				// 获取验证码事件返回
				Toast.makeText(this, R.string.yanzhengmaSent,
						Toast.LENGTH_SHORT).show();
				break;

			}
		} else {
			Toast.makeText(this, "验证码注册失败", Toast.LENGTH_SHORT).show();
		}

		return false;
	}

}
