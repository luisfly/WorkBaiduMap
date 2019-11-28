package com.example.workbaidumap;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * 登录页面设计
 */
public class LoginActivity extends AppCompatActivity {
    private Button submit = null;
    private WebView webView = null;
    private EditText username = null;
    private EditText password = null;

    // message 处理器
    private Handler handler = new Handler() {

        public void handleMessage(@NotNull Message msg) {
            switch (msg.what) {
                case 0:{
                    AlertDialog.Builder dialog = new AlertDialog.Builder (LoginActivity.this);
                    dialog.setTitle("登录失败");
                    dialog.setMessage("用户名或者密码有误");
                    //Log.e("");
                    // OK按钮的事件
                    dialog.setPositiveButton("OK", new DialogInterface. OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInner, int which) {
                            //dialog.
                        }
                    });
                    dialog.show();
                }
                break;
                case 1:{
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("Driver", (Driver) msg.obj);
                    startActivity(intent);
                }
                break;
                default:break;
            }
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 全透明沉浸式状态栏
        setStatusBarFullTransparent();

        setContentView(R.layout.activity_login);
        submit = (Button) findViewById(R.id.submit);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        submit.setOnClickListener((View v)->{
            new Thread(()->{
                //Driver driver = HttpUtils.DriverIdentifly(new Driver(username.getText().toString(), password.getText().toString()));
                Driver driver = new Driver(username.getText().toString(), password.getText().toString());
                Message message = new Message();
                List<HttpMessageObject> data = HttpUtils.GetData("Login", driver);
                /*if (data != null && data.size() != 0) {
                    message.obj = (Driver) HttpUtils.GetData("Login", driver).get(0);
                    message.what = 1;
                } else {
                    message.what = 0;
                }*/

                message.obj = new Driver("1000001","11451245","admin");
                message.what = 1;

                // 发送消息
                handler.sendMessage(message);
            }).start();
        });
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
