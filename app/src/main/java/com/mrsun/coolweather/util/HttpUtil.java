package com.mrsun.coolweather.util;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by MrSun on 2017/3/28.
 * 网路请求工具类
 */

public class HttpUtil {
    /**
     * 发送http请求
     * @param address 地址
     * @param callback 回调
     */
    public static void sendOkHttpRequest(String address, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
