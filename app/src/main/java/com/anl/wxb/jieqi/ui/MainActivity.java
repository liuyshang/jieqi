package com.anl.wxb.jieqi.ui;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.anl.base.AnlActivity;
import com.anl.base.annotation.view.ViewInject;
import com.anl.wxb.jieqi.R;
import com.anl.wxb.jieqi.adapter.JieqiPagerAdapter;
import com.anl.wxb.jieqi.db.Db;
import com.anl.wxb.jieqi.view.FixedSpeedScroller;
import com.anl.wxb.jieqi.view.ZoomOutPageTransformer;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AnlActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    /**
     * 返回按钮
     */
    @ViewInject(id = R.id.rl_back)
    private RelativeLayout rlBack;
    /**
     * 左翻页按钮
     */
    @ViewInject(id = R.id.btn_page_left)
    private Button btnPageLeft;
    /**
     * 右翻页按钮
     */
    @ViewInject(id = R.id.btn_page_right)
    private Button btnPageRight;
    /**
     * ViewPager
     */
    @ViewInject(id = R.id.view_pager)
    private ViewPager viewPager;

    private LayoutInflater mInflater;
    private JieqiPagerAdapter madapter;
    private List<View> mlistviews = new ArrayList<>();
    private int current_Index = 0;  //当前页面的编号
    //    private String path = "/data/data/com.anl.wxb.jieqi/databases/jieqi.db";   //文件夹databases的路径
//    private File file_jieqi = new File(path);
    private String password = "a";  //数据库加密密码
    private SQLiteDatabase dbwrite;
    private Db dbhelper;
    private Context mContext;
    private Field mField;
    private FixedSpeedScroller mScroller;

    /**
     * 点击事件处理
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_page_left:
                if (current_Index != 0) {
                    current_Index--;
                    viewPager.arrowScroll(1);
                }
                break;
            case R.id.btn_page_right:
                if (current_Index != 2) {
                    current_Index++;
                    viewPager.arrowScroll(2);
                }
                break;
            case R.id.iv1:
                goToContentActivity("0");
                break;
            case R.id.iv2:
                goToContentActivity("1");
                break;
            case R.id.iv3:
                goToContentActivity("2");
                break;
            case R.id.iv4:
                goToContentActivity("3");
                break;
            case R.id.iv5:
                goToContentActivity("4");
                break;
            case R.id.iv6:
                goToContentActivity("5");
                break;
            case R.id.iv7:
                goToContentActivity("6");
                break;
            case R.id.iv8:
                goToContentActivity("7");
                break;
            case R.id.iv9:
                goToContentActivity("8");
                break;
            case R.id.iv10:
                goToContentActivity("9");
                break;
            case R.id.iv11:
                goToContentActivity("10");
                break;
            case R.id.iv12:
                goToContentActivity("11");
                break;
            case R.id.iv13:
                goToContentActivity("12");
                break;
            case R.id.iv14:
                goToContentActivity("13");
                break;
            case R.id.iv15:
                goToContentActivity("14");
                break;
            case R.id.iv16:
                goToContentActivity("15");
                break;
            case R.id.iv17:
                goToContentActivity("16");
                break;
            case R.id.iv18:
                goToContentActivity("17");
                break;
            case R.id.iv19:
                goToContentActivity("18");
                break;
            case R.id.iv20:
                goToContentActivity("19");
                break;
            case R.id.iv21:
                goToContentActivity("20");
                break;
            case R.id.iv22:
                goToContentActivity("21");
                break;
            case R.id.iv23:
                goToContentActivity("22");
                break;
            case R.id.iv24:
                goToContentActivity("23");
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        SQLiteDatabase.loadLibs(this);
        ifDBExist();
        initPagerView();
        setListener();
        onPageSelected(0);
    }

    private void setListener() {
        rlBack.setOnClickListener(this);
        btnPageLeft.setOnClickListener(this);
        btnPageRight.setOnClickListener(this);
        viewPager.setOnPageChangeListener(this);
    }

    /**
     * 判断数据库是否已经写入数据
     */
    private void ifDBExist() {
        File file = this.getDatabasePath("jieqi.db");
        if (!file.exists()) {
            dbhelper = new Db(this, "jieqi.db", null, 1);
            dbwrite = dbhelper.getWritableDatabase(password);
            new writeDataAsyncTask().execute();
        }
    }

    /**
     * 初始化 pagerview
     */
    private void initPagerView() {
        mInflater = LayoutInflater.from(mContext);
        mlistviews.add(mInflater.inflate(R.layout.layout1, null));
        mlistviews.add(mInflater.inflate(R.layout.layout2, null));
        mlistviews.add(mInflater.inflate(R.layout.layout3, null));

        madapter = new JieqiPagerAdapter(mContext, mlistviews);
        viewPager.setAdapter(madapter);
        viewPager.setCurrentItem(current_Index);
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());

        try {
            mField = ViewPager.class.getDeclaredField("mScroller");
            mField.setAccessible(true);
            mScroller = new FixedSpeedScroller(viewPager.getContext(), new LinearInterpolator());
            mField.set(viewPager,mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        View view = mlistviews.get(position);
        current_Index = position;
        switch (position) {
            case 0:
                pageSelectedOne(view);
                break;
            case 1:
                pageSelectedTwo(view);
                break;
            case 2:
                pageSelectedThree(view);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 跳转到内容页面, 传递参数 “number”，标记点击的图片
     */
    private void goToContentActivity(String str) {
        Intent intent = new Intent();
        intent.setClass(mContext, ContentActivity.class);
        intent.setPackage(getPackageName());
        intent.putExtra("number", str);
        startActivityForResult(intent, RESULT_FIRST_USER);
    }

    /**
     * 第一页
     *
     * @param view
     */
    private void pageSelectedOne(View view) {
        ImageView iv1 = (ImageView) view.findViewById(R.id.iv1);
        ImageView iv2 = (ImageView) view.findViewById(R.id.iv2);
        ImageView iv3 = (ImageView) view.findViewById(R.id.iv3);
        ImageView iv4 = (ImageView) view.findViewById(R.id.iv4);
        ImageView iv5 = (ImageView) view.findViewById(R.id.iv5);
        ImageView iv6 = (ImageView) view.findViewById(R.id.iv6);
        ImageView iv7 = (ImageView) view.findViewById(R.id.iv7);
        ImageView iv8 = (ImageView) view.findViewById(R.id.iv8);
        iv1.setOnClickListener(this);
        iv2.setOnClickListener(this);
        iv3.setOnClickListener(this);
        iv4.setOnClickListener(this);
        iv5.setOnClickListener(this);
        iv6.setOnClickListener(this);
        iv7.setOnClickListener(this);
        iv8.setOnClickListener(this);
    }

    /**
     * 第二页
     *
     * @param view
     */
    private void pageSelectedTwo(View view) {
        ImageView iv9 = (ImageView) view.findViewById(R.id.iv9);
        ImageView iv10 = (ImageView) view.findViewById(R.id.iv10);
        ImageView iv11 = (ImageView) view.findViewById(R.id.iv11);
        ImageView iv12 = (ImageView) view.findViewById(R.id.iv12);
        ImageView iv13 = (ImageView) view.findViewById(R.id.iv13);
        ImageView iv14 = (ImageView) view.findViewById(R.id.iv14);
        ImageView iv15 = (ImageView) view.findViewById(R.id.iv15);
        ImageView iv16 = (ImageView) view.findViewById(R.id.iv16);
        iv9.setOnClickListener(this);
        iv10.setOnClickListener(this);
        iv11.setOnClickListener(this);
        iv12.setOnClickListener(this);
        iv13.setOnClickListener(this);
        iv14.setOnClickListener(this);
        iv15.setOnClickListener(this);
        iv16.setOnClickListener(this);
    }

    /**
     * 第三页
     *
     * @param view
     */
    private void pageSelectedThree(View view) {
        ImageView iv17 = (ImageView) view.findViewById(R.id.iv17);
        ImageView iv18 = (ImageView) view.findViewById(R.id.iv18);
        ImageView iv19 = (ImageView) view.findViewById(R.id.iv19);
        ImageView iv20 = (ImageView) view.findViewById(R.id.iv20);
        ImageView iv21 = (ImageView) view.findViewById(R.id.iv21);
        ImageView iv22 = (ImageView) view.findViewById(R.id.iv22);
        ImageView iv23 = (ImageView) view.findViewById(R.id.iv23);
        ImageView iv24 = (ImageView) view.findViewById(R.id.iv24);
        iv17.setOnClickListener(this);
        iv18.setOnClickListener(this);
        iv19.setOnClickListener(this);
        iv20.setOnClickListener(this);
        iv21.setOnClickListener(this);
        iv22.setOnClickListener(this);
        iv23.setOnClickListener(this);
        iv24.setOnClickListener(this);
    }

    /**
     * 数据库写入数据 线程
     */
    private class writeDataAsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            ContentValues contentValues = new ContentValues();

            contentValues.put("name", "0");
            contentValues.put("text", getResources().getString(R.string.lichun));
            contentValues.put("explain", getResources().getString(R.string.lichun_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "1");
            contentValues.put("text", getResources().getString(R.string.yushui));
            contentValues.put("explain", getResources().getString(R.string.yushui_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "2");
            contentValues.put("text", getResources().getString(R.string.jingzhe));
            contentValues.put("explain", getResources().getString(R.string.jingzhe_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "3");
            contentValues.put("text", getResources().getString(R.string.chunfen));
            contentValues.put("explain", getResources().getString(R.string.chunfen_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "4");
            contentValues.put("text", getResources().getString(R.string.qingming));
            contentValues.put("explain", getResources().getString(R.string.qingming_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "5");
            contentValues.put("text", getResources().getString(R.string.guyu));
            contentValues.put("explain", getResources().getString(R.string.guyu_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "6");
            contentValues.put("text", getResources().getString(R.string.lixia));
            contentValues.put("explain", getResources().getString(R.string.lixia_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "7");
            contentValues.put("text", getResources().getString(R.string.xiaoman));
            contentValues.put("explain", getResources().getString(R.string.xiaoman_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "8");
            contentValues.put("text", getResources().getString(R.string.mangzhong));
            contentValues.put("explain", getResources().getString(R.string.mangzhong_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "9");
            contentValues.put("text", getResources().getString(R.string.xiazhi));
            contentValues.put("explain", getResources().getString(R.string.xiazhi_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "10");
            contentValues.put("text", getResources().getString(R.string.xiaoshu));
            contentValues.put("explain", getResources().getString(R.string.xiaoshu_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "11");
            contentValues.put("text", getResources().getString(R.string.dashu));
            contentValues.put("explain", getResources().getString(R.string.dashu_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "12");
            contentValues.put("text", getResources().getString(R.string.liqiu));
            contentValues.put("explain", getResources().getString(R.string.liqiu_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "13");
            contentValues.put("text", getResources().getString(R.string.chushu));
            contentValues.put("explain", getResources().getString(R.string.chushu_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "14");
            contentValues.put("text", getResources().getString(R.string.bailu));
            contentValues.put("explain", getResources().getString(R.string.bailu_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "15");
            contentValues.put("text", getResources().getString(R.string.qiufen));
            contentValues.put("explain", getResources().getString(R.string.qiufen_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "16");
            contentValues.put("text", getResources().getString(R.string.hanlu));
            contentValues.put("explain", getResources().getString(R.string.hanlu_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "17");
            contentValues.put("text", getResources().getString(R.string.shangjiang));
            contentValues.put("explain", getResources().getString(R.string.shangjiang_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "18");
            contentValues.put("text", getResources().getString(R.string.lidong));
            contentValues.put("explain", getResources().getString(R.string.lidong_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "19");
            contentValues.put("text", getResources().getString(R.string.xiaoxue));
            contentValues.put("explain", getResources().getString(R.string.xiaoxue_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "20");
            contentValues.put("text", getResources().getString(R.string.daxue));
            contentValues.put("explain", getResources().getString(R.string.daxue_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "21");
            contentValues.put("text", getResources().getString(R.string.dongzhi));
            contentValues.put("explain", getResources().getString(R.string.dongzhi_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "22");
            contentValues.put("text", getResources().getString(R.string.xiaohan));
            contentValues.put("explain", getResources().getString(R.string.xiaohan_detail));
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "23");
            contentValues.put("text", getResources().getString(R.string.dahan));
            contentValues.put("explain", getResources().getString(R.string.dahan_detail));
            dbwrite.insert("user", null, contentValues);

            dbwrite.close();
            dbhelper.close();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                int num = data.getIntExtra("page", 0);
                current_Index = num;
                viewPager.setCurrentItem(current_Index);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbhelper != null) {
            dbhelper.close();
        }
        if (dbwrite != null) {
            dbwrite.close();
        }
    }
}


