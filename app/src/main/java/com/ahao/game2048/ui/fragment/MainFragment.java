package com.ahao.game2048.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.ahao.androidlib.ui.fragment.BaseFragment;
import com.ahao.game2048.R;
import com.ahao.game2048.ui.view.IconButton;

/**
 * Created by Avalon on 2016/8/4.
 */
public class MainFragment extends BaseFragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    private IconButton mClassicBtn;
    private IconButton mTimerBtn;
    private IconButton mHowBtn;
    private IconButton mAboutBtn;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    protected void initView(View rootView) {
        mClassicBtn = (IconButton) rootView.findViewById(R.id.btn_classic);
        mTimerBtn   = (IconButton) rootView.findViewById(R.id.btn_timer);
        mHowBtn     = (IconButton) rootView.findViewById(R.id.btn_how);
        mAboutBtn   = (IconButton) rootView.findViewById(R.id.btn_about);
    }

    @Override
    protected void setListener(View rootView) {
        mClassicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(GameFragment.newInstance(false));
            }
        });
        mTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(GameFragment.newInstance(true));
            }
        });
        mHowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(HowPlayFragment.newInstance());
            }
        });
        mAboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(AboutFragment.newInstance());
            }
        });
    }
}
