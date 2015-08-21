package com.anl.wxb.jieqi;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.anl.wxb.jieqi.R;
import com.anl.wxb.jieqi.widgets.JustifyTextView;
import com.anl.wxb.jieqi.widgets.MyScrollView;
import com.anl.base.AnlActivity;

import com.anl.wxb.jieqi.widgets.VerticalSeekBar;
import com.anl.base.annotation.view.ViewInject;

/**
 * Created by admin on 2015/8/7.
 */
public class ContentActivity extends AnlActivity {

    @ViewInject(id = R.id.actionbar_btn_back)
    ImageView actionbar_btn_back;
    @ViewInject(id = R.id.actionbar_text_back)
    TextView actionbar_text_back;
    @ViewInject(id = R.id.actionbar_text_title)
    TextView actionbar_text_title;

    @ViewInject(id = R.id.content_page_left)
    Button content_page_left;
    @ViewInject(id = R.id.content_page_right)
    Button content_page_right;

    @ViewInject(id = R.id.content_text_left)
    JustifyTextView content_text_left;
    @ViewInject(id = R.id.content_scrollview)
    MyScrollView content_scrollview;
    @ViewInject(id = R.id.content_text_right)
    JustifyTextView content_text_right;
    @ViewInject(id = R.id.content_image_icon)
    ImageView content_image_icon;
    @ViewInject(id = R.id.content_image_text)
    ImageView content_image_text;

    @ViewInject(id = R.id.content_btn_progress)
    VerticalSeekBar content_btn_progress;
    @ViewInject(id = R.id.content_btn_down)
    ImageView content_btn_down;
    @ViewInject(id = R.id.content_btn_up)
    ImageView content_btn_up;

    //    接受从MainActivity传输的数据，确定点击的节气
    String current_page = null;
    //    点击左右翻页按钮时，计数加减1
    int count = 0;
    //    seekbar中判断是否是空闲状态
    boolean scroll_falg = false;

    //    ScrollView的高度
    float mScrollHieght = 0;
    //    详解内容的高度
    float mTextViewHeight = 0;
    //    Seekbar的高度
    float mSeekBarHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_2);

        Bundle bundle = this.getIntent().getExtras();
        current_page = bundle.getString("name");
        count = Integer.parseInt(current_page);

        GetImage(count);
        GetText(current_page);

        initSlip();
        initClick();
    }

    //    ScrollView文本
    private void initSlip() {
        //每次点击上，下按钮，5/6个SCrollView的高度
        content_btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollHieght = content_scrollview.getHeight();
                content_scrollview.smoothScrollBy(0, (int) (-5 * mScrollHieght / 6));
            }
        });
        content_btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mScrollHieght = content_scrollview.getHeight();
                content_scrollview.smoothScrollBy(0, (int) (5 * mScrollHieght / 6));
            }
        });


        content_btn_progress.setOnSeekBarChangeListener(new VerticalSeekBar.OnSeekBarChangeListener() {
        //  mTextViewHeight = scrollHeight + scrollY
            @Override
            public void onProgressChanged(VerticalSeekBar VerticalSeekBar, int progress, boolean fromUser) {
                if (scroll_falg) {
                    mScrollHieght = content_scrollview.getHeight();
                    mTextViewHeight = content_text_right.getHeight();
                    mSeekBarHeight = content_btn_progress.getHeight();
                    int Height = (int) ((100 - progress) * (mTextViewHeight - mScrollHieght) / 100);
                    content_scrollview.smoothScrollTo(0, Height);

//                    Log.e("TAG_progress", String.valueOf(progress));
//                    Log.e("TAG_textHeight", String.valueOf(content_text_right.getHeight()));
//                    Log.e("TAG_seekbarHeight", String.valueOf(content_btn_progress.getHeight()));
//                    Log.e("TAG_scrollHeight", String.valueOf(content_scrollview.getHeight()));
//                    Log.e("TAG_scrollY", String.valueOf(content_scrollview.getScrollY()));
//                    Log.e("TAG_scrollY/scrollH", String.valueOf(content_scrollview.getScrollY() / content_scrollview.getHeight()));
                }
            }


            @Override
            public void onStartTrackingTouch(VerticalSeekBar VerticalSeekBar) {
                scroll_falg = true;
            }

            @Override
            public void onStopTrackingTouch(VerticalSeekBar VerticalSeekBar) {
                scroll_falg = false;
            }
        });

        //mTextViewHeight = mTextViewHeight + scrollY()
        content_scrollview.setOverScrollMode(View.OVER_SCROLL_NEVER);
        content_scrollview.setScrollViewListener(new ScrollViewListener() {
            @Override
            public void onScrollChanged(MyScrollView myScrollView, int x, int y, int oldx, int oldy) {
                if (!scroll_falg) {
                    mScrollHieght = content_scrollview.getHeight();
                    mTextViewHeight = content_text_right.getHeight();
                    mSeekBarHeight = content_btn_progress.getHeight();
                    int position = (int) Math.floor(100 - y * 100 / ((mTextViewHeight - mScrollHieght) * 10 / 10.0));
                    if (position > 92) {
                        position = 92;
                    }
                    content_btn_progress.setProgress(position);

//                    content_btn_progress.scrollTo(0, (int) (- y * mSeekBarHeight / (mTextViewHeight - mScrollHieght)));
//                    Log.e("count", String.valueOf((y * mSeekBarHeight / (mTextViewHeight - mScrollHieght))));
//                    Log.e("y", String.valueOf(y));
//                    Log.e("mscrollY", String.valueOf(content_scrollview.getScrollY()));
//                    Log.e("position", String.valueOf(position));
//                    Log.e("mScrollHeight", String.valueOf(mScrollHieght));
//                    Log.e("mTextViewHeight", String.valueOf(mTextViewHeight));
//                    Log.e("cscrollY", String.valueOf(content_btn_progress.getScrollY()));
//                    Log.e("cscrollX", String.valueOf(content_btn_progress.getScrollX()));
//                    Log.e("mSeekBarHeight", String.valueOf(mSeekBarHeight));
                }

            }
        });
    }

    //获取文本内容
    private void GetText(String current_page) {
        Db db = new Db(this);
        SQLiteDatabase dbread = db.getReadableDatabase();
        Cursor cursor = dbread.rawQuery("select * from user where name=?", new String[]{current_page});
        if (cursor.moveToFirst()) {
            String text = cursor.getString(cursor.getColumnIndex("text"));
            content_text_left.setText(text);
            String explain = cursor.getString(cursor.getColumnIndex("explain"));
            content_text_right.setText(explain);
        }
        dbread.close();
    }

    //获取图片
    private void GetImage(int current_page) {
        switch (current_page) {
            case 0:
                content_image_text.setImageResource(R.drawable.text_lichun);
                content_image_icon.setImageResource(R.drawable.icon_lichun);
                actionbar_text_title.setText("24节气——立春");
                content_page_left.setVisibility(View.INVISIBLE);
                break;
            case 1:
                content_image_text.setImageResource(R.drawable.text_yushui);
                content_image_icon.setImageResource(R.drawable.icon_yushui);
                actionbar_text_title.setText("24节气——雨水");
                break;
            case 2:
                content_image_text.setImageResource(R.drawable.text_jingzhe);
                content_image_icon.setImageResource(R.drawable.icon_jingzhe);
                actionbar_text_title.setText("24节气——惊蛰");
                break;
            case 3:
                content_image_text.setImageResource(R.drawable.text_chunfen);
                content_image_icon.setImageResource(R.drawable.icon_chunfen);
                actionbar_text_title.setText("24节气——春分");
                break;
            case 4:
                content_image_text.setImageResource(R.drawable.text_qingming);
                content_image_icon.setImageResource(R.drawable.icon_qingming);
                actionbar_text_title.setText("24节气——清明");
                break;
            case 5:
                content_image_text.setImageResource(R.drawable.text_guyu);
                content_image_icon.setImageResource(R.drawable.icon_guyu);
                actionbar_text_title.setText("24节气——谷雨");
                break;

            case 6:
                content_image_text.setImageResource(R.drawable.text_lixia);
                content_image_icon.setImageResource(R.drawable.icon_lixia);
                actionbar_text_title.setText("24节气——立夏");
                break;
            case 7:
                content_image_text.setImageResource(R.drawable.text_xiaoman);
                content_image_icon.setImageResource(R.drawable.icon_xiaoman);
                actionbar_text_title.setText("24节气——小满");
                break;
            case 8:
                content_image_text.setImageResource(R.drawable.text_mangzhong);
                content_image_icon.setImageResource(R.drawable.icon_mangzhong);
                actionbar_text_title.setText("24节气——芒种");
                break;
            case 9:
                content_image_text.setImageResource(R.drawable.text_xiazhi);
                content_image_icon.setImageResource(R.drawable.icon_xiazhi);
                actionbar_text_title.setText("24节气——夏至");
                break;
            case 10:
                content_image_text.setImageResource(R.drawable.text_xiaoshu);
                content_image_icon.setImageResource(R.drawable.icon_xiaoshu);
                actionbar_text_title.setText("24节气——小暑");
                break;
            case 11:
                content_image_text.setImageResource(R.drawable.text_dashu);
                content_image_icon.setImageResource(R.drawable.icon_dashu);
                actionbar_text_title.setText("24节气——大暑");
                break;

            case 12:
                content_image_text.setImageResource(R.drawable.text_liqiu);
                content_image_icon.setImageResource(R.drawable.icon_liqiu);
                actionbar_text_title.setText("24节气——立秋");
                break;
            case 13:
                content_image_text.setImageResource(R.drawable.text_chushu);
                content_image_icon.setImageResource(R.drawable.icon_chushu);
                actionbar_text_title.setText("24节气——处暑");
                break;
            case 14:
                content_image_text.setImageResource(R.drawable.text_bailu);
                content_image_icon.setImageResource(R.drawable.icon_bailu);
                actionbar_text_title.setText("24节气——白露");
                break;
            case 15:
                content_image_text.setImageResource(R.drawable.text_qiufen);
                content_image_icon.setImageResource(R.drawable.icon_qiufen);
                actionbar_text_title.setText("24节气——秋分");
                break;
            case 16:
                content_image_text.setImageResource(R.drawable.text_hanlu);
                content_image_icon.setImageResource(R.drawable.icon_hanlu);
                actionbar_text_title.setText("24节气——寒露");
                break;
            case 17:
                content_image_text.setImageResource(R.drawable.text_shuangjiang);
                content_image_icon.setImageResource(R.drawable.icon_shuangjiang);
                actionbar_text_title.setText("24节气——霜降");
                break;

            case 18:
                content_image_text.setImageResource(R.drawable.text_lidong);
                content_image_icon.setImageResource(R.drawable.icon_lidong);
                actionbar_text_title.setText("24节气——立冬");
                break;
            case 19:
                content_image_text.setImageResource(R.drawable.text_xiaoxue);
                content_image_icon.setImageResource(R.drawable.icon_xiaoxue);
                actionbar_text_title.setText("24节气——小雪");
                break;
            case 20:
                content_image_text.setImageResource(R.drawable.text_daxue);
                content_image_icon.setImageResource(R.drawable.icon_daxue);
                actionbar_text_title.setText("24节气——大雪");
                break;
            case 21:
                content_image_text.setImageResource(R.drawable.text_dongzhi);
                content_image_icon.setImageResource(R.drawable.icon_dongzhi);
                actionbar_text_title.setText("24节气——冬至");
                break;
            case 22:
                content_image_text.setImageResource(R.drawable.text_xiaohan);
                content_image_icon.setImageResource(R.drawable.icon_xiaohan);
                actionbar_text_title.setText("24节气——小寒");
                break;
            case 23:
                content_image_text.setImageResource(R.drawable.text_dahan);
                content_image_icon.setImageResource(R.drawable.icon_dahan);
                actionbar_text_title.setText("24节气——大寒");
                content_page_right.setVisibility(View.INVISIBLE);
                break;


        }

    }

    //左右翻页按钮的点击处理，actionbar两个返回按钮的点击处理，并传输count数据
    private void initClick() {
        actionbar_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("count",count);
                setResult(RESULT_OK,intent);
                finish();
            }
        });

        actionbar_text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("count",count);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        //每次左右翻页，设置SeekBar的thumb在最上端,因为偏移设置91,textview
        //第一页左翻，最后一页右翻，按钮不现实
        content_page_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_scrollview.scrollTo(0, 0);
                content_btn_progress.setProgress(91);

                count--;
                if(count == 0) {
                    content_page_left.setVisibility(View.INVISIBLE);
                }
                content_page_right.setVisibility(View.VISIBLE);
                GetImage(count);
                GetText("" + count);
            }
        });
        content_page_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content_scrollview.scrollTo(0, 0);
                content_btn_progress.setProgress(91);

                count++;
                if(count == 23){
                    content_page_right.setVisibility(View.INVISIBLE);
                }
                content_page_left.setVisibility(View.VISIBLE);
                GetImage(count);
                GetText("" + count);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent();
            intent.putExtra("count",count);
            setResult(RESULT_OK, intent);
            finish();
        }

        return super.onKeyDown(keyCode, event);
    }
}
