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
                return -1; // We don't know the compressed length in advance!
            }

            @Override
            public BufferedSource source() {

                BufferedSource bufferedSource = Okio.buffer(new GzipSource(Okio.source(body.byteStream())));
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                GZIPOutputStream gos = null;
//                try {
//                    gos = new GZIPOutputStream(baos);
//                    InputStream is = body.byteStream();
//                    int index = -1;
//                    byte[] b = new byte[1024];
//                    while (is != null && (index = is.read(b)) != -1) {
//                        gos.write(b, 0, index);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                return bufferedSource;
            }


//            @Override
//            public void writeTo(BufferedSink sink) throws IOException {
//                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
//                body.writeTo(gzipSink);
//                gzipSink.close();
//            }
        };
    }

}
