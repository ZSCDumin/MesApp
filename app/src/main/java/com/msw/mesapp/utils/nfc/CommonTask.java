package com.msw.mesapp.utils.nfc;

import java.io.UnsupportedEncodingException;

 
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
 

public class CommonTask {
	static AlertDialog mDialog;

	 

	public static String getHex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (int i = bytes.length - 1; i >= 0; --i) {
			int b = bytes[i] & 0xff;
			if (b < 0x10)
				sb.append('0');
			sb.append(Integer.toHexString(b));
			if (i > 0) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	public static String hexToAscii(String hexValue) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < hexValue.length(); i += 2) {
			String str = hexValue.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}
		return output.toString();
	}
	
	 
	public static String dectoString( int mac){
	    String ret = "";
	    while ( mac > 0 ){
	      switch (mac %10){
	        case 0: ret = "Zero" + ret;
	            break;
	        case 1: ret = "One" + ret;
	            break;
	        case 2: ret = "Two" + ret;
	            break;
	        case 3: ret = "Three" + ret;
	            break;
	        case 4: ret = "Four" + ret;
	            break;
	        case 5: ret = "Five" + ret ;
	            break;
	        case 6: ret = "Six" + ret;
	            break;  
	        case 7: ret = "Seven" + ret;
	            break;
	        case 8: ret = "Eight" + ret;
	            break;  
	        case 9: ret = "Nine" + ret;
	            break;    
	      }
	      mac /=10;
	    }
	    return ret;
	  }

	  
	  public static final byte[] HEX_CHAR_TABLE = {
	    (byte)'0', (byte)'1', (byte)'2', (byte)'3',
	    (byte)'4', (byte)'5', (byte)'6', (byte)'7',
	    (byte)'8', (byte)'9', (byte)'a', (byte)'b',
	    (byte)'c', (byte)'d', (byte)'e', (byte)'f'
	  };    

	  public static String getHexString(byte[] raw) 
	    throws UnsupportedEncodingException 
	  {
	    byte[] hex = new byte[2 * raw.length];
	    int index = 0;

	    for (byte b : raw) {
	      int v = b & 0xFF;
	      hex[index++] = HEX_CHAR_TABLE[v >>> 4];
	      hex[index++] = HEX_CHAR_TABLE[v & 0xF];
	    }
	    return new String(hex, "ASCII");
	  }
	  
	  public static String getHexString(short[] raw) 
	  throws UnsupportedEncodingException 
	  {
	    byte[] hex = new byte[2 * raw.length];
	    int index = 0;
	    
	    for (short b : raw) {
	      int v = b & 0xFF;
	      hex[index++] = HEX_CHAR_TABLE[v >>> 4];
	      hex[index++] = HEX_CHAR_TABLE[v & 0xF];
	    }
	    return new String(hex, "ASCII");
	  }
	  
	  public static String getHexString(short raw) {  
	    byte[] hex = new byte[2];
	    int v = raw & 0xFF;
	    hex[0] = HEX_CHAR_TABLE[v >>> 4];
	    hex[1] = HEX_CHAR_TABLE[v & 0xF];
	    try {
	      return new String(hex, "ASCII");
	    } catch (UnsupportedEncodingException e) {}
	    return "";
	  } 
}
