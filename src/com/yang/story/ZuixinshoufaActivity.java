package com.yang.story;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import cn.smssdk.gui.CommonDialog;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yang.MYModel.Story;
import com.yang.MyAdapter.SearchGvAdapter;
import android.app.Activity;

import android.app.Dialog;

import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ZuixinshoufaActivity extends Activity {
	LinearLayout zuixinshoufa_back;
	List<Story> storyList;
	GridView zuixin_list;
	Dialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_zuixinshoufa);
		init();
	}

	private void init() {
		zuixin_list = (GridView) findViewById(R.id.zuixin_list);
		zuixin_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(ZuixinshoufaActivity.this,
						XiangqingActivity.class);
				intent.putExtra("story", storyList.get(arg2));
				startActivity(intent);

			}
		});
		// 获得首发数据

		updateData();
		zuixinshoufa_back = (LinearLayout) findViewById(R.id.zuixinshoufa_back);
		zuixinshoufa_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});

	}

	private void updateData() {
		storyList = new ArrayList<Story>();
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase
				+ "list_top_file_api.do?limitNumber=20", null,
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
								Story story = new Story();
								while (reader.hasNext()) {
									String tagName = reader.nextName();
									if (tagName.equals("fileID")) {
										story.setFileID(reader.nextInt());
									} else if (tagName.equals("outlineID")) {
										story.setOutlineID(reader.nextInt());
									} else if (tagName.equals("fileTitle")) {
										story.setFileTitle(reader.nextString());
									} else if (tagName
											.equals("fileDescription")) {
										story.setFileDescription(reader
												.nextString());
									} else if (tagName.equals("fileName")) {
										story.setFileName(reader.nextString());
									} else if (tagName.equals("fileUrl")) {
										story.setFileUrl(reader.nextString());
									} else if (tagName.equals("fileDuration")) {
										story.setFileDuration(reader.nextInt());
									} else if (tagName.equals("hour")) {
										story.setHour(reader.nextInt());
									} else if (tagName.equals("minute")) {
										story.setMinute(reader.nextInt());
									} else if (tagName.equals("second")) {
										story.setSecond(reader.nextInt());
									} else if (tagName.equals("picBigUrl")) {
										story.setPicBigUrl(reader.nextString());
									} else if (tagName.equals("picNormalUrl")) {
										story.setPicNormalUrl(reader
												.nextString());
									} else if (tagName.equals("picSmallUrl")) {
										story.setPicSmallUrl(reader
												.nextString());
									} else if (tagName.equals("fileSize")) {
										story.setFileSize(reader.nextInt());
									} else if (tagName
											.equals("collectionNumber")) {
										reader.nextInt();
									} else if (tagName.equals("playNumber")) {
										story.setRenqi(reader.nextInt());
									} else if (tagName.equals("zanNumber")) {
										reader.nextInt();
									} else if (tagName.equals("commentNumber")) {
										story.setCommentNumber(reader.nextInt());
									} else if (tagName.equals("zanValue")) {
										story.setRating(reader.nextInt());
									} else if (tagName.equals("fileSize")) {
										story.setFileSize(reader.nextInt());
									} else if (tagName.equals("uploadTime")) {
										reader.nextString();
									}
								}
								reader.endObject();
								storyList.add(story);
								// Toast.makeText(FrameActivity.this,
								// storyList.size()+"",
								// Toast.LENGTH_SHORT).show();
							}
							reader.endArray();
							reader.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						SearchGvAdapter shoufaAdapter = new SearchGvAdapter(
								ZuixinshoufaActivity.this, storyList);
						zuixin_list.setAdapter(shoufaAdapter);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(ZuixinshoufaActivity.this, "服务器错误",
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
}
