package com.ahao.game2048.ui.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.widget.GridLayout;

import com.ahao.androidlib.util.ArrayUtils;
import com.ahao.androidlib.util.MathUtils;
import com.ahao.androidlib.util.SharedPreferencesUtils;
import com.ahao.game2048.R;
import com.ahao.game2048.common.Common;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Avalon on 2016/8/5.
 */
public class GameLayout extends GridLayout implements ViewTreeObserver.OnGlobalLayoutListener{
    private static final String TAG = GameLayout.class.getSimpleName();

    private static final int INVALID = -1;
    private static final int DEFAULT_COLUMN = 4;
    private static final int DEFAULT_GAP = 10;
    private static final int MOVE_DOWN  = 0b00000000;
    private static final int MOVE_UP    = 0b00000001;
    private static final int MOVE_LEFT  = 0b00000010;
    private static final int MOVE_RIGHT = 0b00000011;


    private Context mContext;
    /** SharedPreference存储文件名 */
    private String spName = Common.SP_NAME;
    /** 是否创建布局 */
    private boolean isCreateLayout;
    /** 是否游戏开始 */
    private boolean isGameStart;
    /** 是否提醒游戏胜利 */
    private boolean isTipWin;
    /** GridLayout列数*/
    private int mColumn = DEFAULT_COLUMN;
    /** item */
    private GameItemView[] mItems;

    private Paint mPaint;
    /** item和GridLayout的背景圆角半径 */
    private int mRadius = 5;
    /** GridLayout的背景颜色 */
    private int mBgColor = Color.parseColor("#a49381");

    /** item的大小 */
    private int mItemSize;
    /** item之间的间隔 */
    private int mGap = DEFAULT_GAP;
    /** item的背景颜色 */
    private int mItemBgColor = Color.parseColor("#beb0a0");

    /** 最高分数 */
    private int mBestScore;
    /** 分数 */
    private int mScore;
    /** 移动步数 */
    private int mMoves;
    /** 所用时间 */
    private int mTime;

    /** 滑动手势监听器 */
    private GestureDetector mGestureDetector;
    private OnGameListener onGameListener;

    /** 计时功能 */
    private static final int TIME_MSG = 0b00000001;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case TIME_MSG:
                    if(onGameListener!=null && isGameStart) {
                        onGameListener.onTimeChange(mTime);
                    }
                    break;
            }
        }
    };


    /** 游戏监听器 */
    public interface OnGameListener{
        void onGameOver();
        void onTimeChange(int time);
        void onMove(int moves);
        void onScoreChange(int score, int best);
        void onWin();
    }

    public void setOnGameListener(OnGameListener onGameListener) {
        this.onGameListener = onGameListener;
    }

    public GameLayout(Context context) {
        this(context, null);
    }

    public GameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        obtainStyledAttributes(context, attrs);
        initView();
        setListener();
        restoreItems();
    }

    /** 从xml文件获取属性 */
    private void obtainStyledAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.GameLayout);
        for (int i = 0; i < ta.getIndexCount(); i++) {
            int attr = ta.getIndex(i);
            switch (attr){
                case R.styleable.GameLayout_radius:
                    mRadius = ta.getInt(attr, 5);
                    break;
                case R.styleable.GameLayout_bgColor:
                    mBgColor = ta.getColor(attr, Color.parseColor("#a49381"));
                    break;
                case R.styleable.GameLayout_gap:
                    mGap = (int) ta.getDimension(attr, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.GameLayout_sharedPreference:
                    spName = ta.getString(attr);
                    break;
            }
        }
        ta.recycle();
    }

    /** 初始化View*/
    private void initView() {
        /** 初始化GridLayout */
        setColumnCount(DEFAULT_COLUMN);
        setFocusable(true);
        setFocusableInTouchMode(true);

        /** 初始化画笔 */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.FILL);

        /** 初始化子Item */
        mItems = new GameItemView[mColumn*mColumn];
        for (int i = 0; i < mItems.length; i++) {
            mItems[i] = new GameItemView(mContext);
            mItems[i].setRadius(mRadius);
            mItems[i].setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }

        /** 初始化计时器 */
        timerTask = new TimerTask() {
            @Override
            public void run() {
                mTime++;
                handler.sendEmptyMessage(TIME_MSG);
            }
        };
        timer.schedule(timerTask, 0, 1000);
    }

    /** 设置监听器 */
    private void setListener() {
        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener(){
            private final static int FLING_MIN_DISTANCE = 50;
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                float x = e2.getX() - e1.getX();
                float y = e2.getY() - e1.getY();
                boolean canMove = false;
                /** 滑动大于最小滑动距离时,开始滑动 */
                if (x > FLING_MIN_DISTANCE && MathUtils.compare(x, y, true) > 0) {
                    canMove = flingTo(MOVE_RIGHT);
                } else if (x < -FLING_MIN_DISTANCE && MathUtils.compare(x, y, true) > 0) {
                    canMove = flingTo(MOVE_LEFT);
                } else if (y > FLING_MIN_DISTANCE && MathUtils.compare(x, y, true) < 0) {
                    canMove = flingTo(MOVE_DOWN);
                } else if (y < -FLING_MIN_DISTANCE && MathUtils.compare(x, y, true) < 0) {
                    canMove = flingTo(MOVE_UP);
                }


                if(canMove) { /** 发生了滑动 */
                    mMoves++;
                    onGameListener.onMove(mMoves);
                    addRandomNum();
                } else if(isGameOver()){ /** 考虑没有发生滑动, 但是游戏还没有结束的情况 */
                    isGameStart = false;
                    onGameListener.onGameOver();
                }

                /** 已经完成2048且提示过一次 */
                if(is2048() && onGameListener!=null && !isTipWin){
                    onGameListener.onWin();
                    isTipWin = true;
                }

                return true;
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
        int length = Math.min(MeasureSpec.getSize(widthSpec), MeasureSpec.getSize(heightSpec));
        int padding = MathUtils.min(getPaddingTop(),getPaddingBottom(),getPaddingLeft(),getPaddingRight());
        mItemSize = (length - 2*padding - (mColumn + 1) * mGap) / mColumn;
        setMeasuredDimension(length, length);
    }

    @Override
    public void onGlobalLayout() {
        /** 添加子View,只执行一次 */
        if(!isCreateLayout) {
            removeAllViews();
            for (int i = 0; i < mItems.length; i++) {
                GridLayout.Spec row = GridLayout.spec(i/mColumn);
                GridLayout.Spec column = GridLayout.spec(i%mColumn);
                GridLayout.LayoutParams lp = new GridLayout.LayoutParams(row, column);
                lp.width = mItemSize;
                lp.height = mItemSize;
                lp.rightMargin = mGap;
                lp.bottomMargin = mGap;
                if(i%mColumn==0)    lp.leftMargin = mGap;
                if(i/mColumn==0)    lp.topMargin = mGap;
                lp.setGravity(Gravity.FILL);
                addView(mItems[i], lp);
            }
            isCreateLayout = true;
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        drawBackground(canvas);
        drawItemBackground(canvas);
        super.dispatchDraw(canvas);
    }

    /** 绘制整个背景 */
    private void drawBackground(Canvas canvas) {
        mPaint.setColor(mBgColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawRoundRect(0, 0, getWidth(), getHeight(), mRadius, mRadius, mPaint);
        } else {
            canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        }
    }

    /** 绘制item背景 */
    private void drawItemBackground(Canvas canvas) {
        mPaint.setColor(mItemBgColor);
        for (int i = 0; i < mItems.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                canvas.drawRoundRect(mGap+(i/mColumn)*(mItemSize+mGap), mGap+(i%mColumn)*(mItemSize+mGap),
                        (i/mColumn+1)*(mItemSize+mGap), (i%mColumn+1)*(mItemSize+mGap), mRadius, mRadius, mPaint);
            } else {
                canvas.drawRect(mGap+(i/mColumn)*(mItemSize+mGap), mGap+(i%mColumn)*(mItemSize+mGap),
                        (i/mColumn+1)*(mItemSize+mGap), (i%mColumn+1)*(mItemSize+mGap), mPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isGameStart) {
            mGestureDetector.onTouchEvent(event);
        }

        return true;
    }

    /** 往moveType方向滑动,滑动成功返回true */
    private boolean flingTo(int moveType) {
        boolean canMove = false;
        for (int i = 0; i < mColumn; i++) {
            int[] row = new int[mColumn];
            for (int j = 0; j < mColumn; j++) {
                int index = getIndexByAction(moveType, i, j);
                row[j] = mItems[index].getNumber();
            }

            /** 判断能否滑动, 当出现0的位置在数字之前即可滑动, 比如: 2048可变为2480 */
            int zero = row.length;
            for (int j = 0; j < row.length; j++) {
                if(row[j]==0){
                    zero = j;
                }else if(j>zero){
                    canMove = true;
                    break;
                } else if(j-1>=0 && row[j-1]==row[j]){
                    canMove = true;
                    break;
                }
            }

            /** 记录每个item移动的距离 */
            int[] move = moveAndmerge(row);

            /** 动画移动和实际移动 */
            for (int j = 0; j < mColumn; j++) {
                int index = getIndexByAction(moveType, i, j);
                moveByAnim(index, move[j], row[j], moveType);
                mItems[index].setNumber(row[j]);

            }
        }
        return canMove;
    }

    /** 根据滑动方向获取item的index */
    private int getIndexByAction(int moveType, int i, int j) {
        int index = INVALID;
        switch (moveType){
            case MOVE_DOWN :
                index = (mColumn-1-j)*mColumn+i;
                break;
            case MOVE_UP   :
                index = j*mColumn+i;
                break;
            case MOVE_LEFT :
                index = i*mColumn+j;
                break;
            case MOVE_RIGHT:
                index = i*mColumn+(mColumn-j-1);
                break;
        }
        return index;
    }

    /** 获得合并后的数组，返回每个item移动的距离
     *  使用动态规划算法
     */
    private int[] moveAndmerge(int[] col){
        int[] move = new int[col.length];
        int last = ArrayUtils.lastOtherNum(col, 0, 0);
        int flag = -1;//合并的位置
        /** 获得每个item的移动距离 */
        for(int i = 1; i <= last; i++){
            if(col[i-1]!=0 && col[i]==0){
                move[i] = 1;
            } else if(col[i-1]==0){
                move[i] = move[i-1]+1;
                if(col[i]!=0 && col[i-move[i]]!=0 && col[i]!=col[i-move[i]]){
                    move[i] = move[i]+move[i-move[i]];
                    move[i] = move[i]-1;
                }else if (flag != i-move[i]) {
                    move[i] = move[i]+move[i-move[i]];
                    if(col[i]==col[i-move[i]]){
                        flag = i;
                    }
                }
            }  else if(col[i-1]==col[i] && flag!=i-1){
                move[i] = move[i-1]+1;
                flag = i;
            } else {
                move[i] = move[i-1];
            }
        }

        /** 获得合并后的数组 */
        for(int i = 1; i <= last; i++){
            if(move[i]!=0){
                if(col[i-move[i]]!=0){
                    mScore += col[i]*2;
                    if(mScore>mBestScore){
                        mBestScore = mScore;
                    }
                    onGameListener.onScoreChange(mScore, mBestScore);
                }
                col[i-move[i]] += col[i];
                col[i] = 0;
            }
        }

        return move;
    }

    /** 根据移动位置，设置移动动画 */
    private void moveByAnim(final int index, int move, final int num, int moveType){
        TranslateAnimation anim = null;
        switch (moveType){
            case MOVE_DOWN :
                anim = new TranslateAnimation(0, 0, -move*(mGap+mItemSize), 0);
                break;
            case MOVE_UP   :
                anim = new TranslateAnimation(0, 0, move*(mGap+mItemSize), 0);
                break;
            case MOVE_LEFT :
                anim = new TranslateAnimation(move*(mGap+mItemSize), 0, 0, 0);
                break;
            case MOVE_RIGHT:
                anim = new TranslateAnimation(-move*(mGap+mItemSize), 0, 0, 0);
                break;
        }
        mItems[index].setAnimation(anim);
        anim.setDuration(move*100);
        anim.setFillAfter(false);
        anim.start();
    }

    /** 随机添加一个View */
    public void addRandomNum() {
        if(!isFull()) {
            int index = MathUtils.randomInt(0, mColumn*mColumn);
            if (mItems[index].getNumber() == 0) {
                mItems[index].setNumber(Math.random() > 0.2d ? 2 : 4);
            } else {/** 如果已经存在, 就重新找空位 */
                addRandomNum();
            }
        } else {/** 如果已经填满了, 游戏结束 */
            isGameStart = false;
            onGameListener.onGameOver();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
        /** 停止计时 */
        timer.cancel();
        /** 数据持久化 */
        saveItems();
    }

    /** 判断是否填满空格 */
    public boolean isFull(){
        boolean flag = true;
        for (int i = 0; i < mColumn*mColumn; i++) {
            int num = mItems[i].getNumber();
            if (num == 0) {
                flag = false;
                break;
            }
        }
        return flag;
    }

    /** 判断是否游戏结束，即填满空格且不能滑动 */
    public boolean isGameOver(){
        boolean flag = true;
        if(!isFull()){
            return false;
        }
        for (int i = 0; i < mColumn*mColumn; i++) {
            if((i%mColumn<mColumn-1 && mItems[i].getNumber()==mItems[i+1].getNumber()) ||
                    (i+mColumn<mColumn*mColumn && mItems[i].getNumber()==mItems[i+mColumn].getNumber())){
                flag = false;
                break;
            }
        }
        return flag;
    }

    /** 判断是否存在2048 */
    public boolean is2048(){
        boolean is2048 = false;
        for (int i = 0; i < mItems.length; i++) {
            if(mItems[i].getNumber()==2048){
                is2048 = true;
                break;
            }
        }
        return is2048;
    }

    /** 保存游戏参数 */
    public void saveItems(){
        for (int i = 0; i < mColumn*mColumn; i++) {
            SharedPreferencesUtils.init(mContext, spName).put(Common.ITEM+i, mItems[i].getNumber());
        }
        SharedPreferencesUtils.init(mContext, spName).put(Common.SCORE_BEST, mBestScore);
        SharedPreferencesUtils.init(mContext, spName).put(Common.SCORE_NOW, mScore);
        SharedPreferencesUtils.init(mContext, spName).put(Common.MOVES, mMoves);
        SharedPreferencesUtils.init(mContext, spName).put(Common.TIME, mTime);
        SharedPreferencesUtils.init(mContext, spName).put(Common.GAME_START, isGameStart);
        SharedPreferencesUtils.init(mContext, spName).put(Common.TIPWIN, isGameStart);
    }

    /** 初始化游戏参数 */
    public void initItems(){
        mScore = 0;
        mMoves = 0;
        mTime = 0;
        isCreateLayout = false;
        isGameStart = true;
        isTipWin = false;
        if(onGameListener!=null) {
            onGameListener.onMove(mMoves);
            onGameListener.onTimeChange(mTime);
            onGameListener.onScoreChange(mScore, mBestScore);
        }
        for (int i = 0; i < mItems.length; i++) {
            mItems[i].setNumber(0);
        }
        addRandomNum();
        addRandomNum();
    }

    /** 回复游戏参数 */
    public void restoreItems(){
        for (int i = 0; i < mColumn*mColumn; i++) {
            int num = (int) SharedPreferencesUtils.init(mContext, spName).get(Common.ITEM+i, 0);
            mItems[i].setNumber(num);
        }
        mBestScore = (int) SharedPreferencesUtils.init(mContext, spName).get(Common.SCORE_BEST, 0);
        mScore = (int) SharedPreferencesUtils.init(mContext, spName).get(Common.SCORE_NOW, 0);
        mMoves = (int) SharedPreferencesUtils.init(mContext, spName).get(Common.MOVES, 0);
        mTime = (int) SharedPreferencesUtils.init(mContext, spName).get(Common.TIME, 0);
        isGameStart = (boolean) SharedPreferencesUtils.init(mContext, spName).get(Common.GAME_START, false);
        isTipWin = (boolean) SharedPreferencesUtils.init(mContext, spName).get(Common.TIPWIN, false);
        if(onGameListener!=null) {
            onGameListener.onMove(mMoves);
            onGameListener.onTimeChange(mTime);
            onGameListener.onScoreChange(mScore, mBestScore);
        }
        if(!isGameStart){
            initItems();
        }
    }

    public int getBestScore() {
        return mBestScore;
    }

    public int getScore() {
        return mScore;
    }

    public int getMoves() {
        return mMoves;
    }

    public int getTime() {
        return mTime;
    }

    public void add1024(){
        mItems[0].setNumber(1024);
        mItems[1].setNumber(1024);
    }
}