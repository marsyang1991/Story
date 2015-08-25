package com.yang.MyAdapter;

import java.net.URLEncoder;
import java.util.List;
import com.lidroid.xutils.BitmapUtils;
import com.yang.MYModel.Story;
import com.yang.story.R;
import com.yang.story.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class SearchGvAdapter extends BaseAdapter {

	private Context context;                        
    private List<Story> datas;    
    private LayoutInflater listContainer;  //视图容器  
    public final class ListItemView{                //自定义控件集合    
            public ImageView image;    
            public TextView title;  
            public RatingBar ratingBar;  
            public TextView shichang;    
            public TextView renqi;;         
     }    

      String res_url_base;
    public SearchGvAdapter(Context context, List<Story> datas) {  
        this.context = context;           
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文  
        this.datas = datas; 
        res_url_base = context.getResources().getString(R.string.db_image_url_base);
    }  
         
    /**
     * ListView Item设置
     */ 
    @Override
	public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub   
        final int selectID = position;  
        //自定义视图  
        ListItemView  listItemView = null;  
        if (convertView == null) {  
            listItemView = new ListItemView();   
            //获取list_item布局文件的视图  
            convertView = listContainer.inflate(R.layout.item_gridview_search, null);  
            //获取控件对象  
            listItemView.image = (ImageView)convertView.findViewById(R.id.image);  
            listItemView.title = (TextView)convertView.findViewById(R.id.gv_title);  
            listItemView.shichang = (TextView)convertView.findViewById(R.id.shichang);  
            listItemView.renqi = (TextView)convertView.findViewById(R.id.renqi);  
            listItemView.ratingBar= (RatingBar)convertView.findViewById(R.id.ratingBar);  
            //设置控件集到convertView  
            convertView.setTag(listItemView);  
        }else {  
            listItemView = (ListItemView)convertView.getTag();  
        }  

        //设置文字和图片  
      //  Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/")
        try {
            BitmapUtils bitmapUtils = new BitmapUtils(context);
            //System.out.println(res_url_base+Util.encoding(datas.get(position).getPicNormalUrl()));
            if(datas.get(position).getPicSmallUrl()!=null||datas.get(position).getPicNormalUrl().equals(""))
            	bitmapUtils.display(listItemView.image, res_url_base+datas.get(position).getPicSmallUrl());
		} catch (Exception e) {
			 System.out.println(res_url_base+datas.get(position).getPicNormalUrl());
		}
   
        listItemView.title.setText(datas.get(position)  
                .getTitle());  
        listItemView.renqi.setText(datas.get(position).getRenqi()+"");  
        listItemView.shichang.setText(Util.parseSecond(datas.get(position).getFileDuration()));  
        listItemView.ratingBar.setRating(Float.parseFloat(datas.get(position).getRating()+""));
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
