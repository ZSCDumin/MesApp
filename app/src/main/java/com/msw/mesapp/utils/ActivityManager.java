package com.msw.mesapp.utils;

/**
 * Created by Mr.Meng on 2017/12/24.
 */

import android.app.Activity;

import java.util.LinkedList;
import java.util.List;

/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class ActivityManager {

    private List<Activity> mActivityList = new LinkedList<Activity>();
    private static ActivityManager instance;

    private ActivityManager(){}
    /**
     * 单一实例
     */
    public static ActivityManager getAppManager(){
        if(instance==null){
            instance=new ActivityManager();
        }
        return instance;
    }
    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity){
        mActivityList.add(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity){
        if(activity!=null){
            mActivityList.remove(activity);
            activity.finish();
            activity=null;
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity(){
        while(mActivityList.size() > 0) {
            Activity activity = mActivityList.get(mActivityList.size() - 1);
            mActivityList.remove(mActivityList.size() - 1);
            activity.finish();
        }
    }
    /**
     * 退出应用程序
     */
    public void AppExit() {
       // Countly.sharedInstance().onStop();
        try {
            finishAllActivity();
        } catch (Exception e) { }
    }
}