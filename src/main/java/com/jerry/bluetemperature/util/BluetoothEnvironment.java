package com.jerry.bluetemperature.util;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * 蓝牙环境检测类
 */
public class BluetoothEnvironment {

    /**
     * 判断设备是否有蓝牙并且支持蓝牙4.0通信
     *
     * @param context
     * @return
     */
    public static boolean isDeviceSupported(Context context) {
        return isBluetoothSupported() && isBluetooth4Supported(context);
    }

    /**
     * 判断设备是否有蓝牙
     *
     * @return
     */
    private static boolean isBluetoothSupported() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter != null;
    }

    /**
     * 判断设备是否支持蓝牙4.0
     */
    private static boolean isBluetooth4Supported(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
    }
}