package com.example.workbaidumap;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

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
        setContentView(R.layout.activity_login);
        submit = (Button) findViewById(R.id.submit);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        submit.setOnClickListener((View v)->{
            new Thread(()->{
                Driver driver = HttpUtils.DriverIdentifly(new Driver(username.getText().toString(), password.getText().toString()));
                Message message = new Message();
                // 根据返回值来判断登录是否成功
                if (driver != null) {
                    message.obj = driver;
                    message.what = 1;
                } else {
                    message.what = 0;
                }

                handler.sendMessage(message);
            }).start();
        });


    }
}
