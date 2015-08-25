package com.yang.MYModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.media.MediaPlayer;

public class DownLoad implements Runnable {

	InputStream is;
	FileOutputStream fos;
	MediaPlayer mp;
	URL url;
	public DownLoad(URL url,MediaPlayer mp) {
		this.url = url;
		this.mp = mp;
	}
	boolean isFirst = true;
	int currentPosition = 0;
	int updateCount = 0;

	@Override
	public void run() {
		byte[] buffer = new byte[96000];
		int len = 0;
		
		try {
			this.is = url.openStream();
			File file = new File("/sdcard/story/temp.dat");
			file.createNewFile();
			fos = new FileOutputStream(file);
			
			while ((len = is.read(buffer)) != -1) {
				Thread.sleep(1000);
				fos.write(buffer, 0, len);
				fos.flush();
				
				if (isFirst) {
					mp = new MediaPlayer();
					mp.setDataSource("/sdcard/story/temp.dat");
					try {
						mp.prepare();
						mp.start();
					} catch (Exception e) {
						continue;
					}
					mp.seekTo(currentPosition);
					isFirst = false;
				}
				if ((mp.getDuration() - mp.getCurrentPosition() < 200)) {//播放完毕
					currentPosition = mp.getCurrentPosition();
					mp.reset();
					isFirst = true;
				} else {
                    updateCount++;
                    if(updateCount>=3){//三次缓冲后进行播放
                    	updateCount=0;
                    	currentPosition = mp.getCurrentPosition();
    					try {
    						mp.prepare();
    						mp.seekTo(currentPosition);
    						mp.start();
    					} catch (Exception e) {
    						continue;
    					}
                    }
				}

			}
			is.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
