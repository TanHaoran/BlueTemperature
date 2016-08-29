package com.jerry.bluetemperature.app;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.WindowManager;

import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerry on 2016/6/23.
 */
public class MyApplication extends Application {


    private static List<Activity> activityList = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public MyApplication() {
        super();
    }

    public static void add(Activity activity) {
        // 设置强制竖屏
        if (activity.getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        activityList.add(activity);

        x.Ext.init(activity.getApplication());
        x.view().inject(activity);

    }

    public static void exit() {
        for (Activity a : activityList) {
            a.finish();
        }
    }

    /**
     * 获取位置定位权限（蓝牙扫描时候使用）
     *
     * @param activity
     */
    public static void getPermission(Activity activity) {
        //判断是否有权限
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //请求权限
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            //判断是否需要 向用户解释，为什么要申请该权限
            ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.ACCESS_COARSE_LOCATION);
        }
    }


    /**
     * 对话框形式的窗口
     * @param activity
     */
    public static void makeActivity2Dialog(Activity activity) {
        WindowManager manager = activity.getWindowManager();
        Display display = manager.getDefaultDisplay(); // 为获取屏幕宽、高
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes(); // 获取对话框当前的参值
        lp.height = (int) (display.getHeight() * 0.55); // 高度设置为屏幕的1.0
        lp.width = (int) (display.getWidth() * 0.8); // 宽度设置为屏幕的0.8
        lp.alpha = 1.0f; // 设置本身透明度
        lp.dimAmount = 0.6f; // 设置黑暗度
        activity.getWindow().setAttributes(lp); // 设置生效
        activity.getWindow().setGravity(Gravity.CENTER); // 设置靠右对齐
    }



}
