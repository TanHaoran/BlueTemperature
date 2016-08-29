package com.jerry.bluetemperature.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.app.MyApplication;
import com.jerry.bluetemperature.util.ThresholdUtil;
import com.jerry.bluetemperature.view.SelectBox;
import com.jerry.bluetemperature.view.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

/**
 * 报警温度设置
 */
@ContentView(R.layout.activity_threshold)
public class ThresholdActivity extends AppCompatActivity {


    @ViewInject(R.id.titlebar)
    private TitleBar mTitleBar;

    @ViewInject(R.id.selectbox_super)
    private SelectBox mSuperBox;
    @ViewInject(R.id.selectbox_high)
    private SelectBox mHighBox;
    @ViewInject(R.id.selectbox_mid)
    private SelectBox mMidBox;
    @ViewInject(R.id.selectbox_low)
    private SelectBox mLowBox;

    private SelectBox[] mFeverBoxes;

    /**
     * 报警温度等级
     */
    private int mLevel = ThresholdUtil.FEVER_LOW;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        initData();
        initView();

    }

    private void initData() {
        mLevel = ThresholdUtil.getInstance(this).getLevel();
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
        mFeverBoxes = new SelectBox[]{mSuperBox, mHighBox, mMidBox, mLowBox};
        setState(mLevel);
    }


    @Event({R.id.selectbox_super, R.id.selectbox_high, R.id.selectbox_mid, R.id.selectbox_low})
    private void onSelect(View v) {
        int index = ThresholdUtil.FEVER_HIGH;
        switch (v.getId()) {
            // 超高热
            case R.id.selectbox_super:
                index = ThresholdUtil.FEVER_SUPER;
                break;
            // 高热
            case R.id.selectbox_high:
                index = ThresholdUtil.FEVER_HIGH;
                break;
            // 中热
            case R.id.selectbox_mid:
                index = ThresholdUtil.FEVER_MID;
                break;
            // 低热
            case R.id.selectbox_low:
                index = ThresholdUtil.FEVER_LOW;
                break;
        }
        // 设置图标状态
        setState(index);
        // 保存到SharedPreferences中
        ThresholdUtil.getInstance(this).saveLevel(index);
    }


    /**
     * 根据序号设置目前选中状态
     *
     * @param index 序号
     */
    public void setState(int index) {
        for (int i = 0; i < mFeverBoxes.length; i++) {
            if (i == index) {
                mFeverBoxes[i].setChecked(true);
            } else {
                mFeverBoxes[i].setChecked(false);
            }
        }
    }
}
