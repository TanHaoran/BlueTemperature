package com.jerry.bluetemperature.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.jerry.bluetemperature.R;
import com.jerry.bluetemperature.util.DensityUtils;

/**
 * Created by Jerry on 2016/5/19.
 */
public class HorizontalProgressBar extends ProgressBar {

    private static final int DEFAULT_FINISH_COLOR = 0xFF0A8B15;
    private static final float DEFAULT_FINISH_HEIGHT = 2;
    private static final int DEFAULT_UNFINISH_COLOR = 0xFF8DC8FB;
    private static final float DEFAULT_UNFINISH_HEIGHT = 2;
    private static final float DEFAULT_TEXT_SIZE = 16;
    private static final int DEFAULT_TEXT_COLOR = 0xFF0A8B15;
    private static final float DEFAULT_TEXT_MARGIN = 10;

    protected int mFinishColor;
    protected float mFinishHeight;
    protected int mUnFinishColor;
    protected float mUnFinishHeight;
    protected float mTextSize;
    protected int mTextColor;
    protected float mTextMargin;

    // 控件实际的宽度
    protected int mWidth;

    protected Paint mPaint = new Paint();

    protected boolean mNoNeedUnFinish = false;


    public HorizontalProgressBar(Context context) {
        this(context, null);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.HorizontalProgressBar);

        mFinishColor = ta.getColor(R.styleable.HorizontalProgressBar_finishColor, DEFAULT_FINISH_COLOR);
        mFinishHeight = ta.getDimension(R.styleable.HorizontalProgressBar_finishHeight, DensityUtils.dp2px(context, DEFAULT_FINISH_HEIGHT));
        mUnFinishColor = ta.getColor(R.styleable.HorizontalProgressBar_unFinishColor, DEFAULT_UNFINISH_COLOR);
        mUnFinishHeight = ta.getDimension(R.styleable.HorizontalProgressBar_unFinishHeight, DensityUtils.dp2px(context, DEFAULT_UNFINISH_HEIGHT));
        mTextSize = ta.getDimension(R.styleable.HorizontalProgressBar_textSize, DensityUtils.sp2px(context, DEFAULT_TEXT_SIZE));
        mTextColor = ta.getColor(R.styleable.HorizontalProgressBar_textColor, DEFAULT_TEXT_COLOR);
        mTextMargin = ta.getDimension(R.styleable.HorizontalProgressBar_textMargin, DensityUtils.dp2px(context, DEFAULT_TEXT_MARGIN));
        // 回收资源
        ta.recycle();
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 宽度我们认为必须指定具体的值
        int width = MeasureSpec.getSize(widthMeasureSpec);
        // 通过测量值和测量模式我们自己算出高度值
        int height = MeasureHeight(heightMeasureSpec);
        // 将测量好的值进行设置
        setMeasuredDimension(width, height);
        // 取得除去padding左右之后实际控件的宽度值
        mWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();

    }

    /**
     * 通过测量值和测量模式我们自己算出高度值
     *
     * @param heightMeasureSpec 高度的测量spec
     * @return
     */
    private int MeasureHeight(int heightMeasureSpec) {
        int height = 0;
        int size = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        // 如果是具体指定的值，如：match_parent或者具体的值，直接使用size就可以了
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            // 测量中间文字的高度
            mPaint.setTextSize(mTextSize);
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            height = (int) (getPaddingTop() + getPaddingBottom() +
                    Math.max(Math.max(mFinishHeight, mUnFinishHeight), textHeight));
            // 如果是AT_MOST，意思是最大不可以超过这个值，我们就取两者的最小值
            if (mode == MeasureSpec.AT_MOST) {
                height = Math.min(size, height);
            }
        }
        return height;
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        // 首先保存canvas的状态，在移动画布绘制完成后用于恢复
        canvas.save();
        // 移动画布要中心位置
        canvas.translate(getPaddingLeft(), getHeight() / 2);


        // 获取文字的宽度
        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);

        // 获得完成进度条实际要绘制的宽度
        float value = getProgress() * 1.0f / getMax();
        float finishWidth = mWidth * value - mTextMargin;

        // 如果绘制完成区域加文本大于总宽度的时候，绘制完成区域就不用增加了
        if (finishWidth + textWidth + mTextMargin > mWidth) {
            finishWidth = mWidth - textWidth - mTextMargin;
            // 此时不用绘制进度条右侧部分了
            mNoNeedUnFinish = true;
        }

        // 开始绘制：
        // 1. 绘制进度条左侧的部分（当这个值是正值的时候才绘制）
        if (finishWidth > 0) {
            mPaint.setColor(mFinishColor);
            mPaint.setStrokeWidth(mFinishHeight);
            canvas.drawLine(0, 0, finishWidth, 0, mPaint);
        }
        // 2. 绘制文字
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, finishWidth + mTextMargin, y, mPaint);
        // 3. 绘制进度条右侧部分
        if (!mNoNeedUnFinish) {
            mPaint.setColor(mUnFinishColor);
            mPaint.setStrokeWidth(mUnFinishHeight);
            canvas.drawLine(finishWidth + mTextMargin * 2 + textWidth, 0, mWidth, 0, mPaint);
        }
        // 恢复画布的状态
        canvas.restore();
    }
}
