package com.jerry.bluetemperature.business;

import com.jerry.bluetemperature.db.AlarmDao;
import com.jerry.bluetemperature.db.XutilDb;
import com.jerry.bluetemperature.entity.Alarm;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by Jerry on 2016/7/8.
 */
public class AlarmService {

    private DbManager db;
    private AlarmDao alarmDao;

    public AlarmService() {
        DbManager.DaoConfig config = XutilDb.getDaoConfig();
        db = x.getDb(config);
        alarmDao = new AlarmDao();
    }

    /**
     * 插入一条历史记录
     *
     * @param alarm
     */
    public void insert(Alarm alarm) {
        alarmDao.insert(db, alarm);
    }

}
