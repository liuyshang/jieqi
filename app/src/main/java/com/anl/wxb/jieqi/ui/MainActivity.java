package com.anl.wxb.jieqi.ui;

//24节气思路：24个节气目录布局文件activity_main,使用ViewPager。每个ViewPager页面8个节气，点击跳转到单个节气详解页面，并传递参数
//           每个节气详解页面两个更变的图片，两个可变的文本。图片，文本从SQLite中获取。详解文本放在ScrollView中，ScrollView与SeekBar同步

import android.content.ContentValues;
import android.content.Intent;
import net.sqlcipher.database.SQLiteDatabase;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.anl.base.AnlActivity;
import com.anl.base.annotation.view.ViewInject;
import com.anl.wxb.jieqi.R;
import com.anl.wxb.jieqi.db.Db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AnlActivity {
    private final static String TAG = "MainActivity";

    @ViewInject(id = R.id.main_image1)
    ImageView main_image1;
    @ViewInject(id = R.id.main_image2)
    ImageView main_image2;
    @ViewInject(id = R.id.main_image3)
    ImageView main_image3;
    @ViewInject(id = R.id.main_image4)
    ImageView main_image4;
    @ViewInject(id = R.id.main_image5)
    ImageView main_image5;
    @ViewInject(id = R.id.main_image6)
    ImageView main_image6;
    @ViewInject(id = R.id.main_image7)
    ImageView main_image7;
    @ViewInject(id = R.id.main_image8)
    ImageView main_image8;

    @ViewInject(id = R.id.main_image9)
    ImageView main_image9;
    @ViewInject(id = R.id.main_image10)
    ImageView main_image10;
    @ViewInject(id = R.id.main_image11)
    ImageView main_image11;
    @ViewInject(id = R.id.main_image12)
    ImageView main_image12;
    @ViewInject(id = R.id.main_image13)
    ImageView main_image13;
    @ViewInject(id = R.id.main_image14)
    ImageView main_image14;
    @ViewInject(id = R.id.main_image15)
    ImageView main_image15;
    @ViewInject(id = R.id.main_image16)
    ImageView main_image16;

    @ViewInject(id = R.id.main_image17)
    ImageView main_image17;
    @ViewInject(id = R.id.main_image18)
    ImageView main_image18;
    @ViewInject(id = R.id.main_image19)
    ImageView main_image19;
    @ViewInject(id = R.id.main_image20)
    ImageView main_image20;
    @ViewInject(id = R.id.main_image21)
    ImageView main_image21;
    @ViewInject(id = R.id.main_image22)
    ImageView main_image22;
    @ViewInject(id = R.id.main_image23)
    ImageView main_image23;
    @ViewInject(id = R.id.main_image24)
    ImageView main_image24;


    @ViewInject(id = R.id.actionbar_text_back)
    TextView actionbar_text_back;
    @ViewInject(id = R.id.actionbar_text_title)
    TextView actionbar_text_title;
    @ViewInject(id = R.id.actionbar_btn_back)
    ImageView actionbar_btn_back;

    @ViewInject(id = R.id.main_page_left)
    Button main_page_left;
    @ViewInject(id = R.id.main_page_right)
    Button main_page_right;

    private ViewPager mviewpager; //页面内容
    private List<View> mlistviews;
    private int current_Index = 0;  //当前页面的编号
    LayoutInflater mInflater;
    MyPagerAdapter madapter;
    private String path = "/data/data/com.anl.wxb.jieqi/databases/jieqi.db";   //文件夹databases的路径
    File file_jieqi = new File(path);
    private String password = "a";  //数据库加密密码
    private SQLiteDatabase dbwrite;
    private Db dbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SQLiteDatabase.loadLibs(this);
        //如果jieqi.db不存在，则写数据到jieqi.db数据库
        if (!file_jieqi.exists()) {
            dbhelper = new Db(this, "jieqi.db", null, 1);
            dbwrite = dbhelper.getWritableDatabase(password);
            new writeData_AsyncTask().execute();
        }
        initPagerView();
        onClick();
    }

    /**
     * 初始化 pagerview
     * */
    private void initPagerView() {
        //绑定layout1_main,layout2_main,layout3_main,
        mInflater = getLayoutInflater();
        mlistviews = new ArrayList<View>();
        mlistviews.add(mInflater.inflate(R.layout.layout1_main, null));
        mlistviews.add(mInflater.inflate(R.layout.layout2_main, null));
        mlistviews.add(mInflater.inflate(R.layout.layout3_main, null));

        madapter = new MyPagerAdapter(mlistviews);
        mviewpager = (ViewPager) findViewById(R.id.main_viewpager);
        mviewpager.setAdapter(madapter);
        mviewpager.setCurrentItem(current_Index);
    }

    /**
     * 点击事件处理
     * */
    private void onClick() {
        actionbar_btn_back = (ImageView) findViewById(R.id.actionbar_btn_back);
        actionbar_text_back = (TextView) findViewById(R.id.actionbar_text_back);
        main_page_left = (Button) findViewById(R.id.main_page_left);
        main_page_right = (Button) findViewById(R.id.main_page_right);

        actionbar_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        actionbar_text_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
        //第一页左翻不处理,左翻按钮隐藏，最后一页右翻不处理,右翻按钮隐藏
        main_page_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current_Index != 0) {
                    current_Index--;
                    mviewpager.setCurrentItem(current_Index);
                }
            }
        });
        main_page_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onCreate => initClick => main_page_right");
                if (current_Index != 2) {
                    current_Index++;
                    mviewpager.setCurrentItem(current_Index);
                }
            }
        });
    }


    /**
     * 适配器
     * */
    private class MyPagerAdapter extends PagerAdapter {
        public List<View> ListViews;
        public View view1;
        public View view2;
        public View view3;

        public MyPagerAdapter(List<View> mlistviews) {
            this.ListViews = mlistviews;
            view1 = mlistviews.get(0);
            view2 = mlistviews.get(1);
            view3 = mlistviews.get(2);
        }

        @Override
        public int getCount() {
            return ListViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        //        点击节气图，使用startActivityForResult()并传递参数
        //        参数确定点击的是第几个节气
        @Override
        public Object instantiateItem(View container, int position) {
            switch (position) {
                case 0:
                    main_image1 = (ImageView) ListViews.get(position).findViewById(R.id.main_image1);
                    main_image1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //新建一个显式意图，第一个参数为当前Activity类对象，第二个参数为你要打开的Activity类
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "0");
                            startActivityForResult(intent, 0);
                        }
                    });
                    main_image2 = (ImageView) ListViews.get(position).findViewById(R.id.main_image2);
                    main_image2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "1");
                            startActivityForResult(intent, 1);
                        }
                    });
                    main_image3 = (ImageView) ListViews.get(position).findViewById(R.id.main_image3);
                    main_image3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "2");
                            startActivityForResult(intent, 2);
                        }
                    });

                    main_image4 = (ImageView) ListViews.get(position).findViewById(R.id.main_image4);
                    main_image4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "3");
                            startActivityForResult(intent, 3);
                        }
                    });

                    main_image5 = (ImageView) ListViews.get(position).findViewById(R.id.main_image5);
                    main_image5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "4");
                            startActivityForResult(intent, 4);
                        }
                    });

                    main_image6 = (ImageView) ListViews.get(position).findViewById(R.id.main_image6);
                    main_image6.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "5");
                            startActivityForResult(intent, 5);
                        }
                    });

                    main_image7 = (ImageView) ListViews.get(position).findViewById(R.id.main_image7);
                    main_image7.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "6");
                            startActivityForResult(intent, 6);
                        }
                    });

                    main_image8 = (ImageView) ListViews.get(position).findViewById(R.id.main_image8);
                    main_image8.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "7");
                            startActivityForResult(intent, 7);
                        }
                    });

                    break;
                case 1:
                    main_image9 = (ImageView) ListViews.get(position).findViewById(R.id.main_image9);
                    main_image9.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "8");
                            startActivityForResult(intent, 8);
                        }
                    });

                    main_image10 = (ImageView) ListViews.get(position).findViewById(R.id.main_image10);
                    main_image10.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "9");
                            startActivityForResult(intent, 9);
                        }
                    });

                    main_image11 = (ImageView) ListViews.get(position).findViewById(R.id.main_image11);
                    main_image11.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "10");
                            startActivityForResult(intent, 10);
                        }
                    });

                    main_image12 = (ImageView) ListViews.get(position).findViewById(R.id.main_image12);
                    main_image12.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "11");
                            startActivityForResult(intent, 11);
                        }
                    });

                    main_image13 = (ImageView) ListViews.get(position).findViewById(R.id.main_image13);
                    main_image13.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "12");
                            startActivityForResult(intent, 12);
                        }
                    });

                    main_image14 = (ImageView) ListViews.get(position).findViewById(R.id.main_image14);
                    main_image14.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "13");
                            startActivityForResult(intent, 13);
                        }
                    });

                    main_image15 = (ImageView) ListViews.get(position).findViewById(R.id.main_image15);
                    main_image15.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "14");
                            startActivityForResult(intent, 14);
                        }
                    });

                    main_image16 = (ImageView) ListViews.get(position).findViewById(R.id.main_image16);
                    main_image16.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "15");
                            startActivityForResult(intent, 15);
                        }
                    });
                    break;

                case 2:
                    main_image17 = (ImageView) ListViews.get(position).findViewById(R.id.main_image17);
                    main_image17.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "16");
                            startActivityForResult(intent, 16);
                        }
                    });

                    main_image18 = (ImageView) ListViews.get(position).findViewById(R.id.main_image18);
                    main_image18.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "17");
                            startActivityForResult(intent, 17);
                        }
                    });

                    main_image19 = (ImageView) ListViews.get(position).findViewById(R.id.main_image19);
                    main_image19.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "18");
                            startActivityForResult(intent, 18);
                        }
                    });

                    main_image20 = (ImageView) ListViews.get(position).findViewById(R.id.main_image20);
                    main_image20.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "19");
                            startActivityForResult(intent, 19);
                        }
                    });

                    main_image21 = (ImageView) ListViews.get(position).findViewById(R.id.main_image21);
                    main_image21.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "20");
                            startActivityForResult(intent, 20);
                        }
                    });

                    main_image22 = (ImageView) ListViews.get(position).findViewById(R.id.main_image22);
                    main_image22.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "21");
                            startActivityForResult(intent, 21);
                        }
                    });

                    main_image23 = (ImageView) ListViews.get(position).findViewById(R.id.main_image23);
                    main_image23.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "22");
                            startActivityForResult(intent, 22);
                        }
                    });

                    main_image24 = (ImageView) ListViews.get(position).findViewById(R.id.main_image24);
                    main_image24.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                            intent.putExtra("name", "23");
                            startActivityForResult(intent, 23);
                        }
                    });
                    break;
                default:
                    break;
            }

            ((ViewPager) container).addView(ListViews.get(position));
            return ListViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(ListViews.get(position));
        }
    }


    /**
     * 接受从ContentActivity发送的数据
     *
     * num接受来自详解页面的数据data,0-7页面设置current_Index=1,8-15页面current_index=2,16-23页面current_Index=3
     * 若直接setCurrentItem(0/1/2),会导致current_Index不同步，点击左右翻页按钮产生bug
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                int num = data.getIntExtra("count", 0);
                if (num == 0 || num == 1 || num == 2 || num == 3 || num == 4 || num == 5 || num == 6 || num == 7) {
                    current_Index = 0;
                    mviewpager.setCurrentItem(current_Index);
                } else if (num == 8 || num == 9 || num == 10 || num == 11 || num == 12 || num == 13 || num == 14 || num == 15) {
                    current_Index = 1;
                    mviewpager.setCurrentItem(current_Index);
                } else if (num == 16 || num == 17 || num == 18 || num == 19 || num == 20 || num == 21 || num == 22 || num == 23) {
                    current_Index = 2;
                    mviewpager.setCurrentItem(current_Index);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 数据库写入数据 线程
     * */
    private class writeData_AsyncTask extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            ContentValues contentValues = new ContentValues();

            contentValues.put("name", "0");
            contentValues.put("text", "立春，是二十四节气中的第一个节气，干支历的岁首 ，建寅月之始日 ；到达时间点在公历每年2月3-5日，太阳到达黄经315°时。");
            contentValues.put("explain", "        自秦代以来，我国就一直以立春作为春季的开始。\n" +
                    "        立春是从天文上来划分的，而在自然界，在人们的心目中，春是温暖，鸟语花香；春是生长，耕耘播种。在气候学中，春季是指候（5天为一候）平均气温10℃至22℃的时段。\n" +
                    "        时至立春，人们明显地感觉到白昼长了，太阳暖了。气温，日照，降雨，这时常处于一年中的转折点，趋于上升或增多。小春作物长势加快，油菜抽苔和小麦拔节时耗水量增加，应该及时浇灌追肥，促进生长。农谚提醒人们：“立春雨水到，早起晚睡觉”大春备耕也开始了。\n" +
                    "        虽然立了“春”，但是盆地大部分地区仍会有霜冻出现，少数年份还会有“白雪却嫌春色晚，故穿庭树作飞花”的景象。这些气候特点，在安排农业生产时都是应该考虑到的。\n" +
                    "        人们常爱寻觅春的信息在哪里呢？那柳条上探出头来的芽苞，“嫩于金色软于丝”；那泥土中跃跃欲出的小草，等待“春风吹又生”；而为着夺取新丰收在田野中辛勤劳动的人们，正在用双手创造真正的春天。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "1");
            contentValues.put("text", "雨水，是二十四节气之中的第二个节气，位于每年正月十五前后（公历2月18-20日）。太阳位于黄经330°。");
            contentValues.put("explain", "        雨水节气的涵义是降雨开始，雨量渐增，在二十四节气的起源地黄河流域，雨水之前天气寒冷，但见雪花纷飞，难闻雨声淅沥。\n" +
                    "        雨水之后气温一般可升至0℃以上，雪渐少而雨渐多。可是在气候温暖的四川盆地，即使隆冬时节，降雨也不罕见。四川盆地这段时间候平均气温多在10℃以上，桃李含苞，樱桃花开，确以进入气候上的春天。除了个别年份外，霜期至此也告终止。嫁接果木，植树造林，正是时候。\n" +
                    "        盆地继冬干之后，常年多春旱，特别是盆地西部更是“春雨贵如油”。农业上要注意保墒，及时浇灌，以满足小麦拔节孕穗，油菜抽苔开花需水关键期的水分供应。川西高原山地仍处于干季，空气温度小，风速大，容易发生森林火灾。另外，寒潮入侵时可引起强降温和暴风雪，对老，弱，幼畜危害极大。\n" +
                    "        所有这些，都要特别注意预防。光阴易逝，季节催人，“一年之计在于春”。小春管理和大春备耕都应抓紧进行，争取今年胜过往年。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "2");
            contentValues.put("text", "惊蛰，古称“启蛰”，是二十四节气中的第3个节气，更是干支历卯月的起始；时间点在公历3月5-6日之间，太阳到达黄经345°时。");
            contentValues.put("explain", "        反映自然物候现象的惊蛰，含义是：春雷乍动，惊醒了蛰伏在土中冬眠的动物。这时，气温回升较快，长江流域大部地区已渐有春雷。\n" +
                    "        四川盆地东部和凉山州南部，常年雨水，惊蛰亦可闻春雷初鸣；而盆地西北部除了个别年份以外，一般要到清明才有雷声，为我国雷暴开始最晚的地区。到了惊蛰，我国大部地区进入春耕大忙季节。真是：季节不等人，一刻值千金。\n" +
                    "        我国盆地惊蛰节气平均气温一般为12℃至14℃，较雨水节气升高3℃以上，是全年气温回升最快的节气。日照时数也有比较明显的增加。但是因为冷暖空气交替，天气不稳定，气温波动甚大。盆地东南部长江河谷地区，多数年份惊势期间气温稳定在12℃以上，有利于水稻和玉米播种，其余地区则常有连续3天以上日平均气温在12℃以下的低温天气出现，不可盲目早播。\n" +
                    "        惊蛰虽然气温升高迅速，但是雨量增多却有限。盆地中部和西北部惊蛰期间降雨总量仅10毫米左右，继常年冬干之后，春旱常常开始露头。这时小麦孕穗，油菜开花都处于需水较多的时期，对水分要求敏感，春旱往往成为影响小春产量的重要因素。植树造林也应该考虑这个气候特点，栽后要勤于浇灌，努力提高树苗成活率。\n" +
                    "        惊蛰时节，春光明媚，万象更新。通过细致观察，积累物候知识，对于因地制宜地安排农事活动是会有帮助的。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "3");
            contentValues.put("text", "春分，是春季九十天的中分点。二十四节气之一，每年公历大约为3月20日左右，太阳位于黄经0°（春分点）时。");
            contentValues.put("explain", "        春分是反映四季变化的节气之一。中国古代习惯以立春，立夏，立秋，立冬表示四季的开始。春分，夏至，秋分，冬至则处于各季的中间。\n" +
                    "        春分这天，太阳光直射赤道，地球各地的昼夜时间相等，所以古代春分秋分又称为“日夜分”，民间有“春分秋分，昼夜平分”的谚语。\n" +
                    "        春分后，中国大部分地区越冬作物进入春季生长阶段。华中有“春分麦起身，一刻值千金”的农谚。我国各地气温则继续回升，但一般不如雨水至春分这段时期上升得快。3月下旬平均气温盆地北部多为13℃至15℃，盆地南部多为15℃至16℃。高原大部分地区已经雪融冰消，旬平均气温约5℃至10℃。我省西南部金沙江，安宁河等河谷地区气温最高，平均已达18℃至20℃左右。盆地除了边缘山区以外，平均十有七，八年日平均气温稳定上升到12℃以上，有利于水稻，玉米等作物播种，植树造林也非常适宜。\n" +
                    "        但是，春分前后盆地常常有一次较强的冷空气入侵，气温显著下降，最低气温可低至5℃以下。有时还有小股冷空气接踵而至，形成持续数天低温阴雨，对农业生产不利。根据这个特点，应充分利用天气预报，抓住冷尾暖头适时播种。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "4");
            contentValues.put("text", "清明，是二十四节气中的第5个节气，更是干支历辰月的起始；时间点在农历每年三月初一前后，太阳到达黄经15°时。");
            contentValues.put("explain", "        清明，又名“三月节”或“踏青节“。是表征物候的节气，含有天气晴朗，草木繁茂的意思。\n" +
                    "        清明这天，民间有踏青，寒食，扫墓等习俗。常言道：“清明断雪，谷雨断霜。”时至清明，盆地气候温暖，春意正浓。\n" +
                    "        但在清明前后，仍然时有冷空气入侵，甚至使日平均气温连续3天以上低于12℃，造成中稻烂秧和早稻死苗，所以水稻播种，栽插要避开暖尾冷头。在川西高原，牲畜经严冬和草料不足的影响，抵抗力弱，需要严防开春后的强降温天气对老弱幼畜的危害。“清明时节雨纷纷”，是唐代著名诗人杜牧对江南春雨的写照。但是就四川而言，情况并非如此。特别是盆地西部，常处于春旱时段，4月上旬雨量一般仅10至20毫米，尚不足江南一带的一半；盆地东部虽然春雨较多，但4月上旬雨量一般也不过20至40毫米，自然降水亦不敷农业生产之需还须靠年前蓄水补充。此外，4月是凉山州一年之中冰雹最多的月份，应当加强对雹灾的防御。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "5");
            contentValues.put("text", "谷雨是二十四节气的第六个节气，每年4月19日～21日时太阳到达黄经30°时为谷雨，源自古人“雨生百谷”之说。");
            contentValues.put("explain", "        俗话说：“雨生百谷”。雨量充足而及时，谷类作物能够茁壮生长。谷雨节气就有这样的涵义。\n" +
                    "        谷雨时节的四川盆地，“杨花落尽子规啼”，柳絮飞落，杜鹃夜啼，牡丹吐蕊，樱桃红熟，自然景物告示人们：时至暮春了。这时，盆地的气温升高较快，一般4月下旬平均气温，除了盆地北部和西部部分地区外，已达20℃至22℃，比中旬增高2℃以上。盆地东部常会有一，二天出现30以上的高温，使人开始有炎热之感。川西南低海拔河谷地带业以进入夏季。盆地春季气温较高的气候特点，有利于在大春作物栽培措施上抓早。适宜红苕栽插的温度为18℃至22℃，这时已能满足。盆地老旱区的经验证明，红苕在谷雨后早栽，能够在伏旱前使藤叶封厢，增强抗旱能力，获得高产稳产。四川盆地东部这时雨水较丰，常年4月下旬雨量约30至50毫米，每年第一场大雨一般出现在这段时间，对水稻栽插和玉米，棉花苗期生长有利。但是盆地其余地区雨水大多不到30毫米，需要采取灌溉措施，减轻干旱影响。川西高原山地，仍处于干季，降水量一般仅5至20毫米。盆地谷雨前后的降雨，常常“随风潜入夜，润物细无声”，这是因为“巴山夜雨”以4，5月份出现机会最多。“蜀天常夜雨，江槛已朝清”，这种夜雨昼晴天气，对大春作物生长和小春作物收获是颇为适宜的。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "6");
            contentValues.put("text", "立夏是二十四节气中的第7个节气，更是阳历辰月的结束以及巳月的起始；时间点在公历5月5-6日之间，太阳到达黄经45度时。");
            contentValues.put("explain", "        顾名思义，立夏是指夏季开始。\n" +
                    "        但是，各地冷暖不同，入夏时间实际上并不一致。按气候学上以五天平均气温高于22℃为夏季的标准，立夏前后，四川盆地南部刚跨进夏季；盆地其余的地区气温为20℃左右，还处于“门外无人问落花，绿阴冉冉遍天涯”的暮春时节；而川西南低海拔河谷则早在4月中旬初即感夏热，立夏时气温已达24℃以上，可谓夏日炎炎了。《易纬》有立夏“电见”之说。但就四川而言，即使在初雷最晚的盆地西北部，常年雷暴也始于4月上，中旬，“电见”无须等到立夏。\n" +
                    "        立夏以后，正是盆地中稻大面积栽插的需水关键期，大雨来临的早迟和雨量的多少，与农业生产关系密切。此时如不下较大的雨，那些无水灌溉的农田就无法梨耙栽秧。据气候资料统计，多年平均大雨开始期，盆地东部在4月中，下旬，中部在5月中，下旬，西部在5月下旬。5月雨量盆地东南部为100至200毫米，西北部为75至100米。盆地西部，中部因大雨开始较晚，雨量偏少，往往有夏旱露头。这段时间，正当盆地收获小春作物，播栽大春作物，特别要注意多变天气的影响。晴天要及时抢收，雨天应抓紧栽插，连阴雨天气须提防小春收获物生芽，霉烂，还要搞好抗旱保苗，警惕20℃以下的低温对早稻的危害。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "7");
            contentValues.put("text", "小满是二十四节气之一，夏季的第二个节气。每年5月20日到22日之间视太阳到达黄经60°时为小满。");
            contentValues.put("explain", "        二十四节气大多可以顾名思义，但是小满却有些令人费解。原来，小满是指麦类等夏熟作物灌浆乳熟，籽粒开始饱满。\n" +
                    "        四川盆地的农谚赋予小满以新的寓意：“小满不满，干断思坎”；“小满不满，芒种不管”。把“满”用来形容雨水的盈缺，指出小满时田里如果蓄不满水，就可能造成田坎干裂，甚至芒种时也无法栽插水稻。因为“立夏小满正栽秧”，“秧奔小满谷奔秋”，小满正是适宜水稻栽插的季节。 +\n" +
                    "        盆地的夏旱严重与否，和水稻栽插面积的多少，有直接的关系；而栽插的迟早，又与水稻单产的高低密切相关。盆地中部和西部，常有冬干春旱，大雨来临又较迟，有些年份要到6月大雨才姗姗而来，最晚甚至可迟至7月。加之常年小满节气雨量不多，平均仅40毫米左右，自然降雨量不能满足栽秧需水量，使得水源缺乏的盆地中部夏旱更为严重。俗话说：“蓄水如蓄粮”，“保水如保粮”。为了抗御干旱，除了改进耕作栽培措施和加快植树造林外，特别需要注意抓好头年的蓄水保水工作。但是，也要注意可能出现的连续阴雨天气，对小春作物收晒的影响。\n" +
                    "        川西高原山地区，这时多已进入雨季，作物生长旺盛，欣欣向荣。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "8");
            contentValues.put("text", "芒种是二十四节气中的第9个节气，更是干支历午月的起始；时间点在公历每年6月6日前后，太阳到达黄经75°时。");
            contentValues.put("explain", "        芒种是表征麦类等有芒作物的成熟，是一个反映农业物候现象的节气。\n" +
                    "        时至芒种，四川盆地麦收季节已经过去，中稻，红苕移栽接近尾声。大部地区中稻进入返青阶段，秧苗嫩绿，一派生机。“东风染尽三千顷，折鹭飞来无处停”的诗句，生动的描绘了这时田野的秀丽景色。\n" +
                    "        到了芒种时节，盆地内尚未移栽的中稻，应该抓紧栽插；如果再推迟，因气温提高，水稻营养生长期缩短，而且生长阶段又容易遭受干旱和病虫害，产量必然不高。红苕移栽至迟也要赶在夏至之前；如果栽苕过迟，不但干旱的影响会加重，而且待到秋来时温度下降，不利于薯块膨大，产量亦将明显降低。农谚“芒种忙忙栽”的道理就在这里。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "9");
            contentValues.put("text", "夏至是二十四节气之一，在每年公历6月21日或22日。夏至这天，太阳运行至黄经90度(处在双子座)。");
            contentValues.put("explain", "        夏至这天，太阳直射北回归线，是北半球一年中白昼最长的一天，四川各地从日出到日没大多为十四小时左右。夏至这虽然白昼最长，太阳高度角最高，但并不是一年中最热的的时候。因为，近地层的热量，这时还在继续积蓄，并没有达到最多之时。\n" +
                    "        过了夏至，我国盆地农业生产因农作物生长旺盛，杂草，病虫迅速滋长蔓延而进入田间管理时期，高原牧区则开始了草肥畜旺的黄金季节。这时，盆地西部雨水量显著增加，使入春以来盆地雨量东多西少的分布形势，逐渐转变为西多东少。如有夏旱，一般这时可望解除。近三十年来，盆地西部6月下旬出现大范围洪涝的次数虽不多，但程度却比较严重。因此，要特别注意作好防洪准备。\n" +
                    "        夏至节气是盆地东部全年雨量最多的节气，往后常受副热带高压控制，出现伏旱。为了增强抗旱能力，夺取农业丰收，在这些地区，抢蓄伏前雨水是一项重要措施。夏至以后地面受热强烈，空气对流旺盛，午后至傍晚常易形成雷阵雨。这种热雷雨骤来疾去，降雨范围小，人们称为“夏雨隔田坎”。唐代诗人刘禹锡在四川，曾巧妙地借喻这种天气，写出“东边日出西边雨，道是无晴却有晴”的著名诗句。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "10");
            contentValues.put("text", "小暑，是二十四节气之第十一个节气，也是干支历午月的结束以及未月的起始；公历每年7月7日或8日时太阳到达黄经105°时为小暑。");
            contentValues.put("explain", "        绿树浓荫，时至小暑。四川盆地小暑时平均气温为26℃左右，已是盛夏，颇感炎热，但还未到最热的时候。\n" +
                    "        常年7月中旬，盆地东南低海拔河谷地区，可开始出现日平均气温高于30℃，日最高气温高于35℃的集中时段，这对杂交水稻抽穗扬花不利。除了事先在作布局上应该充分考虑这个因素外，已经栽插的要采取相应的补救措施。在川西高原北部，此时仍可见霜雪，相当于盆地初春时节景象。\n" +
                    "        小暑前后，盆地西部进入暴雨最多季节，常年7，8两月的暴雨日数可占全年的75%以上，一般为3天左右。在地势起伏较大的地方，常有山洪暴发，甚至引起泥石流。但在盆地东部，小暑以后因常受副热带高压控制，多连晴高温天气，开始进入伏旱期。我国盆地这一东旱西涝的气候特点，与农业丰歉关系很大，必须及早分别采取抗旱，防洪措施，尽量减轻危害。\n" +
                    "        小暑前后，我国各地进入雷暴最多的季节。雷暴是一种剧烈的天气现象，常与大风，暴雨相伴出现，有时还有冰雹，容易造成灾害，亦须注意预防。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "11");
            contentValues.put("text", "大暑是二十四节气之一，北半球在每年7月22-24日之间，南半球在每年1月20-21日之间，太阳位于黄经120°。");
            contentValues.put("explain", "        暑是炎热的意思。表明它是一年中最热的节气。一般说来，大暑节气是盆地一年中日照最多，气温最高的时期，是盆地西部雨水最丰沛，雷暴最常见，30℃以上高温日数最集中的时期，也是盆地东部35℃以上高温出现最频繁的时期。\n" +
                    "        大暑前后气温高本是气候正常的表现，因为较高的气温有利于大春作物扬花灌浆，但是气温过高，农作物生长反而受到抑制，水稻结实率明显下降。盆地西部入伏后，光，热，水都处于一年的高峰期，三者互为促进，形成对大春作物生长的良好气候条件，但是需要注意防洪排涝。+\n" +
                    "        盆地东部这时高温长照却往往与少雨相伴出现，不仅会限制光热优势的发挥，还会加剧伏旱对大春作物的不利影响，为了抗御伏旱，除了前期要注意蓄水以外，还应该根据盆地东部的气候特点，改进作物栽培措施，立足于“早”，以趋利避害。燠热的大暑是茉莉，荷花盛开的季节，馨香沁人的茉莉，天气愈热香愈浓郁，给人洁净芬芳的享受。高洁的荷花，不畏烈日骤雨，晨开暮敛，诗人赞美它“映日荷花别样红”，生机勃勃的盛夏，正孕育着丰收。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "12");
            contentValues.put("text", "立秋，是二十四节气中的第13个节气，更是干支历未月的结束以及申月的起始；时间在农历每年七月初一前后（公历8月7-9日之间）。");
            contentValues.put("explain", "        “立秋之日凉风至”明确地把立秋与天凉联系起来。可见，立秋就是凉爽的秋季开始了。\n" +
                    "        由于各地纬度，海拔高度等的不同，实际上是不可能都在立秋这一天同时进入秋季的。按照气候学上以候（5天）平均气温在10℃至22℃之间为春，秋的标准，在我国除了那些纬度偏北和海拔较高的地方以外，立秋时多未入秋，仍然处于炎夏之中，即使在东北的大部分地区，这时也还看不到凉风阵阵，黄叶飘飘的秋天景色。对于地处中亚热带的四川盆地来说，常年8月暑气犹重。气候资料统计表明，盆地要到9月中，下旬方才先后进入秋季；在全年皆冬或者冬长无夏，春秋相连的高原和高山地区，说不上秋季什么时间开始。\n" +
                    "        立秋以后，我国盆地晚稻拔节孕穗，棉花裂铃吐絮，丝毫不可放松田间管理；中稻，夏玉米进入灌浆成熟阶段，要提防冰雹，大风，暴雨的危害。盆地东部和西部，应该分别继续做好抗旱，防洪工作。\n" +
                    "        立秋后的盆地，时令虽仍属盛夏，但“立秋十天遍地黄”一个金色“秋天”就要到来了。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "13");
            contentValues.put("text", "处暑，是二十四节气之中的第14个节气，交节时间点在公历8月23日前后，太阳到达黄经150°。");
            contentValues.put("explain", "        处暑是反映气温变化的一个节气。“处”含有躲藏，终止意思，“处暑”表示炎热暑天结束了。\n" +
                    "        四川盆地处平均气温一般较立秋降低1.5℃左右，个别年份8月下旬盆地西部可能出现连续3天以上日平均气温在23℃以下的低温，影响杂交水稻开花。但是，由于盆地处暑时仍基本上受夏季风控制，所以还常有盆地西部最高气温高于30℃，盆地东部高于35℃的天气出现。特别是长江沿岸低海拔地区，在伏旱延续的年份里，更感到“秋老虎”的余威。川西高原进入处暑秋意正浓，海拔3500米以上已呈初冬景象，牧草渐萎，霜雪日增。\n" +
                    "        处暑是四川盆地雨量分布由西多东少向东多西少转换的前期。这时盆地中部的雨量常是一年里的次高点，比大暑或白露时为多。因此，为了保证冬春农田用水，必须认真抓好这段时间的蓄水工作。高原地区处暑至秋分会出现连续阴雨水天气，地农牧业生产不利。我国盆地这时也正是收获中稻的大忙时节。一般年辰处暑节气内，盆地日照仍然比较充足，除了盆地西部以外，雨日不多，有利于中稻割晒和棉花吐絮。可是少数年份也有如杜诗所说“三伏适已过，骄阳化为霖”的景况，秋绵雨会提前到来。所以要特别注意天气预报，做好充分准备，抓住每个晴好天气，不失时机地搞好抢收抢晒。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "14");
            contentValues.put("text", "白露是二十四节气中的第十五个节气，是干支历申月结束及酉月起始；时间点公历每年9月7日到9日，太阳到达黄经165度时。");
            contentValues.put("explain", "        露是由于温度降低，水汽在地面或近地物体上凝结而成的水珠。所以，白露实际上是表征天气已经转凉。\n" +
                    "        我国盆地二十四节气的气候中，白露有着气温迅速下降，绵雨开始，日照骤减的明显特点，深刻地反映出由夏到秋的季节转换。盆地常年白露期间的平均气温比处暑要低3℃左右，大部地区候(5天)平均气温先后降至22℃以下。按气候学划分四季的标准，时序开始进入秋季。盆地秋雨多出现于白露至霜降前，以岷江，青衣江中下游地区最多，盆地中部相对较少。“滥了白露，天天走溜路”的农谚，虽然不能以白露这一天是否有雨水来作天气预报，但是，一般白露节前后确实常有一段连阴雨天气；而且，自此盆地降雨多具有强度小，雨日多，常连绵的特点了。与此相应，盆地白露期间日照较处暑骤减一半左右，递减趋势一直持续到冬季。\n" +
                    "        白露时节的上述气候特点，对晚稻抽穗扬花和棉桃爆桃是不利的，也影响中稻的收割和翻晒，所以农谚有“白露天气晴，谷米白如银”的说法。充分认识白露气候特点，并且采取相应的农技措施，才能减轻或避免秋雨危害。另一方面，也要趁雨抓紧蓄水，特别是盆地东部的白露是继小满，夏至后又一个雨量较多的节气，更不要错过良好时机。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "15");
            contentValues.put("text", "秋分，农历二十四节气中的第十六个节气，时间一般为每年的9月22或23日。太阳到达黄经180度时。");
            contentValues.put("explain", "        秋分是表征季节变化的节气。秋分这天，太阳位于黄经180度，阳光几乎直射赤道，昼夜几乎等长。\n" +
                    "        这时，四川盆地候温普遍降至22℃以下，进入了凉爽的秋季。“一场秋雨一场寒”。一股股南下的冷空气，与逐渐衰减的暖湿空气相遇，产生一次次降雨，气温也一次次下降。在川西高原北部，日最低气温降到0℃以下，已经可见到漫天絮飞舞，大地素裹银装的壮丽雪景。\n" +
                    "        秋分以后，四川省雨量明显减少，暴雨，大雨一般很少出现；不过，降雨日数却反而有所增加，常常阴雨连绵，夜雨率也较高。唐代著名诗人李商隐“巴山夜雨涨秋池”的名句，生动形象地描绘出四川秋多夜雨的气候特色。我国盆地和凉山州秋多绵雨，湿害严重，对秋收，秋耕和秋种影响颇大。要抢晴收晒，理墒防渍，抓好“三秋”生产的质量和进度。同时，还要充分利用秋季阴雨寡照，土土壤墒情较好的气象条件，不失时机地大搞植树造林，努力提高树苗成活率。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "16");
            contentValues.put("text", "寒露是二十四节气中的第17个节气，是干支历酉月的结束以及戌月的起始；时间点在公历每年10月8日或9日视太阳到达黄经195°时。");
            contentValues.put("explain", "        古代把露作为天气转凉变冷的表征。仲秋白露节气“露凝而白”，至季秋寒露时已是“露气寒冷，将凝结”为霜了。\n" +
                    "        这时，我国各地气温继续下降。盆地日平均气温多不到20℃，即使在长江沿岸地区，水银柱也很难升到30℃以上，而最低气温却可降至10℃以下。川西高原除了少数河谷低地以外，候（5天）平均气温普遍低于10℃，用气候学划分四季的标准衡量，已是冬季了。\n" +
                    "        千里霜铺，万里雪飘，与盆地秋色迥然不同。常年寒露期间，盆地雨量亦日趋减少。盆地西部多在20毫米上下，东部一般为30至40毫米左右。绵雨甚频，朝朝暮暮，溟溟霏霏，影响“三秋”生产，成为我省盆地的一种灾害性天气。伴随着绵雨的气候特征是：湿度大，云量多，日照少，阴天多，雾日亦自此显著增加。但是，秋绵雨严重与否，直接影响“三秋”的进度与质量。为此，一方面，要利用天气预报，抢晴天收获和播种；另一方面，也要因地制宜，采取深沟高厢等各种有效的耕作措施，减轻湿害，提高播种质量。在高原地区，寒露前后是雪害最严重的季节之一，积雪阻塞交通，危害畜牧业生产，应该注意预防。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "17");
            contentValues.put("text", "霜降，二十四节气之一，每年公历10月23日左右，太阳位于黄经210°时。");
            contentValues.put("explain", "        霜降节气含有天气渐冷，开始降霜的意思。\n" +
                    "        纬度偏南的四川盆地，平均气温多在16℃左右，离初霜日期还有三个节气。在盆地南部河谷地带，则要到隆冬时节，才能见霜。当然，即使在纬度相同的地方，由于海拔高度和地形不同，贴地层空气的温度和湿度有差异，初霜期和霜日数也就不一样了。用科学的眼光来看，“露结为霜”的说法是不准确的。露滴冻结而成的冻露，是坚硬的小冰珠。而霜冻是指由于温度剧降而引起的作物冻害现象，其致害温度因作物，品种和生育期的不同而异；而形成霜，则必须地面或地物的温度降到0℃以下，并且贴地层中空气中的水气含量要达到一定程度。因此，发生霜冻时不一定出现霜，出现霜时也不一定就有霜冻发生。但是，因为见霜时的温度已经比较低，要是继续冷却，便很容易导致霜冻的发生。\n" +
                    "        霜降过后，我省盆地开始大量收挖红苕。若收挖过早，苕块尚未充分膨大，就会影响产量；但收挖过迟，有可能遭受早霜冻危害，苕块受冻变质，不耐贮藏，故适时挖苕很重要。北宋大文学家苏轼有诗曰：“千树扫作一番黄，只有芙蓉独自芳。”四川盆地气候温和，霜降期间，田畴青葱，橙黄桔绿，秋菊竞放，一树树芙蓉盛开，把富饶的“天府”打扮得更加艳丽。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "18");
            contentValues.put("text", "立冬，是二十四节气之一，作为干支历戌月的结束以及亥月的起始；时间点在公历每年11月7-8日之间，即太阳位于黄经225°");
            contentValues.put("explain", "        “立，建始也”，表示冬季自此开始。“立冬之日，水始冰，地始冻”。\n" +
                    "        现在，人们常以凛冽北风，寒冷的霜雪，作为冬天的象征。我国盆地亦霜雪稀少，所以，在气候学上，不固定以“立冬”这天作为各地冬季的开始，而是以气温来划分季节，即候（５天）平均气温低于10℃为冬季，这样就比较节合当时的物候景观。立冬时节的我国盆地，仍处于“三秋”繁忙时期，平均气温一般为12℃至15℃。绵雨业已结束。气候条件适宜于油菜移栽。生长期较短而春性较强的小麦也要抓播种，因为立冬后期多有强冷空气侵袭，气温常有较大幅度下降，如果播后气温低，出苗缓慢，分孽不足，就会影响产量。红苕在日平均气温低于15℃时，生长已渐趋停止，应该及时收获。盆地西北部个别年份立冬曾出现过早霜，更要早挖窖，免冻害。高原地区这时已是干季，湿度迅减，风速渐增，对森林火险必须高度警惕。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "19");
            contentValues.put("text", "小雪，是二十四节气中的第20个。11月22或23日，太阳到达黄经240°，此时称为小雪节气。");
            contentValues.put("explain", "        小雪表示降雪的起始时间和程度。雪是寒冷天气的产物。\n" +
                    "        小雪节气，四川盆地北部开始进入冬季。“荷尽已无擎雨盖，菊残犹有霜枝”,已呈初冬景象。因为北面有秦岭，大巴山屏障，阻挡冷空气入侵，刹减了寒潮的严威，致使盆地“冬暖”显著。全年降雪日数多在５天以下，比同纬度的长江中，下游地区少得多。大雪以前降雪的机会极少，即使隆冬时节，也难得观赏到“千树万树梨花开”的迷人景色。由于盆地冬季近地面层气温常保持在０℃以上，所以积雪比降雪更不容易。偶尔虽见天空“纷纷扬扬”,却不见地上“碎琼乱玉”。然而，在寒冷的川西高原，常年10月一般就开始降雪了。高原西北部全年降雪日数可达60天以上，一些高寒地区全年都有降雪的可能。\n" +
                    "        小雪期间，盆地西北部一般可见初霜，要预防霜冻对农作物的危害。甘，阿两州北部，最低气温多在零下15℃左右，应该做好牲畜的防寒保暖工作。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "20");
            contentValues.put("text", "“大雪”是二十四节气中的第21个节气，更是干支历亥月的结束以及子月的起始；时间点在公历每年的12月7或8日，其时视太阳到达黄经255度。");
            contentValues.put("explain", "        “大雪”表明这时降雪开始大起来了。\n" +
                    "        四川盆地冬季气候温和而少雨雪，平均气温较长江中下游地区约高2℃至4℃，雨量仅占全年的5％左右。偶有降雪，大多出现在1，2月份；地面积雪三，五年难见到一次。如果能够目睹大地白雪皑垲，绿树披银饰玉，常是终身难忘的趣事。\n" +
                    "        “瑞雪兆丰年”，是中国广为流传的农谚。在北方，一层厚厚而疏松的积雪，象给小麦盖御寒的棉被。雪中所含的氮化合物比雨水多４倍，积雪慢慢融化后渗入土中，能增加土壤中的氮素，易被农作物吸收利用。雪水温度低，能冻死地表层越冬的害虫，也给农业生产带来好处。但是，在南方，雪后如逢晴夜，地面热量散失较多，则会出现冻害，使豌，胡豆等作物受到一定损失。我国盆地平均气温尚多在8℃至9℃，小麦油菜仍可缓慢生长，需要加强田间管理，预防冻害。大雪期间，盆地降水多在15毫米以下，盆地西部更不足5毫米，已是“冬干”时期。这时，盆地气候还有多雾的特点，一般12月是雾日最多的月份。雾通常出现在夜间无云或少云的清晨，气象学称之为辐射雾。俗话：“十雾九晴”。雾多在午前消散，午后的阳光会显得格外温暖。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "21");
            contentValues.put("text", "冬至，是中国农历中一个重要的节气，冬至俗称“冬节”，“长至节”，“亚岁”等。时间在每年的公历12月21日至23日之间。");
            contentValues.put("explain", "        冬至是按天文划分的节气，古称“日短”，“日短至”。冬至这天，太阳位于黄经270度，阳光几乎直射南回归线，是北半球一年中白昼最短的一天。\n" +
                    "        我国各地日出到日没有10小时左右。冬至以后，随着地球在绕日轨道上运行，阳光直射地带便逐渐北移，使北半球白天渐增长，夜晚逐渐缩短。冬至日虽基太阳高工最低，日照时间最短，地面吸收的热量比散失的热量少，但是地面过去长期积累的热量，还在继续散失，近地层气温尚未降至最低，所以这时还不是一年中最冷的时候。过了冬至，虽然昼渐长，夜渐短，但是在短期内仍然是昼短夜长，地面每天吸收的热量，还是比散失的热量少，所以气温并没有立即回升之势。群众中习惯自冬至起“数九”，每九天为一个“九”。到“三九”前后，地面积蓄的热量最少，天气也最冷，所以说“冷在三九”。天文学上把冬至作为冬季的开始，这对于我国多数地区来说，显然偏迟。\n" +
                    "        冬至期间，川西高原平均气温普遍在０℃以下，四川盆地也只有６℃至８℃左右。不过，川西南低海拔河谷地区，即使在当地最冷的１月上旬，平均气温仍然在10℃以上，真可谓秋去春平，全年无冬。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "22");
            contentValues.put("text", "小寒是二十四节气中的第23个节气，更是干支历子月的结束以及丑月的起始,时间点在公历每年1月5-7日之间，太阳位于黄经285°。");
            contentValues.put("explain", "        寒即寒冷，小寒表示寒冷的程度。俗话说，“冷在三九”。“三九”多在1月9日至17日，也恰在小寒节气内。但这只是一般规律，少数年份大寒也可能比小寒冷。而人们记忆犹新的1975年冬，气温最低的节气竟是大雪哩！\n" +
                    "        四川盆地冬暖显著，隆冬1月，三峡以东霜雪交侵，常有冰冻，最低气温在零下10℃左右。而盆地北部最低气温却很少低于零下5℃，盆地南部0℃以下的低温更不多见。我国隆冬最冷的地区是川北高原北部，最低气温在可达零下30℃左右，天寒地冻，滴水成冰。川西南低海拔河谷地带，则是我国隆冬最暖的地方，１月平均气温在12℃左右，只有很少年份可能出现０℃以下的低温。加之逆温效应十分显著，所以香蕉，芒果等热带水果能够良好生长。盆地冬季最低气温不低，有利于不春生产，也适宜发展多种经营。“受命不迁，生南国兮”的柑桔，生长一般要求最低气温不低于零下５℃，年温高于15℃，盆地内绝大多数地区都能满足，副热带植物也几乎应有尽有。四川号称“天府之国”，得天独厚的气候条件，应当是一个很重要的因素。");
            dbwrite.insert("user", null, contentValues);

            contentValues.put("name", "23");
            contentValues.put("text", "大寒是二十四节气中的最后一个节气。每年1月20日前后太阳到达黄经300°时为大寒。");
            contentValues.put("explain", "        如小寒一样，大寒也是表征天气寒冷程度的节气。近代气象观测几记录虽然表明，在中国绝大部分地区，大寒不如小寒冷，但是，在某些年份和沿海少数地方，全年最低气温仍然会出现在大寒节气内。\n" +
                    "        大寒时节，在一定生育期内需要有适当的低温。冬性较强的小麦，油菜，通过春化阶段就要求较低的温度，否则不能正常生长发育。我省盆地常年冬暖，过早播种的小麦，油菜，往往长势太旺，提前拔节，抽苔，抗寒能力大大减弱，容易遭受低温霜冻的危害。可见，因地制宜选择作物品种，适时播栽，并采取有效的促进和控制措施，乃是夺取高产的重要一环。");
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
    protected void onDestroy() {
        super.onDestroy();
    }
}


