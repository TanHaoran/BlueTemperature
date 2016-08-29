package com.jerry.bluetemperature.db;

import com.jerry.bluetemperature.entity.History;
import com.jerry.bluetemperature.util.L;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.Date;
import java.util.List;

/**
 * Created by Jerry on 2016/7/8.
 */
public class HistoryDao {


    /**
     * 插入一条历史记录
     *
     * @param db
     * @param history
     */
    public void insert(DbManager db, History history) {
        try {
            db.save(history);
            L.i("保存历史记录成功");
        } catch (DbException e) {
            L.i("保存历史记录异常");
            e.printStackTrace();
        }
    }


    /**
     * 查询所有数据
     *
     * @param db
     */
    public List<History> queryAll(DbManager db) {
        try {
            List<History> histories = db.selector(History.class).orderBy("id", true).findAll();
            if (histories!= null) {
                L.i("查询历史记录成功：" + histories.toString());
            }
            return histories;
        } catch (DbException e) {
            L.i("查询历史记录异常");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 查询最近start天内的体温历史记录
     *
     * @param db
     * @param start
     * @return
     */
    public List<History> queryLastSomeDay(DbManager db, Date start) {
        try {
            List<History> histories = db.selector(History.class).where("start", ">", start).orderBy("id", true).findAll();
            if (histories != null) {
                L.i("查询历史记录成功：" + histories.toString());
            }
            return histories;
        } catch (DbException e) {
            L.i("查询历史记录异常");
            e.printStackTrace();
        }
        return null;
    }
}
