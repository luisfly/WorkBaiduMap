package com.example.workbaidumap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 2019.9.10 (1) 出现问题时绘制百度地图时，地图绘制失败，先是使用旧项目中的 LBS压缩文件以及jniLibs文件
 *               后改为重新在百度官网上下载基础包后，重新测试，地图绘制成功
 *      9.12 (1) 暂时不使用百度鹰眼路程绘制，由于鹰眼存在使用次数限制，现改手动编程在地图上绘制实现
 *           (2) 现在存在问题，在模拟器上测试时，无法获得定位，即使使用网络定位，在真机上测试时，
 *               获取了定位权限之后，gps无法自动打开，还是必须要用户手动，开启定位
 *      9.16 (1) 完成路线绘制功能，但是实测时发现app退到后台后，定位停止获取
 *      9.17 (1) 完成后台自动定位功能
 *
 */
public class MainActivity extends AppCompatActivity {

    /* 最外层布局获取 */
    private DrawerLayout drawerLayout = null;
    /* 地图控件 */
    private MapView mMapView = null;
    /* 百度地图的实体 */
    private BaiduMap mBaiduMap = null;
    /* 侧滑菜单打开按钮 */
    private Button slipMenu = null;
    /* 地图上线段绘制控制 */
    private Polyline mPolyline = null;
    /* 今日行程绘制 */
    private Button tRoad = null;
    /* 发送数据 */
    private Button post = null;
    /* 后台定位 */
    private NotificationUtils mNotificationUtils;
    private Notification notification;
    /* 权限获取 */
    private List<String> permissionList = new ArrayList<>();
    /* 地图初始化的时候，判定是否为初次定位，是否需要移动地图 */
    private boolean isFirstLocate = true;
    private int count = 0;

    /* 当前位置记录 */
    private List<LatLng> nowLoc = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* 百度地图初始化，必须要有 */
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);

        setContentView(R.layout.activity_main);
        // 获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmpView);
        mBaiduMap = mMapView.getMap();
        // 侧滑主动打开按钮
        slipMenu = (Button) findViewById(R.id.button);
        // 布局获取
        drawerLayout = (DrawerLayout) findViewById(R.id.home);
        // 路径绘制
        tRoad = (Button) findViewById(R.id.tRoad);
        // okhttp发送数据
        post = (Button) findViewById(R.id.post) ;

        // 显示普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位绘制功能
        mBaiduMap.setMyLocationEnabled(true);

        //状态栏自动隐藏,或者使用状态栏透明
        getWindow().getDecorView().setSystemUiVisibility(View.INVISIBLE);

        // 权限询问
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!permissionList.isEmpty()) {
            String [] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 1);
        }

        // 侧滑界面使用按钮打开完成
        slipMenu.setOnClickListener((View v)->{
            drawerLayout.openDrawer(GravityCompat.START);
        });

        tRoad.setOnClickListener((View v)->{
            drawRoad();
        });

        // 数据发送按钮
        post.setOnClickListener((View v)->{
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    okhttpSend();
                }
            });
            thread.start();
            Toast.makeText(this, "you have click thread!",Toast.LENGTH_SHORT).show();
        });

        // 7.0测试成功
        //设置后台定位
        //android8.0及以上使用NotificationUtils
        if (Build.VERSION.SDK_INT >= 26) {
            mNotificationUtils = new NotificationUtils(this);
            Notification.Builder builder2 = mNotificationUtils.getAndroidChannelNotification
                    ("适配android 8限制后台定位功能", "正在后台定位");
            notification = builder2.build();
        } else {
            //获取一个Notification构造器
            Notification.Builder builder = new Notification.Builder(MainActivity.this);
            Intent nfIntent = new Intent(MainActivity.this, MainActivity.class);

            builder.setContentIntent(PendingIntent.
                    getActivity(MainActivity.this, 0, nfIntent, 0)) // 设置PendingIntent
                    .setContentTitle("适配android 8限制后台定位功能") // 设置下拉列表里的标题
                    .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
                    .setContentText("正在后台定位") // 设置上下文内容
                    .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

            notification = builder.build(); // 获取构建好的Notification
        }


        // 地图上绘制线测试
        //drawRoad();

        // okhttp数据发送测试,因为android不允许ui线程执行数据发送操作,需要自行写线程执行
        //Thread thread = new Thread(new Runnable() {
        //    @Override
        //    public void run() {
        //        okhttpSend();
        //    }
        //});
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
        // 9.11 修改在activity创建完成后再执行这个定位功能实现
        initLocationOption();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时必须调用mMapView. onPause ()
        mMapView.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时必须调用mMapView.onDestroy()
        mMapView.onDestroy();
    }

    /**
     * 2019.9.10 开启定位功能并在地图上绘制，将绘制功能写在位置监听器中，在获取位置同时定位
     * 因为android不支持在主线程外对控件进行绘制，所以暂时将监听器作为内部类写在主线程内
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            //获取纬度信息
            double latitude = location.getLatitude();
            //获取经度信息
            double longitude = location.getLongitude();
            //获取定位精度，默认值为0.0f
            float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
            int errorCode = location.getLocType();

            // System.out.println("纬度：" + latitude + " 经度：" + longitude + " 错误码：" + errorCode);

            // 调用绘制方法
            drawThePosition(latitude, longitude);

            // 将当前位置存储在数组中,当点过多时必定会出现内存溢出情况
            // 必须要有去除无用点的机制
            LatLng now = new LatLng(latitude, longitude);
            nowLoc.add(now);

        }
    }

    /**
     * 将定位信息绘制在地图上，在初次定位时将移动地图并且调整地图缩放
     * @param latitude 纬度信息
     * @param longitude 经度信息
     */
    private void drawThePosition(double latitude ,double longitude) {
        /* if it is the init move the location */
        if (isFirstLocate) {
            /* 获取当前位置赋值latlng对象 */
            LatLng ll = new LatLng(latitude, longitude);
            /* 设定新的位置 */
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            /* 移动地图位置以及调整当前缩放 */
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            /* 非初次定位不会将位置移动到当前位置 */
            mBaiduMap.animateMapStatus(update);
            isFirstLocate = false;
        }

        count++;
        //Toast.makeText(this, "纬度：" + latitude + " 经度：" + longitude + " 计数： " + count, Toast.LENGTH_SHORT).show();
        /* 当前位置显示构造器 */
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        /* 设置当前位置 */
        locationBuilder.latitude(latitude);
        locationBuilder.longitude(longitude);
        /* 将当前位置更新到地图上 */
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }

    /**
     * 9.16 地图上路线绘制测试,测试成功，确实能在百度地图上绘制线段
     *      进行修改，改为可以自行绘制行走路路线
     */
    private void drawRoad() {
        /*
        //构建折线点坐标
        LatLng p1 = new LatLng(39.97923, 116.357428);
        LatLng p2 = new LatLng(39.94923, 116.397428);
        LatLng p3 = new LatLng(39.97923, 116.437428);
        List<LatLng> points = new ArrayList<LatLng>();
        points.add(p1);
        points.add(p2);
        points.add(p3);
        //dottedLine是否为虚线
        OverlayOptions ooPolyline = new PolylineOptions().width(5).color(0xAAFF0000).dottedLine(true).points(points);
        //添加在地图中
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);*/

        // 线的属性定义
        OverlayOptions ooPolyline = new PolylineOptions().width(5).color(0xAAFF0000).dottedLine(false).points(nowLoc);
        // 添加在地图中
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
    }

    /**
     * 2019.9.10 连续定位功能测试，定位功能设置
     *      9.11 发现若开启autoNotifyMode将会屏蔽掉自动扫描功能，只有当位置发生变化才会执行回调函数
     */
    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        LocationClient locationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        LocationClientOption locationOption = new LocationClientOption();
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType("bd09ll");
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(8000);
        //可选，设置是否需要地址信息，默认不需要
        locationOption.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true);
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(false);
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.setLocationNotify(true);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false);
        //可选，默认false，设置是否开启Gps定位
        locationOption.setOpenGps(true);
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false);

        /* 9.11 标注开启下面的自动回调模式之后定时扫描位置功能关闭，只有当位置发生变化是才会执行listener中的回调函数 */
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode();
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000,3, LocationClientOption.LOC_SENSITIVITY_HIGHT);


        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
        //开始定位
        locationClient.start();
    }

    private void buttonInit() {

    }

    /**
     * OKHttp post发送数据测试,测试未成功，因为公司电脑的android模拟器无法访问本机localhost
     *
     */
    private void okhttpSend() {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder().add("data1", "test1")
                    .add("data2", "test2").build();
            Request request = new Request.Builder().url("http://10.0.2.2:64475/Test/okhttp").post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            if(response.isSuccessful()) {
                Log.i("OKHttp", "发送成功");
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
