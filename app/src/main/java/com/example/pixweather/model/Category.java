package com.example.pixweather.model;

public class Category {
    int id;
    String hour;
    int temperature;
    String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category(int id, String hour, int temperature, String description) {
        this.id = id;
        this.hour = hour;
        this.temperature = temperature;
        this.description = description;
    }
}
