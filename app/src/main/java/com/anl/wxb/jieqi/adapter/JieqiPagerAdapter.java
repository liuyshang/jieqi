package com.anl.wxb.jieqi.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * author: Lance
 * time: 2016/3/1 14:15
 * e-mail: lance.cao@anarry.com
 */
public class JieqiPagerAdapter extends PagerAdapter{

    private Context context;
    private List<View> list = new ArrayList<>();

    public JieqiPagerAdapter(Context context, List<View> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(list.get(position));
        return list.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView(list.get(position));
    }
}
