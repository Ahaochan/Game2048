package com.ahao.game2048.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ahao.androidlib.ui.fragment.BaseFragment;
import com.ahao.game2048.R;
import com.ahao.game2048.common.Common;

/**
 * Created by Avalon on 2016/8/14.
 */
public class HowPlayFragment extends BaseFragment {

    private TextView titleText, part1Text, part2Text, part3Text, part4Text, part5Text;
    private ImageButton backBtn;

    private boolean isHa;

    public static HowPlayFragment newInstance(boolean isHa) {
        Bundle args = new Bundle();
        args.putBoolean(Common.HA_MODE, isHa);
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
        titleText = (TextView) rootView.findViewById(R.id.text_how_title);
        part1Text = (TextView) rootView.findViewById(R.id.text_how_plant1);
        part2Text = (TextView) rootView.findViewById(R.id.text_how_plant2);
        part3Text = (TextView) rootView.findViewById(R.id.text_how_plant3);
        part4Text = (TextView) rootView.findViewById(R.id.text_how_plant4);
        part5Text = (TextView) rootView.findViewById(R.id.text_how_plant5);
        backBtn   = (ImageButton) rootView.findViewById(R.id.btn_back);

        isHa = getArguments().getBoolean(Common.HA_MODE, false);
        titleText.setText(getString(isHa ? R.string.How_to_play_ha : R.string.How_to_play));
        part1Text.setText(getString(isHa ? R.string.How_to_play_part1_ha : R.string.How_to_play_part1));
        part2Text.setText(getString(isHa ? R.string.How_to_play_part2_ha : R.string.How_to_play_part2));
        part3Text.setText(getString(isHa ? R.string.How_to_play_part3_ha : R.string.How_to_play_part3));
        part4Text.setText(getString(isHa ? R.string.How_to_play_part4_ha : R.string.How_to_play_part4));
        part5Text.setText(getString(isHa ? R.string.How_to_play_part5_ha : R.string.How_to_play_part5));
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
