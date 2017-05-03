package com.eggsy.okhttp.interceptor;

import android.util.Log;

import com.eggsy.okhttp.Config;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eggsy on 17-5-3.
 */

public class ResponseHeaderInterceptor implements Interceptor {

    private static final String TAG = Config.PRINT_HTTP_TAG;

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        Response response = chain.proceed(request);

        Headers headers = response.headers();

        Log.d(TAG, "[HTTP]"+String.format("Received response header %n%s",
                headers));

        return response;
    }
}
