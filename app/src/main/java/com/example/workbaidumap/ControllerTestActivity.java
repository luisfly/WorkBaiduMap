package com.example.workbaidumap;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.FitEntity.HttpMessageObject;
import com.example.FitEntity.TruckTask;
import com.example.control.CustomBottomSheetDialogForWebView;
import com.example.control.NerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * 控件测试
 */
public class ControllerTestActivity extends AppCompatActivity {
    private List<HttpMessageObject> testData = new ArrayList<>();
    private Button test_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller_test);

        test_dialog = (Button) findViewById(R.id.test_dialog);
        setTestList();
        test_dialog.setOnClickListener((View v)->{
            CustomBottomSheetDialogForWebView test =
                    new CustomBottomSheetDialogForWebView(ControllerTestActivity.this, testData);
            test.show();
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
}
