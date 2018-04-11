package com.msw.mesapp.utils.nfc;

import java.util.ArrayList;
 
 

public class CommonValues {
	public static CommonValues commonValues;
	
	public ArrayList<MifareClassic1k> mifareClassic1kList = new ArrayList<MifareClassic1k>();
	public ArrayList<MifareUltraLightC> mifareUltraLightCList = new ArrayList<MifareUltraLightC>();
	public ArrayList<MifareUltraLightC> mifareUltraLightList = new ArrayList<MifareUltraLightC>();
	
	//public String Name;
	public String UID="";
	public String Type="";
	public String Memory="";
	public String Sector="";
	public String Block="";
	public String ultraLightCPageSize="0";
	public String ultraLightCPageCount="0";
	
	public static void Initalization(){
		if(commonValues == null)
			commonValues = new CommonValues();
	}
	
	public static CommonValues getInstance(){
		 
			
		return commonValues;
	}
}
