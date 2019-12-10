package com.example.workbaidumap;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.FitEntity.HttpMessageObject;
import com.example.FitEntity.TruckTask;
import com.example.control.CustomBottomSheetDialogForWebView;
import com.example.control.NerItem;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * 控件测试
 */
public class ControllerTestActivity extends AppCompatActivity {
    private List<HttpMessageObject> testData = new ArrayList<>();
    private Button test_dialog;
    private QRCodeView mQRCodeView;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        setContentView(R.layout.activity_controller_test);

        test_dialog = (Button) findViewById(R.id.test_dialog);
        setTestList();
        test_dialog.setOnClickListener((View v)->{
            CustomBottomSheetDialogForWebView test =
                    new CustomBottomSheetDialogForWebView(ControllerTestActivity.this, testData);
            test.show();
        });


        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
        mQRCodeView.changeToScanQRCodeStyle(); //扫二维码
        mQRCodeView.setDelegate(new QRCodeView.Delegate() {

            @Override
            public void onScanQRCodeSuccess(String result) {
                Log.d("二维码扫描结果", "result:" + result);

                Toast.makeText(activity, result, Toast.LENGTH_LONG).show();

                //扫描得到结果震动一下表示
                vibrate();

                //获取结果后三秒后，重新开始扫描
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mQRCodeView.startSpot();
                    }
                }, 3000);
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
                Toast.makeText(activity, "打开相机错误！", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.start_spot).setOnClickListener(new View.OnClickListener() {
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
        });
    }

    /**
     * 测试用数据
     */
    private void setTestList() {
        for(int i = 0; i < 3; i++) {
            TruckTask t = new TruckTask();
            t.setTruckPaperNO("00101");
            t.setTruckLoadNO("11111");
            t.setDCNO("11001");
            testData.add(t);
        }
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
}
