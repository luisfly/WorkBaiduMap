package com.example.workbaidumap;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

/**
 * 登录页面设计
 */
public class LoginActivity extends AppCompatActivity {
    private Button submit = null;
    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submit = (Button) findViewById(R.id.submit);
        webView = (WebView) findViewById(R.id.webshow);

        webView.loadUrl("www.baidu.com");

        submit.setOnClickListener((View v)->{
            Intent intent = new Intent(LoginActivity.this, StartPageActivity.class);
            startActivity(intent);
        });
    }
}
