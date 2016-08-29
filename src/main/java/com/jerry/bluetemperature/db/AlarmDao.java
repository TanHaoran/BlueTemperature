package com.jerry.bluetemperature.db;

import com.jerry.bluetemperature.entity.Alarm;
import com.jerry.bluetemperature.util.L;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by Jerry on 2016/7/8.
 */
public class AlarmDao {


    /**
     * 插入一条闹钟
     *
     * @param db
     * @param alarm
     */
    public void insert(DbManager db, Alarm alarm) {
        try {
            db.save(alarm);
            L.i("保存闹钟成功");
        } catch (DbException e) {
            L.i("保存闹钟成功");
            e.printStackTrace();
        }
    }


    /**
     * 查询所有闹钟数据
     *
     * @param db
     */
    public List<Alarm> queryAll(DbManager db) {
        try {
            List<Alarm> alarms = db.selector(Alarm.class).orderBy("id", true).findAll();
            L.i("查询闹钟成功：" + alarms.toString());
            return alarms;
        } catch (DbException e) {
            L.i("查询闹钟异常");
            e.printStackTrace();
        }
        return null;
    }



}
