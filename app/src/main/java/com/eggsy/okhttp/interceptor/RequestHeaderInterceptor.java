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

public class RequestHeaderInterceptor implements Interceptor {

    private static final String TAG = Config.PRINT_HTTP_TAG;

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        Headers headers = request.headers();

        Log.d(TAG, "[HTTP][REQUEST]"+String.format("Request header %n%s",
                headers));

        Response response = chain.proceed(request);

        return response;
    }
}
