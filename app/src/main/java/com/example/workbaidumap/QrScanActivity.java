package com.example.workbaidumap;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class QrScanActivity extends AppCompatActivity {
    private QRCodeView mQRCodeView;
    private Activity activity;
    private String res;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        activity = this;
        setContentView(R.layout.activity_qr_scan);

        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.changeToScanQRCodeStyle(); //扫二维码
        // 开始扫描
        mQRCodeView.startSpot();
        mQRCodeView.setDelegate(new QRCodeView.Delegate() {

            @Override
            public void onScanQRCodeSuccess(String result) {
                Log.d("二维码扫描结果", "result:" + result);
                res = result;

                Toast.makeText(activity, result, Toast.LENGTH_LONG).show();

                Intent intent = new Intent();
                intent.putExtra("data_return", result);
                setResult(RESULT_OK, intent);
                finish();

                //扫描得到结果震动一下表示
                //vibrate();

                //获取结果后三秒后，重新开始扫描
                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mQRCodeView.startSpot();
                    }
                }, 3000);*/
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
                Toast.makeText(activity, "打开相机错误！", Toast.LENGTH_SHORT).show();
            }
        });

        /*findViewById(R.id.start_spot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.startSpot();
                Toast.makeText(activity, "startSpot", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.stop_spot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.stopSpot();
                Toast.makeText(activity, "stopSpot", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.open_flashlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.openFlashlight();
                Toast.makeText(activity, "openFlashlight", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.close_flashlight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mQRCodeView.closeFlashlight();
                Toast.makeText(activity, "closeFlashlight", Toast.LENGTH_SHORT).show();
            }
        });*/

//        findViewById(R.id.scan_qrcode).setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                mQRCodeView.changeToScanQRCodeStyle();
//                Toast.makeText(activity,"changeToScanQRCodeStyle",Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        //强制手机摄像头镜头朝向前边
        //mQRCodeView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);

        mQRCodeView.showScanRect(); //显示扫描方框
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mQRCodeView.onDestroy();
        super.onDestroy();
    }

    //震动
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_scan);
    }*/

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
