package com.example.knowledge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ItemDecoration;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static androidx.recyclerview.widget.RecyclerView.*;

public class shouye extends AppCompatActivity {
    public int dates;
    public String data_str = String.valueOf(dates);
    public int date2;
    public String data_str1;

    private RecyclerView mRvShouye;
    private SmartRefreshLayout mSmartRefreshLayout;
    private Map map;
    private List<Map<String,Object>> list = new ArrayList<>();

    //轮播图
    private ViewPager mLoopPager;
    private LooperPagerAdapter mLoopPagerAdapter;
    private static List<Integer> sPics = new ArrayList<>();

    static {
        sPics.add(R.drawable.bb2);
        sPics.add(R.drawable.zg);
        sPics.add(R.drawable.mgdx);
        sPics.add(R.drawable.zgm);
    }

    private int page = 0;
    private int pageSize = 10;
    private Calendar
            c = Calendar.getInstance();
    int s =c.get(Calendar.HOUR_OF_DAY);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shouye);

        initView();
//        Random random = new Random();//准备数据
//       for(int i = 0;i<5;i++)
//        {
//            sColos.add(Color.argb(random.nextInt(255),random.nextInt(255),random.nextInt(255),random.nextInt(255)));
//        }
        //设置样式
//        mLoopPagerAdapter.setData(sColos);
//        mLoopPagerAdapter.notifyDataSetChanged();

        final TextView day = (TextView) findViewById(R.id.day);
        final TextView month = (TextView) findViewById(R.id.month);
        //final TextView year =(TextView) findViewById(R.id.year);
        final TextView text2 = (TextView) findViewById(R.id.textView2);
        //显示时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy年MM月  ");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("dd日  ");
        final Date data = new Date(System.currentTimeMillis());
        day.setText(simpleDateFormat1.format(data));
        month.setText(simpleDateFormat.format(data));
        if (s >= 18 && s < 6)
            text2.setText("晚上好！");
        else if (s >= 6 && s < 12)
            text2.setText("早上好！");
        else if (s == 12)
            text2.setText("中午好！");
        else
            text2.setText("晚上好！");
        mRvShouye = (RecyclerView) findViewById(R.id.rv_shouye);
        //刷新加载
        mSmartRefreshLayout = (SmartRefreshLayout) findViewById(R.id.srl);
        mSmartRefreshLayout.setRefreshHeader(new BezierRadarHeader(this).setEnableHorizontalDrag(true));
        mSmartRefreshLayout.setRefreshFooter(new BallPulseFooter(this).setSpinnerStyle(SpinnerStyle.Scale));
        //下拉刷新
        mSmartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000);
                Toast.makeText(shouye.this, "刷新成功！", Toast.LENGTH_SHORT).show();
            }
        });
        //上拉加载
        mSmartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
                date2 = dates - 1;
                data_str1 = String.valueOf(date2);
                Toast.makeText(shouye.this, "加载成功！", Toast.LENGTH_SHORT).show();
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HttpURLConnection connection = null;
                        BufferedReader reader = null;
                        try {
                                URL url = new URL("https://news-at.zhihu.com/api/3/news/before/" +data_str1);
                                connection = (HttpURLConnection) url.openConnection();
                                connection.setRequestMethod("GET");
                                connection.setConnectTimeout(8000);
                                connection.setReadTimeout(8000);
                                InputStream in = connection.getInputStream();
                                reader = new BufferedReader(new InputStreamReader(in));
                                StringBuilder response = new StringBuilder();
                                String line;
                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }
                                showResponse(response.toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                            //Toast.makeText(shouye.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                        } finally {
                            if (reader != null) {
                                try {
                                    reader.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    //Toast.makeText(shouye.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }
                });
                thread.start();
            }

            public void showResponse(final String string) {
                try {
                    JSONObject jsonObject = new JSONObject(string);
                    Log.i("zyr", string);
                    dates = jsonObject.getInt("date");
                    final String date = String.valueOf(dates);
                    JSONArray jsonArray = jsonObject.getJSONArray("stories");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        final String title = jsonObject1.getString("title");
                        final JSONArray imagesArray = jsonObject1.getJSONArray("images");
                        String images = imagesArray.getString(0);
                        final int id = jsonObject1.getInt("id");
                        final String hint = jsonObject1.getString("hint");
                        final String url = jsonObject1.getString("url");
                        final String year,month,day;
                        Map<String, Object> map = new HashMap<>();
                        if (i==0){
                            year = date.substring(0,4);
                            month = date.substring(4,6);
                            day = date.substring(6,8);
                        }
                        else {
                            year = null;
                            month = null;
                            day = null;
                        }
                       //Map<String, Object> map = new HashMap<>();
                        map.put("i",i);
                        map.put("day",day);
                        map.put("month",month);
                        map.put("year",year);
                        map.put("title", title);
                        map.put("picture", images);
                        map.put("id", id);
                        map.put("hint", hint);
                        map.put("url", url);
                        list.add(map);
                    }//获取并解析数据

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mRvShouye.setLayoutManager(new LinearLayoutManager(shouye.this));
                            mRvShouye.addItemDecoration(new MyDecoration());
                            mRvShouye.setAdapter(new LinearAdapter(shouye.this, list));
                        }

                        class MyDecoration extends RecyclerView.ItemDecoration {
                            @Override
                            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull State state) {
                                super.getItemOffsets(outRect, view, parent, state);
                                outRect.set(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.Hight));
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
                    Thread thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HttpURLConnection connection = null;
                            BufferedReader reader = null;
                            try {
                            URL url = new URL("https://news-at.zhihu.com/api/3/stories/latest");
                            connection = (HttpURLConnection) url.openConnection();
                            connection.setRequestMethod("GET");
                            connection.setConnectTimeout(8000);
                            connection.setReadTimeout(8000);
                            InputStream in = connection.getInputStream();
                            reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder response = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                                showResponse(response.toString());
                             } catch (Exception e) {
                                e.printStackTrace();
                                //Toast.makeText(shouye.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                            } finally {
                                if (reader != null) {
                                    try {
                                        reader.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        //Toast.makeText(shouye.this, "请检查网络连接", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                        }
                    });

                    thread.start();
                }
                private void initView(){
                    mLoopPager = (ViewPager) this.findViewById(R.id.looper_pager);
                    mLoopPagerAdapter = new LooperPagerAdapter();
                    mLoopPagerAdapter.setData(sPics);
                    mLoopPager.setAdapter(mLoopPagerAdapter);
                }
                public void showResponse(final String string) {

                    try {
                        JSONObject jsonObject = new JSONObject(string);
                        dates= jsonObject.getInt("date");
                        final String date = String.valueOf(dates);
                        //map.put("riqi",dates);
                        JSONArray jsonArray = jsonObject.getJSONArray("stories");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                            final String title = jsonObject1.getString("title");
                            final JSONArray imagesArray = jsonObject1.getJSONArray("images");
                            String images = imagesArray.getString(0);//这一步真的非常重要
                            final String id = jsonObject1.getString("id");
                            final String hint = jsonObject1.getString("hint");
                            final String url = jsonObject1.getString("url");
                            final String year,month,day;
                            Map<String, Object> map = new HashMap<>();
                            if (i==0){
                                year = date.substring(0,4);
                                month = date.substring(4,6);
                                day = date.substring(6,8);
                            }
                            else {
                                year = null;
                                month = null;
                                day = null;
                            }
                            //Map<String,Object> map = new HashMap<>();
                            map.put("i",i);
                            map.put("day",day);
                            map.put("month",month);
                            map.put("year",year);
                            map.put("title", title);
                            map.put("picture", images);
                            map.put("id", id);
                            map.put("url", url);
                            map.put("hint", hint);
                            list.add(map);
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mRvShouye.setLayoutManager(new LinearLayoutManager(shouye.this));
                                //mRvShouye.addItemDecoration(new MyDecoration());
                                mRvShouye.setAdapter(new LinearAdapter(shouye.this, list));
                            }
                        });

                    } catch (JSONException e) {
                    e.printStackTrace();
            }

    }
}

