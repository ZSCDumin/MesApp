package com.android.dev;
 

 
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class BarcodeAPI {

	private static final String TAG = "BarcodeAPI";

	public static BarcodeAPI instance = null;
	public Handler m_handler = null;
	public static   final int BARCODE_READ = 10;
	public static synchronized BarcodeAPI getInstance( ) {
		if (instance == null) {
			instance = new BarcodeAPI();
		}
		return instance;
	}
 

	private BarcodeAPI() {
		
	}

	/**
	 * 
	 * 打开扫描头
	 * 注：只能调一次，不能多次重复调用。
	 * 
	 */
	public native void open();
	
	/**
	 * 设置扫描头类型 
	 * 
	 * @param value   10:5110;
	 * 				  20: N3680
	 * 				  30:-
	 * 				  40:MJ-2000
	 * 				  50: ES4650
	 * 
	 * 要在Open之后调用
	 * 				  
	 */
	public native void setScannerType(int value);

	/**
	 * 触发扫描
	 */
	public native void scan();

	/**
	 * 停止扫描
	 */
	public native void stopScan();
	/**
	 * 设置扫描模式(单扫/连扫)
	 * @param isContinue :true :连扫;false:单扫 
	 */
	public native void setScanMode(boolean isContinue);
	/**
	 * 设置补光灯
	 * @param isOpen:true:开启;false:关闭
	 */
	public native void setLights(boolean isOpen);
	/**
	 * 当前模式,是否是连扫
	 * @return 1:连扫
	 */
	public native int isContinueModel();
	
	/**
	 * 设置超时时间
	 * @param seconds 秒
	 * @return
	 */
	public native int setReadTime(int seconds);

	/**
	 * 关闭扫描头
	 */
	public native void close();
	/**
	 * 获取设备唯一ID
	 * @return
	 */
	public native String getMachineCode();
	
	public native String getApiVer();
	
	
	/**
	 * 设置数据的编码
	 * @param encoding ：gbk/utf-8
	 */
	public native void setEncoding(String encoding);

	public void setScanResults(String info) {
		if (info == null) {
			return;
		} 
		 if(m_handler!=null){
			 Message m = Message.obtain(m_handler, BARCODE_READ);
				m.obj = info;
				m_handler.sendMessage(m);
		 }

	}

	 
 

	static {
		try{
		  System.loadLibrary("TD5Barcode");
		}catch(Throwable ex){
			ex.printStackTrace();
			Log.d("BarcodeAPI","TD5Barcode加载失败");
		}
	}
}
