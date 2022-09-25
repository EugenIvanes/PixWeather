package com.example.pixweather.model;

public class Day {
    public Day(int id, int tempe, int Night,String  description,String date) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.tempe = tempe;
        this.Night = Night;
    }

    int id;
    String date;
    String description;
    int tempe;
    int Night;

    public int getNight() {
        return Night;
    }

    public void setNight(int night) {
        Night = night;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTempe() {
        return tempe;
    }

    public void setTempe(int tempe) {
        this.tempe = tempe;
    }
}
