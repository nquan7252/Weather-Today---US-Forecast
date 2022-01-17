package com.example.weathertoday;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    ArrayList<MainItem> mainItems;
    Context context;
    boolean isday;
    public MainAdapter(Context context, ArrayList<MainItem> mainItems, boolean isday){
        this.mainItems = mainItems;
        this.context=context;
        this.isday=isday;
    }
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.daily,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MainAdapter.ViewHolder holder, int position) {
        holder.imageView.setImageResource(mainItems.get(position).getImg());
        holder.status.setText(mainItems.get(position).getStatus());
        holder.temp.setText(mainItems.get(position).getTemp());
        holder.temp1.setText(mainItems.get(position).getTemp1());
        holder.day.setText(mainItems.get(position).getDayy());
        if (isday){
            holder.status.setTextColor(Color.parseColor("#264653"));
            holder.temp.setTextColor(Color.parseColor("#264653"));
            holder.day.setTextColor(Color.parseColor("#264653"));
           // holder.temp1.setTextColor();//
        }
        else{
            holder.status.setTextColor(Color.WHITE);
            holder.temp.setTextColor(Color.WHITE);
            holder.day.setTextColor(Color.WHITE);
            holder.temp1.setTextColor(Color.parseColor("#E6E6E6"));
        }
    }

    @Override
    public int getItemCount() {
        return mainItems.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView temp,status,day,temp1;
        public ViewHolder(View itemView){
            super(itemView);
            imageView=itemView.findViewById(R.id.statusimg);
            temp=itemView.findViewById(R.id.temptv);
            temp1=itemView.findViewById(R.id.temptv1);
            status=itemView.findViewById(R.id.statustv);
            day=itemView.findViewById(R.id.daytv);
        }
    }
}
