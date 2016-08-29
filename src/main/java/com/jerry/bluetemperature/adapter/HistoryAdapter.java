package com.jerry.bluetemperature.adapter;

import android.content.Context;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.entity.History;
import com.jerry.bluetemperature.util.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * Created by Jerry on 2016/7/5.
 */
public class HistoryAdapter extends CommonAdapter<History> {
    public HistoryAdapter(Context context, List<History> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, History item) {

        // 设置测试日期
        setDate(helper, item);
        // 设置最高温度
        setTemperature(helper, item);
        // 设置起始时间和持续时间
        setStartAndDuration(helper, item);
        // 设置体温状态
        setState(helper, item);
    }


    /**
     * 设置测试日期
     *
     * @param helper
     * @param item
     */
    private void setDate(ViewHolder helper, History item) {
        Date start = item.getStart();
        String md = DateUtil.getMD(start);
        helper.setText(R.id.tv_date, md);
    }

    /**
     * 设置最高温度
     *
     * @param helper
     * @param item
     */
    private void setTemperature(ViewHolder helper, History item) {
        String values = item.getValues();
        String[] ts = values.split(";");
        float max = getMaxValue(ts);
        helper.setText(R.id.tv_value, max + "°C");
    }

    /**
     * 获取体温值当中的最大值
     *
     * @param ts
     * @return
     */
    public static float getMaxValue(String[] ts) {
        float max = 0;
        for (String t : ts) {
            float value = Float.parseFloat(t);
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    /**
     * 设置起始时间和持续时间
     *
     * @param helper
     * @param item
     */
    private void setStartAndDuration(ViewHolder helper, History item) {
        Date start = item.getStart();
        Date end = item.getEnd();
        String s = DateUtil.getHM(start);
        String e = DateUtil.getHM(end);
        String duration = DateUtil.calculateDuration(start, end);
        helper.setText(R.id.tv_time, s + "~" + e);
        helper.setText(R.id.tv_duration, duration);
    }

    /**
     * 设置体温状态
     *
     * @param helper
     * @param item
     */
    private void setState(ViewHolder helper, History item) {
        String valus = item.getValues();
        float max = getMaxValue(valus.split(";"));
        String state = History.NORMAL;
        if (max < History.NORMAL_LOW) {
            state = History.LOWER;
            helper.getConvertView().setBackgroundResource(R.drawable.history_bg_green);
        } else if (max >= History.NORMAL_LOW && max <= History.NORMAL_HIGH) {
            state = History.NORMAL;
            helper.getConvertView().setBackgroundResource(R.drawable.history_bg_blue);
        } else if (max > History.NORMAL_HIGH) {
            state = History.HIGHER;
            helper.getConvertView().setBackgroundResource(R.drawable.history_bg_orange);
        }
        helper.setText(R.id.tv_state, state);
    }


}
