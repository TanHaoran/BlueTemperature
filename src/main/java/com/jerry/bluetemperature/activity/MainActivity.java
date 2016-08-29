package com.jerry.bluetemperature.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.app.MyApplication;
import com.jerry.bluetemperature.business.HistoryService;
import com.jerry.bluetemperature.constant.ServiceConstant;
import com.jerry.bluetemperature.entity.Device;
import com.jerry.bluetemperature.entity.History;
import com.jerry.bluetemperature.util.BluetoothEnvironment;
import com.jerry.bluetemperature.util.BluetoothUtil;
import com.jerry.bluetemperature.util.DateUtil;
import com.jerry.bluetemperature.util.L;
import com.jerry.bluetemperature.util.MyCallBack;
import com.jerry.bluetemperature.util.SPUtils;
import com.jerry.bluetemperature.util.T;
import com.jerry.bluetemperature.util.ThresholdUtil;
import com.jerry.bluetemperature.util.XUtil;
import com.jerry.bluetemperature.view.FeverDialog;
import com.jerry.bluetemperature.view.MyDialog;
import com.jerry.bluetemperature.view.RippleBackground;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * 主界面
 */
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    /**
     * 默认每10秒更新一次体温数据
     */
    private static final int DEFAULT_MONITOR = 10 * 1000;

    @ViewInject(R.id.ripple)
    private RippleBackground mRipple;

    @ViewInject(R.id.iv_bluetooth)
    private ImageView mImage;

    @ViewInject(R.id.tv_tips)
    private TextView mTips;

    @ViewInject(R.id.ll_value)
    private LinearLayout mValueLayout;

    @ViewInject(R.id.tv_value)
    private TextView mTextValue;

    @ViewInject(R.id.ll_duration)
    private LinearLayout mDurationLayout;

    @ViewInject(R.id.ll_device)
    private LinearLayout mDeviceLayout;


    @ViewInject(R.id.tv_duration)
    private TextView mTextDuration;

    @ViewInject(R.id.tv_device_id)
    private TextView mTextId;

    @ViewInject(R.id.tv_device_battery)
    private TextView mTextBattery;

    @ViewInject(R.id.tv_stop)
    private TextView mTextStop;


    /**
     * 开始的时间
     */
    private Date mStart;

    /**
     * 测量的温度值
     */
    private StringBuilder mValues;


    /**
     * 绑定设备的id
     */
    private String mOldId;

    /**
     * 是否手动停止
     */
    private boolean mStop = false;

    private FeverDialog mFeverDialog;

    /**
     * app安装版本
     */
    private String installedVersion;
    /**
     * app服务最新版本
     */
    private String serviceVersion;

    /**
     * 已经开始测量的分钟数
     */
    private int mPastMinute = 0;

    private static final int DEVICE_FOUND = 0x0001;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DEVICE_FOUND:
                    Device d = (Device) msg.obj;
                    // 如果搜索到的设备id和保存的保定的设备id一致，则显示体温信息
                    if (mOldId.equals(d.getDeviceId())) {
                        setTemperatureData(d);
                    }
                    break;
            }
        }
    };

    private Runnable mMonitor = new Runnable() {
        @Override
        public void run() {
            BluetoothUtil.getInstance().startMonitorBroadcast(new BluetoothUtil.OnDeviceFound() {

                @Override
                public void onLeScan(Device blueInfo) {
                    Message msg = mHandler.obtainMessage();
                    msg.what = DEVICE_FOUND;
                    msg.obj = blueInfo;
                    msg.sendToTarget();
                }
            });
            // 每隔DEFAULT_MONITOR时间，进行重新获取温度数据
            mHandler.postDelayed(mMonitor, DEFAULT_MONITOR);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        MyApplication.getPermission(this);
        update();
    }


    @Override
    protected void onResume() {
        super.onResume();
        // 初始化界面
        initView();
        // 如果手动停止了就不进行蓝牙搜索
        if (mStop) {
            return;
        }
        // 检测设备是否支持蓝牙4.0
        checkBluetoothEnvironment();
        // 检测蓝牙是否打开
        if (checkBluetoothOpen()) {
            // 检测是否绑定设备
            mOldId = checkDeviceBind();
            // 如果id为空，需要先绑定设备
            if (TextUtils.isEmpty(mOldId)) {
                mTips.setText("首次使用，请先绑定设备！");
            } else {
                startMinitor();
                mTips.setText("点击图标，开始监测体温！");
            }
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        mRipple.startRippleAnimation();
        mFeverDialog = new FeverDialog(this);
    }

    /**
     * 检测设备是否支持蓝牙4.0，如果不支持就自动退出程序,如果支持就初始化蓝牙相关操作
     */
    private void checkBluetoothEnvironment() {
        if (!BluetoothEnvironment.isDeviceSupported(this)) {
            T.showShort(this, "设备不支持蓝牙4.0");
            MyApplication.exit();
        }
        BluetoothUtil.getInstance().initBluetooth(this, null);
    }

    /**
     * 检测蓝牙是否打开
     */
    private boolean checkBluetoothOpen() {
        if (!BluetoothUtil.getInstance().isBluetoothOpen()) {
            mTips.setText("蓝牙已关闭，请手动打开！");
            return false;
        } else {
            mTips.setText("正在搜索设备……");
            return true;
        }
    }

    /**
     * 检测是否绑定设备
     *
     * @return
     */
    private String checkDeviceBind() {
        //检测是否已经保存了绑定设备的id
        return (String) SPUtils.get(this, Device.DEVICE_ID, "");
    }

    /**
     * 开始搜索蓝牙设备
     */
    private void startMinitor() {
        mHandler.post(mMonitor);
    }

    /**
     * 显示体温信息等操作
     */
    private void setTemperatureData(Device d) {
        // 第一次要初始化开始时间
        if (mStart == null) {
            mStart = new Date();
            mValues = new StringBuilder();
        }

        mImage.setVisibility(View.INVISIBLE);
        mTips.setVisibility(View.INVISIBLE);
        mValueLayout.setVisibility(View.VISIBLE);
        mTextStop.setVisibility(View.VISIBLE);
        mDurationLayout.setVisibility(View.VISIBLE);
        mDeviceLayout.setVisibility(View.VISIBLE);

        Date date = new Date();
        // 设置体温值
        String value = formatValue(d.getTemperature());
        mTextValue.setText(value);
        // 判断提示是否需要报警
        setAlarmListener(Float.parseFloat(value));
        //当测量时间超过1分钟就统计一次体温值
        if (DateUtil.calculateDurationMinutes(mStart, date) - mPastMinute > 0) {
            mValues.append(value + ";");
            mPastMinute++;
        }
        // 设置测量时间
        String duration = DateUtil.calculateDuration(mStart, date);
        mTextDuration.setText(duration);
        // 设置设备id
        mTextId.setText(d.getDeviceId());
        // 设置设备电量
        mTextBattery.setText("电量：" + d.getBatteryPower() + "%");
    }

    /**
     * 格式化体温数据
     *
     * @param temperature
     * @return
     */
    private String formatValue(float temperature) {
        return new DecimalFormat("##.#").format(temperature);
    }

    /**
     * 设置报警监听
     *
     * @param value
     */
    private void setAlarmListener(final float value) {
        ThresholdUtil.getInstance(this).check(value, new ThresholdUtil.OnAlarmListener() {
            @Override
            public void onAlarm(String msg) {
                showFeverDialog("体温：" + value + "°C,  " + msg + "！");
            }
        });
    }

    /**
     * 当体温进入报警区域时，发出警告提示
     *
     * @param msg
     */
    private void showFeverDialog(String msg) {
        if (!mFeverDialog.isShowing()) {
            mFeverDialog.showDialog(msg);
        }
    }

    /**
     * 点击蓝牙图标，开始检测体温
     *
     * @param v
     */
    @Event(R.id.iv_bluetooth)
    private void onSearch(View v) {
        if (TextUtils.isEmpty(mOldId)) {
            return;
        }
        if (checkBluetoothOpen()) {
            mTips.setText("搜索设备……");
            startMinitor();
        }
    }

    /**
     * 停止测量
     *
     * @param v
     */
    @Event(R.id.tv_stop)
    private void onStop(View v) {
        mStop = true;
        // 停止扫描
        BluetoothUtil.getInstance().stopMonitorBroadcast();
        // 设置界面
        mImage.setVisibility(View.VISIBLE);
        mTips.setVisibility(View.VISIBLE);
        mTips.setText("点击图标，进行监测！");
        mValueLayout.setVisibility(View.INVISIBLE);
        mTextStop.setVisibility(View.INVISIBLE);
        mDurationLayout.setVisibility(View.INVISIBLE);
        mDeviceLayout.setVisibility(View.INVISIBLE);
        // 移除线程
        mHandler.removeCallbacks(mMonitor);
        // 将数据保存至数据库
        saveHistory();
        // 重置开始时间和体温值
        mStart = null;
        mValues = null;
    }

    /**
     * 将数据保存至数据库
     */
    private void saveHistory() {
        HistoryService historyService = new HistoryService();
        History history = new History();
        history.setValues(mValues.toString());
        history.setStart(mStart);
        Date end = new Date();
        history.setEnd(end);
        if (!DateUtil.ZERO_MINUTE.equals(DateUtil.calculateDuration(mStart, end))) {
            if (!TextUtils.isEmpty(mValues)) {
                historyService.insert(history);
            }
        } else {
            T.showLong(this, "未测量够1分钟，无法保存数据!");
        }
    }

    /**
     * 历史
     *
     * @param v
     */
    @Event(R.id.ll_history)
    private void onHistory(View v) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    /**
     * 设置
     *
     * @param v
     */
    @Event(R.id.ll_setting)
    private void onSetting(View v) {
        Intent intent = new Intent(this, SettingActivity.class);
        startActivity(intent);
    }

    /**
     * 检测是否有新版本
     */
    private void update() {
        // 获取当前App安装的版本号
        getInstalledVersion();
        // 获取服务端App最新的版本号
        getServiceVersion();
    }

    /**
     * 获取目前App的版本
     *
     * @return
     */
    private String getInstalledVersion() {
        installedVersion = "0.0";
        try {
            PackageManager manager = getPackageManager();
            PackageInfo info = manager.getPackageInfo(getPackageName(), 0);
            installedVersion = info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return installedVersion;
    }

    /**
     * 获取服务端App最新的版本号
     */
    private void getServiceVersion() {
        XUtil.Get(ServiceConstant.GET_VERSION, null, new MyCallBack<String>() {
            @Override
            public void onSuccess(String result) {
                if (!TextUtils.isEmpty(result)) {
                    try {
                        JSONObject object = new JSONObject(result);
                        serviceVersion = object.getString("version");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 比较两个版本是否一致
                    compareVersion();
                }
            }
        });
    }

    /**
     * 比较两个版本是否一致
     */
    private void compareVersion() {
        L.i("当前版本号：" + installedVersion);
        L.i("服务版本号：" + serviceVersion);
        if (!installedVersion.equals(serviceVersion)) {
            new MyDialog(this).showDialog("发现新版本，是否立刻更新？").setOnOkListener(new MyDialog.OnOkListener() {
                @Override
                public void onOk() {
                    Intent intent = new Intent(MainActivity.this, DownloadingActivity.class);
                    startActivity(intent);
                }
            });
        }
    }


    /**
     * 屏蔽退出键改为home键
     */
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BluetoothUtil.getInstance().unregisterReceiver();
    }
}