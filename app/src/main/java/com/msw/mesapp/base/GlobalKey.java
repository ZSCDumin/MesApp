package com.msw.mesapp.base;

/**
 * Created by Mr.Meng on 2018/2/10.
 */

public class GlobalKey {

    //权限
    public final static  class  permiss{
        public final static String SPKEY = "000";

        public final static String Inspect_Worker = "86"; //工人巡检
        public final static String Inspect_Monitor = "87"; //巡检审核
        public final static String Repair_Report = "88"; //维修申请
        public final static String Repair_Reporter = "89"; //报修人
        public final static String Repair_Worker = "90"; //维修人
        public final static String Repair_Soorer = "91"; //评价人
        public final static String QrManner = "92"; //条码管理
    }
    //....
    public final static class Login {
        public final static String DATA = "001";
        public final static String CODE = "001CODE";
        public final static String NFC  = "001NFC";
    }
}
