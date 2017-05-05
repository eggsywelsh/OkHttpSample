package com.eggsy.okhttp.interceptor;

import android.util.Log;

import com.eggsy.okhttp.Config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by eggsy on 17-5-3.
 * <p>
 * print request param interceptor
 */

public class RequestAddParamInterceptor implements Interceptor {

    private static final String TAG = Config.PRINT_HTTP_TAG;

    private static Map<String, String> commonParamsMap;

    static {
        commonParamsMap = new HashMap<>();
        commonParamsMap.put("timeStamp", String.valueOf(System.currentTimeMillis()));
        commonParamsMap.put("appVersion", "1.0");
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();

        String requestMethod = request.method();

        if ("GET".equalsIgnoreCase(requestMethod)) {
            // add common params
            request = addCommonParamsByGet(request, commonParamsMap);
        } else if ("POST".equalsIgnoreCase(requestMethod) || "PUT".equalsIgnoreCase(requestMethod)
                || "DELETE".equalsIgnoreCase(requestMethod)
                || "PATCH".equalsIgnoreCase(requestMethod)
                ) {
            // add common params
            request = addCommonParamsByForm(request, commonParamsMap);
        }

        return chain.proceed(request);
    }

    private static Request addCommonParamsByGet(Request request, Map<String, String> addParamsMap) {
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
        for (Map.Entry<String, String> entry : addParamsMap.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }
        HttpUrl newGetUrl = urlBuilder.build();

        Log.d(TAG, "print final request url:" + newGetUrl.toString());

        request = request.newBuilder().url(newGetUrl).build();

        return request;
    }

    private static Request addCommonParamsByForm(Request request, Map<String, String> addParamsMap) {
        if (request.body() instanceof MultipartBody) {
            MultipartBody oldMultipartBody = (MultipartBody) request.body();
            MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

            for (Map.Entry<String, String> entry : addParamsMap.entrySet()) {
                multipartBuilder.addFormDataPart(entry.getKey(), entry.getValue());
            }

            if (oldMultipartBody != null && oldMultipartBody.size() > 0) {
                List<MultipartBody.Part> oldParts = ((MultipartBody) request.body()).parts();
                if (oldParts != null && oldParts.size() > 0) {
                    for (MultipartBody.Part part : oldParts) {
                        multipartBuilder.addPart(part);
                    }
                }
            }
            request = request.newBuilder().post(multipartBuilder.build()).build();
        } else if (request.body() instanceof FormBody) {
            FormBody formBody = (FormBody) request.body();
            FormBody.Builder bodyBuilder = new FormBody.Builder();
            for (int i = 0; i < formBody.size(); i++) {
                bodyBuilder.add(formBody.name(i), formBody.value(i));
            }

            request = request.newBuilder().method(request.method(), formBody).build();
        }

        return request;
    }

    private static Map<String, String> parseGetParams(Request request) {
        Map<String, String> params = null;
        Set<String> names = request.url().queryParameterNames();
        if (names != null || names.size() > 0) {
            params = new HashMap<>();
            for (int i = 0; i < names.size(); i++) {
                String name = request.url().queryParameterName(i);
                String value = request.url().queryParameterValue(i);
                params.put(name, value);
            }
        }
        printParams(params);
        return params;
    }

    private static Map<String, String> parseFormParams(Request request) {
        Map<String, String> params = null;
        FormBody body = null;
        try {
            body = (FormBody) request.body();
        } catch (ClassCastException c) {
        }
        if (body != null) {
            int size = body.size();
            if (size > 0) {
                params = new HashMap<>();
                for (int i = 0; i < size; i++) {
                    params.put(body.name(i), body.value(i));
                }
            }
        }
        printParams(params);
        return params;
    }

    private static void printParams(Map<String, String> params) {
        if (params != null) {
            Log.d(TAG, "print request params:");
            for (Map.Entry<String, String> entry : params.entrySet()) {
                Log.d(TAG, "    name=" + entry.getKey() + " , value=" + entry.getValue());
            }
        }
    }
}
