package com.example.workbaidumap;

import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 系统中负责网络数据传输的模块，借用okhttp进行实现，并将方法定义为类方法
 * 注意：因为android的官方设定要求，不允许UI线程进行数据传输，所以传输数据的时候必须要新建线程来实现
 */
public class HttpUtils {

  /**
   * OKHttp post发送数据测试,测试未成功，现发现原因是 vs 自带的 iis express 版无法访问而已
   * 10.14 okhttp get 方法获取 www.baidu.com 数据成功
   * 10.15 okhttp post 方法向本地发送数据成功
   */
  public static void okhttpSend() {
    try {
      OkHttpClient client = new OkHttpClient();

      Location location = new Location(24.66,25.11, "133311", new Date(System.currentTimeMillis()));
            /*RequestBody requestBody = new FormBody.Builder().add("name", "luisfly")
                    .add("data2", "test2").build();*/

      // 发送自定义对象思路，将对象转化为json,再通过okhttp进行发送
      Gson gson = new Gson();
      String loc = gson.toJson(location);
      RequestBody requestBody = new FormBody.Builder().add("location", loc).build();

      // post方法
      Request request = new Request.Builder().url("http://10.0.2.2:8080/Location").post(requestBody)
              .build();
      // get方法
      //Request request = new Request.Builder().url("http://www.baidu.com").build();

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
