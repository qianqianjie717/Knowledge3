package com.example.knowledge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;

public class commentAdapter extends RecyclerView.Adapter<commentAdapter.LinearViewHolder> {

    private Context mcontext;
    private List<Map<String,Object>> list;

    private boolean flagLike;

    public commentAdapter(Context context, List<Map<String, Object>> list_cm){
        this.mcontext = context;
        this.list = list_cm;
    }
    @NonNull
    @Override
    public commentAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /*if (viewType == 0){
            return new LinearAdapter.LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.comment_item2,parent,false));
        }else {
            return new LinearAdapter.LinearViewHolder2(LayoutInflater.from(mcontext).inflate(R.layout.comment_item,parent,false));
        }*/
        return new LinearViewHolder(LayoutInflater.from(mcontext).inflate(R.layout.comment_item,parent,false));
    }
    @Override
    public int getItemCount(){
        return list.size();
    }
    @Override
    public void onBindViewHolder(@NonNull LinearViewHolder holder, int position) {
        /*if (getItemViewType(position)== 1){
            ((LinearAdapter.LinearViewHolder)holder).textVxiew1.setText(list.get(position).get("author").toString());
            ((LinearAdapter.LinearViewHolder)holder).textView2.setText(list.get(position).get("content").toString());
        }else{
            ((LinearAdapter.LinearViewHolder2)holder).textView1.setText("长评");
        }*/
        holder.textView1.setText(list.get(position).get("author").toString());
        holder.textView2.setText(list.get(position).get("content").toString());
        Glide.with(mcontext).load(list.get(position).get("picture")).into(holder.imageView);
    }
    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView textView1;
        private TextView textView2;
        private ImageView imageView;
        private ImageView thumb;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            textView1 = (TextView) itemView.findViewById(R.id.author);
            textView2 = (TextView) itemView.findViewById(R.id.comment);
            imageView = (ImageView) itemView.findViewById(R.id.touXiang);
        }
    }/*class LinearViewHOlder2 extends RecyclerView.ViewHolder{

        private TextView textView1;

        public LinearViewHOlder2(@NonNull View itemView) {
            super(itemView);
            textView1 = (TextView) itemView.findViewById(R.id.text);
        }
    }*/
    /* @Override
    public int getItemViewType(int position) {
        if ((list.get(position).get("s").toString()).equals(position + 1)) {
            return 0;
        } else {
            return 1;
        }
    }*/

}





