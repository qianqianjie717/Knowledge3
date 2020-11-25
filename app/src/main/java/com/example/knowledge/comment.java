package com.example.knowledge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class comment extends AppCompatActivity {

    private RecyclerView mRecycleView;
    private RecyclerView longRecycleView;
    private Map map_cm;
    private Map map1_cm;
    private List<Map<String,Object>> list_cm = new ArrayList<>();
    //private List<Map<String,Object>> list1_cm = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent intent = getIntent();
        final String id_at = intent.getStringExtra("mid");
        Log.d("yht",id_at);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                String a = "https://news-at.zhihu.com/api/4/story/"+id_at+"/short-comments";
                String b = "https://news-at.zhihu.com/api/4/story/"+id_at+"/long-comments";
                //String c = " https://news-at.zhihu.com/api/3/story-extra/"+id_at+"";
                try {
                    //String a = "https://news-at.zhihu.com/api/4/story/"+id_at+"/short-comments";
                    URL url = new URL(a);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try{
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                try {
                    URL url = new URL(b);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try{
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                /*try {
                    URL url = new URL(c);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while((line = reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    if(reader != null){
                        try{
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }*/
           }
        });
        thread.start();

    }
    public void showResponse(final String string){
        try {
            JSONObject jsonObject = new JSONObject(string);
            Log.d("yht",string);
            //final int l = jsonObject.getInt("long_comments");
            //map_cm.put("l",l);
            //Log.d("yht", String.valueOf(lcomments));
            //final int s = jsonObject.getInt("short_comments");
            //map_cm.put("s",s);
            final JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                final String author = jsonObject1.getString("author");
                final String content = jsonObject1.getString("content");
                Map<String,Object>map_cm = new HashMap<>();
                map_cm.put("author",author);
                map_cm.put("content",content);
                list_cm.add(map_cm);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mRecycleView = (RecyclerView) findViewById(R.id.rv_comment);
                    TextView text =(TextView) findViewById(R.id.Text);
                    TextView sText =(TextView) findViewById(R.id.shortText);
                    sText.setText("短评"+jsonArray.length());
                    //Log.d("yht", String.valueOf(scomments));
                    text.setText("评论");
                    mRecycleView.setLayoutManager(new LinearLayoutManager(comment.this));
                    mRecycleView.addItemDecoration(new MyDecoration());
                    mRecycleView.setAdapter(new commentAdapter(comment.this,list_cm));
                }
                class MyDecoration extends RecyclerView.ItemDecoration{
                    @Override
                    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                        super.getItemOffsets(outRect, view, parent, state);
                        outRect.set(0,0,0,getResources().getDimensionPixelOffset(R.dimen.Hight));
                    }
                }
            });

        }catch (JSONException e){
            e.printStackTrace();
        }

    }

}
