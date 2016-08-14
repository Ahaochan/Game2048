package com.ahao.game2048.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.ahao.game2048.R;

/**
 * Created by Avalon on 2016/8/4.
 */
public class IconButton extends View {
    private static final String TAG = IconButton.class.getSimpleName();
    private static final int INVALID_STATE = -1;
    private Context mContext;

    private int mBgColor = Color.GRAY;
    private String mText;
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
    private int mTextColor = Color.BLACK;
    private int mXRadius = 5;
    private int mYRadius = 5;
    private int mMinWidth = 0;
    private int mMaxWidth = Integer.MAX_VALUE;


    private Paint mPaint;

    private Drawable mIconDrawable;
    private Rect mIconRect;

    private Paint mTextPaint;
    private int mTextWidth;
    private int mTextHeight;

    private Paint mRipplePaint;
    private float mTouchX = 0;
    private float mTouchY = 0;
    private float mRippleRadius;
    private float mCurrentRippleRadius = INVALID_STATE;


    public IconButton(Context context) {
        this(context, null);
    }

    public IconButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconButton(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        obtainStyledAttributes(mContext, attrs);

        mRipplePaint = new Paint();
        mRipplePaint.setColor(Color.BLACK);
        mRipplePaint.setAlpha(75);
        mRipplePaint.setAntiAlias(true);

        mPaint = new Paint();
        mPaint.setColor(mBgColor);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);
        mTextPaint.setFakeBoldText(true);
        Paint.FontMetricsInt textMetrics = mTextPaint.getFontMetricsInt();
        mTextHeight = (int) Math.ceil(textMetrics.leading-textMetrics.ascent);
        mTextWidth = (int) mTextPaint.measureText(mText);

    }

    private void obtainStyledAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.IconButton);
        for(int i = 0; i < ta.getIndexCount(); i++){
            int attr = ta.getIndex(i);
            switch (attr){
                case R.styleable.IconButton_icon:
                    mIconDrawable = ta.getDrawable(attr);
                    break;
                case R.styleable.IconButton_bgColor:
                    mBgColor = ta.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.IconButton_text:
                    mText = ta.getString(attr);
                    break;
                case R.styleable.IconButton_textSize:
                    mTextSize = (int) ta.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.IconButton_textColor:
                    mTextColor = ta.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.IconButton_radius:
                    int radius = ta.getInt(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    mXRadius = radius;
                    mYRadius = radius;
                    break;
                case R.styleable.IconButton_xRadius:
                    mXRadius = ta.getInt(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.IconButton_yRadius:
                    mYRadius = ta.getInt(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.IconButton_minWidth:
                    mMinWidth = (int) ta.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 0, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.IconButton_maxWidth:
                    mMaxWidth = (int) ta.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5000, getResources().getDisplayMetrics()));
                    break;
            }
        }
        ta.recycle();
        if(mMinWidth>mMaxWidth){
            throw new IllegalArgumentException("mMinWidth must be smaller than mMaxWidth!");
        }
        if(mXRadius<0 || mYRadius<0){
            throw new IllegalArgumentException("mXRadius or mYRadius must be no-zero!");
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getWidthMeasureSpec(MeasureSpec.getMode(widthMeasureSpec), MeasureSpec.getSize(widthMeasureSpec));
        int height = getHeightMeasureSpec(MeasureSpec.getMode(heightMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        setMeasuredDimension(width, height);
        measureIconRect();

    }

    /** 测量宽度 */
    private int getWidthMeasureSpec(int mode, int size) {
        int width = 0;
        if(mode == MeasureSpec.EXACTLY){
            width = size;
        } else {
            width = mTextWidth+getPaddingLeft()+getPaddingRight();
            width *= 1.6f;

            if(width<mMinWidth){
                width = mMinWidth;
            } else if(width > mMaxWidth) {
                width = mMaxWidth;
            }
            if(mode == MeasureSpec.AT_MOST){
                width = Math.min(width, size);
            }
        }
        return width;
    }

    /** 测量高度 */
    private int getHeightMeasureSpec(int mode, int size) {
        int height = 0;
        if(mode == MeasureSpec.EXACTLY){
            height = size;
        } else {
            height = mTextHeight + getPaddingTop() + getPaddingBottom();
            height *= 1.2;
            if(mode == MeasureSpec.AT_MOST){
                height = Math.min(height, size);
            }
        }
        return height;
    }

    /** 测量图标宽高 */
    private void measureIconRect() {
        if(mIconDrawable != null) {
            int iconSize = Math.max(mIconDrawable.getIntrinsicWidth(), mIconDrawable.getIntrinsicHeight());
            int left = getPaddingLeft();
            int top = (int) (getMeasuredHeight()*1.0f / 2 - iconSize*1.0f / 2);
            mIconRect = new Rect(left, top, left + iconSize, top + iconSize);
            mIconDrawable.setBounds(mIconRect);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        drawIcon(canvas);
        drawText(canvas);
        drawRipple(canvas);
    }

    /** 画圆角矩形 */
    private void drawBackground(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mXRadius,mYRadius,mPaint);
        } else {
            canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        }
    }

    /** 画图标 */
    private void drawIcon(Canvas canvas) {
        if(mIconDrawable!=null) {
            mIconDrawable.draw(canvas);
        }
    }

    /** 画文字 */
    private void drawText(Canvas canvas) {
        int x = (int) ((getMeasuredWidth()*1.0f)/2-mTextWidth*1.0f/2);
        int y = (int) ((getMeasuredHeight()*1.0f/2+mTextHeight*1.0f/2)*0.95);

//        Log.i(TAG, "drawText: "+mText+":"+(y-mTextHeight)+","+(getMeasuredHeight()-y));
        canvas.drawText(mText, x, y, mTextPaint);
    }

    /** 画波纹点击效果 */
    private void drawRipple(Canvas canvas) {
        if(mCurrentRippleRadius < mRippleRadius) {
            mCurrentRippleRadius += 25;
            canvas.drawCircle(mTouchX, mTouchY, mCurrentRippleRadius, mRipplePaint);
            invalidate();
        } else {
            mCurrentRippleRadius = INVALID_STATE;
            mRippleRadius = INVALID_STATE;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mTouchX = event.getX();
                mTouchY = event.getY();
                measureRippleRadius(mTouchX, mTouchY);
                invalidate();
                break;
        }
        return super.onTouchEvent(event);
    }

    /** 测量最大圆半径 */
    private void measureRippleRadius(float mTouchX, float mTouchY) {
        if(mTouchX<=getMeasuredWidth()/2){
            if(mTouchY<=getMeasuredHeight()/2){
                mRippleRadius = (float) Math.hypot(getMeasuredWidth()-mTouchX, getMeasuredHeight()-mTouchY);
            } else {
                mRippleRadius = (float) Math.hypot(getMeasuredWidth()-mTouchX, mTouchY);
            }
        } else {
            if(mTouchY<=getMeasuredHeight()/2){
                mRippleRadius = (float) Math.hypot(mTouchX, getMeasuredHeight()-mTouchY);
            } else {
                mRippleRadius = (float) Math.hypot(mTouchX, mTouchY);
            }
        }
    }

}
