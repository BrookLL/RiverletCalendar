package com.riverlet.lib.entity;

public class Day {
    private int year;
    private int month;
    private int date;
    private int day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return year + "年" + month + "月" + date + "日  周" + day;
    }

    public boolean isToday(int currentViewYear, int currentViewMonth, int date) {
        return this.date == date && currentViewYear == year && currentViewMonth == month;
    }
}
