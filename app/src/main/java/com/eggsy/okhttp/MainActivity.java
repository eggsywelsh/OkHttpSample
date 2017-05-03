package com.eggsy.okhttp;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.eggsy.okhttp.interceptor.LogInterceptor;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    String baseUrl = "http://192.168.1.221:8080/sframework/demo";

    ExecutorService executorService = Executors.newCachedThreadPool();

    OkHttpClient client;

    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        initData();
    }

    public void initData() {
        client = new OkHttpClient();
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_get_url)
    public void clickGetUrl(View v) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(appendUrlParam("getUrl")).build();

                try {
                    Response response = client.newCall(request).execute();

                    String resonseData = response.body().string();

                    Log.d(TAG, resonseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btn_post_log)
    public void clickPostLog(View v) {

        OkHttpClient logHttpClient = new OkHttpClient.Builder().addInterceptor(new LogInterceptor()).build();

        Request request = new Request.Builder().url(appendUrlParam("getUrl")).build();

        Call call = logHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "request onFailed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resonseData = response.body().string();

                Log.d(TAG, resonseData);
            }
        });
    }

    @OnClick(R.id.btn_post_content)
    public void clickPostContent(View v) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder().url(appendUrlParam("postJson")).post(RequestBody.create(JSON, bowlingJson("eggsy", "welsh"))).build();

                try {
                    Response response = client.newCall(request).execute();

                    String resonseData = response.body().string();

                    Log.d(TAG, resonseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @OnClick(R.id.btn_post_form)
    public void clickPostForm(View v) {

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = new FormBody.Builder().add("userName", "eggsy").add("password", "123456").build();

                Request request = new Request.Builder().url(appendUrlParam("postForm")).post(formBody).build();

                try {
                    Response response = client.newCall(request).execute();

                    String resonseData = response.body().string();

                    Log.d(TAG, resonseData);
                } catch (
                        IOException e)

                {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btn_post_with_header)
    public void clickPostHeader(View v) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                RequestBody formHeaderBody = new FormBody.Builder()
                        .add("userName", "eggsy")
                        .add("password", "123456")
                        .build();

                Request request = new Request.Builder().url(appendUrlParam("postHeader"))
                        .header("encrpy", "MD5")
                        .addHeader("encrpyValue", "fewerg3464rbe5y34")
                        .post(formHeaderBody).build();

                try {
                    Response response = client.newCall(request).execute();

                    String resonseData = response.body().string();

                    Log.d(TAG, resonseData);
                } catch (
                        IOException e)

                {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btn_post_file)
    public void clickPostFile(View v) {

        AssetManager assetManager = getResources().getAssets();

        byte[] fileByteArray = null;

        try {

            InputStream is = assetManager.open("img_example_id_face.png");
            ByteArrayOutputStream bais = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int index = 0;
            while ((index = is.read(b)) != -1) {
                bais.write(b, 0, index);
            }
            fileByteArray = bais.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final byte[] fFfileByteArray = fileByteArray;

        executorService.execute(new Runnable() {
            @Override
            public void run() {

                RequestBody formHeaderBody = new FormBody.Builder()
                        .add("userName", "eggsy")
                        .add("password", "123456")
                        .build();

                MultipartBody multipartBody = new MultipartBody.Builder()
                        .addPart(formHeaderBody)
                        .addFormDataPart("uploadFile", "img_example_id_face.png", RequestBody.create(MediaType.parse("application/octet-stream"), fFfileByteArray))
                        .build();

                Request request = new Request.Builder().url(appendUrlParam("postFile"))
                        .header("encrpy", "MD5")
                        .addHeader("encrpyValue", "fewerg3464rbe5y34")
                        .post(multipartBody).build();

                try {
                    Response response = client.newCall(request).execute();

                    String resonseData = response.body().string();

                    Log.d(TAG, resonseData);
                } catch (
                        IOException e)

                {
                    e.printStackTrace();
                }
            }
        });
    }

    private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    @OnClick(R.id.btn_post_cookie)
    public void clickPostCookie(View v) {

        OkHttpClient okHttpClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                Log.d(TAG, "save cookie from server");
                for (Cookie cookie : cookies) {
                    Log.d(TAG, "cookie name=" + cookie.name() + " , value=" + cookie.value());
                }
                cookieStore.put(url.host(), cookies);
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url.host());
                Log.d(TAG, "post cookie from local");
                if (cookies != null && cookies.size() > 0) {
                    for (Cookie cookie : cookies) {
                        Log.d(TAG, "cookie name=" + cookie.name() + " , value=" + cookie.value());
                    }
                }
                return cookies != null ? cookies : new ArrayList<Cookie>();
            }
        }).build();

        RequestBody formHeaderBody = new FormBody.Builder()
                .add("userName", "eggsy")
                .add("password", "123456")
                .build();

        Request request = new Request.Builder().url(appendUrlParam("postCookie")).post(formHeaderBody).build();

        Call call = okHttpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "request failed");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String resonseData = response.body().string();

                Log.d(TAG, resonseData);
            }
        });
    }

    private String bowlingJson(String player1, String player2) {
        return "{'winCondition':'HIGH_SCORE',"
                + "'name':'Bowling',"
                + "'round':4,"
                + "'lastSaved':1367702411696,"
                + "'dateStarted':1367702378785,"
                + "'players':["
                + "{'name':'" + player1 + "','history':[10,8,6,7,8],'color':-13388315,'total':39},"
                + "{'name':'" + player2 + "','history':[6,10,5,10,10],'color':-48060,'total':41}"
                + "]}";
    }

    private String appendUrlParam(String... paths) {
        StringBuilder sb = new StringBuilder(baseUrl);
        for (String path : paths) {
            sb.append("/" + path);
        }
        return sb.toString();
    }

}