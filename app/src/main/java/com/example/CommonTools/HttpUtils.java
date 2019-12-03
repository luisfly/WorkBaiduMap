package com.example.CommonTools;

import android.util.Log;

import com.example.FitEntity.DataRec;
import com.example.FitEntity.Driver;
import com.example.FitEntity.HttpMessageObject;
import com.example.FitEntity.Location;
import com.example.workbaidumap.RecJudge;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 系统中负责网络数据传输的模块，借用okhttp进行实现，并将方法定义为类方法
 * 注意：因为android的官方设定要求，不允许UI线程进行数据传输，所以传输数据的时候必须要新建线程来实现
 *
 * 2019.11.22 目标时将所有方法整合到通用方法中，但是还是存在无法解决的问题
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
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Location location = new Location(24.66,25.11, "133311", format.format(new Date()));
            //new Date(System.currentTimeMillis())
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
     * 2019.11.13 测试成功，现在是使用 LinkedTreeMap 接收，再通过 LinkedTreeMap -> jsonString -> 指定对象类型
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
                String resmessage = response.body().string();
                // 检测回传数据
                RecJudge recJudge = gson.fromJson(resmessage, RecJudge.class);
                // isSuccess 字段为1，即数据查询成功
                if (recJudge.getIsSuccess() == 1) {

                    // 通用接收类型接收 json 数据
                    DataRec dataRec = gson.fromJson(resmessage, DataRec.class);
                    String data = gson.toJson(dataRec.getsContent().get(0).getData().get(0));
                    Driver redriver = gson.fromJson(data, Driver.class);
                    Log.i("OKHttp", "发送成功,数据返回" + resmessage);
                    Log.i("Driver", dataRec.getsMessage() + " : " + redriver.getDriverNO() + " : " + redriver.getPassword());
                    return redriver;
                } else {
                      // 否则数据查询失败
                    Log.i("Okhttp", recJudge.getsMessage());
                    return null;
                }

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
           DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
           Location location = new Location(latitude, longitude, driverNO, format.format(new Date()));

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



    /**
     * 2019.11.22
     * 面向对象所有单个数据发送的方法
     * 本方法对应数据库中的 insert、update、delete 操作，操作正确时数据库并不会返回数据
     * 利用 api 的 get 方法
     * @param bussinessName 执行 fit 中的 business 名字
     * @param httpObject 传入的值
     */
    public static void PostSingleData(@NotNull String bussinessName, @NotNull HttpMessageObject httpObject) {
        try {
            OkHttpClient client = new OkHttpClient();
            HttpMessageObject obj = httpObject;
            obj.setBusinessName(bussinessName);


            // 发送自定义对象思路，将对象转化为json,再通过okhttp进行发送
            Gson gson = new Gson();
            String postObject = gson.toJson(obj);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), postObject);

            // post方法,将数据保存在报文主体
            Request request = new Request.Builder().url("http://192.168.0.247:2088/api/GetData").post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();

            if(response.isSuccessful()) {
                // 返回信息获取
                String resmessage = response.body().string();
                Log.d("OKHttp", resmessage);
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
     * 2019.11.25
     * 面向对象同时发送多个数据发送的方法，后续完善
     * 本方法对应数据库中的 insert、update、delete 操作，操作正确时数据库并不会返回数据
     * 利用 api 的 get 方法
     * @param bussinessName 执行 fit 中的 business 名字
     * @param httpObjList 传入的列
     */
    public static void PostMulData(@NotNull String bussinessName, List<HttpMessageObject> httpObjList) {

    }


    /**
     * 2019.11.22
     * 所有数据发送的方法
     * 本方法对应数据库中的 select 操作，数据库会主动返回数据
     * 利用 api 的 get 方法
     *  @param bussinessName 执行 fit 中的 business 名字
     *  @param httpObject 传入值以及返回值的类型
     */
    public static List<HttpMessageObject> GetData(@NotNull String bussinessName, HttpMessageObject httpObject) {
        List<HttpMessageObject> recObjs = new ArrayList<HttpMessageObject>();

        try {

            OkHttpClient client = new OkHttpClient();
            HttpMessageObject obj = httpObject;
            obj.setBusinessName(bussinessName);

            // 发送自定义对象思路，将对象转化为json,再通过okhttp进行发送
            Gson gson = new Gson();
            String sendJson = gson.toJson(obj);
            // 注意mediaType.parse为okhttp3.8.1使用的方法，到 okhttp 3.11.1时要使用 mediaType.get 方法
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), sendJson);

            // post方法
            Request request = new Request.Builder().url("http://192.168.0.247:2088/api/GetData").post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                String resmessage = response.body().string();
                // 检测回传数据
                RecJudge recJudge = gson.fromJson(resmessage, RecJudge.class);
                // isSuccess 字段为1，即数据查询成功
                if (recJudge.getIsSuccess() == 1) {

                    // 通用接收类型接收 json 数据
                    DataRec dataRec = gson.fromJson(resmessage, DataRec.class);
                    List<String> data = new ArrayList<String>();
                    List<DataRec.Content> contents = dataRec.getsContent();
                    // 解析嵌套 json
                    for (DataRec.Content content : contents) {
                        for (LinkedTreeMap map : content.getData()) {
                            // 先将LinkedTreeMap转回json String
                            String datas = gson.toJson(map);
                            // 再将json转到指定对象
                            recObjs.add(gson.fromJson(datas, httpObject.getClass()));
                        }
                    }

                    Log.i("GetData", "数据量: " + dataRec.getsContent().size());
                    return recObjs;
                } else {
                    // 否则数据查询失败
                    Log.e("GetData", recJudge.getsMessage());
                    return null;
                }

            } else {
                Log.e("GetData", "Unexpected code: " + response);
                throw new IOException("Unexpected code: " + response);
            }
        } catch (IOException ex) {
            ex.getStackTrace();
            return null;
        }

    }

    /**
     * 2019.11.22
     * 所有数据发送的方法
     * 本方法对应数据库中的 select 操作，数据库会主动返回数据
     * 利用 api 的 get 方法
     *  @param bussinessName 执行 fit 中的 business 名字
     *  @param httpObject 传入值以及返回值的类型
     */
    public static List<HttpMessageObject> GetData(@NotNull String bussinessName, HttpMessageObject httpObject, Class reClass) {
        List<reClass> recObjs = new ArrayList<reClass>();

        try {

            OkHttpClient client = new OkHttpClient();
            HttpMessageObject obj = httpObject;
            obj.setBusinessName(bussinessName);

            // 发送自定义对象思路，将对象转化为json,再通过okhttp进行发送
            Gson gson = new Gson();
            String sendJson = gson.toJson(obj);
            // 注意mediaType.parse为okhttp3.8.1使用的方法，到 okhttp 3.11.1时要使用 mediaType.get 方法
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), sendJson);

            // post方法
            Request request = new Request.Builder().url("http://192.168.0.247:2088/api/GetData").post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                String resmessage = response.body().string();
                // 检测回传数据
                RecJudge recJudge = gson.fromJson(resmessage, RecJudge.class);
                // isSuccess 字段为1，即数据查询成功
                if (recJudge.getIsSuccess() == 1) {

                    // 通用接收类型接收 json 数据
                    DataRec dataRec = gson.fromJson(resmessage, DataRec.class);
                    List<String> data = new ArrayList<String>();
                    List<DataRec.Content> contents = dataRec.getsContent();
                    // 解析嵌套 json
                    for (DataRec.Content content: contents) {
                        for (LinkedTreeMap map : content.getData()) {
                            // 先将LinkedTreeMap转回json String
                            String datas = gson.toJson(map);
                            // 再将json转到指定对象
                            // recObjs.add(gson.fromJson(datas, httpObject.getClass()));
                            recObjs.add(gson.fromJson(datas, reClass));
                            httpObject.get
                        }
                    }

                    Log.i("GetData", "数据量: " + dataRec.getsContent().size());
                    return recObjs;
                } else {
                    // 否则数据查询失败
                    Log.e("GetData", recJudge.getsMessage());
                    return null;
                }

            } else {
                Log.e("GetData", "Unexpected code: " + response);
                throw new IOException("Unexpected code: " + response);
            }
        } catch (IOException ex) {
            ex.getStackTrace();
            return null;
        }

    }


    /**
     * 2019.11.27
     * 所有数据发送的方法,本方法暂定用于 fit 中需要返回多个表的情况
     * 本方法对应数据库中的 select 操作，数据库会主动返回数据
     * 利用 api 的 get 方法
     * @param bussinessName 执行 fit 中的 business 名字
     * @param httpObject 传入值以及返回值的类型
     * @param reClass 返回表数据时接收类的类名以及对应的表名
     */
    public static HashMap<String, List<HttpMessageObject>> GetData(@NotNull String bussinessName,
         HttpMessageObject httpObject, HashMap<String, Class<? extends HttpMessageObject>> reClass) {
        // 返回列表
        HashMap<String, List<HttpMessageObject>> recAll = new HashMap<String, List<HttpMessageObject>>();

        try {

            OkHttpClient client = new OkHttpClient();
            HttpMessageObject obj = httpObject;
            obj.setBusinessName(bussinessName);

            // 发送自定义对象思路，将对象转化为json,再通过okhttp进行发送
            Gson gson = new Gson();
            String sendJson = gson.toJson(obj);
            // 注意mediaType.parse为okhttp3.8.1使用的方法，到 okhttp 3.11.1时要使用 mediaType.get 方法
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), sendJson);

            // post方法
            Request request = new Request.Builder().url("http://192.168.0.247:2088/api/GetData").post(requestBody)
                    .build();

            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                String resmessage = response.body().string();
                // 检测回传数据
                RecJudge recJudge = gson.fromJson(resmessage, RecJudge.class);
                // isSuccess 字段为1，即数据查询成功
                if (recJudge.getIsSuccess() == 1) {

                    // 通用接收类型接收 json 数据
                    DataRec dataRec = gson.fromJson(resmessage, DataRec.class);
                    //List<String> data = new ArrayList<String>();
                    List<DataRec.Content> contents = dataRec.getsContent();
                    // 解析嵌套 json
                    for (DataRec.Content content: contents) {
                        List<HttpMessageObject> recObjs = new ArrayList<HttpMessageObject>();
                        List<LinkedTreeMap> datas = content.getData();
                        for (LinkedTreeMap map : datas) {
                            // 先将LinkedTreeMap转回json String
                            String data = gson.toJson(map);
                            // 再将json转到指定对象
                            recObjs.add(gson.fromJson(data, reClass.get(content.getTableName())));
                        }
                        // 将 table 数据列表以键值对方式放入 HashMap 中
                        // 要拿出来的时候就以表名查找
                        recAll.put(content.getTableName(), recObjs);
                    }

                    Log.i("GetData", "数据量: " + dataRec.getsContent().size());
                    return recAll;
                } else {
                    // 否则数据查询失败
                    Log.e("GetData", recJudge.getsMessage());
                    return null;
                }

            } else {
                Log.e("GetData", "Unexpected code: " + response);
                throw new IOException("Unexpected code: " + response);
            }
        } catch (IOException ex) {
            ex.getStackTrace();
            return null;
        }

    }

    /**
     * 模仿 rf 基于面向过程的方法思路编写数据接收发送的方法
     * 本方法设置发送参数
     * @param Parmname 参数名
     * @param value 参数值
     */
    public void setParm(String Parmname, String value) {

    }

    /**
     * 模仿 rf 基于面向过程的方法思路编写数据接收发送的方法
     * 本方法设置接收参数，返回 string 对象
     * @param Parmname 参数名
     * @return 参数值
     */
    public String getParm(String Parmname) {
        return null;
    }
}
