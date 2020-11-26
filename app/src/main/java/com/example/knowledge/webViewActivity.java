package com.example.knowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ldoublem.thumbUplib.ThumbUpView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.IdentityHashMap;
import java.util.Map;

public class webViewActivity extends AppCompatActivity {

    private WebView mWvMain;
    private String mUrl;
    private String mId;
    private String long_comments;
    private String short_comments;

    private boolean flagLike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        //设置点赞效果
        final ImageView thumb = (ImageView) findViewById(R.id.thumb);
        thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //int collectNum = Integer.parseInt(tvCollent.getText().toString());
                if (flagLike) {
                    thumb.setImageResource(R.drawable.thumb);
                } else {
                    thumb.setImageResource(R.drawable.afterlike);
                }
                flagLike = !flagLike;
            }
        });
        //设置收藏效果
        ThumbUpView mThumbUpView = (ThumbUpView) findViewById(R.id.tpv);
        mThumbUpView.setCracksColor(Color.rgb(176, 226, 255));
        mThumbUpView.setFillColor(Color.rgb(176, 226, 255));
        mThumbUpView.setEdgeColor(Color.rgb(176, 226, 255));
        mThumbUpView.setOnThumbUp(new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {
                if (like == true) {
                    Toast.makeText(webViewActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(webViewActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                }
            }
        });
        mThumbUpView.Like();
        mThumbUpView.UnLike();

        mWvMain = (WebView) findViewById(R.id.wv);
        mUrl = getIntent().getStringExtra("Url");
        mId = getIntent().getStringExtra("Id");
        long_comments = getIntent().getStringExtra("l");
        short_comments = getIntent().getStringExtra("s");
        mWvMain.getSettings().setJavaScriptEnabled(true);
        mWvMain.loadUrl(mUrl);
        Log.d("yht", mId);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String c = " https://news-at.zhihu.com/api/3/story-extra/" + mId + "";
                try {
                    URL url = new URL(c);
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
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
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
            final String l = jsonObject.getString("long_comments");
            final String s = jsonObject.getString("short_comments");
            final String all = jsonObject.getString("comments");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Button button = (Button) findViewById(R.id.Button);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(webViewActivity.this, comment.class);
                            intent.putExtra("mid", mId);
                            intent.putExtra("l",l);
                            intent.putExtra("s",s);
                            intent.putExtra("all",all);
                            startActivity(intent);
                        }
                    });
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}