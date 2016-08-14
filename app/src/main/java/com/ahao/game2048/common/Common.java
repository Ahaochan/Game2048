package com.ahao.game2048.common;

/**
 * Created by Avalon on 2016/8/13.
 */
public class Common {
    private Common(){}

    public static final String SP_NAME    = "spName";//SharedPreferences的文件名
    public static final String IS_CREATE  = "isCreate";//创建布局
    public static final String GAME_START = "game_start";//开始游戏
    public static final String SCORE_BEST = "bestScore";//最佳分数
    public static final String SCORE_NOW  = "nowScore";//当前分数
    public static final String ITEM       = "item";//每个item的数值
    public static final String MOVES      = "moves";//移动步数
    public static final String TIME       = "time";//所用时间
    public static final String TIPWIN     = "tip_win";//是否提醒胜利

    public static final String GAME_MODE  = "game_mode";//游戏模式
    public static final String GAME_CLASSIC_MODE = "classic_mode";//传统模式
    public static final String GAME_TIME_MODE = "time_mode";//时间模式
}
