package com.jerry.bluetemperature.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.app.MyApplication;
import com.jerry.bluetemperature.constant.ServiceConstant;
import com.jerry.bluetemperature.util.AutoInstall;
import com.jerry.bluetemperature.util.L;
import com.jerry.bluetemperature.util.MyProgressCallBack;
import com.jerry.bluetemperature.util.T;
import com.jerry.bluetemperature.util.XUtil;
import com.jerry.bluetemperature.view.RoundProgressBar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.io.IOException;

@ContentView(R.layout.activity_downloading)
public class DownloadingActivity extends Activity {

    @ViewInject(R.id.round_progress_bar)
    private RoundProgressBar mRoundProgressBar;

    @ViewInject(R.id.layout)
    private LinearLayout mLayout;

    @ViewInject(R.id.tv_ok)
    private TextView mOk;

    private File apkFile;


    private static final String APK_SAVE_URL = Environment.getExternalStorageDirectory().getAbsolutePath() + "/BlueTemperature";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.add(this);
        MyApplication.makeActivity2Dialog(this);
        downloadAPK();
    }

    /**
     * 下载APK文件
     */
    private void downloadAPK() {
        XUtil.DownLoadFile(ServiceConstant.APK_URL, APK_SAVE_URL, new MyProgressCallBack<File>() {

            @Override
            public void onSuccess(File result) {
                super.onSuccess(result);
                try {
                    apkFile = result;
                    L.i("下载成功");
                    T.showLong(DownloadingActivity.this, "下载成功！");
                    boolean create = apkFile.createNewFile();
                    L.i("创建文件：" + create);
                    mLayout.setVisibility(View.VISIBLE);
                    mOk.setText("安装");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                super.onError(ex, isOnCallback);
                L.i("下载失败");
                T.showLong(DownloadingActivity.this, "下载失败！");
                mLayout.setVisibility(View.VISIBLE);
                mOk.setText("确定");

            }

            @Override
            public void onLoading(long total, long current,
                                  boolean isDownloading) {
                super.onLoading(total, current, isDownloading);
                L.i("current/total：" + current + "/" + total + "(" + (current * 100 / total) + "%)");
                mRoundProgressBar.setProgress((int) (current * 100 / total));

            }
        });
    }

    @Event(R.id.tv_ok)
    private void onOk(View v) {
        AutoInstall.setUrl(apkFile.getAbsolutePath());
        AutoInstall.install(this);
        finish();
    }

    @Event(R.id.tv_cancel)
    private void onCancel(View v) {
        finish();
    }
}
