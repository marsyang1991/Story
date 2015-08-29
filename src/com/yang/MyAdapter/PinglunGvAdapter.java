package com.yang.MyAdapter;

import java.util.List;
import com.lidroid.xutils.BitmapUtils;
import com.yang.MYModel.Pinglun;
import com.yang.story.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class PinglunGvAdapter extends BaseAdapter {

	private Context context;
	private List<Pinglun> datas;
	private LayoutInflater listContainer; // 视图容器

	public final class ListItemView { // 自定义控件集合
		public ImageView image;
		public TextView username;
		public TextView content;
		public TextView time;
	}

	public PinglunGvAdapter(Context context, List<Pinglun> datas) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.datas = datas;
	}

	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(
					R.layout.item_listview_pinglunlist, null);
			// 获取控件对象
			listItemView.image = (ImageView) convertView
					.findViewById(R.id.pinglun_image);
			listItemView.username = (TextView) convertView
					.findViewById(R.id.pinglun_tv_username);
			listItemView.content = (TextView) convertView
					.findViewById(R.id.pinglun_tv_content);
			listItemView.time = (TextView) convertView
					.findViewById(R.id.pinglun_time);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}

		// 设置文字和图片
		// listItemView.image.setImageResource(R.drawable.ic_default_touxiang);
		if(datas.get(position).getPortraitURL()!=null)
		{
		BitmapUtils bitmapUtils = new BitmapUtils(context);
		bitmapUtils.display(listItemView.image, context.getResources()
				.getString(R.string.db_url_base)
				+ datas.get(position).getPortraitURL());
		}else {
			listItemView.image.setImageResource(R.drawable.ic_default_touxiang);
		}
		listItemView.username.setText(datas.get(position).getNickName());
		listItemView.content.setText(datas.get(position).getCommentText());
		listItemView.time.setText(datas.get(position).getCommentTime());
		return convertView;
	}

	@Override
	public int getCount() {
		return datas.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

}
