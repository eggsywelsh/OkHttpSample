package com.eggsy.okhttp.interceptor;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eggsy on 17-5-2.
 */

public class LogInterceptor implements Interceptor {

    private static final String TAG = "HTTP";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        long beginTime = System.nanoTime();

        Response response = chain.proceed(request);

        long endTime = System.nanoTime();

        Log.d(TAG,"[HTTP]"+request.url()+"[TIME]"+(endTime-beginTime)/1000000+"ms");

        return response;
    }
}
