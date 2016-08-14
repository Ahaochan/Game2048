package com.ahao.game2048.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.ahao.androidlib.ui.fragment.BaseFragment;
import com.ahao.game2048.R;

/**
 * Created by Avalon on 2016/8/14.
 */
public class HowPlayFragment extends BaseFragment {

    private ImageButton backBtn;

    public static HowPlayFragment newInstance() {
        Bundle args = new Bundle();
        HowPlayFragment fragment = new HowPlayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.frag_how_play;
    }

    @Override
    protected void initView(View rootView) {
        backBtn = (ImageButton) rootView.findViewById(R.id.btn_back);
    }

    @Override
    protected void setListener(View rootView) {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HowPlayFragment.this.removeFragment();
            }
        });
    }
}
