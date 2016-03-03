package com.anl.wxb.jieqi.view;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * author: admin
 * time: 2016/3/3 14:57
 * e-mail: lance.cao@anarry.com
 */
public class FixedSpeedScroller extends Scroller {
    private int time = 400;

    public FixedSpeedScroller(Context context) {
        super(context);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, time);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, time);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
