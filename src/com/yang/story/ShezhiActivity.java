package com.yang.story;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShezhiActivity extends Activity{
	LinearLayout shezhi_back;
	RelativeLayout shezhi_xiugai, shezhi_kefu, shezhi_about;
	Button shezhi_tuichu;
	boolean isLogin;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shezhi);
		Intent intent = getIntent();
		isLogin = intent.getBooleanExtra("isLogin", false);
		init();
	}
	private void init()
	{
		shezhi_back = (LinearLayout)findViewById(R.id.shezhi_back);
		shezhi_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		shezhi_xiugai = (RelativeLayout)findViewById(R.id.shezhi_xiugai);
		shezhi_xiugai.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isLogin){
					Intent intent = new Intent(ShezhiActivity.this,XiugaimimaActivity.class);
					SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
					
					intent.putExtra("phonenum", preferences.getString("username", ""));
					startActivity(intent);
				}
				else {
					Toast.makeText(ShezhiActivity.this, "尚未登录，请返回登录", Toast.LENGTH_SHORT).show();
				}
			}
		});
		shezhi_kefu = (RelativeLayout)findViewById(R.id.shezhi_kefudianhua);
		final TextView kefudianhua = (TextView)findViewById(R.id.tv_kefu);	
		shezhi_kefu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri.parse("tel:"+kefudianhua.getText().toString()); 
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);  
				startActivity(intent);
			}
		});
	
		shezhi_about = (RelativeLayout)findViewById(R.id.shezhi_about);
		shezhi_about.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(ShezhiActivity.this,AboutActivity.class));
			}
		});
		shezhi_tuichu = (Button)findViewById(R.id.shezhi_tuichu);
		shezhi_tuichu.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isLogin){
					SharedPreferences preferences = getSharedPreferences("userInfo", MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putBoolean("flag", false);
					editor.commit();
					finish();
				}else {
					Toast.makeText(ShezhiActivity.this, "尚未登录，请返回登录", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
	
	
}
