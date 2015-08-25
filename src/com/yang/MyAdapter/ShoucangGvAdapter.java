package com.yang.MyAdapter;

import java.util.List;
import com.yang.MYModel.Story;
import com.yang.story.FrameActivity;
import com.yang.story.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

public class ShoucangGvAdapter extends BaseAdapter {

	private Context context;                        
    private List<Story> datas;    
    private LayoutInflater listContainer;           //视图容器  
    public final class ListItemView{                //自定义控件集合    
            public ImageView image;    
            public TextView kemu;    
            public RatingBar ratingBar;  
            public TextView shichang;    
            public TextView renqi;         
     }    

      
    public ShoucangGvAdapter(Context context, List<Story> datas) {  
        this.context = context;           
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文  
        this.datas = datas;   
    }  
         
    /**
     * ListView Item设置
     */ 
    @Override
	public View getView(final int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub   
        final int selectID = position;  
        //自定义视图  
        ListItemView  listItemView = null;  
        if (convertView == null) {  
            listItemView = new ListItemView();   
            //获取list_item布局文件的视图  
            convertView = listContainer.inflate(R.layout.item_gridview_shoucang, null);  
            //获取控件对象  
            listItemView.image = (ImageView)convertView.findViewById(R.id.image);  
            listItemView.kemu = (TextView)convertView.findViewById(R.id.kemu);  
            listItemView.shichang = (TextView)convertView.findViewById(R.id.shichang);  
            listItemView.renqi = (TextView)convertView.findViewById(R.id.renqi);  
            listItemView.ratingBar= (RatingBar)convertView.findViewById(R.id.ratingBar);  
            ImageView iv_shoucang = (ImageView)convertView.findViewById(R.id.shoucang);
            iv_shoucang.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					//Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(FrameActivity.ACTION_DELETE_SHOUCANG); 
					intent.putExtra("position", position);
	                context.sendBroadcast(intent); 
					/**************/
				}
			});
            //设置控件集到convertView  
            convertView.setTag(listItemView);  
        }else {  
            listItemView = (ListItemView)convertView.getTag();  
        }  

        //设置文字和图片  
        listItemView.image.setBackgroundResource(R.drawable.test);  
        listItemView.kemu.setText(datas.get(position)  
                .getTitle());  
        listItemView.renqi.setText(datas.get(position).getRenqi()+"");  
        listItemView.shichang.setText(datas.get(position).getHour()+"小时"+datas.get(position).getMinute()+"分钟"+datas.get(position).getSecond()+"秒");  
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
