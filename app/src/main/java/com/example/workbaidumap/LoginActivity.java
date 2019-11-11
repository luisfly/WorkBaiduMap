package com.example.workbaidumap;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 登录页面设计
 */
public class LoginActivity extends AppCompatActivity {
    private Button submit = null;
    private WebView webView = null;
    private EditText username = null;
    private EditText password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submit = (Button) findViewById(R.id.submit);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);

        submit.setOnClickListener((View v)->{
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            new Thread(()->{
                HttpUtils.DriverIdentifly(new Driver(username.getText().toString(), password.getText().toString()));
            }).start();
        });
    }
}
