package com.jerry.bluetemperature.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.util.Date;

/**
 * Created by Jerry on 2016/7/7.
 * 闹钟
 */

@Table(name = "alarm")
public class Alarm {

    public static final String MONDAY = "Monday";
    public static final String TUESDAY = "Tuesday";
    public static final String WEDNESDAY = "Wednesday";
    public static final String THURSDAY = "Thursday";
    public static final String FRIDAY = "Friday";
    public static final String SATURDAY = "Saturday";
    public static final String SUNDAY = "Sunday";

    /**
     * 记录id（主键、自增长）
     */
    @Column(name = "id", isId = true, autoGen = true)
    private int id;

    /**
     * 时间
     */
    @Column(name = "date")
    private Date date;

    /**
     * 响铃天数（多个天数中间使用“；”分割）
     */
    @Column(name = "days")
    private String days;

    /**
     * 是否循环
     */
    @Column(name = "loop")
    private boolean isLoop;

    /**
     * 是否就开启
     */
    @Column(name = "open")
    private boolean isOpen;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public boolean isLoop() {
        return isLoop;
    }

    public void setLoop(boolean loop) {
        isLoop = loop;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }


    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", date=" + date +
                ", days='" + days + '\'' +
                ", isLoop=" + isLoop +
                ", isOpen=" + isOpen +
                '}';
    }
}
