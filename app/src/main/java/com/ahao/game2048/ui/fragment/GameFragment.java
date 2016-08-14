package com.ahao.game2048.ui.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahao.androidlib.ui.fragment.BaseFragment;
import com.ahao.game2048.R;
import com.ahao.game2048.common.Common;
import com.ahao.game2048.ui.view.GameLayout;

/**
 * Created by Avalon on 2016/8/5.
 */
public class GameFragment extends BaseFragment {
    private static final String TAG = "GameFragment";
    private static final int TIMER = 0b00000001;

    /** 两种游戏模式 */
    private GameLayout timeGameLayout, classicGameLayout;
    /** 当前游戏模式,强引用两种游戏模式 */
    private GameLayout currentLayout;
    /** 返回按钮,重置按钮 */
    private ImageButton backBtn, refreshBtn;
    /** 当前分数TextView, 最佳分数TextView */
    private TextView scoreText, bestText;
    /** 移动步数TextView, 所用时间TextView */
    private TextView movesText, timeText;
    /** 游戏结束的提示TextView */
    private TextView tipText;

    /** 记录所用时间 */
    private int time;
    /** 是否为时间模式 */
    private boolean isTimeMode;

    public static GameFragment newInstance(boolean isTimeMode) {
        Bundle args = new Bundle();
        args.putBoolean(Common.GAME_MODE, isTimeMode);
        GameFragment fragment = new GameFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.frag_game;
    }

    @Override
    protected void initView(View rootView) {
        timeGameLayout = (GameLayout) rootView.findViewById(R.id.layout_time_game);
        classicGameLayout = (GameLayout)rootView.findViewById(R.id.layout_classic_game);
        scoreText  = (TextView)    rootView.findViewById(R.id.text_score);
        bestText   = (TextView)    rootView.findViewById(R.id.text_best);
        backBtn    = (ImageButton) rootView.findViewById(R.id.btn_back);
        refreshBtn = (ImageButton) rootView.findViewById(R.id.btn_refresh);
        movesText  = (TextView)    rootView.findViewById(R.id.text_moves);
        timeText   = (TextView)    rootView.findViewById(R.id.text_time);
        tipText   = (TextView)    rootView.findViewById(R.id.text_tip);

        /** 获取游戏模式 */
        Bundle args = getArguments();
        if(args!=null){
            isTimeMode = args.getBoolean(Common.GAME_MODE, false);
        }

        /** 根据游戏模式, 对布局进行隐藏显示, 并强引用使用的布局*/
        if(isTimeMode){
            timeGameLayout.setVisibility(View.VISIBLE);
            classicGameLayout.setVisibility(View.GONE);
            currentLayout = timeGameLayout;
        } else {
            timeGameLayout.setVisibility(View.GONE);
            classicGameLayout.setVisibility(View.VISIBLE);
            currentLayout = classicGameLayout;
        }

        /** 获取当前游戏模式的最佳分数并显示 */
        int bestScore = currentLayout.getBestScore();
        bestText.setText(convertScore(bestScore));
        /** 获取当前游戏模式的上一次的分数并显示 */
        int nowScore = currentLayout.getScore();
        scoreText.setText(convertScore(nowScore));

        /** 获取当前游戏模式的上一次的步数并显示 */
        movesText.setText(currentLayout.getMoves()+" moves");

        /** 获取当前游戏模式的上一次的时间并显示 */
        time = currentLayout.getTime();
        timeText.setText(convertTime(time));

        /** 隐藏游戏结束的提示TextView */
        tipText.setVisibility(View.GONE);
    }


    @Override
    protected void setListener(View rootView) {
        currentLayout.setOnGameListener(new GameLayout.OnGameListener() {
            @Override
            public void onGameOver() {
                ObjectAnimator.ofFloat(tipText, "aplha", 0f, 1f).setDuration(1000).start();
                ObjectAnimator.ofFloat(tipText, "translationY", -600f, 1f).setDuration(1000).start();
                tipText.setText(getResources().getString(R.string.game_over));
                tipText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onWin() {
                ObjectAnimator.ofFloat(tipText, "aplha", 0f, 1f).setDuration(1000).start();
                ObjectAnimator.ofFloat(tipText, "translationY", -600f, 1f).setDuration(1000).start();
                tipText.setText(getResources().getString(R.string.game_win));
                tipText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTimeChange(int time) {
                timeText.setText(convertTime(time));
                if(isTimeMode && time>0){
                    currentLayout.addRandomNum();
                }
            }

            @Override
            public void onMove(int moves) {
                movesText.setText(moves+" moves");
                if(tipText.getVisibility()==View.VISIBLE){
                    tipText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onScoreChange(int score, int best) {
                scoreText.setText(convertScore(score));
                bestText.setText(convertScore(best));
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameFragment.this.removeFragment();
            }
        });

        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tipText.setVisibility(View.GONE);
                currentLayout.initItems();
            }
        });
    }

    /** 对时间进行格式化转换, 化为 xx:xx 的类型 */
    private String convertTime(int time) {
        StringBuilder sb = new StringBuilder();
        if (time/60<10){
            sb.append("0");
        }
        sb.append(time / 60).append(":");
        if(time%60<10) {
            sb.append("0");
        }
        sb.append(time % 60);
        return sb.toString();
    }

    /** 对分数进行格式化转换, 化为 xx.x k 的类型 */
    private String convertScore(int score){
        StringBuilder sb = new StringBuilder();
        if(score>10000){
            sb.append(score/1000).append(".");
            sb.append(score%1000/100);
            sb.append("k");
        }else {
            sb.append(score);
        }
        return sb.toString();

    }
}
