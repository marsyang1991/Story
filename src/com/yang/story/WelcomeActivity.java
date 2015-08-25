package com.yang.story;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

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
			Intent intent = new Intent(WelcomeActivity.this,FrameActivity.class);
			startActivity(intent);
			isFirstRun();
			finish();
		}
	};
	
    //判断是否是第一次运行
    private boolean isFirstRun() {
		SharedPreferences sharedPreferences = this.getSharedPreferences(
				"share", MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);
		Editor editor = sharedPreferences.edit();
		if (isFirstRun) {
			File file = new File("/sdcard/story/");
			if(!file.exists())
			{
				file.mkdir();
			}
			return true;
			
		} else {
			editor.putBoolean("isFirstRun", false);
			editor.commit();
			return false;
		}
	}
    //按下后退键效果
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return true;
	}
}
