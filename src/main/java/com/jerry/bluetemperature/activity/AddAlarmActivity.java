package com.jerry.bluetemperature.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.adapter.TimeWheelAdapter;
import com.jerry.bluetemperature.app.MyApplication;
import com.jerry.bluetemperature.entity.Alarm;
import com.jerry.bluetemperature.util.DensityUtils;
import com.jerry.bluetemperature.view.TitleBar;
import com.wx.wheelview.widget.WheelView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加闹钟
 */
@ContentView(R.layout.activity_add_alarm)
public class AddAlarmActivity extends AppCompatActivity {

    private static final int DEFAULT_TEXT_MARGIN_LEFT = 45;


    @ViewInject(R.id.titlebar)
    private TitleBar mTitleBar;

    @ViewInject(R.id.wheel_meridiem)
    private WheelView mWheelMeridiem;

    @ViewInject(R.id.wheel_hour)
    private WheelView mWheelHour;

    @ViewInject(R.id.wheel_minute)
    private WheelView mWheelMinute;

    @ViewInject(R.id.cb_monday)
    private CheckBox mMonday;

    @ViewInject(R.id.cb_tuesday)
    private CheckBox mTuesday;

    @ViewInject(R.id.cb_wednesday)
    private CheckBox mWednesday;

    @ViewInject(R.id.cb_thursday)
    private CheckBox mThursday;

    @ViewInject(R.id.cb_friday)
    private CheckBox mFriday;

    @ViewInject(R.id.cb_saturday)
    private CheckBox mSaturday;

    @ViewInject(R.id.cb_sunday)
    private CheckBox mSunday;


    private List<String> meridiem = new ArrayList<>();
    private List<String> hours = new ArrayList<>();
    private List<String> minutes = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        // 初始化时间数据
        initData();
        initView();
    }

    /**
     * 初始化时间数据
     */
    private void initData() {
        // 初始化上午、下午
        meridiem.add("上");
        meridiem.add("下");
        // 初始化小时
        for (int i = 1; i <= 12; i++) {
            hours.add(String.valueOf(i));
        }
        // 初始化分钟
        for (int i = 0; i <= 59; i++) {
            minutes.add(String.valueOf(i));
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
                // 保存闹铃，并退出
                saveAlarm();
                finish();
            }

        });


        // 上午、下午
        mWheelMeridiem.setLoop(false);
        mWheelMeridiem.setWheelSize(3);
        mWheelMeridiem.setWheelData(meridiem);

        // 小时
        mWheelHour.setLoop(true);
        mWheelHour.setWheelSize(7);
        mWheelHour.setWheelData(hours);

        // 分钟
        mWheelMinute.setLoop(true);
        mWheelMinute.setWheelSize(7);
        mWheelMinute.setWheelData(minutes);

        initWheel(mWheelMeridiem, "午");
        initWheel(mWheelHour, "时");
        initWheel(mWheelMinute, "分");
    }


    /**
     * 保存闹铃
     */
    private void saveAlarm() {
        Alarm alarm = generateAlarm();
    }

    /**
     * 根据选择的日期生成一个Alarm实体
     * @return
     */
    private Alarm generateAlarm() {
        Alarm alarm = new Alarm();
        return null;
    }

    /**
     * 设置wheel的样式和数据
     *
     * @param wheel
     * @param extraText
     */
    private void initWheel(WheelView wheel, String extraText) {
        // 附加字的大小
        int extraTextSize = DensityUtils.sp2px(this, 14);
        // Wheel控件的一个bug，无法直接使用白色，所以这里全部使用0xfffffffe来代替白色
        int white = 0xfffffffe;

        wheel.setWheelAdapter(new TimeWheelAdapter(this));
        wheel.setSkin(WheelView.Skin.Holo);
        wheel.setExtraText(extraText, white, extraTextSize, DEFAULT_TEXT_MARGIN_LEFT);

        // 设置选中背景样式
        WheelView.WheelViewStyle style = new WheelView.WheelViewStyle();
        // 设置边框的颜色
        style.holoBorderColor = 0x66aedfff;
        style.textSize = 18;
        style.selectedTextSize = 20;
        style.backgroundColor = Color.TRANSPARENT;
        style.textColor = 0xffaedfff;
        style.selectedTextColor = white;
        wheel.setStyle(style);
    }
}
