package com.jerry.bluetemperature.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.app.MyApplication;
import com.jerry.bluetemperature.view.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

@ContentView(R.layout.activity_instructions)
public class InstructionsActivity extends AppCompatActivity {


    @ViewInject(R.id.titlebar)
    private TitleBar mTitleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        initView();
    }

    private void initView() {
        // 标题点击事件
        mTitleBar.setOnTitlebarClickListener(new TitleBar.OnTitlebarOnClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {

            }
        });
    }
}
