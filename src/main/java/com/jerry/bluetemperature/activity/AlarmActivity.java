package com.jerry.bluetemperature.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.adapter.AlarmAdapter;
import com.jerry.bluetemperature.app.MyApplication;
import com.jerry.bluetemperature.entity.Alarm;
import com.jerry.bluetemperature.util.DensityUtils;
import com.jerry.bluetemperature.view.BaseSwipeListViewListener;
import com.jerry.bluetemperature.view.SwipeListView;
import com.jerry.bluetemperature.view.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 测量闹钟设置
 */
@ContentView(R.layout.activity_alarm)
public class AlarmActivity extends AppCompatActivity {

    private static final int DEFAULT_BTN_WIDHT = 74; //dp

    @ViewInject(R.id.titlebar)
    private TitleBar mTitleBar;

    @ViewInject(R.id.swipelistview)
    private SwipeListView mSwipeListView;

    private AlarmAdapter mAdapter;
    private List<Alarm> mDatas = new ArrayList<>();

    /**
     * 目前侧滑打开的序号
     */
    private int mOpenPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);

        initData();
        initView();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        for (int i = 0; i <= 100; i++) {
            mDatas.add(new Alarm());
        }
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

        mAdapter = new AlarmAdapter(this, mDatas, R.layout.item_alarm, mSwipeListView);

        // 根据屏幕的宽度来确定删除按钮的宽度
        int screenWidth = DensityUtils.getScreenWidth(this);
        mSwipeListView.setOffsetLeft(screenWidth - DensityUtils.dp2px(this, DEFAULT_BTN_WIDHT));
        mSwipeListView.setAdapter(mAdapter);

        mSwipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {

            @Override
            public void onClickFrontView(int position) {
                if (position != mOpenPosition) {
                    mSwipeListView.closeOpenedItems();
                }
            }


            @Override
            public void onListChanged() {
                mSwipeListView.closeOpenedItems();
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
                mOpenPosition = position;
            }
        });


    }

    @Event(R.id.tv_add_alarm)
    private void onAddAlarm(View v) {
        Intent intent = new Intent(this, AddAlarmActivity.class);
        startActivity(intent);
    }
}
