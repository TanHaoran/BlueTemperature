package com.jerry.bluetemperature.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.util.DensityUtils;

/**
 * Created by Jerry on 2016/5/19.
 */
public class RoundProgressBar extends HorizontalProgressBar {

    private static final float DEFAULT_RADIUS = 20;

    /**
     * 从已完成和未完成中取得的较大的宽度
     */
    private float mMaxWidth;
    private float mRadius;

    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);

        mRadius = ta.getDimension(R.styleable.RoundProgressBar_radius, DensityUtils.dp2px(context, DEFAULT_RADIUS));

        // 回收资源
        ta.recycle();
        init();
    }

    /**
     * 初始化变量
     */
    private void init() {
        // 设置画笔的抗锯齿等属性
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMaxWidth = Math.max(mFinishHeight, mUnFinishHeight);
        int expect = (int) (mMaxWidth + mRadius * 2 + getPaddingLeft() + getPaddingRight());
        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);
        mWidth = Math.min(width, height);
//        mRadius = (mWidth - getPaddingLeft() - getPaddingRight()) / 2;
        setMeasuredDimension(mWidth, mWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(getPaddingLeft() + mMaxWidth / 2, getPaddingTop() + mMaxWidth / 2);
        // 1. 绘制文字
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        canvas.drawText(text, mRadius - textWidth / 2, mRadius + mPaint.descent(), mPaint);
        // 2. 绘制未完成区域
        // 设置画笔的风格用来绘制圆和弧
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(mUnFinishColor);
        mPaint.setStrokeWidth(mUnFinishHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        // 3. 绘制完成的区域
        mPaint.setColor(mFinishColor);
        mPaint.setStrokeWidth(mFinishHeight);
        float angle = getProgress() * 1.0f / getMax() * 360;
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 270, angle, false, mPaint);

        canvas.restore();
    }
}
