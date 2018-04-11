package com.msw.mesapp.utils;

import android.app.Activity;

import com.sdsmdg.tastytoast.TastyToast;

/**
 * Created by Mr.Meng on 2018/3/15.
 */

public class ToastUtil {
    public static int Default = TastyToast.DEFAULT;
    public static int Warning = TastyToast.WARNING;
    public static int Error = TastyToast.ERROR;
    public static int Info = TastyToast.INFO;
    public static int Confusion = TastyToast.CONFUSING;
    public static int Success = TastyToast.SUCCESS;

    public static void showToast(Activity activity,String info, int type){
        if(activity != null)
        TastyToast.makeText(activity, info, TastyToast.LENGTH_SHORT, type);
    }
    public static void showToastLong(Activity activity,String info, int type){
        if(activity != null)
        TastyToast.makeText(activity, info, TastyToast.LENGTH_LONG, type);
    }
}
