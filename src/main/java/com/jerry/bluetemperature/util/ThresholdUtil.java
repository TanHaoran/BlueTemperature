package com.jerry.bluetemperature.util;

import android.content.Context;

/**
 * Created by Jerry on 2016/7/12.
 */
public class ThresholdUtil {

    /**
     * 最低限
     */
    public static final String THRESHOLD_LOW = "threshold_low";
    /**
     * 最高限
     */
    public static final String THRESHOLD_HIGH = "threshold_high";
    /**
     * 限制范围
     */
    public static final String THRESHOLD_MESSAGE = "threshold_message";

    /**
     * 超高热
     */
    public static final float SUPER_HIGH = 100;
    public static final float SUPER_LOW = 41.1f;
    public static final String SUPER_MSG = "超高热";

    /**
     * 高热
     */
    public static final float HIGH_HIGH = 41.0f;
    public static final float HIGH_LOW = 39.1f;
    public static final String HIGH_MSG = "高热";

    /**
     * 中热
     */
    public static final float MID_HIGH = 39.0f;
    public static final float MID_LOW = 38.1f;
    public static final String MID_MSG = "中热";

    /**
     * 低热
     */
    public static final float LOW_HIGH = 38.0f;
    public static final float LOW_LOW = 37.1f;
    public static final String LOW_MSG = "低热";


    /**
     * 超高热
     */
    public static final int FEVER_SUPER = 0;
    /**
     * 高热
     */
    public static final int FEVER_HIGH = 1;
    /**
     * 中热
     */
    public static final int FEVER_MID = 2;
    /**
     * 低热
     */
    public static final int FEVER_LOW = 3;

    private Context context;

    private OnAlarmListener mAlarmListener;

    private static ThresholdUtil instance;


    public interface OnAlarmListener {
        void onAlarm(String msg);
    }

    private ThresholdUtil(Context context) {
        this.context = context;
    }

    public static ThresholdUtil getInstance(Context context) {
        if (instance == null) {
            synchronized (ThresholdUtil.class) {
                if (instance == null) {
                    instance = new ThresholdUtil(context);
                }
            }
        }
        return instance;
    }


    /**
     * 保存到SharedPreferences中
     *
     * @param index
     */
    public void saveLevel(int index) {
        float low = 0;
        float high = 0;
        String msg = "";
        switch (index) {
            case FEVER_SUPER:
                high = SUPER_HIGH;
                low = SUPER_LOW;
                msg = SUPER_MSG;
                break;
            case FEVER_HIGH:
                high = HIGH_HIGH;
                low = HIGH_LOW;
                msg = HIGH_MSG;
                break;
            case FEVER_MID:
                high = MID_HIGH;
                low = MID_LOW;
                msg = MID_MSG;
                break;
            case FEVER_LOW:
                high = LOW_HIGH;
                low = LOW_LOW;
                msg = LOW_MSG;
                break;
        }
        SPUtils.put(context, THRESHOLD_LOW, low);
        SPUtils.put(context, THRESHOLD_HIGH, high);
        SPUtils.put(context, THRESHOLD_MESSAGE, msg);
    }

    /**
     * 获取目前保存的报警温度信息
     *
     * @return
     */
    public int getLevel() {
        String msg = (String) SPUtils.get(context, THRESHOLD_MESSAGE, LOW_MSG);
        int level;
        if (msg.equals(SUPER_MSG)) {
            level = FEVER_SUPER;
        } else if (msg.equals(HIGH_MSG)) {
            level = FEVER_HIGH;
        } else if (msg.equals(MID_MSG)) {
            level = FEVER_MID;
        } else {
            level = FEVER_LOW;
        }
        return level;
    }

    /**
     * 检测体温是否需要报警
     *
     * @param value
     */
    public void check(float value, OnAlarmListener onAlarmListener) {
        mAlarmListener = onAlarmListener;
        float low = (float) SPUtils.get(context, ThresholdUtil.THRESHOLD_LOW, ThresholdUtil.LOW_LOW);
        String msg = (String) SPUtils.get(context, ThresholdUtil.THRESHOLD_MESSAGE, "体温异常");
        if (value >= low) {
            if (mAlarmListener != null) {
                mAlarmListener.onAlarm(msg);
            }
        }

    }
}
