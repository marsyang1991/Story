package com.yang.story;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yang.MYModel.Chapter;
import com.yang.MYModel.Outline;
import com.yang.MYModel.Textbook;

import kankan.wheel.widget.WheelView;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.JsonReader;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingBook2Activity extends Activity {
	WheelView bixuanxiuWheelView, shangxiaceWheelView, zhangWheelView,
			jieWheelView;
	String provinceId, cityId, subjectId, versionId,gradeId;
	String urlBase;
	List<Textbook> bookList = new ArrayList<Textbook>();
	List<Map<Integer, List<Chapter>>> chapterLists = new ArrayList<Map<Integer,List<Chapter>>>();
	Button button;
	Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				ArrayList<Map<String, String>> bixuanxiuList = new ArrayList<Map<String,String>>();
				for (Textbook book : bookList) {
					getChapter(book.getBookID());
					int isrequired = book.getIsBixiu();
					boolean flag = false;
					for (Map<String, String> map : bixuanxiuList) {
						if(map.get("id").equals(isrequired+""))
						{
							flag=true;
						}
					}
					if(!flag)
					{
						Map<String, String> map2 = new HashMap<String, String>();
						map2.put("id", isrequired+"");
						if(isrequired==1)
						{
							
								map2.put("name","必修");
								
						}else{
								map2.put("name", "选修");
							
						}
						bixuanxiuList.add(map2);
					}
				}
				
//				bixuanxiuWheelView.setViewAdapter(new ListMapWheelAdapter(
//						SettingBook2Activity.this, subjectList));
				break;
			case 2:
				
				break;
			case 3:
				break;
			default:
				break;
			}
		}
		
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		urlBase = getResources().getString(R.string.db_url_base);
		setContentView(R.layout.activity_settextbook2);
		bixuanxiuWheelView = (WheelView) findViewById(R.id.wheel_suanbixiu);
		shangxiaceWheelView = (WheelView) findViewById(R.id.wheel_shangxiace);
		zhangWheelView = (WheelView) findViewById(R.id.wheel_zhang);
		jieWheelView = (WheelView) findViewById(R.id.wheel_jie);
		Intent intent = getIntent();
		Bundle data = intent.getExtras();
		provinceId = data.getString("provinceId");
		cityId = data.getString("cityId");
		subjectId = data.getString("subjectId");
		versionId = data.getString("versionId");
		gradeId = data.getString("gradeId");
		getBook();
		button = (Button)findViewById(R.id.settextbook2_okbt);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				SharedPreferences preferences  = getSharedPreferences("userInfo", MODE_PRIVATE);
				Editor editor = preferences.edit();
				editor.putInt("versionId", Integer.parseInt(versionId));
				editor.putInt("courseId", Integer.parseInt(subjectId));
				editor.putInt("gradeId", Integer.parseInt(gradeId));
				editor.putInt("courseId", Integer.parseInt(subjectId));
				
			}
		});
	}

	private void getBook() {
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase + "list_book_api.do?" + "areaID="
				+ cityId+"&subjectID="+subjectId+"&versionID"+versionId+"&gradeID="+gradeId, null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String string = responseInfo.result;
						JsonReader reader = new JsonReader(new StringReader(
								string));
						try {
							bookList = new ArrayList<Textbook>();
							reader.beginArray();
							while(reader.hasNext())
							{
								reader.beginObject();
								Textbook book = new Textbook();
								while(reader.hasNext())
								{
									String tagName = reader.nextName();
									
									if (tagName.equals("areaID")) 
									{
										book.setAreaID(reader.nextInt());
									} else if (tagName.equals("gradeID"))
									{
										book.setGradeId(reader.nextInt());
									}else if (tagName.equals("subjectID"))
									{
										book.setSubjectId(reader.nextInt());
									}else if (tagName.equals("versionID"))
									{
										book.setVersionId(reader.nextInt());
									}else if (tagName.equals("bookID"))
									{
										book.setBookID(reader.nextInt());
									}else if (tagName.equals("isRequired"))
									{
										book.setIsBixiu(reader.nextInt());
									}else if (tagName.equals("bookName"))
									{
										book.setBookName(reader.nextString());
									}else if (tagName.equals("subVersion"))
									{
										book.setSubversion(reader.nextInt());
									}else if (tagName.equals("subjectName"))
									{
										book.setCourse(reader.nextString());
									}else if (tagName.equals("gradeName"))
									{
										book.setGrade(reader.nextString());
									}else if (tagName.equals("versionName"))
									{
										book.setVersion(reader.nextString());
									}else if (tagName.equals("isDeleted"))
									{
										reader.nextInt();
									}
									
								}
								bookList.add(book);
								reader.endObject();
							}
							reader.endArray();
							reader.close();
							Message msg = new Message();
							msg.what = 1;
							
							handler.sendMessage(msg);
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						
					}
		});
		
	}
	
	private void getChapter(final int bookID) {
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase + "list_chapter_api.do?" + "bookID="
				+ bookID, null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String string = responseInfo.result;
						JsonReader reader = new JsonReader(new StringReader(
								string));
						try {
							reader.beginArray();
							ArrayList<Chapter> chapterList = new ArrayList<Chapter>();
							while(reader.hasNext())
							{
								reader.beginObject();
								Chapter c = new Chapter();
								while(reader.hasNext())
								{
									String tagName = reader.nextName();
									
									if (tagName.equals("chapterID")) 
									{
										c.setChapterID (reader.nextInt());
									} else if (tagName.equals("chapterNumber"))
									{
										c.setChapterNumber(reader.nextInt());
									}else if (tagName.equals("chapterText"))
									{
										c.setChapterText(reader.nextString());
									}else if (tagName.equals("bookID"))
									{
										c.setBookID(reader.nextInt());
									}else if (tagName.equals("parentID"))
									{
										c.setParentID(reader.nextInt());
									}else if (tagName.equals("bookName"))
									{
										c.setBookName(reader.nextString());
									}
									
								}
								chapterList.add(c);
								reader.endObject();
							}
							reader.endArray();
							reader.close();
							Message msg = new Message();
							msg.what = 2;
							Bundle data = new Bundle();
							data.putInt("bookID", bookID);
							data.putParcelableArrayList("chapterList", chapterList);
							msg.setData(data);
							handler.sendMessage(msg);
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						
					}
		});
		
	}
	private void getOutline(final int chapterID) {
		String urlBase = getResources().getString(R.string.db_url_base);
		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, urlBase + "list_outline_api.do?" + "chapterID="
				+ chapterID, null,
				new RequestCallBack<String>() {

					@Override
					public void onSuccess(ResponseInfo<String> responseInfo) {
						String string = responseInfo.result;
						JsonReader reader = new JsonReader(new StringReader(
								string));
						try {
							reader.beginArray();
							ArrayList<Outline> outLineList = new ArrayList<Outline>();
							while(reader.hasNext())
							{
								reader.beginObject();
								Outline outline = new Outline();
								while(reader.hasNext())
								{
									String tagName = reader.nextName();
									
									if (tagName.equals("chapterID")) 
									{
										outline.setChapterID (reader.nextInt());
									} else if (tagName.equals("outLineID"))
									{
										outline.setOutLineID(reader.nextInt());
									}else if (tagName.equals("outLineText"))
									{
										outline.setOutLineText(reader.nextString());
									}else if (tagName.equals("outlineNumber"))
									{
										outline.setOutLineNumber(reader.nextInt());
									}
									
								}
								outLineList.add(outline);
								reader.endObject();
							}
							reader.endArray();
							reader.close();
							Message msg = new Message();
							msg.what = 3;
							Bundle data = new Bundle();
							data.putInt("chapterID", chapterID);
							data.putParcelableArrayList("outLineList", outLineList);
							msg.setData(data);
							handler.sendMessage(msg);
						}catch(Exception e)
						{
							e.printStackTrace();
						}
						
					}

					@Override
					public void onFailure(HttpException error, String msg) {
						// TODO Auto-generated method stub
						
					}
		});
		
	}
}
