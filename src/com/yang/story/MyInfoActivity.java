package com.yang.story;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONObject;
import org.json.JSONTokener;

import cn.smssdk.gui.CommonDialog;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yang.MYModel.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyInfoActivity extends Activity implements OnClickListener {
	LinearLayout backLayout;
	RelativeLayout touxiangLayout;
	RelativeLayout zhanghaoLayout;
	TextView tv_xingming;
	TextView tv_xingbie;
	RelativeLayout xingbieLayout;
	RelativeLayout dateLayout;
	RelativeLayout mingziLayout;
	RelativeLayout qqLayout;
	RelativeLayout youxiangLayout;
	TextView tv_qq;
	TextView tv_youxiang;
	TextView tv_save;
	TextView tv_date, tv_zhanghao;
	Builder dialog_builder;
	AlertDialog dialog;
	LayoutInflater inflater;
	MyHandler myhandler;
	ImageView myInfo_iv_touxiang;
	Bitmap newBitmap;
	Dialog pd;
	User user = FrameActivity.user;
	boolean flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myinfo);
		init();
	}

	private void init() {
		tv_zhanghao = (TextView) findViewById(R.id.tv_zhanghao);
		tv_zhanghao.setText(user.getUsername());
		myInfo_iv_touxiang = (ImageView) findViewById(R.id.myInfo_iv_touxiang);
		backLayout = (LinearLayout) findViewById(R.id.myinfo_back);
		backLayout.setOnClickListener(this);
		tv_save = (TextView) findViewById(R.id.myinfo_save);
		tv_save.setOnClickListener(this);
		touxiangLayout = (RelativeLayout) findViewById(R.id.myinfo_touxiang);
		touxiangLayout.setOnClickListener(this);
		zhanghaoLayout = (RelativeLayout) findViewById(R.id.myinfo_zhanghao);
		tv_xingming = (TextView) findViewById(R.id.tv_mingzi);
		tv_xingbie = (TextView) findViewById(R.id.tv_xingbie);
		xingbieLayout = (RelativeLayout) findViewById(R.id.myinfo_xingbie);
		xingbieLayout.setOnClickListener(this);
		dateLayout = (RelativeLayout) findViewById(R.id.myinfo_date);
		dateLayout.setOnClickListener(this);
		tv_qq = (TextView) findViewById(R.id.tv_QQ);
		tv_youxiang = (TextView) findViewById(R.id.tv_youxiang);
		inflater = LayoutInflater.from(MyInfoActivity.this);
		tv_date = (TextView) findViewById(R.id.tv_date);
		mingziLayout = (RelativeLayout) findViewById(R.id.myinfo_mingzi);
		mingziLayout.setOnClickListener(this);
		qqLayout = (RelativeLayout) findViewById(R.id.myinfo_QQ);
		qqLayout.setOnClickListener(this);
		youxiangLayout = (RelativeLayout) findViewById(R.id.myinfo_youxiang);
		youxiangLayout.setOnClickListener(this);
		myhandler = new MyHandler();
		initData();
	}

	private void initData() {
		if(FrameActivity.user.getPortait()!=null)
		{
		BitmapUtils bitmapUtils = new BitmapUtils(MyInfoActivity.this);
		bitmapUtils.display(myInfo_iv_touxiang,
				getResources().getString(R.string.db_url_base)
						+ FrameActivity.user.getPortait());
		}else {
			myInfo_iv_touxiang.setImageResource(R.drawable.ic_default_touxiang);
		}
		tv_xingming.setText(user.getNickname());
		tv_xingbie.setText(user.getGender());
		tv_date.setText(user.getDate());
		tv_qq.setText(user.getQq());
		tv_youxiang.setText(user.getEmail());
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.dialog_gender_male:
		case R.id.dialog_gender_female:
			tv_xingbie.setText(((TextView) arg0).getText());
			dialog.cancel();
			break;
		case R.id.myinfo_touxiang:
			Intent intent = new Intent(MyInfoActivity.this,
					SelectPicPopupWindow.class);
			startActivityForResult(intent, 1);
			break;
		case R.id.myinfo_xingbie:
			dialog_builder = new Builder(MyInfoActivity.this);
			View genderView = inflater.inflate(R.layout.dialog_gender, null);
			TextView dialog_gengder_male = (TextView) genderView
					.findViewById(R.id.dialog_gender_male);
			dialog_gengder_male.setOnClickListener(this);
			TextView dialog_gengder_female = (TextView) genderView
					.findViewById(R.id.dialog_gender_female);
			dialog_gengder_female.setOnClickListener(this);
			dialog_builder.setView(genderView);
			dialog = dialog_builder.show();
			break;
		case R.id.myinfo_date:
			dialog_builder = new Builder(this);
			View view = inflater.inflate(R.layout.dialog_date, null);
			final DatePicker picker = (DatePicker) view
					.findViewById(R.id.dialog_datePicker);
			String dateOrigin = tv_date.getText().toString();
			if (dateOrigin != "") {
				String[] date = dateOrigin.split("-");
				picker.init(Integer.parseInt(date[0]),
						Integer.parseInt(date[1]), Integer.parseInt(date[2]),
						null);
			}
			dialog_builder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							int year = picker.getYear();
							int month = picker.getMonth() + 1;
							int day = picker.getDayOfMonth();
							tv_date.setText(year + "-" + month + "-" + day);
						}
					});
			dialog_builder.setNegativeButton("Cancel", null);
			dialog_builder.setView(view);
			dialog_builder.show();
			break;
		case R.id.myinfo_mingzi:
			dialog_builder = new Builder(this);
			View view2 = inflater.inflate(R.layout.dialog_mingzi, null);
			final EditText content = (EditText) view2
					.findViewById(R.id.dialog_editText);
			Button ok2 = (Button) view2.findViewById(R.id.bt_ok);
			Button cancel2 = (Button) view2.findViewById(R.id.bt_cancel);
			ok2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putInt("id", R.id.tv_mingzi);
					b.putString("content", content.getText().toString());
					msg.setData(b);
					myhandler.sendMessage(msg);
					dialog.cancel();
				}
			});
			cancel2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.cancel();
				}
			});
			content.setText(tv_xingming.getText());
			dialog_builder.setView(view2);
			dialog = dialog_builder.show();
			break;
		case R.id.myinfo_QQ:
			dialog_builder = new Builder(this);
			View view3 = inflater.inflate(R.layout.dialog_qq, null);
			final EditText content3 = (EditText) view3
					.findViewById(R.id.dialog_editText);
			Button ok3 = (Button) view3.findViewById(R.id.bt_ok);
			Button cancel3 = (Button) view3.findViewById(R.id.bt_cancel);
			ok3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putInt("id", R.id.tv_QQ);
					b.putString("content", content3.getText().toString());
					msg.setData(b);
					myhandler.sendMessage(msg);
					dialog.cancel();
				}
			});
			cancel3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.cancel();
				}
			});
			content3.setText(tv_qq.getText());
			dialog_builder.setView(view3);
			dialog = dialog_builder.show();
			break;
		case R.id.myinfo_youxiang:
			dialog_builder = new Builder(this);
			View view4 = inflater.inflate(R.layout.dialog_youxiang, null);
			final EditText content4 = (EditText) view4
					.findViewById(R.id.dialog_editText);
			Button ok4 = (Button) view4.findViewById(R.id.bt_ok);
			Button cancel4 = (Button) view4.findViewById(R.id.bt_cancel);
			ok4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Message msg = new Message();
					Bundle b = new Bundle();
					b.putInt("id", R.id.tv_youxiang);
					b.putString("content", content4.getText().toString());
					msg.setData(b);
					myhandler.sendMessage(msg);
					dialog.cancel();
				}
			});
			cancel4.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					dialog.cancel();
				}
			});
			content4.setText(tv_youxiang.getText());
			dialog_builder.setView(view4);
			dialog = dialog_builder.show();
			break;
		case R.id.myinfo_back:
			this.finish();
			break;
		case R.id.myinfo_save:
			/************ 上传数据库，本地保存 ****************/
			updataToDb();
			break;
		}

	}

	private void updataToDb() {
		String urlBase = getResources().getString(R.string.db_url_base);

		File f = null;
		try {
			myInfo_iv_touxiang.setDrawingCacheEnabled(true);
			Bitmap touxiang = myInfo_iv_touxiang.getDrawingCache();
			f = new File("/sdcard/Story/touxiang.png");
			FileOutputStream out = new FileOutputStream(f);
			touxiang.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			myInfo_iv_touxiang.setDrawingCacheEnabled(false);
			HttpUtils http = new HttpUtils();
			RequestParams params = new RequestParams();
			params.addBodyParameter("uid", FrameActivity.user.getUid() + "");
			params.addBodyParameter("portrait", f);
			http.send(HttpMethod.POST, urlBase + "upload_portrait.do", params,
					new RequestCallBack<String>() {

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							String string = responseInfo.result;
							JSONTokener jsonParser = new JSONTokener(string);
							JSONObject jsonObject = (JSONObject) jsonParser
									.nextValue();
							Boolean flag = Boolean.parseBoolean(jsonObject
									.getString("rst"));
							if (flag) {
								Toast.makeText(MyInfoActivity.this, "保存图片成功",
										Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(FrameActivity.ACTION_LOG);
								sendBroadcast(intent);
							} else {
								Toast.makeText(MyInfoActivity.this, "保存图片失败",
										Toast.LENGTH_SHORT).show();
							}
							
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Toast.makeText(MyInfoActivity.this, "保存图片服务器错误",
									Toast.LENGTH_SHORT).show();

						}
					});

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpUtils http = new HttpUtils();
		int xingbie = 0;
		if (tv_xingbie.getText().toString().equals("男")) {
			xingbie = 1;
		} else if (tv_xingbie.getText().toString().equals("女")) {
			xingbie = 2;
		}
		RequestParams params = new RequestParams();
		params.addBodyParameter("uid",FrameActivity.user.getUid()+"");
		params.addBodyParameter("userName", tv_zhanghao.getText().toString());
		params.addBodyParameter("nickName", tv_xingming.getText().toString());
		params.addBodyParameter("gender", xingbie+"");
		params.addBodyParameter("qqNumber",tv_qq.getText().toString());
		params.addBodyParameter("email", tv_youxiang.getText().toString());
		http.send(HttpMethod.POST, urlBase + "api_update_user.do", params,
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

							Toast.makeText(MyInfoActivity.this, "保存成功",
									Toast.LENGTH_SHORT).show();
						} else {

							Toast.makeText(MyInfoActivity.this, "保存失败",
									Toast.LENGTH_SHORT).show();
						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(MyInfoActivity.this, "服务器错误",
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == -1)
			return;
		if (resultCode == 0) {
			Bitmap bitmap = data.getParcelableExtra("image");
			newBitmap = Util.thumbnailWithImageWithoutScale(
					MyInfoActivity.this, bitmap);
			myInfo_iv_touxiang.setImageBitmap(newBitmap);
		} else if (resultCode == 1) {
			String uriString = data.getExtras().getString("imageUri");
			Uri uri = Uri.parse(uriString);
			System.out.println(uriString);
			ContentResolver cr = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(uri));

				myInfo_iv_touxiang.setImageBitmap(Util
						.thumbnailWithImageWithoutScale(MyInfoActivity.this,
								bitmap));
			} catch (FileNotFoundException e) {
				Log.e("Exception", e.getMessage(), e);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			Bundle data = msg.getData();
			TextView tv = (TextView) MyInfoActivity.this.findViewById(data
					.getInt("id"));
			tv.setText(data.getString("content"));
			super.handleMessage(msg);
		}

	}

}
