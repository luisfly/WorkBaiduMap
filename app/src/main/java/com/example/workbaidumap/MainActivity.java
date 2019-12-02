package com.example.workbaidumap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * 2019.9.10 (1) 出现问题时绘制百度地图时，地图绘制失败，先是使用旧项目中的 LBS压缩文件以及jniLibs文件
 *               后改为重新在百度官网上下载基础包后，重新测试，地图绘制成功
 *      9.12 (1) 暂时不使用百度鹰眼路程绘制，由于鹰眼存在使用次数限制，现改手动编程在地图上绘制实现
 *           (2) 现在存在问题，在模拟器上测试时，无法获得定位，即使使用网络定位，在真机上测试时，
 *               获取了定位权限之后，gps无法自动打开，还是必须要用户手动，开启定位
 *      9.16 (1) 完成路线绘制功能，但是实测时发现app退到后台后，定位停止获取
 *      9.17 (1) 完成后台自动定位功能
 *      10.10 (1) 测试网页打开app成功，还未测试数据发送
 *      10.17 (1) 目前问题位置初始化，第一次启动的时候，有时候会失败
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
    /* 侧滑选项布局 */
    private NavigationView navigationView = null;
    /* 今日行程绘制 放到nav中 */
    //private Button tRoad = null;
    /* 发送数据  放到nav中*/
    //private Button post = null;
    /* 后台定位 */
    private NotificationUtils mNotificationUtils;
    private Notification notification;
    /* 权限获取 */
    private List<String> permissionList = new ArrayList<>();
    /* 地图初始化的时候，判定是否为初次定位，是否需要移动地图 */
    private boolean isFirstLocate = true;
    private int count = 0;
    // 司机信息
    private String driverNO = null;
    private String driverName = null;

    /* 当前位置记录 */
    private List<LatLng> nowLoc = new ArrayList<>();

    /* 测试数据 */
    private boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全透明沉浸式状态栏
        setStatusBarFullTransparent();

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
        // 侧滑选项布局获取
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // 路径绘制 放到了navigation view中
        // tRoad = (Button) findViewById(R.id.tRoad);
        // okhttp发送数据
        // post = (Button) findViewById(R.id.post) ;


        // 显示普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        // 开启定位绘制功能
        mBaiduMap.setMyLocationEnabled(true);

        Driver driver = (Driver) getIntent().getSerializableExtra("Driver");
        driverNO = driver.getDriverNO();
        driverName = driver.getDriverName();
        Log.d("司机信息", driver.getDriverName() + " : " + driver.getDriverNO() + " : " + driver.getPassword());

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

        // 侧滑选项选择监听器
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 点击路线绘制按钮
                switch (item.getItemId()) {
                    case R.id.tRoad:{
                        // 测试数据添加
                        //testAdd();
                        // 路径绘制
                        drawRoad();
                    }
                    break;
                    case R.id.offMap:{
                        // 离线地图下载
                    }
                    break;
                    case R.id.post:{
                        // 数据发送测试
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                HttpUtils.okhttpSend();
                                // HttpUtils.LocSend(24.66,25.11, driver.getDriverNO());
                            }
                        }).start();
                        //mBaiduMap.clear();
                        //clicked = false;
                    }
                    break;
                    case R.id.shop: {
                        Intent intent = new Intent(MainActivity.this, WebShopActivity.class);
                        startActivity(intent);
                    }
                    break;
                    case R.id.originLoc: {
                        Intent intent = new Intent(MainActivity.this, NotAPILocationActivity.class);
                        startActivity(intent);
                    }
                    break;
                    // 我的任务
                    case R.id.task: {
                        Intent intent = new Intent(MainActivity.this, TaskActivity.class);
                        intent.putExtra("Driver", driver);
                        startActivity(intent);
                    }
                    break;
                    // 发车
                    case R.id.startTra: {
                        Intent intent = new Intent(MainActivity.this, StartTransationActivity.class);
                        intent.putExtra("Driver", driver);
                        startActivity(intent);
                    }
                    break;
                    // 交货
                    case R.id.EndTra: {
                        Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
                        intent.putExtra("Driver", driver);
                        startActivity(intent);
                    }
                    break;
                    // 跟踪
                    case R.id.LoadFollow: {
                        Intent intent = new Intent(MainActivity.this, LoadTrackActivity.class);
                        intent.putExtra("Driver", driver);
                        startActivity(intent);
                    }
                    break;
                    default:
                }
                return false;
            }
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

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时必须调用mMapView. onResume ()
        mMapView.onResume();
        // 9.11 修改在activity创建完成后再执行这个定位功能实现，否则手机启动时，有时候无法执行成功
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
     * 因为 android 不支持在主线程外对控件进行绘制，所以暂时将监听器作为内部类写在主线程内
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
            Log.d("BDLocation时间", location.getTime());
            // Toast.makeText(MainActivity.this, "BDLocation时间" + location.getTime(), Toast.LENGTH_SHORT).show();

            // System.out.println("纬度：" + latitude + " 经度：" + longitude + " 错误码：" + errorCode);

            // 调用绘制方法
            drawThePosition(latitude, longitude);

            // 将当前位置存储在数组中,当点过多时必定会出现内存溢出情况
            // 必须要有去除无用点的机制
            LatLng now = new LatLng(latitude, longitude);
            // 初始化失败的点不加入绘制路径的殿中
            if (latitude != 4.9E-324 && longitude != 4.9E-324) {
                // 发送定位信息到后台,必须新建线程发送
                new Thread(()->{ HttpUtils.LocSend(latitude, longitude, driverNO);}).start();
                // 记录当前位置
                // nowLoc.add(now);
            }
        }
    }

    /**
     * 将定位信息绘制在地图上，在初次定位时将移动地图并且调整地图缩放
     * 当初始化获取点出现 4.9E-324 情况时，将不绘制当前位置点
     * @param latitude 纬度信息
     * @param longitude 经度信息
     */
    private void drawThePosition(double latitude ,double longitude) {
        // 如果获取到的点，是大西洋就不绘制点
        if (latitude != 4.9E-324 && longitude != 4.9E-324) {
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
                // 当定位位置不为大西洋的时候才算是初始化定位视图成功
                isFirstLocate = false;
            }

            // 单纯计数值，到时候应该要删除
            // count++;
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
        // 演示测试区域
        /*if (!clicked) {
            //********************************************
            //构建Marker图标
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.loc_point);

            //构建MarkerOption，用于在地图上添加Marker
            OverlayOptions startPoint = new MarkerOptions()
                    .position(nowLoc.get(0))
                    .icon(bitmap);

            OverlayOptions endPoint = new MarkerOptions()
                    .position(nowLoc.get(nowLoc.size() - 1))
                    .icon(bitmap);

            Marker markerStart = (Marker) mBaiduMap.addOverlay(startPoint);
            Marker markerEnd = (Marker) mBaiduMap.addOverlay(endPoint);

            Bundle sBundle = new Bundle();
            sBundle.putInt("id", 1);
            Bundle eBundle = new Bundle();
            eBundle.putInt("id", 2);


            markerEnd.setExtraInfo(eBundle);
            markerStart.setExtraInfo(sBundle);

            mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    Bundle bundle = marker.getExtraInfo();
                    int id = bundle.getInt("id");

                    switch (id) {
                        case 1: {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle("锐志诚达电子科技有限公司");
                            dialog.setMessage("地点:佛山市南海区桂城夏南二银富写字楼203室\n出发时间: 2019.11.28 11:28:07");
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInner, int which) {
                                    //dialog.
                                    dialogInner.dismiss();
                                }
                            });
                            dialog.show();
                        }
                        break;
                        case 2: {
                            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                            dialog.setTitle("沙园地铁站口");
                            dialog.setMessage("地点:广州市海珠区工业大道北\n到达时间: 2019.11.28 12:18:23");
                            dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInner, int which) {
                                    //dialog.
                                    dialogInner.dismiss();
                                }
                            });
                            dialog.show();
                        }
                        break;
                        default:
                            break;
                    }

                    return false;
                }
            });
        }*/


        // ********************************************
        if(nowLoc.size() >= 2 && !clicked) {
            // 线的属性定义
            OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).dottedLine(false).points(nowLoc);
            // 添加在地图中
            mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
            clicked = true;
            /* 移动显示位置 */
            LatLng ll = nowLoc.get(0);
            /* 设定新的位置 */
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
            /* 移动地图位置以及调整当前缩放 */
            mBaiduMap.animateMapStatus(update);
            update = MapStatusUpdateFactory.zoomTo(16f);
            /* 非初次定位不会将位置移动到当前位置 */
            mBaiduMap.animateMapStatus(update);
        } else {
            //Log.e("DrawRoad", "路径绘制的点必须要大于两个");
        }
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

    /**
     * 控件初始化函数，未来当整个活动布局完成后，所有控件初始化工作放在这里
     */
     private void buttonInit() {

    }

    /**
     * 全透明沉浸式状态栏方法
     * 注意：方法要放在 super.onCreate 后，还有是 setContentView 前
     * 并且要在本活动布局文件根布局中增添 android:fitsSystemWindows="false"
     */
    private void setStatusBarFullTransparent() {
        // 21版本 Android 5.0,4.4以下不支持沉浸试共嗯
        if (Build.VERSION.SDK_INT >= 21){
            Window window = getWindow();
            View decorView = window.getDecorView();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // 注意 SYSTEM_UI_FLAG_LIGHT_STATUS_BAR , SYSTEM_UI_FLAG_LAYOUT_STABLE 影响装填栏字体，stable为浅色 bar 为深色
            // decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //19表示4.4
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    /*private void testAdd() {
        nowLoc.add(new LatLng(23.058495,113.183836));
        nowLoc.add(new LatLng(23.058495,113.178985));
        nowLoc.add(new LatLng(23.059382,113.173406));
        nowLoc.add(new LatLng(23.060908,113.166045));
        nowLoc.add(new LatLng(23.060762,113.162752));
        nowLoc.add(new LatLng(23.061336,113.161984));
        nowLoc.add(new LatLng(23.071004,113.161922));
        nowLoc.add(new LatLng(23.072799,113.164077));
        nowLoc.add(new LatLng(23.073289,113.166009));
        nowLoc.add(new LatLng(23.073148,113.17421));
        nowLoc.add(new LatLng(23.071951,113.189086));
        nowLoc.add(new LatLng(23.071851,113.199147));
        nowLoc.add(new LatLng(23.071918,113.219233));
        nowLoc.add(new LatLng(23.070721,113.230983));
        nowLoc.add(new LatLng(23.071386,113.238457));
        nowLoc.add(new LatLng(23.071984,113.240541));
        nowLoc.add(new LatLng(23.078501,113.246362));
        nowLoc.add(new LatLng(23.082823,113.251177));
        nowLoc.add(new LatLng(23.082757,113.251177));
        nowLoc.add(new LatLng(23.08914,113.253261));
        nowLoc.add(new LatLng(23.090669,113.25398));
        nowLoc.add(new LatLng(23.095223,113.260951));
        nowLoc.add(new LatLng(23.0961,113.262819));
        nowLoc.add(new LatLng(23.096482,113.264939));
        nowLoc.add(new LatLng(23.096174,113.265945));
        nowLoc.add(new LatLng(23.094855,113.267032));
    }*/

    /**
     * OKHttp post发送数据测试,测试未成功，因为公司电脑的android模拟器无法访问本机localhost
     * 10.15 关于app网路请求方法已经转移到httpUtils
     */
    /*private void okhttpSend() {
        try {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder().add("data1", "test1")
                    .add("data2", "test2").build();
            // post方法
            Request request = new Request.Builder().url("http://10.0.2.2:64475/Test/okhttp").post(requestBody)
                    .build();
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
    }*/
}
