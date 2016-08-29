package com.jerry.bluetemperature.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jerry.bluetemperature.R;

/**
 * Created by Buzzly on 2016/4/24.
 */
public class TitleBar extends RelativeLayout {

    private static final int DEFAULT_LAYOUT_WIDTH = 60;

    private static final int DEFAULT_TEXT_SIZE = 14;
    private static final int DEFAULT_TITLE_SIZE = 20;

    private static final int DEFAULT_TEXT_COLOR = 0xffffffff;
    private static final int DEFAULT_TITLE_COLOR = 0xffffffff;

    private RelativeLayout mLeftLayout;
    private TextView mLeftText;
    private ImageView mLeftImg;

    private RelativeLayout mRightLayout;
    private TextView mRightText;
    private ImageView mRightImg;

    private TextView mTitleText;

    private String mLeftString;
    private float mLeftTextSize;
    private int mLeftTextColor;
    private int mLeftRes;
    private boolean mLeftVisible;

    private String mRightString;
    private float mRightTextSize;
    private int mRightTextColor;
    private int mRightRes;
    private boolean mRightVisible;

    private String mTitleString;
    private float mTitleTextSize;
    private int mTitleTextColor;

    private OnTitlebarOnClickListener clickListener;

    public interface OnTitlebarOnClickListener {
        void onLeftClick();

        void onRightClick();
    }

    public void setOnTitlebarClickListener(OnTitlebarOnClickListener clickListener) {
        this.clickListener = clickListener;
    }


    public TitleBar(Context context) {
        this(context, null);
    }

    public TitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        // 取得自定义的属性
        mLeftString = ta.getString(R.styleable.TitleBar_left_text);
        mLeftTextSize = ta.getDimension(R.styleable.TitleBar_left_textSize, sp2px(context, DEFAULT_TEXT_SIZE));
        mLeftTextColor = ta.getColor(R.styleable.TitleBar_left_textColor, DEFAULT_TEXT_COLOR);
        mLeftRes = ta.getResourceId(R.styleable.TitleBar_left_img, 0);
        mLeftVisible = ta.getBoolean(R.styleable.TitleBar_left_visible, true);

        mRightString = ta.getString(R.styleable.TitleBar_right_text);
        mRightTextSize = ta.getDimension(R.styleable.TitleBar_right_textSize, sp2px(context, DEFAULT_TEXT_SIZE));
        mRightTextColor = ta.getColor(R.styleable.TitleBar_right_textColor, DEFAULT_TEXT_COLOR);
        mRightRes = ta.getResourceId(R.styleable.TitleBar_right_img, 0);
        mRightVisible = ta.getBoolean(R.styleable.TitleBar_right_visible, true);

        mTitleString = ta.getString(R.styleable.TitleBar_title_text);
        mTitleTextSize = ta.getDimension(R.styleable.TitleBar_title_textSize, sp2px(context, DEFAULT_TITLE_SIZE));
        mTitleTextColor = ta.getColor(R.styleable.TitleBar_title_textColor, DEFAULT_TITLE_COLOR);
        initView(context);


        ta.recycle();
    }

    private void initView(Context context) {
        // 创建子View
        mLeftLayout = new RelativeLayout(context);
        mLeftText = new TextView(context);
        mLeftImg = new ImageView(context);

        mRightLayout = new RelativeLayout(context);
        mRightText = new TextView(context);
        mRightImg = new ImageView(context);

        mTitleText = new TextView(context);

        // 构建左侧的按钮
        mLeftText.setText(mLeftString);
        mLeftText.setTextSize(TypedValue.COMPLEX_UNIT_PX,mLeftTextSize);
        mLeftText.setTextColor(mLeftTextColor);
        mLeftImg.setImageResource(mLeftRes);
        LayoutParams leftTextLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        leftTextLp.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        LayoutParams leftImgLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        leftImgLp.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        mLeftLayout.addView(mLeftText, leftTextLp);
        mLeftLayout.addView(mLeftImg, leftImgLp);

        // 构建右侧的按钮
        mRightText.setText(mRightString);
        mRightText.setTextSize(TypedValue.COMPLEX_UNIT_PX,mRightTextSize);
        mRightText.setTextColor(mRightTextColor);
        mRightImg.setImageResource(mRightRes);
        LayoutParams rightTextLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rightTextLp.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        LayoutParams rightImgLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        rightImgLp.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        mRightLayout.addView(mRightText, rightTextLp);
        mRightLayout.addView(mRightImg, rightImgLp);

        // 将左右按钮添加到布局中
        LayoutParams leftLp = new LayoutParams(dp2px(context, DEFAULT_LAYOUT_WIDTH),
                ViewGroup.LayoutParams.MATCH_PARENT);
        LayoutParams rightLp = new LayoutParams(dp2px(context, DEFAULT_LAYOUT_WIDTH),
                ViewGroup.LayoutParams.MATCH_PARENT);
        rightLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        addView(mLeftLayout, leftLp);
        addView(mRightLayout, rightLp);

        // 将标题添加到布局中
        mTitleText.setText(mTitleString);
        mTitleText.setTextSize(TypedValue.COMPLEX_UNIT_PX,mTitleTextSize);
        mTitleText.setTextColor(mTitleTextColor);
        LayoutParams titleLp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        titleLp.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(mTitleText, titleLp);

        // 根据是否设置图片来决定显示图片还是文字
        if (mLeftRes != 0) {
            mLeftText.setVisibility(INVISIBLE);
        }
        if (mRightRes != 0) {
            mRightText.setVisibility(INVISIBLE);
        }

        // 判断左右按钮是否显示
        if (!mLeftVisible) {
            mLeftLayout.setVisibility(INVISIBLE);
        }
        if (!mRightVisible) {
            mRightLayout.setVisibility(INVISIBLE);
        }

        // 设置左右按钮的点击监听事件
        mLeftLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onLeftClick();
                }
            }
        });
        mRightLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) {
                    clickListener.onRightClick();
                }
            }
        });
    }

    /**
     * 设置左按钮是否显示
     *
     * @param visible 是否显示
     */
    public void setLeftVisible(boolean visible) {
        if (visible) {
            mLeftLayout.setVisibility(VISIBLE);
        } else {
            mLeftLayout.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置右按钮是否显示
     *
     * @param visible 是否显示
     */
    public void setRightVisible(boolean visible) {
        if (visible) {
            mRightLayout.setVisibility(VISIBLE);
        } else {
            mRightLayout.setVisibility(INVISIBLE);
        }
    }

    /**
     * 设置左按钮显示的文字
     *
     * @param text 文字内容
     */
    public void setLeftText(String text) {
        mLeftText.setText(text);
        mLeftText.setVisibility(VISIBLE);
        mLeftImg.setVisibility(INVISIBLE);
    }

    /**
     * 设置右按钮显示的文字
     *
     * @param text 文字内容
     */
    public void setRightText(String text) {
        mRightText.setText(text);
        mRightText.setVisibility(VISIBLE);
        mRightImg.setVisibility(INVISIBLE);
    }

    /**
     * 设置左侧按钮的图标
     *
     * @param resource
     */
    public void setLeftRes(int resource) {
        mLeftImg.setImageResource(resource);
        mLeftText.setVisibility(INVISIBLE);
        mLeftImg.setVisibility(VISIBLE);
    }

    /**
     * 设置右侧按钮的图标
     *
     * @param resource
     */
    public void setRightRes(int resource) {
        mRightImg.setImageResource(resource);
        mRightText.setVisibility(INVISIBLE);
        mRightImg.setVisibility(VISIBLE);
    }

    /**
     * 设置标题栏显示的文字
     *
     * @param title 文字内容
     */
    public void setTitleText(String title) {
        mTitleText.setText(title);
    }


    /**
     * dp转px
     *
     * @param context
     * @return
     */
    public int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @return
     */
    public int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }
}