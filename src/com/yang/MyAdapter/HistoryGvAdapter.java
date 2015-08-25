package com.yang.MyAdapter;

import java.util.List;

import com.lidroid.xutils.BitmapUtils;
import com.yang.MYModel.Story;
import com.yang.story.R;
import com.yang.story.Util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;

public class HistoryGvAdapter extends BaseAdapter {

	private Context context; 
    private List<Story> datas;
    private int[] flags;//0 不可编辑，1 可编辑，2 选中 
    private LayoutInflater listContainer;           //视图容器  
    ImageView iv_delete; 
    public final class ListItemView{                //自定义控件集合    
            public ImageView image;    
            public TextView kemu;    
            public RatingBar ratingBar;  
            public TextView shichang;    
            public TextView renqi;;         
     }    

      
    public HistoryGvAdapter(Context context, List<Story> datas, int[] flags) {  
        this.context = context;           
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文  
        this.datas = datas;
        this.flags = flags;
    }  
         
    public int[] getFlags() {
		return flags;
	}

	public void setFlags(int[] flags) {
		this.flags = flags;
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
            convertView = listContainer.inflate(R.layout.item_gridview_history, null);  
            RelativeLayout rl = (RelativeLayout)convertView.findViewById(R.id.rl);
            //获取控件对象  
            listItemView.image = (ImageView)convertView.findViewById(R.id.image);  
            listItemView.kemu = (TextView)convertView.findViewById(R.id.kemu);  
            listItemView.shichang = (TextView)convertView.findViewById(R.id.shichang);  
            listItemView.renqi = (TextView)convertView.findViewById(R.id.renqi);  
            listItemView.ratingBar= (RatingBar)convertView.findViewById(R.id.ratingBar);  
            iv_delete = (ImageView)convertView.findViewById(R.id.dustbin);
            switch (flags[position]) {
			case 0:
				//rl.setFocusable(true);
				iv_delete.setImageResource(R.drawable.ic_right);
				iv_delete.setEnabled(false);
				break;
			case 1:
				//rl.setFocusable(false);
				iv_delete.setImageResource(R.drawable.ic_to_choose);
				iv_delete.setEnabled(true);
				break;
			case 2:
				//rl.setFocusable(false);
				iv_delete.setImageResource(R.drawable.ic_choosed);
				iv_delete.setEnabled(true);
				break;
			default:
				break;
			}
            iv_delete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					if(flags[position]==1)
					{
						iv_delete.setImageResource(R.drawable.ic_choosed);
						flags[position]=2;
					}else if (flags[position]==2) {
						iv_delete.setImageResource(R.drawable.ic_to_choose);
						flags[position]=1;
					}
					/**************/
				}
			});
            //设置控件集到convertView  
            convertView.setTag(listItemView);  
        }else {  
            listItemView = (ListItemView)convertView.getTag();  
        }  

        //设置文字和图片  
        //listItemView.image.setBackgroundResource(R.drawable.test);  
        BitmapUtils bitmapUtils = new BitmapUtils(context);
        bitmapUtils.display(listItemView.image, context.getResources().getString(R.string.db_image_url_base)+datas.get(position).getPicSmallUrl());
        listItemView.kemu.setText(datas.get(position)  
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
