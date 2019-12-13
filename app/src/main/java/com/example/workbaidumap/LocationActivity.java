package com.example.workbaidumap;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.example.CommonTools.HttpUtils;
import com.example.FitEntity.HttpMessageObject;
import com.example.FitEntity.Location;
import com.example.FitEntity.TruckTask;

import java.util.ArrayList;
import java.util.List;

/**
 * 位置展示，任务跟踪时展示装车单号的行驶路线
 */
public class LocationActivity extends AppCompatActivity {
    private BaiduMap mBaiduMap;
    private MapView mMapView;
    private Polyline mPolyline;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0: {
                    drawRoute((List<LatLng>) msg.obj);
                }break;
                default:break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全透明沉浸式状态栏
        setStatusBarFullTransparent();

        /* 百度地图初始化，必须要有 */
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);

        setContentView(R.layout.activity_location);

        // 地图控件获取
        mMapView = (MapView) findViewById(R.id.baidumap);
        mBaiduMap = mMapView.getMap();

        initRoute();

    }

    /**
     * 获取指定装车单号的路线
     **/
    private void initRoute() {
        new Thread(()->{
            List<LatLng> locations = new ArrayList<>();
            TruckTask truckTask = (TruckTask) getIntent().getSerializableExtra("PaperNO");
            List<HttpMessageObject> res = HttpUtils.GetData("@Get_APaperRoute", truckTask, Location.class);
            for(HttpMessageObject loc : res) {
                Location change = (Location) loc;
                LatLng data = new LatLng(change.getLatitude(), change.getLongitude());
                locations.add(data);
                Log.d("initRoute", change.toString());
            }

            Message message = new Message();

            message.what = 0;
            message.obj = locations;

            handler.sendMessage(message);

        }).start();
    }

    /**
     * 行驶路线绘制方法
     */
    private void drawRoute(List<LatLng> loc) {
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.loc_point);

        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions startPoint = new MarkerOptions()
                .position(loc.get(0))
                .icon(bitmap);

        OverlayOptions endPoint = new MarkerOptions()
                .position(loc.get(loc.size() - 1))
                .icon(bitmap);

        // 添加起点终点
        Marker markerStart = (Marker) mBaiduMap.addOverlay(startPoint);
        Marker markerEnd = (Marker) mBaiduMap.addOverlay(endPoint);

        // 线的属性定义,以及绘制的路线导入
        OverlayOptions ooPolyline = new PolylineOptions().width(10).color(0xAAFF0000).dottedLine(false).points(loc);
        // 添加在地图中
        mPolyline = (Polyline) mBaiduMap.addOverlay(ooPolyline);
        /* 移动显示位置 */
        LatLng ll = loc.get(0);
        /* 设定新的位置 */
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
        /* 移动地图位置以及调整当前缩放 */
        mBaiduMap.animateMapStatus(update);
        update = MapStatusUpdateFactory.zoomTo(16f);
        /* 非初次定位不会将位置移动到当前位置 */
        mBaiduMap.animateMapStatus(update);

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
}
