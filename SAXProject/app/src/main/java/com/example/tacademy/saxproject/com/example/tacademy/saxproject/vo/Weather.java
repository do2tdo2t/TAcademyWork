package com.example.tacademy.saxproject.com.example.tacademy.saxproject.vo;

import java.util.ArrayList;

/**
 * Created by Tacademy on 2016-07-27.
 */
public class Weather {
    public String year;
    public String month;
    public String day;
    public String hour;
    public ArrayList<Local> list = new ArrayList<>();

    @Override
    public String toString() {
        return "Weather{" +
                "day='" + day + '\'' +
                ", year='" + year + '\'' +
                ", month='" + month + '\'' +
                ", hour='" + hour + '\'' +
                ", list=" + list +
                '}';
    }
}
