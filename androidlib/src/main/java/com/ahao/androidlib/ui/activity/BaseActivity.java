package com.ahao.androidlib.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.ahao.androidlib.ui.fragment.BaseFragment;

/**
 * Created by Avalon on 2016/5/12.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private final static String TAG = BaseActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getActLayoutId());
        /** 避免重复添加Fragment */
        if (getSupportFragmentManager().getFragments() == null) {
            BaseFragment firstFragment = getFirstFragment();
            if (firstFragment != null) {
                addFragment(firstFragment);
            }
        }

    }

    /** 获取第一个fragment */
    protected abstract BaseFragment getFirstFragment();

    /** 获得Activity的布局id */
    protected abstract int getActLayoutId();

    /** 布局中被Fragment填充的Layout的ID */
    protected abstract int getFragContentLayoutId();


    /** 获得FragmentManager */
    public FragmentManager getBaseFragmentManager(){
        return getSupportFragmentManager();
    }

    /** 添加fragment */
    public void addFragment(BaseFragment fragment) {
        if (fragment != null) {
            getBaseFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                    .replace(getFragContentLayoutId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commitAllowingStateLoss();
        }
    }

    /** 移除fragment */
    public void removeFragment() {
        if (getFragmentCount() > 1) {
            getBaseFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    /** 获得当前Fragment的数量 */
    public int getFragmentCount(){
        return getBaseFragmentManager().getBackStackEntryCount();
    }

    /** 监听返回键 */
    @Override
    public void onBackPressed() {
        if (getFragmentCount() <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
