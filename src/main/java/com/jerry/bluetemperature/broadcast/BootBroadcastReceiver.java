package com.jerry.bluetemperature.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.jerry.bluetemperature.activity.MainActivity;

/**
 * 用来接收开机启动消息的监听器
 */
public class BootBroadcastReceiver extends BroadcastReceiver {

    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)) {
            Intent ootStartIntent = new Intent(context, MainActivity.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }

    }

}