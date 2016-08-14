package com.ahao.game2048.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Avalon on 2016/8/11.
 */
public class AnimLayer extends FrameLayout{

    private List<GameItemView> mItems = new ArrayList<GameItemView>();

    public AnimLayer(Context context) {
        this(context, null);
    }

    public AnimLayer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimLayer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void createMoveAnim(final GameItemView from, final GameItemView to,
                               int fromX, int toX, int fromY, int toY){

        final GameItemView item = getItem(from.getNumber());
        int itemSize = from.getWidth();

        LayoutParams lp = new LayoutParams(itemSize, itemSize);
        lp.leftMargin = fromX*itemSize;
        lp.topMargin = fromY*itemSize;
        item.setLayoutParams(lp);

        if (to.getNumber()<=0) {
            to.setVisibility(View.INVISIBLE);
        }
        TranslateAnimation ta = new TranslateAnimation(0, itemSize*(toX-fromX), 0, itemSize*(toY-fromY));
        ta.setDuration(100);
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                to.setVisibility(View.VISIBLE);
                recycleItem(item);
            }
        });
        item.startAnimation(ta);
    }

    private GameItemView getItem(int num){
        GameItemView item;
        if (mItems.size()>0) {
            item = mItems.remove(0);
        }else{
            item = new GameItemView(getContext());
            addView(item);
        }
        item.setVisibility(View.VISIBLE);
        item.setNumber(num);
        return item;
    }

    private void recycleItem(GameItemView item){
        item.setVisibility(View.INVISIBLE);
        item.setAnimation(null);
        mItems.add(item);
    }


    public void createItem(GameItemView item) {
        ScaleAnimation sa = new ScaleAnimation(0.1f, 1f, 0.1f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setDuration(100);

        item.setAnimation(null);
        item.startAnimation(sa);
    }
}
