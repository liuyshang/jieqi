package com.anl.wxb.jieqi.ui;

import android.content.Intent;
import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabase;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.anl.wxb.jieqi.R;
import com.anl.wxb.jieqi.db.Db;
import com.anl.wxb.jieqi.widgets.JustifyTextView;
import com.anl.wxb.jieqi.widgets.MyScrollView;
import com.anl.base.AnlActivity;

import com.anl.base.annotation.view.ViewInject;
import com.anl.wxb.jieqi.widgets.VerticalSeekBar;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by admin on 2015/8/7.
 */
public class ContentActivity extends AnlActivity implements MyScrollView.ScrollViewListener, SeekBar.OnSeekBarChangeListener {
    private final static String TAG = "ContentActivity";

    @ViewInject(id = R.id.RL_back)
    RelativeLayout RL_back;
    @ViewInject(id = R.id.actionbar_text_title)
    TextView actionbar_text_title;
    @ViewInject(id = R.id.page_left)
    Button page_left;
    @ViewInject(id = R.id.page_right)
    Button page_right;
    @ViewInject(id = R.id.bg_activity)
    RelativeLayout bg_activity;
    @ViewInject(id = R.id.text_left)
    JustifyTextView text_left;
    @ViewInject(id = R.id.scrollview)
    MyScrollView scrollview;
    @ViewInject(id = R.id.text_right)
    JustifyTextView text_right;
    @ViewInject(id = R.id.image_icon)
    ImageView image_icon;
    @ViewInject(id = R.id.image_text)
    ImageView image_text;
    @ViewInject(id = R.id.btn_progress)
    VerticalSeekBar btn_progress;
    @ViewInject(id = R.id.btn_down)
    ImageView btn_down;
    @ViewInject(id = R.id.btn_up)
    ImageView btn_up;
    //接受从MainActivity传输的数据，确定点击的节气
    String current_page = null;
    //点击左右翻页按钮时，计数加减1
    int count = 0;
    //seekbar中判断是否是空闲状态
    boolean scroll_falg = false;

    private String path = "/data/data/com.anl.wxb.jieqi/databases/jieqi.db";
    private String password = "a";  //数据库加密密码
    private Db db;
    private SQLiteDatabase dbread;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        //加载.so文件
        SQLiteDatabase.loadLibs(this);
        //打开数据库
        db = new Db(this, "jieqi.db", null, 1);
        //接受从MainActivity.java发送的数据
        Bundle bundle = this.getIntent().getExtras();
        current_page = bundle.getString("name");
        count = Integer.parseInt(current_page);
        bg_activity.setVisibility(View.INVISIBLE);
        showdialog();
        //解密数据库线程
        new Decrypt_Sqlite().execute();
        setListener();
        onClick();
    }


    /**
    *等待数据加载期间，显示Loading效果
     *  */
    private void showdialog() {
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }


    /**
     * 获取文本内容
     */
    private void getText(String current_page) {
        Cursor cursor = dbread.rawQuery("select * from user where name=?", new String[]{current_page});
        if (cursor.moveToFirst()) {
            String text = cursor.getString(cursor.getColumnIndex("text"));
            text_left.setText(text);
            String explain = cursor.getString(cursor.getColumnIndex("explain"));
            text_right.setText(explain);
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 获取图片
     */
    private void getImage(int current_page) {
        switch (current_page) {
            case 0:
                image_text.setImageResource(R.drawable.text_lichun);
                image_icon.setImageResource(R.drawable.icon_lichun);
                actionbar_text_title.setText("24节气——立春");
                page_left.setVisibility(View.INVISIBLE);
                break;
            case 1:
               image_text.setImageResource(R.drawable.text_yushui);
               image_icon.setImageResource(R.drawable.icon_yushui);
                actionbar_text_title.setText("24节气——雨水");
                break;
            case 2:
               image_text.setImageResource(R.drawable.text_jingzhe);
               image_icon.setImageResource(R.drawable.icon_jingzhe);
                actionbar_text_title.setText("24节气——惊蛰");
                break;
            case 3:
               image_text.setImageResource(R.drawable.text_chunfen);
               image_icon.setImageResource(R.drawable.icon_chunfen);
                actionbar_text_title.setText("24节气——春分");
                break;
            case 4:
               image_text.setImageResource(R.drawable.text_qingming);
               image_icon.setImageResource(R.drawable.icon_qingming);
                actionbar_text_title.setText("24节气——清明");
                break;
            case 5:
               image_text.setImageResource(R.drawable.text_guyu);
               image_icon.setImageResource(R.drawable.icon_guyu);
                actionbar_text_title.setText("24节气——谷雨");
                break;

            case 6:
               image_text.setImageResource(R.drawable.text_lixia);
               image_icon.setImageResource(R.drawable.icon_lixia);
                actionbar_text_title.setText("24节气——立夏");
                break;
            case 7:
               image_text.setImageResource(R.drawable.text_xiaoman);
               image_icon.setImageResource(R.drawable.icon_xiaoman);
                actionbar_text_title.setText("24节气——小满");
                break;
            case 8:
               image_text.setImageResource(R.drawable.text_mangzhong);
               image_icon.setImageResource(R.drawable.icon_mangzhong);
                actionbar_text_title.setText("24节气——芒种");
                break;
            case 9:
               image_text.setImageResource(R.drawable.text_xiazhi);
               image_icon.setImageResource(R.drawable.icon_xiazhi);
                actionbar_text_title.setText("24节气——夏至");
                break;
            case 10:
               image_text.setImageResource(R.drawable.text_xiaoshu);
               image_icon.setImageResource(R.drawable.icon_xiaoshu);
                actionbar_text_title.setText("24节气——小暑");
                break;
            case 11:
               image_text.setImageResource(R.drawable.text_dashu);
               image_icon.setImageResource(R.drawable.icon_dashu);
                actionbar_text_title.setText("24节气——大暑");
                break;

            case 12:
               image_text.setImageResource(R.drawable.text_liqiu);
               image_icon.setImageResource(R.drawable.icon_liqiu);
                actionbar_text_title.setText("24节气——立秋");
                break;
            case 13:
               image_text.setImageResource(R.drawable.text_chushu);
               image_icon.setImageResource(R.drawable.icon_chushu);
                actionbar_text_title.setText("24节气——处暑");
                break;
            case 14:
               image_text.setImageResource(R.drawable.text_bailu);
               image_icon.setImageResource(R.drawable.icon_bailu);
                actionbar_text_title.setText("24节气——白露");
                break;
            case 15:
               image_text.setImageResource(R.drawable.text_qiufen);
               image_icon.setImageResource(R.drawable.icon_qiufen);
                actionbar_text_title.setText("24节气——秋分");
                break;
            case 16:
               image_text.setImageResource(R.drawable.text_hanlu);
               image_icon.setImageResource(R.drawable.icon_hanlu);
                actionbar_text_title.setText("24节气——寒露");
                break;
            case 17:
               image_text.setImageResource(R.drawable.text_shuangjiang);
               image_icon.setImageResource(R.drawable.icon_shuangjiang);
                actionbar_text_title.setText("24节气——霜降");
                break;

            case 18:
               image_text.setImageResource(R.drawable.text_lidong);
               image_icon.setImageResource(R.drawable.icon_lidong);
                actionbar_text_title.setText("24节气——立冬");
                break;
            case 19:
               image_text.setImageResource(R.drawable.text_xiaoxue);
               image_icon.setImageResource(R.drawable.icon_xiaoxue);
                actionbar_text_title.setText("24节气——小雪");
                break;
            case 20:
               image_text.setImageResource(R.drawable.text_daxue);
               image_icon.setImageResource(R.drawable.icon_daxue);
                actionbar_text_title.setText("24节气——大雪");
                break;
            case 21:
               image_text.setImageResource(R.drawable.text_dongzhi);
               image_icon.setImageResource(R.drawable.icon_dongzhi);
                actionbar_text_title.setText("24节气——冬至");
                break;
            case 22:
               image_text.setImageResource(R.drawable.text_xiaohan);
               image_icon.setImageResource(R.drawable.icon_xiaohan);
                actionbar_text_title.setText("24节气——小寒");
                break;
            case 23:
               image_text.setImageResource(R.drawable.text_dahan);
               image_icon.setImageResource(R.drawable.icon_dahan);
                actionbar_text_title.setText("24节气——大寒");
               page_right.setVisibility(View.INVISIBLE);
                break;
        }
    }

    /**
     * 滑动监听
     */
    private void setListener() {
        btn_progress.setOnSeekBarChangeListener(this);
        scrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        scrollview.setScrollViewListener(this);
    }

    /**
     * seekbar 监听
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (scroll_falg) {
            int height_text = text_right.getHeight();
            int height_scroll = scrollview.getHeight();
            int position_seekbar = progress * (height_text - height_scroll) / 100;
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
            int height_text = text_right.getHeight();
            int height_scroll = scrollview.getHeight();
            int position_scroll = y * 100 / (height_text - height_scroll);
            btn_progress.setProgress(position_scroll);
        }
    }


    /**
     * 点击处理
     */
    private void onClick() {
        /*listview 上下按钮翻页*/
       btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mScrollHieght = scrollview.getHeight();
                scrollview.smoothScrollBy(0, (int) (-5 * mScrollHieght / 6));
            }
        });
       btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mScrollHieght = scrollview.getHeight();
                scrollview.smoothScrollBy(0, (int) (5 * mScrollHieght / 6));
            }
        });
        /*actionbar 返回*/
        RL_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("count", count);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        /*页面 翻页*/
       page_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               scrollview.scrollTo(0, 0);
               btn_progress.setProgress(0);
                count--;
                if (count == 0) {
                   page_left.setVisibility(View.INVISIBLE);
                }
               page_right.setVisibility(View.VISIBLE);
                getImage(count);
                current_page = "" + count;
                getText(current_page);
            }
        });
       page_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollview.scrollTo(0, 0);
               btn_progress.setProgress(0);
                count++;
                if (count == 23) {
                   page_right.setVisibility(View.INVISIBLE);
                }
               page_left.setVisibility(View.VISIBLE);
                getImage(count);
                current_page = "" + count;
                getText("" + count);
            }
        });
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
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            pDialog.cancel();
            bg_activity.setVisibility(View.VISIBLE);
            getText(current_page);
            getImage(count);
        }
    }


    /**
     * 点击硬件返回按钮，传递数据
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("count", count);
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbread.close();
        db.close();
    }
}
