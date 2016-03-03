package com.anl.wxb.jieqi.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.anl.base.AnlActivity;
import com.anl.base.annotation.view.ViewInject;
import com.anl.wxb.jieqi.R;
import com.anl.wxb.jieqi.db.Db;
import com.anl.wxb.jieqi.view.JustifyTextView;
import com.anl.wxb.jieqi.view.MyScrollView;
import com.anl.wxb.jieqi.view.VerticalSeekBar;

import net.sqlcipher.database.SQLiteDatabase;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by admin on 2015/8/7.
 */
public class ContentActivity extends AnlActivity implements MyScrollView.ScrollViewListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    /**
     * 返回按钮
     */
    @ViewInject(id = R.id.rl_back)
    private RelativeLayout rlBack;
    /**
     * 页面标题
     */
    @ViewInject(id = R.id.tv_title)
    private TextView tvTitle;
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
     * 背景
     */
    @ViewInject(id = R.id.bg_activity)
    private RelativeLayout bgActivity;
    /**
     * 节气图片
     */
    @ViewInject(id = R.id.iv_icon)
    private ImageView ivIcon;
    /**
     * 节气文字
     */
    @ViewInject(id = R.id.iv_text)
    private ImageView ivText;
    /**
     * 节气介绍
     */
    @ViewInject(id = R.id.tv_introduction)
    private JustifyTextView tvIntroduction;
    /**
     * ScrollView
     */
    @ViewInject(id = R.id.scrollview)
    private MyScrollView scrollview;
    /**
     * 节气详解
     */
    @ViewInject(id = R.id.tv_detail)
    private JustifyTextView tvDetail;
    /**
     * Progress
     */
    @ViewInject(id = R.id.btn_progress)
    private VerticalSeekBar btn_progress;
    /**
     * 下翻页 按钮
     */
    @ViewInject(id = R.id.iv_down)
    private ImageView ivDown;
    /**
     * 上翻页 按钮
     */
    @ViewInject(id = R.id.iv_up)
    private ImageView ivUp;

    /**
     * 显示节气的标记
     */
    private String number = null;
    /**
     * seekbar中判断是否是空闲状态
     */
    boolean scroll_falg = false;
    private Context mContext;
    private String password = "a";  //数据库加密密码
    private Db db;
    private SQLiteDatabase dbread;
    private SweetAlertDialog mDialog;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                setIntentData();
                finish();
                break;
            case R.id.iv_up:
                scrollview.smoothScrollBy(0, -(5 * (scrollview.getHeight()) / 6));
                break;
            case R.id.iv_down:
                scrollview.smoothScrollBy(0, (5 * (scrollview.getHeight()) / 6));
                break;
            case R.id.btn_page_left:
                pageChange("left");
                break;
            case R.id.btn_page_right:
                pageChange("right");
                break;
            default:
                break;
        }
    }

    /**
     * 翻页
     * @param str
     */
    private void pageChange(final String str) {
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator anim1 = ObjectAnimator.ofFloat(bgActivity, "alpha", 1.0f, 0.3f).setDuration(100);
        anim1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bgActivity.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        anim1.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (str.equals("left")) {
                    pageLeft();
                } else if (str.equals("right")){
                    pageRight();
                }
            }
        });
        ObjectAnimator anim2 = ObjectAnimator.ofFloat(bgActivity,"alpha",0.3f,1.0f).setDuration(400);
        anim2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bgActivity.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        set.playSequentially(anim1, anim2);
        set.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        mContext = ContentActivity.this;
        SQLiteDatabase.loadLibs(this);        //加载.so文件
        db = new Db(this, "jieqi.db", null, 1);        //打开数据库
        getIntentData();
        new Decrypt_Sqlite().execute();        //解密数据库线程
        setListener();
    }

    /**
     * 获取传递的参数
     */
    private void getIntentData() {
        Intent intent = getIntent();
        number = intent.getStringExtra("number");
    }

    private void setListener() {
        btn_progress.setOnSeekBarChangeListener(this);
        scrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        scrollview.setScrollViewListener(this);
        rlBack.setOnClickListener(this);
        ivUp.setOnClickListener(this);
        ivDown.setOnClickListener(this);
        btnPageLeft.setOnClickListener(this);
        btnPageRight.setOnClickListener(this);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (scroll_falg) {
            int position_seekbar = progress * (tvDetail.getHeight() - scrollview.getHeight()) / 100;
            scrollview.smoothScrollTo(0, position_seekbar);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        scroll_falg = true;
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        scroll_falg = false;
    }

    /**
     * scrollview监听
     */
    @Override
    public void onScrollChanged(MyScrollView myScrollView, int x, int y, int oldx, int oldy) {
        if (!scroll_falg) {
            int position_scroll = y * 100 / (tvDetail.getHeight() - scrollview.getHeight());
            btn_progress.setProgress(position_scroll);
        }
    }

    /**
     * 异步解密数据库
     */
    private class Decrypt_Sqlite extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] params) {
            //解密数据库
            dbread = db.getReadableDatabase(password);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showdialog();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            mDialog.cancel();
            bgActivity.setVisibility(View.VISIBLE);
            setText(number);
            setImage(Integer.parseInt(number));
        }
    }

    /**
     * 等待数据加载期间，显示Loading效果
     */
    private void showdialog() {
        bgActivity.setVisibility(View.INVISIBLE);
        mDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        mDialog.setTitleText("Loading");
        mDialog.setCancelable(false);
        mDialog.show();
    }

    /**
     * 设置文本内容
     */
    private void setText(String number) {
        Cursor cursor = dbread.rawQuery("select * from user where name=?", new String[]{number});
        if (cursor.moveToFirst()) {
            tvIntroduction.setText(cursor.getString(cursor.getColumnIndex("text")));
            tvDetail.setText(cursor.getString(cursor.getColumnIndex("explain")));
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 设置图片
     */
    private void setImage(int number) {
        switch (number) {
            case 0:
                ivText.setImageResource(R.drawable.text_lichun);
                ivIcon.setImageResource(R.drawable.icon_lichun);
                tvTitle.setText("立春");
                btnPageLeft.setVisibility(View.INVISIBLE);
                break;
            case 1:
                ivText.setImageResource(R.drawable.text_yushui);
                ivIcon.setImageResource(R.drawable.icon_yushui);
                tvTitle.setText("雨水");
                break;
            case 2:
                ivText.setImageResource(R.drawable.text_jingzhe);
                ivIcon.setImageResource(R.drawable.icon_jingzhe);
                tvTitle.setText("惊蛰");
                break;
            case 3:
                ivText.setImageResource(R.drawable.text_chunfen);
                ivIcon.setImageResource(R.drawable.icon_chunfen);
                tvTitle.setText("春分");
                break;
            case 4:
                ivText.setImageResource(R.drawable.text_qingming);
                ivIcon.setImageResource(R.drawable.icon_qingming);
                tvTitle.setText("清明");
                break;
            case 5:
                ivText.setImageResource(R.drawable.text_guyu);
                ivIcon.setImageResource(R.drawable.icon_guyu);
                tvTitle.setText("谷雨");
                break;
            case 6:
                ivText.setImageResource(R.drawable.text_lixia);
                ivIcon.setImageResource(R.drawable.icon_lixia);
                tvTitle.setText("立夏");
                break;
            case 7:
                ivText.setImageResource(R.drawable.text_xiaoman);
                ivIcon.setImageResource(R.drawable.icon_xiaoman);
                tvTitle.setText("小满");
                break;
            case 8:
                ivText.setImageResource(R.drawable.text_mangzhong);
                ivIcon.setImageResource(R.drawable.icon_mangzhong);
                tvTitle.setText("芒种");
                break;
            case 9:
                ivText.setImageResource(R.drawable.text_xiazhi);
                ivIcon.setImageResource(R.drawable.icon_xiazhi);
                tvTitle.setText("夏至");
                break;
            case 10:
                ivText.setImageResource(R.drawable.text_xiaoshu);
                ivIcon.setImageResource(R.drawable.icon_xiaoshu);
                tvTitle.setText("小暑");
                break;
            case 11:
                ivText.setImageResource(R.drawable.text_dashu);
                ivIcon.setImageResource(R.drawable.icon_dashu);
                tvTitle.setText("大暑");
                break;
            case 12:
                ivText.setImageResource(R.drawable.text_liqiu);
                ivIcon.setImageResource(R.drawable.icon_liqiu);
                tvTitle.setText("立秋");
                break;
            case 13:
                ivText.setImageResource(R.drawable.text_chushu);
                ivIcon.setImageResource(R.drawable.icon_chushu);
                tvTitle.setText("处暑");
                break;
            case 14:
                ivText.setImageResource(R.drawable.text_bailu);
                ivIcon.setImageResource(R.drawable.icon_bailu);
                tvTitle.setText("白露");
                break;
            case 15:
                ivText.setImageResource(R.drawable.text_qiufen);
                ivIcon.setImageResource(R.drawable.icon_qiufen);
                tvTitle.setText("秋分");
                break;
            case 16:
                ivText.setImageResource(R.drawable.text_hanlu);
                ivIcon.setImageResource(R.drawable.icon_hanlu);
                tvTitle.setText("寒露");
                break;
            case 17:
                ivText.setImageResource(R.drawable.text_shuangjiang);
                ivIcon.setImageResource(R.drawable.icon_shuangjiang);
                tvTitle.setText("霜降");
                break;
            case 18:
                ivText.setImageResource(R.drawable.text_lidong);
                ivIcon.setImageResource(R.drawable.icon_lidong);
                tvTitle.setText("立冬");
                break;
            case 19:
                ivText.setImageResource(R.drawable.text_xiaoxue);
                ivIcon.setImageResource(R.drawable.icon_xiaoxue);
                tvTitle.setText("小雪");
                break;
            case 20:
                ivText.setImageResource(R.drawable.text_daxue);
                ivIcon.setImageResource(R.drawable.icon_daxue);
                tvTitle.setText("大雪");
                break;
            case 21:
                ivText.setImageResource(R.drawable.text_dongzhi);
                ivIcon.setImageResource(R.drawable.icon_dongzhi);
                tvTitle.setText("冬至");
                break;
            case 22:
                ivText.setImageResource(R.drawable.text_xiaohan);
                ivIcon.setImageResource(R.drawable.icon_xiaohan);
                tvTitle.setText("小寒");
                break;
            case 23:
                ivText.setImageResource(R.drawable.text_dahan);
                ivIcon.setImageResource(R.drawable.icon_dahan);
                tvTitle.setText("大寒");
                btnPageRight.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }

    /**
     * 点击硬件返回按钮，传递数据
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            setIntentData();
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 设置跳转到主页，传递的参数
     */
    private void setIntentData() {
        int count = Integer.parseInt(number);
        Intent intent = new Intent();
        intent.setPackage(getPackageName());
        intent.setClass(mContext, MainActivity.class);
        if (count == 0 || count == 1 || count == 2 || count == 3
                || count == 4 || count == 5 || count == 6) {
            intent.putExtra("page", 0);
        } else if (count == 7 || count == 8 || count == 9 || count == 10
                || count == 11 || count == 12 || count == 13
                || count == 14 || count == 15) {
            intent.putExtra("page", 1);
        } else if (count == 16 || count == 17 || count == 18
                || count == 19 || count == 20 || count == 21
                || count == 22 || count == 23) {
            intent.putExtra("page", 2);
        }
        this.setResult(RESULT_OK, intent);
    }

    /**
     * 向右翻页
     */
    private void pageRight() {
        int count = Integer.parseInt(number);
        scrollview.scrollTo(0, 0);
        btn_progress.setProgress(0);
        count++;
        if (count == 23) {
            btnPageRight.setVisibility(View.INVISIBLE);
        }
        btnPageLeft.setVisibility(View.VISIBLE);
        setImage(count);
        number = String.valueOf(count);
        setText("" + count);
    }

    /**
     * 想左翻页
     */
    private void pageLeft() {
        int count = Integer.parseInt(number);
        scrollview.scrollTo(0, 0);
        btn_progress.setProgress(0);
        count--;
        if (count == 0) {
            btnPageLeft.setVisibility(View.INVISIBLE);
        }
        btnPageRight.setVisibility(View.VISIBLE);
        setImage(count);
        number = "" + count;
        setText(number);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
        if (dbread != null) {
            dbread.close();
        }
    }
}
