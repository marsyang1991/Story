package com.yang.story;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectPicPopupWindow extends Activity implements OnClickListener{
	TextView btn_take_photo,btn_pick_photo, btn_cancel;
	LinearLayout layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_touxiangxuanze);
		btn_cancel = (TextView)findViewById(R.id.xuanzetouxiang_cancel);
		btn_take_photo = (TextView)findViewById(R.id.xuanzetouxiang_camare);
		btn_pick_photo = (TextView)findViewById(R.id.xuanzetouxiang_galary);
		layout=(LinearLayout)findViewById(R.id.pop_layout); 
	 
		getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
        //添加按钮监听  
        btn_cancel.setOnClickListener(this);  
        btn_pick_photo.setOnClickListener(this);  
        btn_take_photo.setOnClickListener(this);  
	}

	  
	    @Override
		public void onClick(View v) {  
	        switch (v.getId()) {  
	        case R.id.xuanzetouxiang_camare:  
	        	Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
	            startActivityForResult(getImageByCamera, 0);  
	            break;  
	        case R.id.xuanzetouxiang_galary:
	        	Intent intent = new Intent(Intent.ACTION_PICK);  
	            intent.setType("image/*");//相片类型  
	            startActivityForResult(intent, 1);  
	            break;  
	        case R.id.xuanzetouxiang_cancel:
	        	setResult(-1); 
	        	finish();
	            break;  
	        default:  
	            break;  
	        }  
	       
	    }

		@Override
		protected void onActivityResult(int requestCode, int resultCode,Intent data)
		{
			Uri uri = null;
			if (requestCode == 1) {
				if(resultCode==Activity.RESULT_OK){
	                uri = data.getData();  
	                Intent intent = new Intent(SelectPicPopupWindow.this, MyInfoActivity.class);
	                intent.putExtra("imageUri", uri.toString());
	                setResult(1, intent);
	                finish();
				}
			} else if (requestCode == 0 ) { 
				if(resultCode==Activity.RESULT_OK)
				{
					Bundle bundle = data.getExtras();
					Uri camareUri = (Uri) bundle.get(MediaStore.EXTRA_OUTPUT); 
					Bitmap camerabmp = (Bitmap) data.getExtras().get("data");  
					Intent intent = new Intent(SelectPicPopupWindow.this, MyInfoActivity.class);
	                intent.putExtra("image", camerabmp);
	                setResult(0, intent);
	                finish();
				}
			}
			else {
				setResult(-1);
				finish();
			}
			
			super.onActivityResult(requestCode, resultCode, data);
		}
	    
	
}
