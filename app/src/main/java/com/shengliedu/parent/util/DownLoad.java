package com.shengliedu.parent.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;

public class DownLoad {
	public static int SUCESS_REQ=1;
	//创建线程池
	private Executor threadPool=Executors.newFixedThreadPool(3);
	private Handler handler;
	private String parentFile;
	
	public DownLoad(Handler handler) {
		super();
		this.handler = handler;
	}

	public DownLoad(Handler handler,String parentFile) {
		super();
		this.handler = handler;
		this.parentFile = parentFile;
	}

	static class DownLoadRunnable implements Runnable{
		private String url;
		private String fileName;
		private long start;
		private long end;
		private Handler handler;
		public DownLoadRunnable(String url, String fileName, long start,
				long end,Handler handler) {
			super();
			this.url = url;
			this.fileName = fileName;
			this.start = start;
			this.end = end;
			this.handler = handler;
		}



		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				URL httpUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) httpUrl
						.openConnection();
				conn.setReadTimeout(10000);
				conn.setRequestProperty("Range", "bytes="+start+"-"+end);
				conn.setRequestMethod("GET");
				RandomAccessFile access=new RandomAccessFile(new File(fileName), "rwd");
				access.seek(start);
				InputStream in=conn.getInputStream();
				byte[] b=new byte[1024*4];
				int len=0;
				while ((len=in.read(b))!=-1) {
					access.write(b,0,len);
				}
				
				if (access!=null) {
					access.close();
				}
				if (in!=null) {
					in.close();
				}
				
				Message message=new Message();
				message.what=SUCESS_REQ;
				message.obj=DownLoad.getFileName(fileName);
				handler.sendMessage(message);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Message message=new Message();
				message.what=2;
				message.obj=fileName;
				handler.sendMessage(message);
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Message message=new Message();
				message.what=2;
				message.obj=DownLoad.getFileName(fileName);
				handler.sendMessage(message);
				e.printStackTrace();
			}
		}
		
	}
	private String fileName;
	public void downLoadFile(String url) {
			try {
				URL httpUrl = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) httpUrl
						.openConnection();
				conn.setReadTimeout(5000);
				conn.setRequestMethod("GET");
				int count=conn.getContentLength();
				int block=count/3;
				
				fileName=getFileName(url);
				File parent=new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/zzkt/compare/"+parentFile+"/");
				File fileDownLoad=new File(parent,fileName);
				for (int i = 0; i < 3; i++) {
					long start=i*block;
					long end=(i+1)*block-1;
					if (i==2) {
						end=count;
					}
					DownLoadRunnable downLoadRunnable=new DownLoadRunnable(url, fileDownLoad.getAbsolutePath(), start, end,handler);
					threadPool.execute(downLoadRunnable);
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				Message message=new Message();
				message.what=2;
				message.obj=fileName;
				handler.sendMessage(message);
				e.printStackTrace();
			} catch (IOException e) {
				Message message=new Message();
				message.what=2;
				message.obj=fileName;
				handler.sendMessage(message);
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
	public static String getFileName(String url){
		return url.substring(url.lastIndexOf("/")+1);
	}
}
