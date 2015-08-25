package com.yang.MyAdapter;

import java.util.List;
import java.util.Map;

import com.yang.story.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class TextbookAdapter extends BaseAdapter {

	private Context context;                        
    private List<Map<String, Object>> datas;    
    private LayoutInflater listContainer;           //视图容器  
    public final class ListItemView{                //自定义控件集合      
            public TextView city;    
            public TextView version;  
            public TextView grade;    
            public TextView course;;         
     }    
    int i;
      
    public TextbookAdapter(Context context, List<Map<String, Object>> datas,int i) {  
        this.context = context;           
        listContainer = LayoutInflater.from(context);   //创建视图容器并设置上下文  
        this.datas = datas;   
        this.i = i;
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
            convertView = listContainer.inflate(R.layout.activity_textbooklist_item, null);  
            //获取控件对象  
            listItemView.city = (TextView)convertView.findViewById(R.id.city);  
            listItemView.version = (TextView)convertView.findViewById(R.id.version);  
            listItemView.grade = (TextView)convertView.findViewById(R.id.grade);  
            listItemView.course = (TextView)convertView.findViewById(R.id.course);  
            //设置控件集到convertView  
            convertView.setTag(listItemView);  
        }else {  
            listItemView = (ListItemView)convertView.getTag();  
        }  

        //设置文字和图片  
        listItemView.city.setText((String) datas.get(position)  
                .get("city"));  
        listItemView.version.setText((String) datas.get(position).get("version"));  
        listItemView.course.setText((String) datas.get(position).get("course"));  
        listItemView.grade.setText((String) datas.get(position).get("grade"));  
      //  boolean flag=(Boolean)(datas.get(position).get("flag"));
        if(position==i&&i!=-1)
        {
        	
        	LinearLayout ll =(LinearLayout)convertView.findViewById(R.id.textbookListItemLayout);
			ll.setBackgroundColor(Color.parseColor("#F8E0CD"));
			TextView cityTextView = (TextView)convertView.findViewById(R.id.city);
			TextView courseTextView = (TextView)convertView.findViewById(R.id.course);
			TextView gradeTextView = (TextView)convertView.findViewById(R.id.grade);
			TextView versionTextView = (TextView)convertView.findViewById(R.id.version);
			cityTextView.setTextColor(Color.parseColor("#F68720"));
			gradeTextView.setTextColor(Color.parseColor("#F68720"));
			courseTextView.setTextColor(Color.parseColor("#F68720"));
			versionTextView.setTextColor(Color.parseColor("#F68720"));

        }
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
