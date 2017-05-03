package com.eggsy.okhttp.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eggsy on 17-5-3.
 *
 * print request param interceptor
 */

public class PrintParamInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();



        return null;
    }
}
