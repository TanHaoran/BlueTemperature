package com.jerry.bluetemperature.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.util.DensityUtils;

/**
 * Created by Jerry on 2016/7/5.
 * 自定义选择条
 */
public class SelectBox extends RelativeLayout {

    private static final int DEFAULT_PADDING = 20;

    private static final int DEFAULT_TEXT_SIZE = 16;
    private static final int DEFAULT_TEXT_COLOR = 0xff434659;

    private String mContent;
    private boolean mChecked;

    private TextView mTextView;
    private ImageView mImageView;


    public SelectBox(Context context) {
        this(context, null);
    }

    public SelectBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SelectBox);

        mContent = ta.getString(R.styleable.SelectBox_content);
        mChecked = ta.getBoolean(R.styleable.SelectBox_checked, false);

        // 初始化布局
        initView(context);

        ta.recycle();
    }

    /**
     * 初始化布局
     *
     * @param context
     */
    private void initView(Context context) {
        /**
         * 初始化外层布局
         */
        if (mChecked) {
            setBackgroundResource(R.drawable.selec_equipment_red);
        } else {
            setBackgroundResource(R.drawable.selec_equipment_blue);
        }
        // 设置左右两边的边距
        setPadding(DensityUtils.dp2px(context, DEFAULT_PADDING), 0, DensityUtils.dp2px(context, DEFAULT_PADDING), 0);

        /**
         * 初始化内部控件
         */
        // 左侧文字
        mTextView = new TextView(context);
        LayoutParams textLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textLp.addRule(CENTER_VERTICAL, TRUE);
        mTextView.setLayoutParams(textLp);
        mTextView.setText(mContent);
        mTextView.setTextColor(DEFAULT_TEXT_COLOR);
        mTextView.setTextSize(DEFAULT_TEXT_SIZE);
        addView(mTextView);

        // 右侧选择框
        mImageView = new ImageView(context);
        LayoutParams imageLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imageLp.addRule(CENTER_VERTICAL, TRUE);
        imageLp.addRule(ALIGN_PARENT_RIGHT, TRUE);
        mImageView.setLayoutParams(imageLp);
        mImageView.setBackgroundResource(R.drawable.circle_icon);
        mImageView.setScaleType(ImageView.ScaleType.CENTER);
        if (mChecked) {
            mImageView.setImageResource(R.drawable.check_icon);
        }
        addView(mImageView);
    }


    /**
     * 设置选中状态
     * @param checked
     */
    public void setChecked(boolean checked) {
        if (checked) {
            setBackgroundResource(R.drawable.selec_equipment_red);
            mImageView.setImageResource(R.drawable.check_icon);
        } else {
            setBackgroundResource(R.drawable.selec_equipment_blue);
            mImageView.setImageResource(0);
        }
    }
}
