package com.mycompany.app.domain;

import com.mycompany.app.domain.auxiliary.Week;

public class Day {
    int id;
    Week dayoftheWeek;
    String date;

    public Day() {

    }

    public Day(int id) {
        this.id = id;
    }

    public Day(int id, String date) {
        this.id = id;
        this.date = date;
    }

    public Day(int id, Week dayoftheWeek) {
        this.id = id;
        this.dayoftheWeek = dayoftheWeek;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Week getDayoftheWeek() {
        return dayoftheWeek;
    }

    public void setDayoftheWeek(Week dayoftheWeek) {
        this.dayoftheWeek = dayoftheWeek;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDay() {
        return this.id;
    }
}
