package com.eggsy.okhttp.interceptor;

import android.util.Log;

import com.eggsy.okhttp.Config;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eggsy on 17-5-2.
 *
 * log interceptor
 * print url and request spend time
 */

public class LogInterceptor implements Interceptor {

    private static final String TAG = "HTTP";

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        long beginTime = System.nanoTime();

        Response response = chain.proceed(request);

        if (Config.IS_PRINT_HTTP_RESPONSE_HEADER){
            Headers headers = response.headers();

            Log.d(TAG, String.format("Received response header %n%s",
                    headers));
        }

        long endTime = System.nanoTime();

        Log.d(TAG,"[HTTP]"+request.url()+"[TIME]"+(endTime-beginTime)/1000000+"ms");

        return response;
    }
}
