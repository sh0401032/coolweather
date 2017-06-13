package com.shaohuan.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 创建者： Administrator
 * <p>
 * 创建时间： 2017/6/10
 * <p>
 * 描述：
 */


public class HttpUtil {
    public static void sendOkHttpRequest(String addred, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(addred).build();
        client.newCall(request).enqueue(callback);
    }
}
