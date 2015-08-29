package com.yang.story;

import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.LinearLayout;

public class WelcomeActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		LinearLayout mLinear = (LinearLayout) findViewById(R.id.WelcomeLinear);
		new Thread() {
			@Override
			public void run() {
				try {
					Thread.sleep(1500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Message msg = hand.obtainMessage();
				hand.sendMessage(msg);
			}
		}.start();
	}

	Handler hand = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			isFirstRun();
			Intent intent = new Intent(WelcomeActivity.this,
					FrameActivity.class);
			startActivity(intent);

			finish();
		}
	};

	// 判断是否是第一次运行
	private boolean isFirstRun() {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		if (isFirstRun) {
			File file = new File("/sdcard/story/");
			editor.putBoolean("isFirstRun", false);
			editor.commit();
			if (!file.exists()) {
				file.mkdir();
			}
			// startActivity(new
			// Intent(WelcomeActivity.this,FrameActivity.class));
			return true;

		} else {
			// startActivity(new
			// Intent(WelcomeActivity.this,FrameActivity.class));
			return false;
		}

	}

	// 按下后退键效果
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return true;
	}
}
