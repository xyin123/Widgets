package com.xyin.widgetlib;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.xyin.widgetlib.sample.CodeActivity;
import com.xyin.widgetlib.widgetlib.VerifyCodeView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void toCodeView(View view) {
        to(CodeActivity.class);
    }

    private void to(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }


}