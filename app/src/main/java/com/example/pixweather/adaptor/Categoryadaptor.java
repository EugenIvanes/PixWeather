package com.example.pixweather.adaptor;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pixweather.MainActivity;
import com.example.pixweather.R;
import com.example.pixweather.model.Category;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Categoryadaptor extends RecyclerView.Adapter<Categoryadaptor.CategoryViewHolder> {


    Context context;
    List<Category> categories;

    public Categoryadaptor(MainActivity context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View categoryItem = LayoutInflater.from(context).inflate(R.layout.categoryitem, parent ,false);
        return new CategoryViewHolder(categoryItem);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.datehouer.setText(categories.get(position).getHour());
        holder.houertemperature.setText(""+categories.get(position).getTemperature()+"°C");
        String imageURL = "https://openweathermap.org/img/wn/"+categories.get(position).getDescription()+"@4x.png";//categories.get(position).getDescription();
        Picasso.get()
                .load(imageURL)
                .into(holder.houerdescription);

    }




    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static final class CategoryViewHolder extends RecyclerView.ViewHolder{
        TextView datehouer;
        TextView houertemperature;
        ImageView houerdescription;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            datehouer  = itemView.findViewById(R.id.datehouer);
            houertemperature  = itemView.findViewById(R.id.houertemperature);
            houerdescription  = itemView.findViewById(R.id.houerdescription);

        }
    }
}
