package com.example.tacademy.saxproject.com.example.tacademy.saxproject.vo;

/**
 * Created by Tacademy on 2016-07-27.
 */
public class Local {
    public String stn_id;
    public String icon;
    public String desc;
    public String ta;
    public String rn_hr1;
    public String name;

    @Override
    public String toString() {
        return "Local{" +
                "desc='" + desc + '\'' +
                ", stn_id='" + stn_id + '\'' +
                ", icon='" + icon + '\'' +
                ", ta='" + ta + '\'' +
                ", rn_hr1='" + rn_hr1 + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
