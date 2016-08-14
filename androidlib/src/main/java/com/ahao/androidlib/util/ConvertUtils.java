package com.ahao.androidlib.util;

import android.support.annotation.CheckResult;

/**
 * Created by Avalon on 2016/8/9.
 */
public class ConvertUtils {
    private ConvertUtils(){}

    /**
     * int 转 boolean
     * @param integer 要转换的值
     * @return  返回转换后的值
     */
    @CheckResult
    public static boolean int2boolean(int integer) {
        return integer != 0;
    }

    /**
     * boolean 转 int
     * @param bool 要转换的值
     * @return  返回转换后的值
     */
    @CheckResult
    public static int boolean2int(boolean bool) {
        return bool ? 1 : 0;
    }

    /**
     * 安全地解析int类型
     * @param str 要解析的字符串
     * @param defaultValue  解析失败返回的默认值
     * @return 返回解析成功的int类型
     */
    @CheckResult
    public static int parseIntSafely(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    /**
     * 安全地解析int类型
     * @param str 要解析的字符串
     * @param defaultValue  解析失败返回的默认值
     * @return 返回解析成功的int类型
     */
    @CheckResult
    public static int parseIntSafely(CharSequence str, int defaultValue) {
        try {
            return Integer.parseInt(String.valueOf(str));
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    /**
     * 安全地解析long类型
     * @param str 要解析的字符串
     * @param defaultValue  解析失败返回的默认值
     * @return 返回解析成功的long类型
     */
    @CheckResult
    public static long parseLongSafely(String str, long defaultValue) {
        try {
            return Long.parseLong(str);
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    /**
     * 安全地解析float类型
     * @param str 要解析的字符串
     * @param defaultValue  解析失败返回的默认值
     * @return 返回解析成功的float类型
     */
    @CheckResult
    public static float parseFloatSafely(String str, float defaultValue) {
        try {
            return Float.parseFloat(str);
        } catch (Throwable e) {
            return defaultValue;
        }
    }



}
