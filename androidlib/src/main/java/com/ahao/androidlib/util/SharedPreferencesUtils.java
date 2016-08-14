package com.ahao.androidlib.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Avalon on 2016/5/17.
 */
public class SharedPreferencesUtils {

    //保存的SharedPreferences文件名
    private static Context mContext;
    private static String mFileName;
    private static Builder mBuilder;
    /**
     *
     * @param fileName 初始化保存的SharedPreferences文件名
     */
    public static Builder init(Context context, String fileName){
        mContext = context;
        mFileName = fileName;
        if(mBuilder == null){
            synchronized (SharedPreferencesUtils.class){
                if(mBuilder == null){
                    mBuilder = new Builder();
                }
            }
        }
        return mBuilder;
    }

    public static class Builder {
        /**
         *
         * @param key       存储的key
         * @param value     存储的value
         * @throws Exception 异常
         */
        public Builder put(String key, Object value){
            if(mFileName == null) {
                throw new NullPointerException("SharedPreferencesUtils not init!");
            }
            String type = value.getClass().getSimpleName();
            SharedPreferences sp = mContext.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();

            switch (type){
                case "Boolean":editor.putBoolean(key, (Boolean) value);break;
                case "Integer":editor.putInt    (key, (Integer) value);break;
                case "Float":  editor.putFloat  (key, (Float)   value);break;
                case "Long":   editor.putLong   (key, (Long)    value);break;
                case "String": editor.putString (key, (String)  value);break;
                default:throw new ClassCastException("Object type is illegal!");
            }
            editor.apply();
            return mBuilder;
        }


        public Object get(String key, Object defaultObject) {
            if(mFileName == null) {
                throw new NullPointerException("SharedPreferencesUtils not init!");
            }
            String type = defaultObject.getClass().getSimpleName();
            SharedPreferences sp = mContext.getSharedPreferences(mFileName, Context.MODE_PRIVATE);
            switch (type){
                case "Boolean":return sp.getBoolean(key, (Boolean) defaultObject);
                case "Integer":return sp.getInt    (key, (Integer) defaultObject);
                case "Float":  return sp.getFloat  (key, (Float)   defaultObject);
                case "Long":   return sp.getLong   (key, (Long)    defaultObject);
                case "String": return sp.getString (key, (String)  defaultObject);
                default:throw new ClassCastException("Object type is illegal!");
            }
        }
    }





}
