package com.yang.story;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import cn.smssdk.gui.CommonDialog;

import com.yang.MYModel.Story;
import com.yang.MYModel.Textbook;
import com.yang.MYModel.User;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.util.Xml;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

public class Util {
	public static final String URL_STRING = "http://123.56.136.209:8080/ssmapi/";
	public static String formatRenqi(int n)
	{
		DecimalFormat df = new DecimalFormat("###.0");  
		if(n<10000)
			return n+"";
		else {
			return df.format(n/10000.0);
		}
	}
	public static String formatFileSize(int size)
	{
		DecimalFormat df = new DecimalFormat("###.0"); 
		if(size<1024)
			return size+"B";
		if(size<1024*1024)
		{
			return df.format(size/1024.0)+"KB";
		}
		else {
			return df.format(size/1024.0/1024.0)+"MB";
		}
	}
	
	public static List<User> getUsers() {
		FileInputStream is;
		List<User> users = null;
		User user = null;
		try {
			is = new FileInputStream(new File("/sdcard/story/user.xml"));
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					users = new ArrayList<User>();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("user")) {
						user = new User();
					} else if (parser.getName().equals("username")) {
						eventType = parser.next();
						user.setUsername(parser.getText());
					} else if (parser.getName().equals("password")) {
						eventType = parser.next();
						user.setPassword(parser.getText());
					}
					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("user")) {
						users.add(user);
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {

			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return users;
	}

	public static String encodingTime(int duration) {
		String secondString = "";
		String minuteString = "";
		int second = duration % 60;
		int minute = (duration / 60) % 60;
		secondString = second < 10 ? "0" + second : "" + second;
		minuteString = minute < 10 ? "0" + minute : "" + minute;
		return minuteString + ":" + secondString;
	}

	public static String encoding(String string) {
		String[] temp = string.split("/");
		String name = temp[temp.length];
		String[] temp2 = name.split(".");
		String resultString = temp[0];
		for (int i = 1; i < temp.length - 1; i++) {
			resultString += "/" + temp[i];
		}
		try {
			resultString += URLEncoder.encode(temp2[0], "utf-8") + "."
					+ temp2[1];
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultString;
	}

	public static List<Story> getHistoryList() {
		FileInputStream is;
		List<Story> storys = null;
		Story story = null;
		Textbook book = null;
		try {
			is = new FileInputStream(new File("/sdcard/story/history.xml"));
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(is, "UTF-8");
			int eventType = parser.getEventType();
			while (eventType != XmlPullParser.END_DOCUMENT) {
				switch (eventType) {
				case XmlPullParser.START_DOCUMENT:
					storys = new ArrayList<Story>();
					break;
				case XmlPullParser.START_TAG:
					if (parser.getName().equals("story")) {
						story = new Story();
					} else if (parser.getName().equals("id")) {
						eventType = parser.next();
						story.setId(Integer.parseInt(parser.getText()));
					} else if (parser.getName().equals("fileDuration")) {
						eventType = parser.next();
						story.setFileDuration(Integer.parseInt(parser.getText()));
					} else if (parser.getName().equals("fileSize")) {
						eventType = parser.next();
						story.setFileSize(Integer.parseInt(parser.getText()));
					} else if (parser.getName().equals("title")) {
						eventType = parser.next();
						story.setTitle(parser.getText());
					} else if (parser.getName().equals("hour")) {
						eventType = parser.next();
						story.setHour(Integer.parseInt(parser.getText()));
					} else if (parser.getName().equals("minute")) {
						eventType = parser.next();
						story.setMinute(Integer.parseInt(parser.getText()));
					} else if (parser.getName().equals("second")) {
						eventType = parser.next();
						story.setSecond(Integer.parseInt(parser.getText()));
					} else if (parser.getName().equals("renqi")) {
						eventType = parser.next();
						story.setRenqi(Integer.parseInt(parser.getText()));
					} else if (parser.getName().equals("rating")) {
						eventType = parser.next();
						story.setRating((int) Double.parseDouble(parser
								.getText()));
					} else if (parser.getName().equals("picSmallUrl")) {
						eventType = parser.next();
						story.setPicSmallUrl(parser.getText());

					} else if (parser.getName().equals("picNormalUrl")) {
						eventType = parser.next();
						story.setPicNormalUrl(parser.getText());
					} else if (parser.getName().equals("picBigUrl")) {
						eventType = parser.next();
						story.setPicBigUrl(parser.getText());
					} else if (parser.getName().equals("description")) {
						eventType = parser.next();
						story.setDescription(parser.getText());
					} else if (parser.getName().equals("fileUrl")) {
						eventType = parser.next();
						story.setFileUrl(parser.getText());
					}

					break;
				case XmlPullParser.END_TAG:
					if (parser.getName().equals("story")) {
						storys.add(story);
					}
					break;
				default:
					break;
				}
				eventType = parser.next();
			}
		} catch (FileNotFoundException e) {

			e.printStackTrace();
			return null;
		} catch (XmlPullParserException e) {

			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return storys;
	}

	public static void writeStoryIntoXml(Story story1) {
		XmlSerializer serializer = Xml.newSerializer();
		try {
			File file = new File("/sdcard/story/history.xml");
			if (!file.exists()) {
				file.createNewFile();
			}
			List<Story> stories = getHistoryList();
			if (stories == null)
				stories = new ArrayList<Story>();
			if (stories.contains(story1)) {
				stories.remove(story1);
			}
			stories.add(0, story1);
			FileWriter writer = new FileWriter("/sdcard/story/history.xml",
					false);
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			for (int i = 0; i < 20 && i < stories.size(); i++) {
				Story story = stories.get(i);
				serializer.startTag("", "story");

				serializer.startTag("", "id");
				serializer.text(story.getId() + "");
				serializer.endTag("", "id");

				serializer.startTag("", "title");
				serializer.text(story.getTitle());
				serializer.endTag("", "title");

				serializer.startTag("", "description");
				serializer.text(story.getDescription());
				serializer.endTag("", "description");

				serializer.startTag("", "hour");
				serializer.text(story.getHour() + "");
				serializer.endTag("", "hour");

				serializer.startTag("", "minute");
				serializer.text(story.getMinute() + "");
				serializer.endTag("", "minute");

				serializer.startTag("", "second");
				serializer.text(story.getSecond() + "");
				serializer.endTag("", "second");

				serializer.startTag("", "fileName");
				serializer.text(story.getFileName());
				serializer.endTag("", "fileName");

				serializer.startTag("", "fileDuration");
				serializer.text(story.getFileDuration() + "");
				serializer.endTag("", "fileDuration");

				serializer.startTag("", "fileSize");
				serializer.text(story.getFileSize() + "");
				serializer.endTag("", "fileSize");

				serializer.startTag("", "fileUrl");
				serializer.text(story.getFileUrl());
				serializer.endTag("", "fileUrl");

				serializer.startTag("", "picBigUrl");
				serializer.text(story.getPicBigUrl());
				serializer.endTag("", "picBigUrl");

				serializer.startTag("", "picNormalUrl");
				serializer.text(story.getPicNormalUrl());
				serializer.endTag("", "picNormalUrl");

				serializer.startTag("", "picSmallUrl");
				serializer.text(story.getPicSmallUrl());
				serializer.endTag("", "picSmallUrl");

				serializer.startTag("", "renqi");
				serializer.text(story.getRenqi() + "");
				serializer.endTag("", "renqi");

				serializer.startTag("", "rating");
				serializer.text(story.getRating() + "");
				serializer.endTag("", "rating");
				serializer.endTag("", "story");
			}
			serializer.endDocument();
			serializer.flush();
			writer.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void deleteHistory(Story story1) {
		XmlSerializer serializer = Xml.newSerializer();
		List<Story> storyList = getHistoryList();
		storyList.remove(story1);
		try {
			FileWriter writer = new FileWriter("/sdcard/story/history.xml",
					false);
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			for (int i = 0; i < 20 && i < storyList.size(); i++) {
				Story story = storyList.get(i);
				serializer.startTag("", "story");

				serializer.startTag("", "id");
				serializer.text(story.getId() + "");
				serializer.endTag("", "id");

				serializer.startTag("", "title");
				serializer.text(story.getTitle());
				serializer.endTag("", "title");

				serializer.startTag("", "description");
				serializer.text(story.getDescription());
				serializer.endTag("", "description");

				serializer.startTag("", "hour");
				serializer.text(story.getHour() + "");
				serializer.endTag("", "hour");

				serializer.startTag("", "minute");
				serializer.text(story.getMinute() + "");
				serializer.endTag("", "minute");

				serializer.startTag("", "second");
				serializer.text(story.getSecond() + "");
				serializer.endTag("", "second");

				serializer.startTag("", "fileName");
				serializer.text(story.getFileName());
				serializer.endTag("", "fileName");

				serializer.startTag("", "fileUrl");
				serializer.text(story.getFileUrl());
				serializer.endTag("", "fileUrl");

				serializer.startTag("", "picBigUrl");
				serializer.text(story.getPicBigUrl());
				serializer.endTag("", "picBigUrl");

				serializer.startTag("", "picNormalUrl");
				serializer.text(story.getPicNormalUrl());
				serializer.endTag("", "picNormalUrl");

				serializer.startTag("", "picSmallUrl");
				serializer.text(story.getPicSmallUrl());
				serializer.endTag("", "picSmallUrl");

				serializer.startTag("", "renqi");
				serializer.text(story.getRenqi() + "");
				serializer.endTag("", "renqi");

				serializer.startTag("", "rating");
				serializer.text(story.getRating() + "");
				serializer.endTag("", "rating");
				serializer.endTag("", "story");
			}
			serializer.endDocument();
			serializer.flush();
			writer.close();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int writeUserXml(User user1) {
		List<User> users = getUsers();
		if (users != null && users.size() != 0) {
			for (User user2 : users) {
				if (user1.isSameUser(user2))
					return -2;
			}
		}
		if (users == null)
			users = new ArrayList<User>();
		users.add(user1);

		XmlSerializer serializer = Xml.newSerializer();
		try {
			File file = new File("/sdcard/story/user.xml");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter writer = new FileWriter("/sdcard/story/user.xml", false);
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			for (User user : users) {
				serializer.startTag("", "user");
				serializer.startTag("", "username");
				serializer.text(user.getUsername());
				serializer.endTag("", "username");
				serializer.startTag("", "password");
				serializer.text(user.getPassword());
				serializer.endTag("", "password");
				serializer.endTag("", "user");
			}
			serializer.endDocument();
			serializer.flush();
			writer.close();
			return 1;

		} catch (Exception e) {
			return -1;
		}
	}

	public static Bitmap thumbnailWithImageWithoutScale(Activity current,
			Bitmap originalBitmap) {
		Display display = current.getWindowManager().getDefaultDisplay();
		int height = display.getHeight();
		int SCALE;// 缩略图大小
		switch (height) {
		case 1080:
			SCALE = 300;
			break;
		case 1920:
			SCALE = 600;
			break;
		default:
			SCALE = 150;
			break;
		}

		// 得到缩略图
		Bitmap bitmap = ThumbnailUtils.extractThumbnail(originalBitmap, SCALE,
				SCALE, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

		return bitmap;

	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	public static Bitmap getDiskBitmap(String pathString) {
		Bitmap bitmap = null;
		try {
			File file = new File(pathString);
			if (file.exists()) {
				bitmap = BitmapFactory.decodeFile(pathString);
			}
		} catch (Exception e) {
		}

		return bitmap;
	}

	public static String parseSecond(int time) {
		String minuteString;
		String secondString;
		if (time < 60 * 60) {
			int minute = time / 60;
			int second = time % 60;
			if (minute < 10)
				minuteString = "0" + minute;
			else {
				minuteString = "" + minute;
			}
			if (second < 10)
				secondString = "0" + second;
			else {
				secondString = "" + second;
			}
			return minuteString + ":" + secondString;
		} else {
			return null;
		}
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		if (listView == null)
			return;

		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// params.height = 300;
		listView.setLayoutParams(params);
	}

	public static void showDialog(Dialog pd, Context context) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
		pd = CommonDialog.ProgressDialog(context);
		pd.show();
	}

	public static void saveUser(Context context, User user) {
		SharedPreferences preferences = context.getSharedPreferences(
				"userInfo", Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putBoolean("flag", true);
		editor.putString("username", user.getUsername());
		editor.putString("password", user.getPassword());
		editor.putString("nickName", user.getNickname());
		/*
		 * if(user.getGender().equals("1")) editor.putString("gender", "男");
		 * else { editor.putString("gender", "女"); }
		 */
		editor.putString("date", user.getDate());
		editor.putString("qq", user.getQq());
		editor.putString("email", user.getEmail());
		editor.putInt("areaID", user.getBook().getAreaID());
		editor.putInt("gradeID", user.getBook().getGradeId());
		editor.putInt("versionID", user.getBook().getVersionId());
		editor.putInt("courseID", user.getBook().getSubjectId());
		editor.putInt("subVersion", user.getBook().getSubversion());
		editor.putInt("required", user.getBook().getIsBixiu());
		editor.commit();
	}

}
