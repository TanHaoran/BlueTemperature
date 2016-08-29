package com.jerry.bluetemperature.adapter;

import android.content.Context;
import android.view.View;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.entity.Device;

import java.util.List;

/**
 * Created by Jerry on 2016/7/5.
 */
public class DeviceAdapter extends CommonAdapter<Device> {

    public DeviceAdapter(Context context, List<Device> mDatas, int itemLayoutId) {
        super(context, mDatas, itemLayoutId);
    }

    @Override
    public void convert(ViewHolder helper, Device item) {
        // 根据是否绑定设置图标和背景
        if (item.isBind()) {
            helper.setVisibility(R.id.iv_select, View.VISIBLE);
            helper.getConvertView().setBackgroundResource(R.drawable.selec_equipment_red);
        } else {
            helper.setVisibility(R.id.iv_select, View.GONE);
            helper.getConvertView().setBackgroundResource(R.drawable.selec_equipment_blue);
        }
        helper.setText(R.id.tv_device, item.getDeviceId());
        // 正在充电时，会显示电量-128
        if (item.getBatteryPower() == -128) {
            helper.setText(R.id.tv_battery, "正在充电");
        } else {
            helper.setText(R.id.tv_battery, item.getBatteryPower() + "%");

        }
    }
}
