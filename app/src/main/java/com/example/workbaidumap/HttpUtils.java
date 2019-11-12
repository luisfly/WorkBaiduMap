package com.example.workbaidumap;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.MediaType;
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
      Log.d("okhttpSend", requestBody.toString());

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


  /**
   * 司机登录验证
   * 2019.11.11 现存在问题是发送的json，被接收的时候
   * 2019.11.12 更改 json 放入 requestBody 方法，测试 fit 成功接收到数据
   */
  public static Driver DriverIdentifly(Driver driver) {
    try {

      OkHttpClient client = new OkHttpClient();

      // 发送自定义对象思路，将对象转化为json,再通过okhttp进行发送
      Gson gson = new Gson();
      String Jdriver = gson.toJson(driver);
      Log.d("Jdriver发送数据", Jdriver);
      // 注意mediaType.parse为okhttp3.8.1使用的方法，到 okhttp 3.11.1时要使用 mediaType.get 方法
      RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), Jdriver);

      // 使用 fit 接口的时候无法使用下面方法，因为传输后对象带有名字， fit 无法识别
      // 但是再mvc架构下请使用下面方法，mvc将会自动根据名字对照相应的参数列表
      //RequestBody requestBody = new FormBody.Builder().add("driver", Jdriver).build();

      // post方法
      Request request = new Request.Builder().url("http://192.168.0.247:2088/api/GetData").post(requestBody)
              .build();

      Response response = client.newCall(request).execute();
      if(response.isSuccessful()) {
        //Log.i("OKHttp", "发送成功,数据" + response.body().string());
        String resmessage = response.body().string();
        // 2019.11.12 转化失败，并没有成功赋值
        DataRec dataRec = gson.fromJson(resmessage, DataRec.class);
        Driver redriver = dataRec.getsContent().get(0).getData().get(0);
        Log.i("OKHttp", "发送成功,数据返回" + resmessage);
        Log.i("Driver", dataRec.getsMessage() + " : " + redriver.getDriverNO() + " : " + redriver.getPassword());
        return redriver;
        // Message message = new Message();
        // message.what = 1;
        // message.obj = redriver;
        // Handler handler = new Handler();
        // handler.sendMessage(message);
      } else {
        Log.e("OKHttp", "Unexpected code: " + response);
        throw new IOException("Unexpected code: " + response);
      }
    } catch (IOException ex) {
      ex.getStackTrace();
      return null;
    }
  }

  /**
   * OKhttp 发送定位数据
   */
  public static void LocSend(double latitude, double longitude, String driverNO) {
    try {
      OkHttpClient client = new OkHttpClient();
      Location location = new Location(latitude, longitude, driverNO, new Date(System.currentTimeMillis()));

      // 发送自定义对象思路，将对象转化为json,再通过okhttp进行发送
      Gson gson = new Gson();
      String loc = gson.toJson(location);
      RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), loc);

      // post方法
      Request request = new Request.Builder().url("http://192.168.0.247:2088/api/GetData").post(requestBody)
              .build();


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
