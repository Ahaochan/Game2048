package com.ahao.androidlib.util;

import android.content.Intent;
import android.net.Uri;

/**
 * Created by Avalon on 2016/8/14.
 */
public class IntentUtils {
    private IntentUtils(){}


    public static Intent openWebDefault(String uri){
        Uri content_url = Uri.parse(uri);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(content_url);
        return intent;
    }
}
