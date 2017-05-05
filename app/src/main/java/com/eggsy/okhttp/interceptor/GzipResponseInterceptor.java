package com.eggsy.okhttp.interceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.GzipSource;
import okio.Okio;

/**
 * Created by eggsy on 17-5-3.
 *
 * gzip decompressed interceptor
 */

public class GzipResponseInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        return response.newBuilder().body(gzip(response.body())).build();
    }

    private ResponseBody gzip(final ResponseBody body) {
        return new ResponseBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return body.contentLength(); // We don't know the compressed length in advance!
            }

            @Override
            public BufferedSource source() {

                BufferedSource bufferedSource = Okio.buffer(new GzipSource(Okio.source(body.byteStream())));
                return bufferedSource;
            }

        };
    }

}
