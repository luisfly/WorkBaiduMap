package com.example.workbaidumap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.baidu.mapapi.model.LatLng;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.Trace;
import com.baidu.trace.model.OnTraceListener;
import com.baidu.trace.model.PushMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * 2019.9.10 (1)出现问题时绘制百度地图时，地图绘制失败，先是使用旧项目中的 LBS压缩文件以及jniLibs文件
 *              后改为重新在百度官网上下载基础包后，重新测试，地图绘制成功
 *      9.12 (1)暂时不使用百度鹰眼路程绘制，由于鹰眼存在使用次数限制，现改手动编程在地图上绘制实现
 *           (2)现在存在问题，在模拟器上测试时，无法获得定位，即使使用网络定位，在真机上测试时，
 *              获取了定位权限之后，gps无法自动打开，还是必须要用户手动，开启定位
 */
public class MainActivity extends AppCompatActivity {

    /* 地图控件 */
    private MapView mMapView = null;
    /* 百度地图的实体 */
    private BaiduMap mBaiduMap = null;
    /* 权限获取 */
    List<String> permissionList = new ArrayList<>();
    /* 地图初始化的时候，判定是否为初次定位，是否需要移动地图 */
    private boolean isFirstLocate = true;

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

        // 显示普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位绘制功能
        mBaiduMap.setMyLocationEnabled(true);

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


        // 当前位置绘制启动,连续定位
        // 9.11 判断其位置错误，不应该在创建时执行，应该在整个app稳定之后去执行，即在onresume中实行
        //initLocationOption();

        // 9.11 鹰眼路径绘制功能测试，测试失败
        // 轨迹服务ID
        long serviceId = 0;
        // 设备标识
        String entityName = "myTrace";
        // 是否需要对象存储服务，默认为：false，关闭对象存储服务。注：鹰眼 Android SDK v3.0以上版本支持随轨迹上传图像等对象数据，若需使用此功能，该参数需设为 true，且需导入bos-android-sdk-1.0.2.jar。
        boolean isNeedObjectStorage = false;
        // 初始化轨迹服务
        Trace mTrace = new Trace(serviceId, entityName, isNeedObjectStorage);
        // 初始化轨迹服务客户端
        LBSTraceClient mTraceClient = new LBSTraceClient(getApplicationContext());
        // 定位周期(单位:秒)
        int gatherInterval = 5;
        // 打包回传周期(单位:秒)
        int packInterval = 10;
        // 设置定位和打包周期
        mTraceClient.setInterval(gatherInterval, packInterval);
        // 初始化轨迹服务监听器
        OnTraceListener mTraceListener = new OnTraceListener() {
            @Override
            public void onBindServiceCallback(int i, String s) {}

            // 开启服务回调
            @Override
            public void onStartTraceCallback(int status, String message) {}
            // 停止服务回调
            @Override
            public void onStopTraceCallback(int status, String message) {}
            // 开启采集回调
            @Override
            public void onStartGatherCallback(int status, String message) {}
            // 停止采集回调
            @Override
            public void onStopGatherCallback(int status, String message) {}
            // 推送回调
            @Override
            public void onPushCallback(byte messageNo, PushMessage message) {}

            @Override
            public void onInitBOSCallback(int i, String s) {}
        };
        // 开启服务
        mTraceClient.startTrace(mTrace, mTraceListener);
        // 开启采集
        mTraceClient.startGather(mTraceListener);
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

        //Toast.makeText(this, "纬度：" + latitude + " 经度：" + longitude, Toast.LENGTH_SHORT).show();
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
        locationOption.setScanSpan(5000);
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
        locationOption.setOpenAutoNotifyMode(3000,1, LocationClientOption.LOC_SENSITIVITY_HIGHT);


        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(locationOption);
        //开始定位
        locationClient.start();
    }

}
