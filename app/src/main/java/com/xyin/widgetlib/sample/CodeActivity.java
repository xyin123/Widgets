package com.xyin.widgetlib.sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xyin.widgetlib.R;
import com.xyin.widgetlib.widgetlib.VerifyCodeView;

public class CodeActivity extends AppCompatActivity {

    TextView tv;
    VerifyCodeView codeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code);

        tv = (TextView) findViewById(R.id.tv);
        codeView = (VerifyCodeView) findViewById(R.id.codeView);

        codeView.setOnCodeCompleteListener(new VerifyCodeView.OnCodeCompleteListener() {
            @Override
            public void onCodeComplete(String code) {
                tv.setText("验证码:" + code);
            }
        });

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeView.setCode("9876");
            }
        });

    }


}
