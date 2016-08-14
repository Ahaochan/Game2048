package com.ahao.game2048.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahao.androidlib.ui.fragment.BaseFragment;
import com.ahao.androidlib.util.IntentUtils;
import com.ahao.game2048.R;

/**
 * Created by Avalon on 2016/8/14.
 */
public class AboutFragment extends BaseFragment {

    private ImageButton backBtn;
    private TextView githubText;

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int setLayoutId() {
        return R.layout.frag_about;
    }

    @Override
    protected void initView(View rootView) {
        githubText = (TextView) rootView.findViewById(R.id.text_github);
        backBtn = (ImageButton) rootView.findViewById(R.id.btn_back);
    }

    @Override
    protected void setListener(View rootView) {
        githubText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(IntentUtils.openWebDefault(githubText.getText().toString()));
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AboutFragment.this.removeFragment();
            }
        });
    }
}
