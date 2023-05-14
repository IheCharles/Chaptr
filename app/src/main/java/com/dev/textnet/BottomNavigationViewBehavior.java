package com.dev.textnet;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.View;

/**
 * Created by Dell on 2018/01/11.
 */

public class BottomNavigationViewBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {
    private int height;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, BottomNavigationView child, int layoutDirection) {
        height=child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }


    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes== ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed,@ViewCompat.NestedScrollType int type) {
        if (dyConsumed>0){
            slidedown(child);
        }else if(dyConsumed<0){
            slideup(child);
        }
    }

    private void slideup(BottomNavigationView child){
        child.clearAnimation();
        child.animate().translationY(0).setDuration(200);
    }
    private void slidedown(BottomNavigationView child){
        child.clearAnimation();
        child.animate().translationY(height).setDuration(200);
    }
}

