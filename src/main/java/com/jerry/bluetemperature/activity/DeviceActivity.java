package com.jerry.bluetemperature.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.adapter.DeviceAdapter;
import com.jerry.bluetemperature.app.MyApplication;
import com.jerry.bluetemperature.entity.Device;
import com.jerry.bluetemperature.util.BluetoothUtil;
import com.jerry.bluetemperature.util.SPUtils;
import com.jerry.bluetemperature.view.TitleBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备绑定
 */
@ContentView(R.layout.activity_device)
public class DeviceActivity extends AppCompatActivity {

    @ViewInject(R.id.titlebar)
    private TitleBar mTitleBar;

    @ViewInject(R.id.btn_search)
    private Button mSearchBtn;


    @ViewInject(R.id.listview)
    private ListView mListView;

    private DeviceAdapter mAdapter;
    private List<Device> mDevices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);

        initView();
        // 搜索设备
        searchDevice();
    }

    /**
     * 点击搜索按钮
     *
     * @param v
     */
    @Event(R.id.btn_search)
    private void onSearch(View v) {
        mDevices = new ArrayList<>();
        searchDevice();
    }

    /**
     * 搜索蓝牙设备
     */
    private void searchDevice() {
        BluetoothUtil.getInstance().startMonitorBroadcast(new BluetoothUtil.OnDeviceFound() {
            @Override
            public void onLeScan(Device device) {
                Message msg = mHandler.obtainMessage();
                msg.obj = device;
                msg.sendToTarget();
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mSearchBtn.setVisibility(View.GONE);
            Device d = (Device) msg.obj;
            // 测是否是绑定的设备
            checkIfBind(d);
            // 检测是否已经搜到设备了
            if (!hasDevice(d)) {
                mDevices.add(d);
                mAdapter.setDatas(mDevices);
                mAdapter.notifyDataSetChanged();
//            initView();
            }
        }
    };

    /**
     * 判断是否数据中已经存在设备了
     *
     * @param device
     * @return
     */
    private boolean hasDevice(Device device) {
        for (Device d : mDevices) {
            if (d.getDeviceId().equals(device.getDeviceId())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 检测是否是绑定的设备
     *
     * @param d
     */
    private boolean checkIfBind(Device d) {
        String bindId = (String) SPUtils.get(DeviceActivity.this, Device.DEVICE_ID, "");
        if (d.getDeviceId().equals(bindId)) {
            d.setBind(true);
            return true;
        }
        return false;
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
        mAdapter = new DeviceAdapter(this, mDevices, R.layout.item_device);
        mListView.setAdapter(mAdapter);
        // 点击子项，绑定设备，保存设备id
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < mDevices.size(); i++) {
                    Device d = mDevices.get(i);
                    if (i == position) {
                        d.setBind(true);
                        // 保存绑定设备的id
                        saveDeviceId(d);
                    } else {
                        d.setBind(false);
                    }
                }
                // 通知界面改变
                mAdapter.notifyDataSetChanged();
            }

        });
    }


    /**
     * 保存绑定设备的id
     *
     * @param d
     */
    private void saveDeviceId(Device d) {
        SPUtils.put(this, Device.DEVICE_ID, d.getDeviceId());
    }
}
