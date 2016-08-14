package com.ahao.game2048.ui.activity;

import com.ahao.androidlib.ui.activity.BaseActivity;
import com.ahao.androidlib.ui.fragment.BaseFragment;
import com.ahao.game2048.R;
import com.ahao.game2048.ui.fragment.MainFragment;

public class MainActivity extends BaseActivity {
    @Override
    protected BaseFragment getFirstFragment() {
        return MainFragment.newInstance();
    }

    @Override
    protected int getActLayoutId() {
        return R.layout.act_base;
    }

    @Override
    protected int getFragContentLayoutId() {
        return R.id.main_frame;
    }
}
