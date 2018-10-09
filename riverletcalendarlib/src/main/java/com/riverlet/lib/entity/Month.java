package com.riverlet.lib.entity;

public class Month {
    private int year;
    private int month;

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

    @Override
    public String toString() {
        return year + "年" + (month < 10 ? "0" : "") + month + "月";
    }
}
