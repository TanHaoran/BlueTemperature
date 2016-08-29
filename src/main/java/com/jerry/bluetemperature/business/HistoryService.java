package com.jerry.bluetemperature.business;

import com.jerry.bluetemperature.db.HistoryDao;
import com.jerry.bluetemperature.db.XutilDb;
import com.jerry.bluetemperature.entity.History;

import org.xutils.DbManager;
import org.xutils.x;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Jerry on 2016/7/8.
 */
public class HistoryService {

    private DbManager db;
    private HistoryDao historyDao;

    public HistoryService() {
        DbManager.DaoConfig config = XutilDb.getDaoConfig();
        db = x.getDb(config);
        historyDao = new HistoryDao();
    }

    /**
     * 插入一条历史记录
     *
     * @param history
     */
    public void insert(History history) {
        historyDao.insert(db, history);
    }

    /**
     * 查询所有的历史数据
     */
    public List<History> queryAll() {
        return historyDao.queryAll(db);
    }

    /**
     * 查询近day天内所有的数据
     *
     * @param day 天数设置
     * @return
     */
    public List<History> queryLastSomeDay(int day) {
        Date end = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(end);
        c.add(Calendar.DAY_OF_YEAR, -day);
        Date start = c.getTime();
        return historyDao.queryLastSomeDay(db, start);
    }
}
