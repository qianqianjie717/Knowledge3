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

import java.util.IdentityHashMap;
import java.util.Map;

public class webViewActivity extends AppCompatActivity {

    private WebView mWvMain;
    private String mUrl;
    private String mId;

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
                if(flagLike){
                    thumb.setImageResource(R.drawable.thumb);
                }else{
                    thumb.setImageResource(R.drawable.afterlike);
                }
                flagLike = !flagLike;
            }
        });
        //设置收藏效果
        ThumbUpView mThumbUpView = (ThumbUpView) findViewById(R.id.tpv);
        mThumbUpView.setCracksColor(Color.rgb(176,226,255));
        mThumbUpView.setFillColor(Color.rgb(176,226,255));
        mThumbUpView.setEdgeColor(Color.rgb(176,226,255));
        mThumbUpView.setOnThumbUp(new ThumbUpView.OnThumbUp() {
            @Override
            public void like(boolean like) {
                if(like==true){
                    Toast.makeText(webViewActivity.this,"已收藏",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(webViewActivity.this,"取消收藏",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mThumbUpView.Like();
        mThumbUpView.UnLike();

        mWvMain = (WebView) findViewById(R.id.wv);
        mUrl = getIntent().getStringExtra("Url");
        mId = getIntent().getStringExtra("Id");
        mWvMain.getSettings().setJavaScriptEnabled(true);
        mWvMain.loadUrl(mUrl);
        Log.d("yht",mId);
        Button button = (Button) findViewById(R.id.Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(webViewActivity.this,comment.class);
                intent.putExtra("mid",mId);
                startActivity(intent);
            }
        });
    }

}
