package com.jerry.bluetemperature.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jerry.bluetemperature.R;

/**
 * @author Jerry Tan
 * @version 1.0
 * @description 自定义提示弹出框（只有确定按钮）
 * @date 2015-10-9 16:22:10
 * @Company Buzzlysoft
 * @email thrforever@126.com
 * @remark
 */
public class FeverDialog extends Dialog {

    private TextView mContent;

    private TextView mButton;


    public FeverDialog(Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_box);


        mContent = (TextView) findViewById(R.id.tv_content);
        mButton = (TextView) findViewById(R.id.tv_ok);


        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    /**
     * 显示弹出窗口
     *
     * @param msg
     */
    public void showDialog(String msg) {
        show();
        mContent.setText(msg);
        // 设置圆角
        getWindow().setBackgroundDrawable(new BitmapDrawable());
    }

}
