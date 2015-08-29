package com.yang.story;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.json.JSONTokener;
import cn.smssdk.gui.CommonDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yang.MYModel.Story;
import com.yang.MYModel.Textbook;
import com.yang.MYModel.User;
import com.yang.MyAdapter.HistoryGvAdapter;
import com.yang.MyAdapter.ShoufaGvAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.JsonReader;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FrameActivity extends Activity {
	public static final String ACTION_LOG = "com.yang.log";
	public static final String ACTION_SHOUCANG = "com.yang.shoucang";
	public static final String ACTION_HISTORY = "com.yang.history";
	public static final int RESULT_SANXIAN = 1;
	public static final int RESULT_LOGIN = 4;
	public static Map<String, Integer> kemuIdMap = new HashMap<String, Integer>();
	public static Map<String, Integer> versionIdMap = new HashMap<String, Integer>();
	public static Map<String, Integer> gradeIdMap = new HashMap<String, Integer>();
	public static User user = new User();
	public static boolean isLogin = false;
	private GridView shoufaGridView, zuireGridView, gv, shoucangGV;
	private HistoryGvAdapter historyAdapter, shoucangAdapter;
	private LinearLayout mMyBottemShouyeBtn, mMyBottemShoucangBtn,
			mMyBottemHistoryBtn, mMyBottemMyBtn, touxiangLayout,
			shoucang_ll_buttons;
	private ImageView mMyBottemShouyeImg, mMyBottemShoucangImg,
			mMyBottemHistoryImg, mMyBottemMyImg;
	private TextView mMyBottemShouyeTxt, mMyBottemShoucangTxt,
			mMyBottemHistoryTxt, mMyBottemMyTxt, my_title_name,
			history_tv_edit, shoucang_tv_edit;
	private ImageView mymyNext, myshoucangNext, myhitoryNext, myfenxiangNext,
			myshezhiNext, my_title_imageview, image_cry, history_iv_left,
			shoucang_iv_left;
	private List<View> list = new ArrayList<View>();
	private android.support.v4.view.ViewPager mViewPager = null;
	private PagerAdapter pagerAdapter = null;
	private LayoutInflater inflater;
	private View shouyeView, shoucangView, historyView, myView;
	private List<Story> shoufaDatas = new ArrayList<Story>(),
			zuireDatas = new ArrayList<Story>(),
			shoucangDatas = new ArrayList<Story>(),
			historyDatas = new ArrayList<Story>();
	private Textbook book = new Textbook();
	private Dialog pd;
	private int[] flags, shoucangFlags;
	private BroadcastReceiver logReceiver,shoucangReceiver,historyReceiver;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			int msgType = msg.what;
			switch (msgType) {
			case 1:// 登录成功后获得用户信息
				Log.e("登录", "登录");
				// Toast.makeText(FrameActivity.this, "登录成功",
				// Toast.LENGTH_SHORT).show();
				updateUser();
				break;
			case 2:// 获得信息后刷新本地信息及界面
					// Toast.makeText(FrameActivity.this, "成功",
					// Toast.LENGTH_SHORT).show();
				Log.e("刷新登录用户", "刷新登录用户");
				isLogin = true;
				Util.saveUser(FrameActivity.this, FrameActivity.user);
				my_title_name.setText(user.getUsername());
				touxiangLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						mymyNext.performClick();
					}
				});
				if (user.getPortait() != null) {
					BitmapUtils bitmapUtils = new BitmapUtils(
							FrameActivity.this);
					bitmapUtils.display(my_title_imageview,
							getResources()
									.getString(R.string.db_url_base)
									+ user.getPortait());
				}
				initShoucangView();
				break;
			case 3:// 获取收藏列表后刷新页面
				if (shoucangDatas.size() != 0) {
					ImageView image_cry_shoucang = (ImageView) shoucangView
							.findViewById(R.id.image_cry);
					image_cry_shoucang.setVisibility(View.GONE);
					shoucangGV = (GridView) shoucangView
							.findViewById(R.id.shoucang_gv);
					shoucangFlags = new int[shoucangDatas.size()];
					HistoryGvAdapter adapter = new HistoryGvAdapter(
							FrameActivity.this, shoucangDatas, shoucangFlags);
					shoucangGV.setAdapter(adapter);
					shoucangGV
							.setOnItemClickListener(new OnShoucangItemClickListenerUnEditable());
					shoucang_tv_edit.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View arg0) {
							shoucang_tv_edit.setVisibility(View.GONE);
							for (int i = 0; i < shoucangFlags.length; i++) {
								shoucangFlags[i] = 1;
							}
							shoucangGV
									.setOnItemClickListener(new OnShoucangItemClickListenerEditable());
							shoucangAdapter = new HistoryGvAdapter(FrameActivity.this,
									shoucangDatas, shoucangFlags);
							shoucangGV.setAdapter(shoucangAdapter);
							shoucang_ll_buttons.setVisibility(View.VISIBLE);
						}
					});
				} else {
					ImageView image_cry_shoucang = (ImageView) shoucangView
							.findViewById(R.id.image_cry);
					image_cry_shoucang.setVisibility(View.VISIBLE);
				}
			case 4:// 刷新最热列表
				ShoufaGvAdapter zuireAdapter = new ShoufaGvAdapter(
						FrameActivity.this, zuireDatas);
				zuireGridView.setAdapter(zuireAdapter);
				zuireGridView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {

						Intent intent = new Intent(FrameActivity.this,
								XiangqingActivity.class);
						intent.putExtra("story", zuireDatas.get(arg2));
						startActivity(intent);
					}
				});
				break;
			case 5:// 刷新首发列表
				ShoufaGvAdapter shoufaAdapter = new ShoufaGvAdapter(
						FrameActivity.this, shoufaDatas);
				shoufaGridView.setAdapter(shoufaAdapter);
				shoufaGridView
						.setOnItemClickListener(new OnItemClickListener() {

							@Override
							public void onItemClick(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {

								Intent intent = new Intent(FrameActivity.this,
										XiangqingActivity.class);
								intent.putExtra("story", shoufaDatas.get(arg2));
								startActivity(intent);
							}
						});
				break;
			case 7:
				initShoucangView();
				shoucang_tv_edit.setVisibility(View.VISIBLE);
				shoucang_ll_buttons.setVisibility(View.GONE);
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_frame);
		initView();
		mMyBottemShouyeBtn.performClick();
		getKemuId();
		getVersionId();
		getGradeId();
	}

	@Override
	protected void onResume() {
		
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver(logReceiver);
		unregisterReceiver(shoucangReceiver);
		unregisterReceiver(historyReceiver);
		super.onDestroy();
	}

	/*
	 * 尝试登录 登录后更新用户资料
	 */
	private void initMyContent() {
		final SharedPreferences preferences = getSharedPreferences("userInfo",
				MODE_PRIVATE);
		boolean flag = preferences.getBoolean("flag", false);
		if (!flag) {
			// 未登录状态
			my_title_imageview
					.setBackgroundResource(R.drawable.ic_default_touxiang);
			my_title_name.setText("点击登录");
			touxiangLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(FrameActivity.this,
							LoginActivity.class));
				}
			});
			return;
		}
		final String username = preferences.getString("username", "");
		final String password = preferences.getString("password", "");
		// 登录状态
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase + "api_login.do?" + "username="
				+ username + "&passwd=" + password, null,
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
						boolean flag = Boolean.parseBoolean(jsonObject
								.getString("rst"));
						if (flag) {
							// 更新用户资料，更新本地数据
							/*
							 * myInfo 标题 设置book
							 */
							int uid = jsonObject.getInt("uid");
							FrameActivity.user.setUid(uid);
							FrameActivity.user.setUsername(username);
							FrameActivity.user.setPassword(password);
							Message msg = new Message();
							msg.what = 1;
							handler.sendMessage(msg);
						} else {
							my_title_imageview
									.setBackgroundResource(R.drawable.ic_default_touxiang);
							my_title_name.setText("点击登录");
							touxiangLayout
									.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											startActivity(new Intent(
													FrameActivity.this,
													LoginActivity.class));
										}
									});

						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(FrameActivity.this, "服务器错误",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						showDialog();
					}

				});

	}

	private void updateUser() {
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(0);
		http.send(HttpMethod.GET, urlBase + "api_get_user.do?" + "uid="
				+ FrameActivity.user.getUid(), null,
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
							reader.beginObject();
							while (reader.hasNext()) {
								String tagName = reader.nextName();
								if (tagName.equals("userName")) {
									reader.nextString();
								} else if (tagName.equals("uid")) {
									reader.nextInt();
								} else if (tagName.equals("nickName")) {
									FrameActivity.user.setNickname(reader
											.nextString());
								} else if (tagName.equals("gender")) {
									int genderInt = reader.nextInt();
									switch (genderInt) {
									case 0:
										FrameActivity.user.setGender("");
										break;
									case 1:
										FrameActivity.user.setGender("男");
										break;
									case 2:
										FrameActivity.user.setGender("女");
										break;
									default:
										break;
									}
								} else if (tagName.equals("lastLoginTime")) {
									reader.nextString();
								} else if (tagName.equals("portrait")) {
									FrameActivity.user.setPortait(reader
											.nextString());
								} else if (tagName.equals("email")) {
									FrameActivity.user.setEmail(reader
											.nextString());
								} else if (tagName.equals("qqNumber")) {
									FrameActivity.user.setQq(reader
											.nextString());
								} else if (tagName.equals("telphone")) {
									reader.nextString();
								} else if (tagName.equals("areaID")) {
									FrameActivity.user.getBook().setAreaID(
											reader.nextInt());
								} else if (tagName.equals("gradeID")) {
									FrameActivity.user.getBook().setGradeId(
											reader.nextInt());

								}
							}
							reader.endObject();
							reader.close();
							isLogin = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
						// 登录成功后
						Message msg = new Message();
						msg.what = 2;
						handler.handleMessage(msg);

					}

					@Override
					public void onFailure(HttpException error, String msg) {

						Toast.makeText(FrameActivity.this, "更新用户数据失败",
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private void initView() {
		logReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				initMyView();
				initShoucangView();
				
			}
		};
		shoucangReceiver = new BroadcastReceiver()
		{

			@Override
			public void onReceive(Context arg0, Intent arg1) {
				initShoucangView();
				initShouyeView();
			}
			
		};
		historyReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context arg0, Intent arg1) {
				initHistoryView();
				
			}
		};
		registerReceiver(logReceiver, new IntentFilter(ACTION_LOG));
		inflater = LayoutInflater.from(this);
		mViewPager = (ViewPager) findViewById(R.id.FramePager);
		mMyBottemShouyeBtn = (LinearLayout) findViewById(R.id.MyBottemShouyeBtn);
		mMyBottemShoucangBtn = (LinearLayout) findViewById(R.id.MyBottemShoucangBtn);
		mMyBottemHistoryBtn = (LinearLayout) findViewById(R.id.MyBottemHistoryBtn);
		mMyBottemMyBtn = (LinearLayout) findViewById(R.id.MyBottemMyBtn);

		mMyBottemShouyeImg = (ImageView) findViewById(R.id.MyBottemShouyeImg);
		mMyBottemShoucangImg = (ImageView) findViewById(R.id.MyBottemShoucangImg);
		mMyBottemHistoryImg = (ImageView) findViewById(R.id.MyBottemHistoryImg);
		mMyBottemMyImg = (ImageView) findViewById(R.id.MyBottemMyImg);

		mMyBottemShouyeTxt = (TextView) findViewById(R.id.MyBottemShouyeTxt);
		mMyBottemShoucangTxt = (TextView) findViewById(R.id.MyBottemShoucangTxt);
		mMyBottemHistoryTxt = (TextView) findViewById(R.id.MyBottemHistoryTxt);
		mMyBottemMyTxt = (TextView) findViewById(R.id.MyBottemMyTxt);
		createView();

		pagerAdapter = new PagerAdapter() {
			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;
			}

			@Override
			public int getCount() {
				return list.size();
			}

			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				container.removeView(list.get(position));
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				View v = list.get(position);
				container.addView(v);
				return v;
			}
		};
		mViewPager.setAdapter(pagerAdapter);
		MyBtnOnclick mytouchlistener = new MyBtnOnclick();
		mMyBottemShouyeBtn.setOnClickListener(mytouchlistener);
		mMyBottemShoucangBtn.setOnClickListener(mytouchlistener);
		mMyBottemHistoryBtn.setOnClickListener(mytouchlistener);
		mMyBottemMyBtn.setOnClickListener(mytouchlistener);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				initBottemBtn();
				int flag = (Integer) list.get((arg0)).getTag();
				if (flag == 0) {
					mMyBottemShouyeImg
							.setImageResource(R.drawable.ic_shouye_yes);
					mMyBottemShouyeTxt.setTextColor(Color.parseColor("#f68720"));
				} else if (flag == 1) {
					mMyBottemShoucangImg
							.setImageResource(R.drawable.ic_shoucang_yes);
					mMyBottemShoucangTxt.setTextColor(Color
							.parseColor("#f68720"));
				} else if (flag == 2) {
					mMyBottemHistoryImg
							.setImageResource(R.drawable.ic_history_yes);
					mMyBottemHistoryTxt.setTextColor(Color
							.parseColor("#f68720"));
				} else if (flag == 3) {
					mMyBottemMyImg.setImageResource(R.drawable.ic_me_yes);
					mMyBottemMyTxt.setTextColor(Color.parseColor("#f68720"));
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	private void createView() {
		shouyeView = inflater.inflate(R.layout.activity_shouye, null);
		initShouyeView();
		shouyeView.setTag(0);
		list.add(shouyeView);
		shoucangView = inflater.inflate(R.layout.activity_shoucang, null);
		shoucangView.setTag(1);
		list.add(shoucangView);
		initShoucangView();
		historyView = inflater.inflate(R.layout.activity_history, null);
		initHistoryView();
		historyView.setTag(2);
		list.add(historyView);
		myView = inflater.inflate(R.layout.activity_my, null);
		initMyView();
		myView.setTag(3);
		list.add(myView);
	}

	private void initMyView() {

		my_title_imageview = (ImageView) myView
				.findViewById(R.id.my_title_image);
		my_title_name = (TextView) myView.findViewById(R.id.my_title_name);
		mymyNext = (ImageView) myView.findViewById(R.id.ic_my_my_next);
		touxiangLayout = (LinearLayout) myView
				.findViewById(R.id.touxianglayout);
		if (!isLogin) {
			touxiangLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(FrameActivity.this,
							LoginActivity.class));
				}
			});
		} else {
			if(user.getPortait()!=null)
			{BitmapUtils bitmapUtils = new BitmapUtils(FrameActivity.this);
			bitmapUtils.display(
					my_title_imageview,
					getResources().getString(R.string.db_image_url_base)
							+ user.getPortait());}
			else {
				my_title_imageview.setImageResource(R.drawable.ic_default_touxiang);
			}
			touxiangLayout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					mymyNext.performClick();
				}
			});
		}
		mymyNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (isLogin) {
					Intent intent = new Intent(FrameActivity.this,
							MyInfoActivity.class);
					startActivity(intent);
				} else {
					Toast.makeText(FrameActivity.this, "请先登录",
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		myshoucangNext = (ImageView) myView
				.findViewById(R.id.ic_my_shoucang_next);
		myshoucangNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mMyBottemShoucangBtn.performClick();
			}
		});
		myhitoryNext = (ImageView) myView.findViewById(R.id.ic_my_history_next);
		myhitoryNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mMyBottemHistoryBtn.performClick();
			}
		});
		myshezhiNext = (ImageView) myView.findViewById(R.id.ic_my_shezhi_next);
		myshezhiNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FrameActivity.this,
						ShezhiActivity.class);
				intent.putExtra("isLogin", isLogin);
				startActivity(intent);

			}
		});
		myfenxiangNext = (ImageView) myView
				.findViewById(R.id.ic_my_fenxiang_next);
		myfenxiangNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				/******* 第三方登录 ********/

			}
		});
		initMyContent();
	}

	private void initHistoryView() {
		/*
		 * 维护一个xml文件读取historyView
		 */
		historyDatas = new ArrayList<Story>();
		final LinearLayout history_ll_buttons = (LinearLayout) historyView
				.findViewById(R.id.history_ll_buttons);
		image_cry = (ImageView) historyView.findViewById(R.id.image_cry);
		history_iv_left = (ImageView) historyView
				.findViewById(R.id.history_iv_left);
		history_tv_edit = (TextView) historyView
				.findViewById(R.id.history_tv_edit);
		history_iv_left.setVisibility(View.VISIBLE);
		history_iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mMyBottemMyBtn.performClick();
			}
		});
		Button history_bt_ok = (Button) historyView
				.findViewById(R.id.history_bt_ok);
		final Button history_bt_concel = (Button) historyView
				.findViewById(R.id.history_bt_concel);
		history_bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < flags.length; i++) {
					if (flags[i] == 2) {
						Util.deleteHistory(historyDatas.get(i));
						historyDatas.remove(i);
					}
				}
				history_bt_concel.performClick();
			}
		});
		history_bt_concel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				history_ll_buttons.setVisibility(View.GONE);
				initHistoryView();
				history_tv_edit.setVisibility(View.VISIBLE);
			}
		});
		gv = (GridView) historyView.findViewById(R.id.history_gv);
		historyDatas = new ArrayList<Story>();
		historyDatas = Util.getHistoryList();
		gv.setOnItemClickListener(new OnHistoryItemClickListenerUnEditable());
		if (historyDatas.size() == 0) {
			image_cry.setVisibility(View.VISIBLE);
		} else {
			image_cry.setVisibility(View.GONE);
			flags = new int[historyDatas.size()];
			historyAdapter = new HistoryGvAdapter(FrameActivity.this,
					historyDatas, flags);
			gv.setAdapter(historyAdapter);
			history_tv_edit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					history_tv_edit.setVisibility(View.GONE);
					for (int i = 0; i < flags.length; i++) {
						flags[i] = 1;
					}
					gv.setOnItemClickListener(new OnHistoryItemClickListenerEditable());
					historyAdapter = new HistoryGvAdapter(FrameActivity.this,
							historyDatas, flags);
					gv.setAdapter(historyAdapter);
					history_ll_buttons.setVisibility(View.VISIBLE);
				}
			});
		}

	}

	class OnHistoryItemClickListenerEditable implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {
			ImageView iv_delete = (ImageView) view.findViewById(R.id.dustbin);
			if (flags[position] == 1) {
				iv_delete.setImageResource(R.drawable.ic_choosed);
				flags[position] = 2;
			} else if (flags[position] == 2) {
				iv_delete.setImageResource(R.drawable.ic_to_choose);
				flags[position] = 1;
			}
			historyAdapter = new HistoryGvAdapter(FrameActivity.this,
					historyDatas, flags);
			gv.setAdapter(historyAdapter);

		}

	}

	class OnHistoryItemClickListenerUnEditable implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(FrameActivity.this,
					XiangqingActivity.class);
			intent.putExtra("story", historyDatas.get(arg2));
			startActivity(intent);

		}

	}

	class OnShoucangItemClickListenerEditable implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long arg3) {
			ImageView iv_delete = (ImageView) view.findViewById(R.id.dustbin);
			if (shoucangFlags[position] == 1) {
				iv_delete.setImageResource(R.drawable.ic_choosed);
				shoucangFlags[position] = 2;
			} else if (shoucangFlags[position] == 2) {
				iv_delete.setImageResource(R.drawable.ic_to_choose);
				shoucangFlags[position] = 1;
			}
			shoucangAdapter = new HistoryGvAdapter(FrameActivity.this,
					shoucangDatas, shoucangFlags);
			shoucangGV.setAdapter(shoucangAdapter);

		}

	}

	class OnShoucangItemClickListenerUnEditable implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Intent intent = new Intent(FrameActivity.this,
					XiangqingActivity.class);
			intent.putExtra("story", shoucangDatas.get(arg2));
			startActivity(intent);

		}

	}

	private void initShoucangView() {
		shoucangDatas = new ArrayList<Story>();
		shoucang_iv_left = (ImageView) shoucangView
				.findViewById(R.id.shoucang_iv_left);
		shoucang_tv_edit = (TextView) shoucangView
				.findViewById(R.id.shoucang_tv_edit);
		shoucang_ll_buttons = (LinearLayout) shoucangView
				.findViewById(R.id.shoucang_ll_buttons);
		shoucang_iv_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mMyBottemMyBtn.performClick();
			}
		});
		Button shoucang_bt_ok = (Button) shoucangView
				.findViewById(R.id.shoucang_bt_ok);
		final Button shoucang_bt_concel = (Button) shoucangView
				.findViewById(R.id.shoucang_bt_concel);
		shoucang_bt_ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				for (int i = 0; i < shoucangFlags.length; i++) {
					if (shoucangFlags[i] == 2) {
						HttpUtils http = new HttpUtils();
						http.configCurrentHttpCacheExpiry(0);
						int uid = FrameActivity.user.getUid();
						int fileId = shoucangDatas.get(i).getFileID();
						shoucangDatas.remove(i);
						String urlBase = getResources().getString(
								R.string.db_url_base);
						http.send(HttpMethod.GET,
								urlBase + "api_del_collection.do?uid=" + uid
										+ "&fileID="
										+ fileId,
								null,new RequestCallBack<String>() {

									@Override
									public void onSuccess(
											ResponseInfo<String> responseInfo) {
										String string = responseInfo.result;
										JSONTokener jsonParser = new JSONTokener(
												string);
										JSONObject jsonObject = (JSONObject) jsonParser
												.nextValue();
										boolean flag = Boolean
												.parseBoolean(jsonObject
														.getString("rst"));
										if (flag) {
											Toast.makeText(FrameActivity.this,
													"取消成功", Toast.LENGTH_SHORT)
													.show();
											Message msg = new Message();
											msg.what = 7;
											handler.sendMessage(msg);
										} else {
											Toast.makeText(FrameActivity.this,
													"取消失败", Toast.LENGTH_SHORT)
													.show();
										}
									}

									@Override
									public void onFailure(HttpException error,
											String msg) {
										Toast.makeText(FrameActivity.this,
												"取消收藏,服务器故障",
												Toast.LENGTH_SHORT).show();
									}
								});

					}
				}
				
			}
		});
		shoucang_bt_concel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				shoucang_ll_buttons.setVisibility(View.GONE);
				for (int i = 0; i < shoucangFlags.length; i++) {
					shoucangFlags[i] = 0;
				}
				shoucangGV.setOnItemClickListener(new OnShoucangItemClickListenerUnEditable());
				shoucangAdapter = new HistoryGvAdapter(FrameActivity.this,
						shoucangDatas, shoucangFlags);
				shoucangGV.setAdapter(shoucangAdapter);
				shoucang_tv_edit.setVisibility(View.VISIBLE);
			}
		});
		shoucangGV = (GridView) shoucangView.findViewById(R.id.shoucang_gv);
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(0);
		final List<Story> storyList = new ArrayList<Story>();
		http.send(HttpMethod.GET, urlBase + "get_user_collection_api.do?uid="
				+ user.getUid(), null, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				if (pd != null && pd.isShowing()) {
					pd.dismiss();
				}
				System.out.println("get_user_collection_api.do?uid="
						+ user.getUid());
				String string = responseInfo.result;
				JsonReader reader = new JsonReader(new StringReader(string));
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
							} else if (tagName.equals("fileDescription")) {
								story.setFileDescription(reader.nextString());
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
								story.setPicNormalUrl(reader.nextString());
							} else if (tagName.equals("picSmallUrl")) {
								story.setPicSmallUrl(reader.nextString());
							} else if (tagName.equals("fileSize")) {
								story.setFileSize(reader.nextInt());
							} else if (tagName.equals("collectionNumber")) {
								reader.nextInt();
							} else if (tagName.equals("playNumber")) {
								story.setRenqi(reader.nextInt());
							} else if (tagName.equals("zanNumber")) {
								reader.nextInt();
							} else if (tagName.equals("commentNumber")) {
								story.setCommentNumber(reader.nextInt());
							} else if (tagName.equals("zanValue")) {
								story.setRating((int)reader.nextDouble());
							} else if (tagName.equals("fileSize")) {
								story.setFileSize(reader.nextInt());
							} else if (tagName.equals("uploadTime")) {
								reader.nextString();
							}
						}
						reader.endObject();
						storyList.add(story);
					}
					reader.endArray();
					reader.close();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(FrameActivity.this, "获取收藏列表错误",
							Toast.LENGTH_SHORT).show();
				}
				shoucangDatas = storyList;
				Message msg = new Message();
				msg.what = 3;
				handler.sendMessage(msg);

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				ImageView image_cry_shoucang = (ImageView) shoucangView
						.findViewById(R.id.image_cry);
				image_cry_shoucang.setVisibility(View.VISIBLE);
				Toast.makeText(FrameActivity.this, "服务器错误", Toast.LENGTH_SHORT)
						.show();

			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				showDialog();
			}

		});
	}

	private void initShouyeView() {

		GridView kemuGridView;
		TextView shoufaMore;
		TextView zuireMore;
		ImageView searchImageView;
		ImageView sanxianImageView;
		/***********************************************************************/
		if (book == null) {
			TextView shouye_partone_title = (TextView) shouyeView
					.findViewById(R.id.shouye_partone_title);
			shouye_partone_title.setText(book.getCourse());
			shoufaMore = (TextView) shouyeView
					.findViewById(R.id.shouye_shoufa_more_bt);
			shoufaMore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {

					Intent intent = new Intent(FrameActivity.this,
							KemuliebiaoActivity.class);
					intent.putExtra("kemu", book.getCourse());
					startActivity(intent);
				}
			});
			/**************************************************************************/
		} else {
			TextView shouye_partone_title = (TextView) shouyeView
					.findViewById(R.id.shouye_partone_title);
			shouye_partone_title.setText("最新首发");
			shoufaMore = (TextView) shouyeView
					.findViewById(R.id.shouye_shoufa_more_bt);
			shoufaMore.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					startActivity(new Intent(FrameActivity.this,
							ZuixinshoufaActivity.class));
				}
			});
		}
		zuireMore = (TextView) shouyeView
				.findViewById(R.id.shouye_zuire_more_bt);
		zuireMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(FrameActivity.this,
						ZuireActivity.class));

			}
		});
		shoufaGridView = (GridView) shouyeView
				.findViewById(R.id.shouye_shoufa_gv);
		zuireGridView = (GridView) shouyeView
				.findViewById(R.id.shouye_zuire_gv);
		kemuGridView = (GridView) shouyeView.findViewById(R.id.shouye_kumu_gv);

		searchImageView = (ImageView) shouyeView
				.findViewById(R.id.shouye_search);
		searchImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(FrameActivity.this,
						SearchActivity.class);
				startActivity(intent);
			}
		});
		sanxianImageView = (ImageView) shouyeView
				.findViewById(R.id.shouye_sanxian);
		sanxianImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				startActivityForResult(new Intent(FrameActivity.this,
						SetProvinceActivity.class), RESULT_SANXIAN);
			}
		});
		/********** 获取数据 **************/
		getShoufaData();
		getZuireData();
		/****************************/
		List<Map<String, Object>> list = getKemuData();
		String[] from = { "name", "image" };
		int[] to = { R.id.gv_txt, R.id.gv_image };
		SimpleAdapter kemuAdapter = new SimpleAdapter(FrameActivity.this, list,
				R.layout.item_gridview_small, from, to);

		kemuGridView.setAdapter(kemuAdapter);
		// Util.setListViewHeightBasedOnChildren(kemuGridView);
		kemuGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				String kemuString = ((TextView) arg1.findViewById(R.id.gv_txt))
						.getText().toString();
				Intent intent = new Intent(FrameActivity.this,
						KemuliebiaoActivity.class);
				intent.putExtra("kemu", kemuString);
				startActivity(intent);
			}
		});

	}

	private void getZuireData() {
		final List<Story> storyList = new ArrayList<Story>();
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(0);
		http.send(HttpMethod.GET, urlBase
				+ "list_hot_file_api.do?limitNumber=2", null,
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
							}
							reader.endArray();
							reader.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						zuireDatas = storyList;
						Message msg = new Message();
						msg.what = 4;
						handler.sendMessage(msg);
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(FrameActivity.this, "服务器错误",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						showDialog();
					}

				});

	}

	private List<Map<String, Object>> getKemuData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "语文");
		map.put("image", R.drawable.yuwen1);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "数学");
		map.put("image", R.drawable.shuxue1);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "英语");
		map.put("image", R.drawable.yingyu1);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "物理");
		map.put("image", R.drawable.wuli1);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "化学");
		map.put("image", R.drawable.huaxue1);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "生物");
		map.put("image", R.drawable.shengwu1);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "历史");
		map.put("image", R.drawable.lishi1);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "政治");
		map.put("image", R.drawable.zhengzhi1);
		list.add(map);
		map = new HashMap<String, Object>();
		map.put("name", "地理");
		map.put("image", R.drawable.dili1);
		list.add(map);

		return list;
	}

	/****** 获得最新首发列表 *******/
	private void getShoufaData() {
		final List<Story> storyList = new ArrayList<Story>();
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.configCurrentHttpCacheExpiry(0);
		http.send(HttpMethod.GET, urlBase
				+ "list_top_file_api.do?limitNumber=2", null,
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
										story.setRating((int) (reader
												.nextDouble()));
									} else if (tagName.equals("fileSize")) {
										story.setFileSize(reader.nextInt());
									} else if (tagName.equals("uploadTime")) {
										reader.nextString();
									}
								}
								reader.endObject();
								storyList.add(story);
							}
							reader.endArray();
							reader.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						shoufaDatas = storyList;
						Message msg = new Message();
						msg.what = 5;
						handler.sendMessage(msg);

					}

					@Override
					public void onFailure(HttpException error, String msg) {
						Toast.makeText(FrameActivity.this, "服务器错误",
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

	private class MyBtnOnclick implements View.OnClickListener {

		@Override
		public void onClick(View arg0) {
			int mBtnid = arg0.getId();
			switch (mBtnid) {
			case R.id.MyBottemShouyeBtn:
				mViewPager.setCurrentItem(0);
				initBottemBtn();
				mMyBottemShouyeImg.setImageResource(R.drawable.ic_shouye_yes);
				mMyBottemShouyeTxt.setTextColor(Color.parseColor("#f68720"));
				break;
			case R.id.MyBottemShoucangBtn:
				mViewPager.setCurrentItem(1);
				initBottemBtn();
				mMyBottemShoucangImg
						.setImageResource(R.drawable.ic_shoucang_yes);
				mMyBottemShoucangTxt.setTextColor(Color.parseColor("#f68720"));
				break;
			case R.id.MyBottemHistoryBtn:
				mViewPager.setCurrentItem(2);
				initBottemBtn();
				mMyBottemHistoryImg.setImageResource(R.drawable.ic_history_yes);
				mMyBottemHistoryTxt.setTextColor(Color.parseColor("#f68720"));
				break;
			case R.id.MyBottemMyBtn:
				mViewPager.setCurrentItem(3);
				initBottemBtn();
				mMyBottemMyImg.setImageResource(R.drawable.ic_me_yes);
				mMyBottemMyTxt.setTextColor(Color.parseColor("#f68720"));
				break;
			}

		}

	}

	private void initBottemBtn() {
		mMyBottemShouyeImg.setImageResource(R.drawable.ic_shouye_no);
		mMyBottemShoucangImg.setImageResource(R.drawable.ic_shoucang_no);
		mMyBottemHistoryImg.setImageResource(R.drawable.ic_history_no);
		mMyBottemMyImg.setImageResource(R.drawable.ic_me_no);

		mMyBottemShouyeTxt.setTextColor(getResources().getColor(
				R.color.frame_selsected_no));
		mMyBottemShoucangTxt.setTextColor(getResources().getColor(
				R.color.frame_selsected_no));
		mMyBottemHistoryTxt.setTextColor(getResources().getColor(
				R.color.frame_selsected_no));
		mMyBottemMyTxt.setTextColor(getResources().getColor(
				R.color.frame_selsected_no));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (keyCode == KeyEvent.KEYCODE_BACK) {
				Builder builder = new Builder(FrameActivity.this);
				builder.setTitle("退出");
				builder.setMessage("离开听故事？");
				builder.setIcon(R.drawable.ic_launcher);

				DialogInterface.OnClickListener dialog = new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						if (arg1 == DialogInterface.BUTTON_POSITIVE) {
							arg0.cancel();
						} else if (arg1 == DialogInterface.BUTTON_NEGATIVE) {
							android.os.Process.killProcess(android.os.Process
									.myPid());
							System.exit(0);
							finish();
						}
					}
				};
				builder.setPositiveButton("留下", dialog);
				builder.setNegativeButton("离开", dialog);
				AlertDialog alertDialog = builder.create();
				alertDialog.show();
			}
		}
		return false;
	}

	private void getKemuId() {
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase + "list_subject_api.do", null,
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
								String name = "";
								Integer id = 0;
								while (reader.hasNext()) {
									String tagName = reader.nextName();

									if (tagName.equals("subjectID")) {
										id = reader.nextInt();
									} else if (tagName.equals("subjectName")) {
										name = reader.nextString();
									} else if (tagName.equals("idDeleted")) {
										reader.nextInt();
									}

								}
								reader.endObject();
								kemuIdMap.put(name, id);
							}

						} catch (Exception e) {

						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}

				});
	}

	private void getVersionId() {
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase + "list_version_api.do", null,
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
								String name = "";
								Integer id = 0;
								while (reader.hasNext()) {
									String tagName = reader.nextName();

									if (tagName.equals("versionID")) {
										id = reader.nextInt();
									} else if (tagName.equals("versionName")) {
										name = reader.nextString();
									} else if (tagName.equals("idDeleted")) {
										reader.nextInt();
									}

								}
								reader.endObject();
								versionIdMap.put(name, id);
							}

						} catch (Exception e) {

						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}

				});
	}

	private void getGradeId() {
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase + "list_grade_api.do", null,
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
								String name = "";
								Integer id = 0;
								while (reader.hasNext()) {
									String tagName = reader.nextName();

									if (tagName.equals("gradeID")) {
										id = reader.nextInt();
									} else if (tagName.equals("gradeName")) {
										name = reader.nextString();
									} else if (tagName.equals("idDeleted")) {
										reader.nextInt();
									}

								}
								reader.endObject();
								gradeIdMap.put(name, id);
							}

						} catch (Exception e) {

						}
					}

					@Override
					public void onFailure(HttpException error, String msg) {

					}

				});
	}

}
