package com.example.workbaidumap;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.location.LocationManagerCompat;

import java.io.IOException;
import java.util.List;

/**
 * 测试 android sdk 原生定位功能，测试成功，可以正常定位
 * 发现问题原生的网络定位以及地理位置编码，若无法访问外网则无法使用
 */
public class NotAPILocationActivity extends AppCompatActivity {
    private LocationManager locationManager;
    private LocationListener myLocationListener;
    private TextView loc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_apilocation);
        //1.获取位置的管理者
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //2.获取定位方式
        //2.1获取所有的定位方式，true:表示返回所有可用定位方式
        List<String> providers = locationManager.getProviders(true);
        for (String string : providers) {
            System.out.println(string);
        }
        //2.2获取最佳的定位方式
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);//设置是否可以定位海拔,如果设置定位海拔，返回一定是gps
        //criteria : 设置定位属性
        //enabledOnly : true如果定位可用就返回
        String bestProvider = locationManager.getBestProvider(criteria, false);
        System.out.println("最佳的定位方式:" + bestProvider);
        Toast.makeText(this, "最佳的定位方式:" + bestProvider, Toast.LENGTH_SHORT).show();
        //3.定位
        myLocationListener = new MyLocationListener();

        // 权限询问
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.

                String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                ActivityCompat.requestPermissions(NotAPILocationActivity.this, permissions, 1);
                //return;
            }
        }
        //provider : 定位的方式
        //minTime : 定位的最小时间间隔
        //minDistance : 定位最小的间隔距离
        //LocationListener : 定位监听
        // 这个网络定位方法必须要能访问外网才能使用，还有那个地理位置编码转化也是需要连接访问外网才能使用
        //locationManager.requestLocationUpdates("network", 5000, 0, myLocationListener);
        locationManager.requestLocationUpdates("gps", 5000, 0, myLocationListener);

    }

    private class MyLocationListener implements LocationListener {
        private String latLongString;

        //当定位位置改变的调用的方法
        //Location : 当前的位置
        @Override
        public void onLocationChanged(Location location) {
            final double latitude = location.getLatitude();//获取纬度，平行
            final double longitude = location.getLongitude();//获取经度，垂直
            Toast.makeText(NotAPILocationActivity.this, "longitude:" + longitude + "  latitude:" + latitude, Toast.LENGTH_SHORT).show();
            Log.e("打印经纬度：", "longitude:" + longitude + "  latitude:" + latitude);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Address> addsList = null;
                    Geocoder geocoder = new Geocoder(NotAPILocationActivity.this);
                    try {
                        addsList = geocoder.getFromLocation(latitude, longitude, 10);//得到的位置可能有多个当前只取其中一个
                        Log.e("打印拿到的城市", addsList.toString());
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    if (addsList != null && addsList.size() > 0) {
                        for (int i = 0; i < addsList.size(); i++) {
                            final Address ads = addsList.get(i);
                            latLongString = ads.getLocality();//拿到城市
//                            latLongString = ads.getAddressLine(0);//拿到地址
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("打印拿到的城市的地址", latLongString + ads.getAddressLine(0) + ads.getAddressLine(1) + ads.getAddressLine(4));
                                    //locationChange.setText(latLongString + ads.getAddressLine(0) + ads.getAddressLine(1));
                                    Toast.makeText(NotAPILocationActivity.this, "当前所在的城市为" + latLongString + ads.getAddressLine(0) + ads.getAddressLine(4) + ads.getAddressLine(1), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }).start();
        }

        // 定位状态改变时调用的方法
        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        // 定位可用时调用的方法
        @Override
        public void onProviderEnabled(String s) {

        }

        // 定位不可用时调用的方法
        @Override
        public void onProviderDisabled(String s) {

        }
    }
}
