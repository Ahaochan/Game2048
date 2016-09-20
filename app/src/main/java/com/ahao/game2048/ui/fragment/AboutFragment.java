package com.ahao.game2048.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahao.androidlib.ui.fragment.BaseFragment;
import com.ahao.androidlib.util.IntentUtils;
import com.ahao.game2048.R;
import com.ahao.game2048.common.Common;

/**
 * Created by Avalon on 2016/8/14.
 */
public class AboutFragment extends BaseFragment {

    private ImageButton backBtn;
    private TextView githubText;
    private TextView part1Text;
    private TextView part2Text;
    private TextView titleText;

    private boolean isHa;

    public static AboutFragment newInstance(boolean isHa) {
        Bundle args = new Bundle();
        args.putBoolean(Common.HA_MODE, isHa);
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
        backBtn = (ImageButton) rootView.findViewById(R.id.btn_back);
        titleText = (TextView) rootView.findViewById(R.id.text_about_title);
        part1Text = (TextView) rootView.findViewById(R.id.text_about_plant1);
        part2Text = (TextView) rootView.findViewById(R.id.text_about_plant2);
        githubText = (TextView) rootView.findViewById(R.id.text_github);

        isHa = getArguments().getBoolean(Common.HA_MODE, false);

        titleText.setText(getString(isHa ? R.string.About_2048_ha : R.string.About_2048));
        part1Text.setText(getString(isHa ? R.string.About_2048_part1_ha : R.string.About_2048_part1));
        part2Text.setText(getString(isHa ? R.string.About_2048_part2_ha : R.string.About_2048_part2));
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
