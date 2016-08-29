package com.jerry.bluetemperature.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.jerry.bluetemperature.entity.Device;


public class BluetoothUtil extends BluetoothProtocol {

    private static final String TAG = "THR";


    private static Context mContext;
    private static BluetoothAdapter mBluetoothAdapter;
    private static BluetoothManager mBluetoothManager;

    /**
     * 上次绑定设备的id
     */
    private String mOldId;

    /**
     * 体温计每次发出3个序列号相同的数据包，用于记录包的序列号
     */
    private int mOldSerialNumber;

    /**
     * 蓝牙接收数据回调
     */
    private IBleStateChanged mIBleStateChanged;

    private static BluetoothUtil mBluetoothUtil;

    /**
     * 获取上次绑定设备的id
     *
     * @return
     */
    public String getOldId() {
        return mOldId;
    }

    /**
     * 获取上次绑定设备的序列号
     *
     * @return
     */
    public int getOldSerialNumber() {
        return mOldSerialNumber;
    }

    /**
     * 当接收到蓝牙信息的回调接口
     */
    private OnDeviceFound mOnDeviceFound;


    public interface OnDeviceFound {
        void onLeScan(Device device);
    }


    /**
     * 蓝牙状态改变的回调接口
     */
    public interface IBleStateChanged {

        void onState(boolean isOpened);

    }

    public static BluetoothUtil getInstance() {
        if (mBluetoothUtil == null) {
            synchronized (BluetoothUtil.class) {
                if (mBluetoothUtil == null) {
                    mBluetoothUtil = new BluetoothUtil();
                }
            }
        }
        return mBluetoothUtil;
    }

    /**
     * 初始化蓝牙4.0环境
     */
    @SuppressLint("NewApi")
    public void initBluetooth(Context context, IBleStateChanged iBleStateChanged) {
        mContext = context;
        mIBleStateChanged = iBleStateChanged;

        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
            mBluetoothAdapter = mBluetoothManager.getAdapter();
            // 注册蓝牙开关变化的接收器
            mContext.registerReceiver(mReceiver, new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED));
            L.i("初始化完毕");
        }
    }

    /**
     * 扫描蓝牙设备的回调函数
     */
    @SuppressLint("NewApi")
    private BluetoothAdapter.LeScanCallback mIBleScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] receiveBytes) {
            L.i("-------------发现设备-------------");
            //如果不是本商家的设备，则直接返回
            if (!equipmentFilter(receiveBytes)) {
                L.i("不是彩虹蛋蛋设备");
                return;
            }

            //CRC8 校验
            if (!checkCrc8(receiveBytes)) {
                return;
            }

            String deviceName = bluetoothDevice.getName();//获取名称
            String deviceId = getDeviceId(receiveBytes);//获取ID号

            if (!TextUtils.isEmpty(deviceId) && !TextUtils.isEmpty(deviceName)) {
                int serialNumber = BluetoothUtil.getInstance().getPackageSerialNumber(receiveBytes);
                boolean isSerialNumberSame = serialNumber == mOldSerialNumber;
                boolean isIdSame = deviceId == mOldId;

                if (isSerialNumberSame && isIdSame) {
                    return;
                }

                mOldSerialNumber = serialNumber;
                mOldId = deviceId;

                /*EventBus.getDefault().postSticky(new DeviceEntityEventBus(deviceName,
                        deviceId,
                        getTemperature(receiveBytes),
                        getBatteryPower(receiveBytes),
                        getSrcTemperature(receiveBytes)));*/
                String str = String.format("%s\n%s\n%s\n%s\n%s\n",
                        deviceName, deviceId,
                        getTemperature(receiveBytes),
                        getBatteryPower(receiveBytes),
                        getSrcTemperature(receiveBytes)
                );
                Log.i(TAG, str);
                if (mOnDeviceFound != null) {
                    Device device = new Device();
                    device.setDeviceId(deviceId);
                    device.setDeviceName(deviceName);
                    device.setDeviceSerialNumber(serialNumber);
                    device.setTemperature(Float.parseFloat(getTemperature(receiveBytes)));
                    device.setSrcTemperature(getSrcTemperature(receiveBytes));
                    device.setBatteryPower(getBatteryPower(receiveBytes));
                    mOnDeviceFound.onLeScan(device);
                }
            }
        }

        /**
         * 设备过滤
         * @param receiveBytes 手机蓝牙接收到的数据
         * @return 如果属于本商家设备则返回true，反之亦然
         */
        private boolean equipmentFilter(byte[] receiveBytes) {
            byte low = receiveBytes[EQUIPMENT_ID_BIT];
            low = (byte) (low ^ KeyCode[EQUIPMENT_ID_BIT]);
            return low == EquipmentLow;
        }

        /**
         * CRC8 数据检验
         * @param receiveBytes 接收到的设备信息
         * @return true 表示CRC检验正确，反之亦然
         */
        private boolean checkCrc8(byte[] receiveBytes) {
            int length = CRC_BIT - TEMP_LOW_BIT + 1;
            byte[] decoded = new byte[length];

            for (int i = TEMP_LOW_BIT; i <= CRC_BIT; i++) {
                decoded[i - TEMP_LOW_BIT] = (byte) (receiveBytes[i] ^ KeyCode[i]);
            }

            byte crc = Crc8.getCrc8(decoded, CRC_BIT - TEMP_LOW_BIT + 1);

            return decoded[length - 1] == crc;
        }

        /**
         * 获取设备的ID号
         * @param receiveBytes 手机蓝牙接收到的数据
         * @return 设备ID号
         */
        private String getDeviceId(byte[] receiveBytes) {
            String id = "";

            for (int i = DEVICE_ID_START_BIT; i <= DEVICE_ID_END_BIT; i++) {
                byte idBit = receiveBytes[i];
                short idUnit = (short) ((idBit ^ KeyCode[i]) & 0xff);
                id += String.valueOf(Integer.toHexString((idUnit & 0x00FF) | 0xFF00).substring(2));
            }

            return id;
        }
    };

    /**
     * 监视蓝牙的开关状态
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);

            switch (blueState) {
                case BluetoothAdapter.STATE_ON: {
                    mIBleStateChanged.onState(true);
                    break;
                }

                case BluetoothAdapter.STATE_OFF: {
                    stopMonitorBroadcast();
                    mIBleStateChanged.onState(false);
                    break;
                }
                default:
                    break;
            }
        }
    };

    /**
     * 开始扫描，监听设备，接收数据并回传
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void startMonitorBroadcast(OnDeviceFound onDeviceFound) {
        stopMonitorBroadcast();
        mOnDeviceFound = onDeviceFound;
        boolean success = mBluetoothAdapter.startLeScan(mIBleScanCallback);
        Log.i(TAG, "扫描蓝牙设备启动：" + success);
    }

    /**
     * 停止扫描蓝牙设备
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void stopMonitorBroadcast() {
        Log.i(TAG, "停止扫描扫描蓝牙设备");
        mBluetoothAdapter.stopLeScan(mIBleScanCallback);
    }


    /**
     * 对接收到的数据进行解码，部分代码采用硬编码的方式
     *
     * @param receiveBytes 手机蓝牙接收到的数据
     * @return 解码获取的温度字符串
     */
    private String getTemperature(byte[] receiveBytes) {
        byte highByte = (byte) (receiveBytes[TEMP_HIGH_BIT] ^ KeyCode[TEMP_HIGH_BIT]);
        byte lowByte = (byte) (receiveBytes[TEMP_LOW_BIT] ^ KeyCode[TEMP_LOW_BIT]);
        short temperature = (short) (((highByte & 0xff) << 8) | (lowByte & 0xff));
        return String.valueOf((temperature / 100.0));
    }

    /**
     * 对接收到的数据进行解码，部分代码采用硬编码的方式
     *
     * @param receiveBytes 手机蓝牙接收到的数据
     * @return 解码获取的原始温度字符串
     */
    private String getSrcTemperature(byte[] receiveBytes) {
        byte highByte = (byte) (receiveBytes[SRC_TEMP_HIGH_BIT] ^ KeyCode[SRC_TEMP_HIGH_BIT]);
        byte lowByte = (byte) (receiveBytes[SRC_TEMP_LOW_BIT] ^ KeyCode[SRC_TEMP_LOW_BIT]);
        short temperature = (short) (((highByte & 0xff) << 8) | (lowByte & 0xff));
        return String.valueOf((temperature / 100.0));
    }

    /**
     * 获取电池电量
     *
     * @param receiveBytes 手机蓝牙接收到的字节数组
     * @return 电池电量百分比
     */
    private int getBatteryPower(byte[] receiveBytes) {
        byte power = receiveBytes[BATTERY_BIT];
        power = (byte) (power ^ KeyCode[BATTERY_BIT]);
        return (int) power;
    }

    /**
     * 获取广播包的序列号
     *
     * @param receiveBytes 手机蓝牙接收到的字节数组
     * @return 序列号
     */
    private int getPackageSerialNumber(byte[] receiveBytes) {
        byte number = receiveBytes[SERIAL_NUMBER_BIT];
        number = (byte) (number ^ KeyCode[SERIAL_NUMBER_BIT]);
        return (int) number + 128;
    }

    /**
     * 检测当前蓝牙是否开启
     *
     * @return 是否开启
     */
    public boolean isBluetoothOpen() {
        return mBluetoothAdapter.isEnabled();
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth() {
        mBluetoothAdapter.enable();
    }

    /**
     * 打开蓝牙
     */
    public void closeBluetooth() {
        mBluetoothAdapter.disable();
    }

    /**
     * ，停止搜索蓝牙设备，解除广播监听器
     */
    public void unregisterReceiver() {
        try {
            stopMonitorBroadcast();
            mContext.unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {
            L.i("广播未注册，无需解注册");
            e.printStackTrace();
        }
    }

}