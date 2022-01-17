package com.example.weathertoday;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter2 extends RecyclerView.Adapter<MainAdapter2.ViewHolder2> implements Filterable {
    ArrayList<City> city;
    ArrayList<City> cityClone;
    Context context2;
    boolean isday1;

    public MainAdapter2(Context context,ArrayList<City>mainModels,boolean isday){
        this.city=mainModels;
        this.context2=context;
        this.isday1=isday;
        cityClone=new ArrayList<>(mainModels);
    }
    @Override
    public MainAdapter2.ViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.city,parent,false);
        return new MainAdapter2.ViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(MainAdapter2.ViewHolder2 holder, int position) {
        holder.cityName.setText(city.get(position).getName());
        holder.cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context2,MainActivity2.class);
                intent.putExtra("latitude",city.get(position).getCitylat());
                intent.putExtra("longitude",city.get(position).getCityLng());
                intent.putExtra("name",city.get(position).getName());
                context2.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return city.size();
    }

    @Override
    public Filter getFilter() {

        return exampleFilter;
    }
    private Filter exampleFilter=new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<City> filteredList=new ArrayList<>();
            if (constraint ==null||constraint.length()==0){
                filteredList.addAll(cityClone);
            }
            else{
                String filterPattern=constraint.toString().toLowerCase().trim();
                for (City item:cityClone){
                    if (item.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            city.clear();
            city.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };
    public class ViewHolder2 extends RecyclerView.ViewHolder{
        TextView cityName;
        public ViewHolder2(View itemView){
            super(itemView);
            cityName =itemView.findViewById(R.id.cityName);
            context2=itemView.getContext();
        }
    }
}
