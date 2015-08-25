package com.yang.MyAdapter;

import java.util.List;
import com.lidroid.xutils.BitmapUtils;
import com.yang.MYModel.Story;
import com.yang.story.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class ShoufaGvAdapter extends BaseAdapter {

	private Context context;                        
    private List<Story> datas;    
    private LayoutInflater listContainer;           //视图容器  
    public final class ListItemView{                //自定义控件集合    
            public ImageView image;    
            public TextView txt;       
     }    
    BitmapUtils bitmapUtils;
     String imageUrl; 
    public ShoufaGvAdapter(Context context, List<Story> datas) {  
        this.context = context;           
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文  
        this.datas = datas;
        bitmapUtils = new BitmapUtils(context);
        imageUrl = context.getResources().getString(R.string.db_image_url_base);
    }  
         
    /**
     * ListView Item设置
     */ 
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub   
        //自定义视图  
        ListItemView  listItemView = null;  
        if (convertView == null) {  
            listItemView = new ListItemView();   
            //获取list_item布局文件的视图  
            convertView = listContainer.inflate(R.layout.item_gridview_large, null);  
            //获取控件对象  
            listItemView.image = (ImageView)convertView.findViewById(R.id.gv_image);  
            listItemView.txt = (TextView)convertView.findViewById(R.id.gv_txt);  
            //设置控件集到convertView  
            convertView.setTag(listItemView);  
        }else {  
            listItemView = (ListItemView)convertView.getTag();  
        }  
        
        //设置文字和图片
      //  bitmapUtils.display(listItemView.image, imageUrl+datas.get(position).getPicSmallUrl());
        bitmapUtils.display(listItemView.image, imageUrl+datas.get(position).getPicBigUrl());
       
        listItemView.txt.setText(datas.get(position).getTitle());  
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
