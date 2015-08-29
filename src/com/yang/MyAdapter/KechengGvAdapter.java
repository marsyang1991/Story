package com.yang.MyAdapter;

import java.util.List;
import java.util.Map;

import com.yang.story.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class KechengGvAdapter extends BaseAdapter {

	private Context context;
	private List<Map<String, Object>> datas;
	private LayoutInflater listContainer; // 视图容器

	public final class ListItemView { // 自定义控件集合
		public ImageView image;
		public TextView txt;
	}

	public KechengGvAdapter(Context context, List<Map<String, Object>> datas) {
		this.context = context;
		listContainer = LayoutInflater.from(context); // 创建视图容器并设置上下文
		this.datas = datas;
	}

	/**
	 * ListView Item设置
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int selectID = position;
		// 自定义视图
		ListItemView listItemView = null;
		if (convertView == null) {
			listItemView = new ListItemView();
			// 获取list_item布局文件的视图
			convertView = listContainer.inflate(R.layout.item_gridview_large,
					null);
			// 获取控件对象
			listItemView.image = (ImageView) convertView
					.findViewById(R.id.gv_image);
			listItemView.txt = (TextView) convertView.findViewById(R.id.gv_txt);
			// 设置控件集到convertView
			convertView.setTag(listItemView);
		} else {
			listItemView = (ListItemView) convertView.getTag();
		}
		// 设置文字和图片
		listItemView.image.setBackgroundResource((Integer) datas.get(position)
				.get("image"));
		listItemView.txt.setText((String) datas.get(position).get("txt"));
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
