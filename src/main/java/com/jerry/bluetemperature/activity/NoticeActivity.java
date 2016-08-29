package com.jerry.bluetemperature.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.app.MyApplication;
import com.jerry.bluetemperature.view.SelectBox;
import com.jerry.bluetemperature.view.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 通知类型设置
 */
@ContentView(R.layout.activity_notice)
public class NoticeActivity extends AppCompatActivity {

    @ViewInject(R.id.titlebar)
    private TitleBar mTitleBar;


    @ViewInject(R.id.selectbox_vibration)
    private SelectBox mVibrationBox;
    @ViewInject(R.id.selectbox_sound)
    private SelectBox mSoundBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        initView();
    }

    private void initView() {
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

    @Event({R.id.selectbox_vibration, R.id.selectbox_sound})
    private void onSelect(View v) {
        if (v.getId() == R.id.selectbox_vibration) {
            mVibrationBox.setChecked(true);
            mSoundBox.setChecked(false);
        } else {
            mVibrationBox.setChecked(false);
            mSoundBox.setChecked(true);
        }
    }
}
