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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.JsonReader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchActivity extends Activity {
	ImageView search_back, search_cha;
	EditText et_search;
	GridView gv_search;
	List<Story> datas;
	TextView tv_search;
	Dialog pd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		search_back = (ImageView) findViewById(R.id.search_back);
		search_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		search_cha = (ImageView) findViewById(R.id.search_cha);
		search_cha.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				et_search.setText("");
			}
		});
		et_search = (EditText) findViewById(R.id.et_search);

		et_search.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				if (arg0.toString().equals("")) {
					search_cha.setVisibility(View.GONE);
				} else {
					search_cha.setVisibility(View.VISIBLE);
				}

			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub

			}
		});
		gv_search = (GridView) findViewById(R.id.gv_search);

		tv_search = (TextView) findViewById(R.id.search_save);
		tv_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String ss = et_search.getText().toString();
				if (!ss.trim().equals("")) {
					String urlBase = getResources().getString(
							R.string.db_url_base);
					HttpUtils http = new HttpUtils();
					final List<Story> storyList = new ArrayList<Story>();
					http.send(HttpMethod.GET, urlBase
							+ "search_file_api.do?keyword=" + ss.trim(), null,
							new RequestCallBack<String>() {

								@Override
								public void onSuccess(
										ResponseInfo<String> responseInfo) {
									if (pd != null && pd.isShowing()) {
										pd.dismiss();
									}
									String string = responseInfo.result;
									JsonReader reader = new JsonReader(
											new StringReader(string));
									try {
										reader.beginArray();
										while (reader.hasNext()) {
											reader.beginObject();
											Story story = new Story();
											while (reader.hasNext()) {
												String tagName = reader
														.nextName();
												if (tagName.equals("fileID")) {
													story.setFileID(reader
															.nextInt());
												} else if (tagName
														.equals("outlineID")) {
													story.setOutlineID(reader
															.nextInt());
												} else if (tagName
														.equals("fileTitle")) {
													story.setFileTitle(reader
															.nextString());
												} else if (tagName
														.equals("fileDescription")) {
													story.setFileDescription(reader
															.nextString());
												} else if (tagName
														.equals("fileName")) {
													story.setFileName(reader
															.nextString());
												} else if (tagName
														.equals("fileUrl")) {
													story.setFileUrl(reader
															.nextString());
												} else if (tagName
														.equals("fileDuration")) {
													story.setFileDuration(reader
															.nextInt());
												} else if (tagName
														.equals("hour")) {
													story.setHour(reader
															.nextInt());
												} else if (tagName
														.equals("minute")) {
													story.setMinute(reader
															.nextInt());
												} else if (tagName
														.equals("second")) {
													story.setSecond(reader
															.nextInt());
												} else if (tagName
														.equals("picBigUrl")) {
													story.setPicBigUrl(reader
															.nextString());
												} else if (tagName
														.equals("picNormalUrl")) {
													story.setPicNormalUrl(reader
															.nextString());
												} else if (tagName
														.equals("picSmallUrl")) {
													story.setPicSmallUrl(reader
															.nextString());
												} else if (tagName
														.equals("fileSize")) {
													story.setFileSize(reader
															.nextInt());
												} else if (tagName
														.equals("collectionNumber")) {
													reader.nextInt();
												} else if (tagName
														.equals("playNumber")) {
													story.setRenqi(reader
															.nextInt());
												} else if (tagName
														.equals("zanNumber")) {
													reader.nextInt();
												} else if (tagName
														.equals("commentNumber")) {
													story.setCommentNumber(reader
															.nextInt());
												} else if (tagName
														.equals("zanValue")) {
													story.setRating(reader
															.nextInt());
												} else if (tagName
														.equals("fileSize")) {
													story.setFileSize(reader
															.nextInt());
												} else if (tagName
														.equals("uploadTime")) {
													reader.nextString();
												}
											}
											reader.endObject();
											storyList.add(story);
											// Toast.makeText(SearchActivity.this,
											// storyList.size()+"",
											// Toast.LENGTH_SHORT).show();
										}
										reader.endArray();
										reader.close();
									} catch (Exception e) {
										e.printStackTrace();
									}
									if (storyList.size() != 0) {
										SearchGvAdapter adapter = new SearchGvAdapter(
												SearchActivity.this, storyList);
										gv_search.setAdapter(adapter);
										gv_search
												.setOnItemClickListener(new OnItemClickListener() {

													@Override
													public void onItemClick(
															AdapterView<?> arg0,
															View arg1,
															int arg2, long arg3) {
														Intent intent = new Intent(
																SearchActivity.this,
																XiangqingActivity.class);
														intent.putExtra(
																"story",
																storyList
																		.get(arg2));
														startActivity(intent);
													}
												});
									} else {
										Toast.makeText(SearchActivity.this,
												"无相关记录", Toast.LENGTH_SHORT)
												.show();
									}

								}

								@Override
								public void onFailure(HttpException error,
										String msg) {
									Toast.makeText(SearchActivity.this,
											"服务器错误", Toast.LENGTH_SHORT).show();

								}

								@Override
								public void onLoading(long total, long current,
										boolean isUploading) {
									showDialog();
								}

							});

				}
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
