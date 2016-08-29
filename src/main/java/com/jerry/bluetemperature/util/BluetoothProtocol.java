package com.jerry.bluetemperature.util;

/**
 * 蓝牙通信协议
 */
public class BluetoothProtocol {

    /*
    解密数组
     */
    protected static byte KeyCode[] = {
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xab, (byte) 0xcd, (byte) 0xef,
            (byte) 0x11, (byte) 0x22, (byte) 0x33, (byte) 0x44, (byte) 0x55, (byte) 0x66, (byte) 0x77, (byte) 0x88,
            (byte) 0x99, (byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd, (byte) 0xee, (byte) 0xff, (byte) 0x00,
            (byte) 0xfe, (byte) 0xdc, (byte) 0xba, (byte) 0x09, (byte) 0x87, (byte) 0x65, (byte) 0x43, (byte) 0x21
    };

    protected static final byte EquipmentLow = (byte) 0x42;

    protected static final int TEMP_LOW_BIT = 5;//2字节温度值的低字节索引
    protected static final int TEMP_HIGH_BIT = 6;//2字节温度值的高字节索引
    protected static final int BATTERY_BIT = 7;//电池电量位索引
    protected static final int DEVICE_ID_START_BIT = 10;//设备ID的起始位
    protected static final int DEVICE_ID_END_BIT = 15;//设备ID的结束位
    protected static final int EQUIPMENT_ID_BIT = 16;//商家id的索引，解密后为0x42
    protected static final int COLOR_BIT = 17;//2字节商家id的高字节索引，解密后为0x53
    protected static final int SERIAL_NUMBER_BIT = 18;//广播包的序列号索引，每上报一次温度值加1
    protected static final int CRC_BIT = 19;//CRC校验位索引
    protected static final int SRC_TEMP_LOW_BIT = 20;//原始数据的低字节
    protected static final int SRC_TEMP_HIGH_BIT = 21;//原始数据的高字节
}
