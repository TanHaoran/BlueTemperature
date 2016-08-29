package com.jerry.bluetemperature.db;

import android.os.Environment;

import org.xutils.DbManager;

import java.io.File;

/**
 * Created by Jerry on 2016/7/8.
 * 数据库操作工具类
 */
public class XutilDb {
    /**
     * 配置文件
     */
    private static DbManager.DaoConfig daoConfig;

    /**
     * 数据库名称
     */
    private static final String DB_NAME = "history.db";

    private XutilDb() {

    }

    public static DbManager.DaoConfig getDaoConfig() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        if (daoConfig == null) {
            synchronized (XutilDb.class) {
                if (daoConfig == null) {
                    daoConfig = new DbManager.DaoConfig()
                            .setDbName(DB_NAME)
                            .setDbDir(file)
                            .setDbVersion(1)
                            .setAllowTransaction(true)
                            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                                @Override
                                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                                }
                            });
                }
            }

        }
        return daoConfig;
    }
}
