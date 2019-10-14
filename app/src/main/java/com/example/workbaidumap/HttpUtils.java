package com.example.workbaidumap;

import android.util.Log;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 系统中负责网络数据传输的模块，借用okhttp进行实现
 * 注意：因为android的官方设定要求，不允许UI线程进行数据传输，所以传输数据的时候必须要新建线程来实现
 */
public class HttpUtils {



    /**
     * OKHttp post发送数据测试,测试未成功，因为公司电脑的android模拟器无法访问本机localhost
     * 10.14 okhttp获取www.baidu.com数据成功
     */
    private void okhttpSend() {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder().add("data1", "test1")
                    .add("data2", "test2").build();
            // post方法
            /*Request request = new Request.Builder().url("http://10.0.2.2:64475/Test/okhttp").post(requestBody)
                    .build();*/
            // get方法
            Request request = new Request.Builder().url("http://www.baidu.com").build();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                Log.i("OKHttp", "发送成功,数据" + response.body().string());
            } else {
                Log.e("OKHttp", "Unexpected code " + response);
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException ex) {
            //Log.e("OKHttp","Unexpected code" + ex.getStackTrace().toString());
            ex.getStackTrace();
        }
    }
}
