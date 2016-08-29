package com.jerry.bluetemperature.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.app.MyApplication;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * 设置
 */
@ContentView(R.layout.activity_setting)
public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
    }

    /**
     * 选择设备
     *
     * @param v
     */
    @Event(R.id.ll_device)
    private void onDevice(View v) {
        Intent intent = new Intent(this, DeviceActivity.class);
        startActivity(intent);
    }

    /**
     * 报警温度
     *
     * @param v
     */
    @Event(R.id.ll_threshold)
    private void onThreshold(View v) {
        Intent intent = new Intent(this, ThresholdActivity.class);
        startActivity(intent);
    }

    /**
     * 通知类型
     *
     * @param v
     */
    @Event(R.id.ll_notice)
    private void onNotice(View v) {
        Intent intent = new Intent(this, NoticeActivity.class);
        startActivity(intent);
    }


    /**
     * 测量闹钟
     *
     * @param v
     */
    @Event(R.id.ll_alarm)
    private void onAlarm(View v) {
//        Intent intent = new Intent(this, DeskClockMainActivity.class);
//        startActivity(intent);
        Intent alarmas = new Intent(AlarmClock.ACTION_SET_ALARM);
        startActivity(alarmas);
    }



    /**
     * 使用说明
     *
     * @param v
     */
    @Event(R.id.ll_instructions)
    private void onInstructions(View v) {
        Intent intent = new Intent(this, InstructionsActivity.class);
        startActivity(intent);
    }


    /**
     * 关于
     *
     * @param v
     */
    @Event(R.id.ll_about_us)
    private void onAbout(View v) {
        Intent intent = new Intent(this, AboutActivity.class);
        startActivity(intent);
    }

    /**
     * 关闭页面
     *
     * @param v
     */
    @Event(R.id.ll_close)
    private void onClose(View v) {
        finish();
    }


}
