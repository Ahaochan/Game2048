package com.ahao.game2048.ui.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import com.ahao.androidlib.ui.fragment.BaseFragment;
import com.ahao.androidlib.util.SharedPreferencesUtils;
import com.ahao.game2048.R;
import com.ahao.game2048.common.Common;
import com.ahao.game2048.ui.view.IconButton;

/**
 * Created by Avalon on 2016/8/4.
 */
public class MainFragment extends BaseFragment {
    private static final String TAG = MainFragment.class.getSimpleName();

    private TextView mHaBtn;
    private IconButton mClassicBtn;
    private IconButton mTimerBtn;
    private IconButton mHowBtn;
    private IconButton mAboutBtn;

    private Context mContext;
    private int mClickNum;
    private boolean isHa;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mClickNum = 0;
        isHa = (boolean) SharedPreferencesUtils.init(context, Common.SP_NAME).get(Common.HA_MODE, false);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.frag_main;
    }

    @Override
    protected void initView(View rootView) {
        mHaBtn = (TextView) rootView.findViewById(R.id.btn_ha);
        mClassicBtn = (IconButton) rootView.findViewById(R.id.btn_classic);
        mTimerBtn = (IconButton) rootView.findViewById(R.id.btn_timer);
        mHowBtn = (IconButton) rootView.findViewById(R.id.btn_how);
        mAboutBtn = (IconButton) rootView.findViewById(R.id.btn_about);

        mHaBtn.setText(getString(isHa ? R.string.game_name_ha : R.string.game_name));
    }

    @Override
    protected void setListener(View rootView) {
        mHaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isHa) {
                    mClickNum++;
                    if (mClickNum >= 5) {
                        mClickNum = 0;
                        showHa();
                    }
                } else {
                    hideHa();
                }
            }
        });
        mClassicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(GameFragment.newInstance(false, isHa));
            }
        });
        mTimerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(GameFragment.newInstance(true, isHa));
            }
        });
        mHowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(HowPlayFragment.newInstance(isHa));
            }
        });
        mAboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFragment(AboutFragment.newInstance(isHa));
            }
        });
    }

    private void showHa() {
        isHa = true;
        SharedPreferencesUtils.init(mContext, Common.SP_NAME).put(Common.HA_MODE, isHa);
        mHaBtn.setText(getString(isHa ? R.string.game_name_ha : R.string.game_name));
    }

    private void hideHa() {
        new AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.dialog_title_ha))
                .setPositiveButton(getString(R.string.dialog_btn_pos), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isHa = false;
                        SharedPreferencesUtils.init(mContext, Common.SP_NAME).put(Common.HA_MODE, isHa);
                        mHaBtn.setText(getString(isHa ? R.string.game_name_ha : R.string.game_name));
                    }
                })
                .setNegativeButton(getString(R.string.dialog_btn_neg), null)
                .create()
                .show();
    }
}
