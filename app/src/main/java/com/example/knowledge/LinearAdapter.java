package com.example.knowledge;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;
public class LinearAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Map<String,Object>> list;
    private Context mContext;
   // private OnItemClickListener mlistener;
    //private List<String> list(用JSON解析数据，并传入到map再传入list里）


    public LinearAdapter(Context context, List<Map<String,Object>> list){
        this.mContext = context;
        this.list = list;
        //this.mlistener = listener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType ==1){
            return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.hahah,parent,false));
        }else{
            return new LinearViewHolder2(LayoutInflater.from(mContext).inflate(R.layout.hahaha2,parent,false));
        }
        //return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.hahah,parent,false));
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).get("i").equals(0)){
            return 0;
        }else{
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {

        /*holder.title.setText(list.get(position).get("title").toString());
        holder.object.setText(list.get(position).get("hint").toString());
        Glide.with(mContext).load(list.get(position).get("picture")).into(((LinearViewHolder) holder).picture);
        */holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),webViewActivity.class);
                intent.putExtra("Id",list.get(position).get("id").toString());
                intent.putExtra("Url",list.get(position).get("url").toString());
                v.getContext().startActivity(intent);
            }
        });

        if(getItemViewType(position) == 1){
            ((LinearViewHolder)holder).title.setText(list.get(position).get("title").toString());
            ((LinearViewHolder)holder).object.setText(list.get(position).get("hint").toString());
            Glide.with(mContext).load(list.get(position).get("picture")).into(((LinearViewHolder) holder).picture);
        }else{
            //((LinearViewHolder2)holder).date.setText(list.get(position).get("riqi").toString());
            LinearViewHolder2 holder2 = (LinearViewHolder2) holder;
            holder2.year.setText(list.get(position).get("year").toString());
            holder2.month.setText(list.get(position).get("month").toString());
            holder2.day.setText(list.get(position).get("day").toString());
            holder2.title.setText(list.get(position).get("title").toString());
            holder2.object.setText(list.get(position).get("hint").toString());
            Glide.with(mContext).load(list.get(position).get("picture")).into(holder2.picture);
        }
   }

    @Override
    public int getItemCount() {
        return list.size();
    }
    class LinearViewHolder extends RecyclerView.ViewHolder {

        private TextView title ;
        private TextView object;
        private ImageView picture;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.text1);
            object = (TextView) itemView.findViewById(R.id.text2);
            picture = (ImageView) itemView.findViewById(R.id.imagine);
        }
    }
    class LinearViewHolder2 extends RecyclerView.ViewHolder{

        private TextView date;
        private TextView title ;
        private TextView object;
        private TextView day,month,year;
        private ImageView picture;

        public LinearViewHolder2(@NonNull View itemView) {
            super(itemView);
            //date = (TextView) itemView.findViewById(R.id.dates);
            year = itemView.findViewById(R.id.year);
            month = itemView.findViewById(R.id.month);
            day = itemView.findViewById(R.id.day);
            title = (TextView) itemView.findViewById(R.id.text1);
            object = (TextView) itemView.findViewById(R.id.text2);
            picture = (ImageView) itemView.findViewById(R.id.imagine);
        }
    }
}

