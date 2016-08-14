package com.ahao.game2048.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.ScaleAnimation;

/**
 * Created by Avalon on 2016/8/5.
 */
public class GameItemView extends View {
    private static final String TAG = "GameItemView";
    private static final SparseArray<Integer> mBgColors = new SparseArray<Integer>();
    private static final SparseArray<Integer> mTextColors = new SparseArray<Integer>();

    /** 初始化颜色 */
    static {
        mBgColors.put(0     , Color.parseColor("#000000"));
        mBgColors.put(2     , Color.parseColor("#eee4da"));
        mBgColors.put(4     , Color.parseColor("#eddfc3"));
        mBgColors.put(8     , Color.parseColor("#f2b179"));
        mBgColors.put(16    , Color.parseColor("#f59d65"));
        mBgColors.put(32    , Color.parseColor("#f57f60"));
        mBgColors.put(64    , Color.parseColor("#f5623d"));
        mBgColors.put(128   , Color.parseColor("#edcf72"));
        mBgColors.put(256   , Color.parseColor("#edcc61"));
        mBgColors.put(512   , Color.parseColor("#edc850"));
        mBgColors.put(1024  , Color.parseColor("#edc53f"));
        mBgColors.put(2048  , Color.parseColor("#edc22e"));
        mBgColors.put(4096  , Color.parseColor("#3c3a32"));
        mBgColors.put(8192  , Color.parseColor("#3c3a32"));
        mBgColors.put(16384 , Color.parseColor("#3c3a32"));

        mTextColors.put(2     , Color.parseColor("#8b8279"));
        mTextColors.put(4     , Color.parseColor("#776e65"));
        mTextColors.put(8     , Color.parseColor("#f9f6f2"));
        mTextColors.put(16    , Color.parseColor("#f9f5f1"));
        mTextColors.put(32    , Color.parseColor("#f9f6f2"));
        mTextColors.put(64    , Color.parseColor("#f8f5ef"));
        mTextColors.put(128   , Color.parseColor("#f8f5ee"));
        mTextColors.put(256   , Color.parseColor("#f6efd9"));
        mTextColors.put(512   , Color.parseColor("#f8f5f1"));
        mTextColors.put(1024  , Color.parseColor("#f9f6f2"));
        mTextColors.put(2048  , Color.parseColor("#f9f6f2"));
        mTextColors.put(4096  , Color.parseColor("#f9f6f2"));
        mTextColors.put(8192  , Color.parseColor("#f9f6f2"));
        mTextColors.put(16384 , Color.parseColor("#f9f6f2"));
    }

    /** 主画笔 */
    private Paint mPaint;

    /** 背景颜色 */
    private int mBgColor;
    /** 圆角大小 */
    private int mRadius = 5;

    /** item的数字*/
    private int mNumber;
    /** 数字转化为String类型*/
    private String mText;
    /** 绘制文字的画笔 */
    private Paint mTextPaint;
    /** 字体大小 */
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 23, getResources().getDisplayMetrics());
    /** 文字范围高度 */
    private int mTextHeight;
    /** 文字范围宽度 */
    private int mTextWidth;

    public GameItemView(Context context) {
        super(context);
        initView();
    }

    /** 初始化View */
    private void initView() {
        /** 初始化背景颜色 */
        mBgColor = mBgColors.get(0);

        /** 初始化主画笔 */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);

        /** 初始化文字画笔 */
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setFakeBoldText(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawNumber(canvas);
    }

    /** 绘制背景 */
    private void drawBackground(Canvas canvas) {
        if(getNumber()>0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(0, 0, getWidth(), getHeight(), mRadius, mRadius, mPaint);
            } else {
                canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
            }
        }
    }

    /** 绘制数字*/
    private void drawNumber(Canvas canvas){
        if(getNumber()>0){
            float x = (getWidth()-mTextWidth)*1.0f/2;
            float y = getHeight()*1.0f/2+mTextHeight*1.0f/2;
            canvas.drawText(mText, x, y, mTextPaint);
        }
    }

    /** 设置数字*/
    public void setNumber(int number) {
        /** 为0则隐藏 */
        if(number==0) {
            mText = "";
            setVisibility(INVISIBLE);
        } else {
            mText = number + "";
            setVisibility(VISIBLE);
            if(mNumber<=0) {
                ScaleAnimation sa = new ScaleAnimation(0, 1, 0, 1, getWidth() / 2, getHeight() / 2);
                setAnimation(sa);
                sa.setDuration(100);
                startAnimation(sa);
            }
        }
        this.mNumber = number;

        /** 根据数字设置背景颜色 */
        mBgColor = mBgColors.get(number, mBgColors.get(0));
        mPaint.setColor(mBgColor);

        /** 根据数字设置文字画笔 */
        if(number>1000){
            mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 18, getResources().getDisplayMetrics());
        }else if(number>100){
            mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 25, getResources().getDisplayMetrics());
        } else {
            mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 28, getResources().getDisplayMetrics());
        }
        mTextPaint.setTextSize(mTextSize);
        Paint.FontMetricsInt textMetrics = mTextPaint.getFontMetricsInt();
        mTextHeight = (int) Math.ceil(textMetrics.leading-textMetrics.ascent);
        mTextWidth = (int) mTextPaint.measureText(mText);
        mTextPaint.setColor(mTextColors.get(number, mTextColors.get(2)));

        /** 重绘 */
        invalidate();
    }

    public int getNumber(){
        return mNumber;
    }

    public void setRadius(int radius) {
        this.mRadius = radius;
        invalidate();
    }
}
