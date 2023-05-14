package com.dev.textnet;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by Dell on 2018/01/29.
 */
public class HPLinearLayoutManager extends LinearLayoutManager {

    public HPLinearLayoutManager(Context context) {
        super(context);
    }

    public HPLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public HPLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /**
     * Magic here
     */
    @Override
    public boolean supportsPredictiveItemAnimations() {
        return false;
    }
}