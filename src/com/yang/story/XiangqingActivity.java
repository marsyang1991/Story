package com.yang.story;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.Templates;

import org.json.JSONObject;
import org.json.JSONTokener;

import cn.smssdk.gui.CommonDialog;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yang.MYModel.DownLoad;
import com.yang.MYModel.Pinglun;
import com.yang.MYModel.Story;
import com.yang.MYModel.StreamingMediaPlayer;
import com.yang.MyAdapter.PinglunGvAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class XiangqingActivity extends Activity{
	LinearLayout xq_back,xq_pinglun;
	TextView shoucangTextView, fenxiangTextView,pingjiaTextView,lixianTextView,
			 showalltTextView,kemuTextView,virsionTextView,renqiTextView,shichangTextView,
			 xq_content,xq_title_txt;
	int orgRating,myRating;
	RatingBar orgRatingBar,myRatingBar;
	SeekBar seekBar;
	boolean isshoucang,isshowall;
	int contentOrginHeight;
	ListView xq_pinglun_list;
	List<Pinglun> pinglunData;
	static MediaPlayer mediaPlayer;
	ImageView playImageView,previorImageView,nextImageView,xq_iv_touxiang;
	Bundle data;
	Story story;
	Dialog pd;
	Dialog dialog;
	Handler handler;
	Runnable updateThread;
	String res_url_base;
	BitmapUtils bitmapUtils;
	String urlBase; 
	HttpHandler downHandler;
	int currentPosition;
	int rating;//用户对该知识点的评分；
	boolean hasCollected;//用户是否收藏
	StreamingMediaPlayer smp;

	@Override
	protected void onDestroy() {
		if(smp!=null){
			smp.stopPlayer();
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					HttpUtils http = new HttpUtils();
					int uid = FrameActivity.user.getUid();
					http.send(HttpMethod.GET, urlBase+"api_play.do?uid="+uid+"&fileID="+story.getFileID(), null, null);
					Util.writeStoryIntoXml(story);
					
				}
			});
			thread.start();
		}
		if(downHandler!=null)
			downHandler.cancel();
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_xiangqing);
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
			
			@Override
			public void onPrepared(MediaPlayer arg0) {
				//arg0.start();
				seekBar.setMax(mediaPlayer.getDuration());
				handler.post(updateThread);
			}
		});
		
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer arg0) {
				mediaPlayer.release();
				mediaPlayer = null;
				seekBar.setProgress(0);
			}
		});
		urlBase = getResources().getString(R.string.db_url_base);
		
		bitmapUtils = new BitmapUtils(this);
		res_url_base = this.getResources().getString(R.string.db_image_url_base);
		Intent intent = getIntent();
		data = intent.getExtras();
		story = (Story)data.getSerializable("story");
		xq_pinglun_list = (ListView)findViewById(R.id.xq_pinglun_list);
		xq_iv_touxiang = (ImageView)findViewById(R.id.xq_iv_touxiang);
		xq_title_txt = (TextView)findViewById(R.id.xq_title_txt);
		myRatingBar = (RatingBar)findViewById(R.id.xq_my_ratingBarlarge);
		myRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				if(!fromUser)
					return;
				HttpUtils http = new HttpUtils();
				int uid = FrameActivity.user.getUid();
				int fileId  = story.getFileID();
				http.send(HttpMethod.GET, urlBase+"api_zan.do?uid="+uid+"&fileID="+story.getFileID()+"&zanValue="+(int)rating, null, new RequestCallBack<String>(){

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String string  = responseInfo.result;
						JSONTokener jsonParser = new JSONTokener(string);
						JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
						boolean flag = Boolean.parseBoolean(jsonObject.getString("rst"));
						if(flag)
						{
							Toast.makeText(XiangqingActivity.this, "评价成功", Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(XiangqingActivity.this, "评价失败", Toast.LENGTH_SHORT).show();
						}
						
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(XiangqingActivity.this, "评价,服务器故障", Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
		xq_content = (TextView)findViewById(R.id.xq_content);
		contentOrginHeight = xq_content.getHeight();
		handler = new Handler();
		updateThread = new Runnable() {
			
			@Override
			public void run() {
				if(mediaPlayer!=null)
				{
					seekBar.setProgress(mediaPlayer.getCurrentPosition());
					handler.postDelayed(updateThread, 100);
				}
			}
		};
		xq_back = (LinearLayout)findViewById(R.id.xq_back);
		xq_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		shoucangTextView = (TextView)findViewById(R.id.xq_shoucang);
		shoucangTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!FrameActivity.isLogin)
				{
					Toast.makeText(XiangqingActivity.this, "尚未登录，无法收藏",Toast.LENGTH_SHORT).show();
					return;
				}
				if(!isshoucang)
				{
					HttpUtils http = new HttpUtils();
					int uid = FrameActivity.user.getUid();
					int fileId = story.getFileID();
					http.send(HttpMethod.GET, urlBase+"api_collection.do?uid="+uid+"&fileID="+story.getFileID(), null, new RequestCallBack<String>(){

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							String string  = responseInfo.result;
							JSONTokener jsonParser = new JSONTokener(string);
							JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
							boolean flag = Boolean.parseBoolean(jsonObject.getString("rst"));
							if(flag)
							{
								Toast.makeText(XiangqingActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
								isshoucang = true;
								shoucangTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_xq_shoucang), null, null, null);
							}
							else
							{
								Toast.makeText(XiangqingActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
							}
							
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Toast.makeText(XiangqingActivity.this, "收藏,服务器故障", Toast.LENGTH_SHORT).show();
						}
					});
				}
				else {
					HttpUtils http = new HttpUtils();
					int uid = FrameActivity.user.getUid();
					http.send(HttpMethod.GET, urlBase+"api_del_collection.do?uid="+uid+"&fileID="+story.getFileID(), null, new RequestCallBack<String>(){

						@Override
						public void onSuccess(ResponseInfo<String> responseInfo) {
							String string  = responseInfo.result;
							JSONTokener jsonParser = new JSONTokener(string);
							JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
							boolean flag = Boolean.parseBoolean(jsonObject.getString("rst"));
							if(flag)
							{
								Toast.makeText(XiangqingActivity.this, "取消成功", Toast.LENGTH_SHORT).show();
								isshoucang = false;
								shoucangTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_shoucang), null, null, null);
							}
							else
							{
								Toast.makeText(XiangqingActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
							}
						}

						@Override
						public void onFailure(HttpException error, String msg) {
							Toast.makeText(XiangqingActivity.this, "取消收藏,服务器故障", Toast.LENGTH_SHORT).show();
						}
					});
				}
				
				
				
			}
		});
		fenxiangTextView = (TextView)findViewById(R.id.xq_fenxiang);
		fenxiangTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				/**********底部弹出分享页面********/
				if(!FrameActivity.isLogin)
				{
					Toast.makeText(XiangqingActivity.this, "尚未登录，无法分享",Toast.LENGTH_SHORT).show();
					return;
				}
				
				
				
			}
		});
		
		lixianTextView = (TextView)findViewById(R.id.xq_lixian);
		lixianTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				HttpUtils http = new HttpUtils();
				downHandler = http.download(res_url_base+story.getFileUrl(), "/sdcard/story/"+story.getFileUrl(), true,true,new RequestCallBack<File>() {

					@Override
					public void onSuccess(ResponseInfo<File> responseInfo) {
						Toast.makeText(XiangqingActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
						
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						if(msg.equals("maybe the file has downloaded completely"))
							Toast.makeText(XiangqingActivity.this, "已下载", Toast.LENGTH_SHORT).show();
						else
							Toast.makeText(XiangqingActivity.this, "下载失败"+msg, Toast.LENGTH_SHORT).show();
						
					}
					@Override public void onLoading(long total, long current, boolean isUploading) 
					{ 
						Toast.makeText(XiangqingActivity.this, "已下载"+current*100/total+"%", Toast.LENGTH_SHORT).show();
					}
				});
				
				
			}
		});
		
		showalltTextView = (TextView)findViewById(R.id.xq_showall);
		showalltTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!isshowall)
				{
					isshowall = true;
					LayoutParams lp = xq_content.getLayoutParams();
					lp.height  = LayoutParams.WRAP_CONTENT;
					xq_content.setLayoutParams(lp);
					showalltTextView.setCompoundDrawablesWithIntrinsicBounds(null, null,getResources().getDrawable(R.drawable.ic_sanjiao_xiangshang), null);

				}
				else {
					isshowall = false;
					LayoutParams lp = xq_content.getLayoutParams();
					lp.height  = Util.dip2px(XiangqingActivity.this, 80);
					xq_content.setLayoutParams(lp);
					showalltTextView.setCompoundDrawablesWithIntrinsicBounds(null,null, getResources().getDrawable(R.drawable.huangsanjiao), null);
				}
			}
		});
		xq_pinglun = (LinearLayout)findViewById(R.id.xq_bt_pinglun);
		xq_pinglun.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(!FrameActivity.isLogin)
				{
					Toast.makeText(XiangqingActivity.this, "尚未登录，无法评论",Toast.LENGTH_SHORT).show();
					return;
				}
				
				Builder builder = new Builder(XiangqingActivity.this);
				LayoutInflater inflater = XiangqingActivity.this.getLayoutInflater();
				View view2 = inflater.inflate(R.layout.dialog_pinglun, null);
				final EditText content = (EditText)view2.findViewById(R.id.dialog_editText);
				Button ok2 = (Button)view2.findViewById(R.id.bt_ok);
				Button cancel2 = (Button)view2.findViewById(R.id.bt_cancel);
				ok2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						String contentString = content.getText().toString();
						/**********上传评论***********/
						HttpUtils http = new HttpUtils();
						int uid = FrameActivity.user.getUid();
						http.send(HttpMethod.GET, urlBase+"api_comment.do?uid="+uid+"&fileID="+story.getFileID()+"&commentText="+contentString, null, new RequestCallBack<String>(){

							@Override
							public void onSuccess(ResponseInfo<String> responseInfo) {
								String string  = responseInfo.result;
								JSONTokener jsonParser = new JSONTokener(string);
								JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
								boolean flag = Boolean.parseBoolean(jsonObject.getString("rst"));
								if(flag)
								{
									Toast.makeText(XiangqingActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
								}
								else
								{
									Toast.makeText(XiangqingActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
								}
								
							}

							@Override
							public void onFailure(HttpException error, String msg) {
								Toast.makeText(XiangqingActivity.this, "评论,服务器故障", Toast.LENGTH_SHORT).show();
							}
						});
						//刷新评论列表
						updatePinglunList();
						dialog.cancel();
						
					}
				});
				cancel2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						dialog.cancel();
					}
				});
				builder.setView(view2);
				dialog = builder.show();
			}
		});
		shichangTextView = (TextView)findViewById(R.id.xq_tv_shichang);
		kemuTextView = (TextView)findViewById(R.id.xq_tv_kecheng);
		renqiTextView = (TextView)findViewById(R.id.xq_tv_renqi);
		orgRatingBar = (RatingBar)findViewById(R.id.xq_ratingBar);
		xq_content = (TextView)findViewById(R.id.xq_content);
		updateWidgeContent();
		ScrollView scrollView = (ScrollView)findViewById(R.id.xq_scrollView);
		playImageView = (ImageView)findViewById(R.id.xq_iv_play);
		previorImageView = (ImageView)findViewById(R.id.xq_iv_previor);
		nextImageView = (ImageView)findViewById(R.id.xq_iv_next);
		seekBar = (SeekBar)findViewById(R.id.seekBar);
		
		
		playImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				try {
						File file = new File("/sdcard/story/"+story.getFileUrl());
						if(file.exists())
						{
							mediaPlayer.setDataSource("/sdcard/story/"+story.getFileUrl()); 
							mediaPlayer.prepare();
						}else {
							/*HttpUtils http = new HttpUtils();
							downHandler = http.download(res_url_base+story.getFileUrl(), "/sdcard/story/temp.dat", true,true,new RequestCallBack<File>() {

								@Override
								public void onSuccess(ResponseInfo<File> responseInfo) {
									Toast.makeText(XiangqingActivity.this, "缓冲完成", Toast.LENGTH_SHORT).show();
									playImageView.setImageResource(R.drawable.ic_play);
								}
								
								@Override
								public void onStart() {
									
									super.onStart();
								}

								@Override
								public void onFailure(HttpException error, String msg) {
									Toast.makeText(XiangqingActivity.this, "播放，服务器错误"+msg, Toast.LENGTH_SHORT).show();
									
								}
								@Override public void onLoading(long total, long current, boolean isUploading) 
								{ 
									try {
										if(mediaPlayer.isPlaying())
										{
											int currentPosition = mediaPlayer.getCurrentPosition();
											mediaPlayer.setDataSource("/sdcard/story/temp.dat");
											mediaPlayer.prepare();
											mediaPlayer.start();
											mediaPlayer.seekTo(currentPosition);
											playImageView.setImageResource(R.drawable.ic_pause);
										}
										else {
											int currentPosition = mediaPlayer.getCurrentPosition();
											mediaPlayer.setDataSource("/sdcard/story/temp.dat");
											mediaPlayer.prepare();
											mediaPlayer.seekTo(currentPosition);
										}
									} catch (IllegalArgumentException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (SecurityException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (IllegalStateException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									//playImageView.setImageResource(R.drawable.ic_huanchong);
								}
							});*/
							
							
						//	URL url = new URL(res_url_base+story.getFileUrl());
							if(smp==null)
							{
								smp = new StreamingMediaPlayer(XiangqingActivity.this, playImageView, seekBar);
								smp.startStreaming(res_url_base+story.getFileUrl(), story.getFileSize(), story.getFileDuration());
								playImageView.setImageResource(R.drawable.ic_pause);
								return;
							}

						//	mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
						//	new Thread(new DownLoad(url, mediaPlayer)).start();
							//InputStream is = url.openStream();
//							File tmp = new File("/sdcard/story/temp.mp3");
//							if(!tmp.exists())
//							{
//								tmp.createNewFile();
//							}
//							FileOutputStream fos = new FileOutputStream(tmp);
//							mediaPlayer = new MediaPlayer();
//							mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
							/*mediaPlayer.setOnPreparedListener(new OnPreparedListener() {
								
								@Override
								public void onPrepared(MediaPlayer arg0) {
									//arg0.start();
									seekBar.setMax(mediaPlayer.getDuration());
									handler.post(updateThread);
								}
							});
							
							mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
								
								@Override
								public void onCompletion(MediaPlayer arg0) {
									mediaPlayer.release();
									mediaPlayer = null;
									seekBar.setProgress(0);
								}
							});*/	
							
						}
						
						//mediaPlayer.setDataSource(res_url_base+story.getFileName());
						
						//mediaPlayer.setDataSource(res_url_base+"/9/7/7/Maid with the Flaxen Hair.mp3");
						
						
						if(!smp.isplaying())
						{
							smp.startPlayer();
							playImageView.setImageResource(R.drawable.ic_pause);
						}
						else {
							smp.pausePlayer();
							playImageView.setImageResource(R.drawable.ic_play);
						}
					
					
					
				} catch (IllegalArgumentException e) {
					Toast.makeText(XiangqingActivity.this, "参数错误", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e) {
					///Toast.makeText(XiangqingActivity.this, "找不到文件", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				}
			}
		});
		scrollView.scrollTo(10, 10);
	}
	private void updatePinglunList() {
		/*************获得评论列表数据*****************/
		pinglunData = new ArrayList<Pinglun>();
		HttpUtils http = new HttpUtils();
		System.out.println(urlBase+"api_comment.do?fileID="+story.getFileID());
		http.send(HttpMethod.GET, urlBase+"api_list_comment.do?fileID="+story.getFileID(), null, new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				String string  = responseInfo.result;
				JsonReader reader = new JsonReader(new StringReader(string));
				try {
					reader.beginArray();
					while(reader.hasNext())
					{
						reader.beginObject();
						Pinglun pl = new Pinglun();
						while (reader.hasNext()) {
							String tagName = reader.nextName();
							if(tagName.equals("fileId"))
							{
								pl.setFileId(reader.nextInt());
							}else if (tagName.equals("commentId")) {
								pl.setCommentId(reader.nextInt());
							}else if (tagName.equals("uid")) {
								pl.setUid(reader.nextInt());
							}else if (tagName.equals("nickName")) {
								pl.setNickName(reader.nextString());
							}else if (tagName.equals("commentText")) {
								pl.setCommentText(reader.nextString());
							}else if (tagName.equals("commentTime")) {
								pl.setCommentTime(reader.nextString());
							}else if (tagName.equals("portraitURL")) {
								pl.setPortraitURL(reader.nextString());
							}
						}
						reader.endObject();
						pinglunData.add(pl);
					}
					reader.endArray();
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				PinglunGvAdapter adapter = new PinglunGvAdapter(XiangqingActivity.this, pinglunData);
				xq_pinglun_list.setAdapter(adapter);
				Util.setListViewHeightBasedOnChildren(xq_pinglun_list);
				
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(XiangqingActivity.this, "评论服务器错误", Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onLoading(long total, long current,
					boolean isUploading) {
				showDialog();
			}

		});		
		
	}
	private void showDialog(){
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(this);
		pd.show();
	}
	private void updateWidgeContent()
	{
		/********科目、版本、总体评价、人气、时长、是否收藏、我的评价、内容简介、标题、评论列表****************/
		orgRatingBar.setProgress((int)story.getRating());
		renqiTextView.setText(story.getRenqi()+"");
		kemuTextView.setText(story.getTitle());
		shichangTextView.setText(Util.parseSecond(story.getFileDuration()));
		xq_title_txt.setText(story.getTitle());
		xq_content.setText(story.getDescription());
		bitmapUtils.display(xq_iv_touxiang, res_url_base+story.getPicNormalUrl());
		
		updatePinglunList();
		getRating();
		getHasCollected();
		
		
	}
	private void getRating()
	{
		/**********获取评分***********/
		HttpUtils http = new HttpUtils();
		int uid = FrameActivity.user.getUid();
		http.send(HttpMethod.GET, urlBase+"api_get_zan.do?uid="+uid+"&fileID="+story.getFileID(), null, new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String string  = responseInfo.result;
				JSONTokener jsonParser = new JSONTokener(string);
				JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
				rating = jsonObject.getInt("rst");
				myRatingBar.setProgress(rating);
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(XiangqingActivity.this, "获取评分,服务器故障", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
	private void getHasCollected()
	{
		HttpUtils http = new HttpUtils();
		int uid = FrameActivity.user.getUid();
		http.send(HttpMethod.GET, urlBase+"api_get_collection.do?uid="+uid+"&fileID="+story.getFileID(), null, new RequestCallBack<String>(){

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String string  = responseInfo.result;
				JSONTokener jsonParser = new JSONTokener(string);
				JSONObject jsonObject = (JSONObject) jsonParser.nextValue();
				isshoucang = Boolean.parseBoolean(jsonObject.getString("rst"));
				if(isshoucang)
				{
					shoucangTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_xq_shoucang), null, null, null);
				}else {
					shoucangTextView.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_shoucang), null, null, null);
				}
			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(XiangqingActivity.this, "评论,服务器故障", Toast.LENGTH_SHORT).show();
			}
		});
		
	}
	
		
}
