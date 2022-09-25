package com.example.pixweather.adaptor;

import android.content.Context;
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
import com.example.pixweather.model.Day;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Dalyadaptor extends RecyclerView.Adapter<Dalyadaptor.DayViewHolder> {


    Context context;
    List<Day> DayWeather;

    public Dalyadaptor(MainActivity context,List<Day> DayWeather){
        this.context = context;
        this.DayWeather =DayWeather;
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View DayItem = LayoutInflater.from(context).inflate(R.layout.dailyweather, parent ,false);
        return new DayViewHolder(DayItem);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        holder.DayData.setText(DayWeather.get(position).getDate());
        holder.Day.setText(""+DayWeather.get(position).getTempe()+"°C");
        holder.Night.setText(""+DayWeather.get(position).getNight()+"°C");
        String Image = "https://openweathermap.org/img/wn/"+DayWeather.get(position).getDescription()+".png";//categories.get(position).getDescription();
        Picasso.get()
                .load(Image)
                .into(holder.DayImage);
    }

    @Override
    public int getItemCount() {
        return DayWeather.size();
    }

    public static final class DayViewHolder extends RecyclerView.ViewHolder {

        TextView Day;
        TextView Night;
        TextView DayData;
        ImageView DayImage;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);

            Day = itemView.findViewById(R.id.Day);
            Night = itemView.findViewById(R.id.Night);
            DayData = itemView.findViewById(R.id.DayData);
            DayImage = itemView.findViewById(R.id.DayImage);

        }
    }
}
