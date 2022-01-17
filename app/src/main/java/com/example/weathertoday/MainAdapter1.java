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

public class MainAdapter1  extends RecyclerView.Adapter<MainAdapter1.ViewHolder1> {
    ArrayList<MainItem> mainModels1;
    Context context1;
    boolean isday1;
    public MainAdapter1(Context context, ArrayList<MainItem> mainItems, boolean isday){
        this.mainModels1= mainItems;
        this.context1=context;
        this.isday1=isday;
    }
    @Override
    public MainAdapter1.ViewHolder1 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly,parent,false);
        return new MainAdapter1.ViewHolder1(view);
    }

    @Override
    public void onBindViewHolder(MainAdapter1.ViewHolder1 holder, int position) {
        holder.imageView1.setImageResource(mainModels1.get(position).getImg());
        holder.status1.setText(mainModels1.get(position).getStatus());
        holder.temp1.setText(mainModels1.get(position).getTemp());
      //  holder.temp11.setText(mainModels1.get(position).getTemp1());
        holder.day1.setText(mainModels1.get(position).getDayy());
        if (isday1){
            holder.status1.setTextColor(Color.parseColor("#264653"));
            holder.temp1.setTextColor(Color.parseColor("#264653"));
            holder.day1.setTextColor(Color.parseColor("#264653"));
          //  holder.temp11.setTextColor(Color.parseColor("#264653"));//note
        }
        else{
            holder.status1.setTextColor(Color.WHITE);
            holder.temp1.setTextColor(Color.WHITE);
            holder.day1.setTextColor(Color.WHITE);
           // holder.temp11.setTextColor(Color.WHITE);//note
        }
    }

    @Override
    public int getItemCount() {
        return mainModels1.size();
    }
    public class ViewHolder1 extends RecyclerView.ViewHolder{
        ImageView imageView1;
        TextView temp1,status1,day1;
        public ViewHolder1(View itemView){
            super(itemView);
            imageView1=itemView.findViewById(R.id.statusimg1);
            temp1=itemView.findViewById(R.id.temptv1);
          //  temp11= itemView.findViewById(R.id.temptv1);
            status1=itemView.findViewById(R.id.statustv1);
            day1=itemView.findViewById(R.id.hourtv1);
        }
    }
}
