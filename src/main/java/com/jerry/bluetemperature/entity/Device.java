package com.jerry.bluetemperature.entity;

import java.io.Serializable;

/**
 * Created by Jerry on 2016/7/6.
 * 蓝牙设备
 */
public class Device implements Serializable {


    public static final String DEVICE_ID = "device_id";

    /**
     * 设备名称
     */
    private String deviceName;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 设备序列号
     */
    private int deviceSerialNumber;
    /**
     * 体温数据
     */
    private float temperature;
    /**
     * 原始体温数据
     */
    private String srcTemperature;
    /**
     * 电池电量
     */
    private int batteryPower;

    /**
     * 是否绑定
     */
    private boolean isBind;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getDeviceSerialNumber() {
        return deviceSerialNumber;
    }

    public void setDeviceSerialNumber(int deviceSerialNumber) {
        this.deviceSerialNumber = deviceSerialNumber;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getSrcTemperature() {
        return srcTemperature;
    }

    public void setSrcTemperature(String srcTemperature) {
        this.srcTemperature = srcTemperature;
    }

    public int getBatteryPower() {
        return batteryPower;
    }

    public void setBatteryPower(int batteryPower) {
        this.batteryPower = batteryPower;
    }

    public boolean isBind() {
        return isBind;
    }

    public void setBind(boolean bind) {
        isBind = bind;
    }
}
