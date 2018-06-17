package com.msw.mesapp.utils;

import android.content.Context;

import com.msw.mesapp.base.GlobalKey;

public class GetCurrentUserIDUtil {

    public static String currentUserId(Context context) {
        String id = "";
        id = SharedPreferenceUtils.getString(context, GlobalKey.Login.CODE, id);
        return id;
    }
}
