package com.example.tacademy.saxproject.com.example.tacademy.saxproject.vo.com.example.tacademy.saxproject.parser;

import android.util.Log;

import com.example.tacademy.saxproject.com.example.tacademy.saxproject.handler.WeatherHandler;
import com.example.tacademy.saxproject.com.example.tacademy.saxproject.vo.Weather;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by Tacademy on 2016-07-27.
 */
public class WeatherSAXParser {
    private static final String TAG = "MainActivity";
    public Weather parse(InputStream is){
        Weather weather = null;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        WeatherHandler handler = new WeatherHandler();
        try{
            saxParser = factory.newSAXParser();
            saxParser.parse(is,handler);
            weather = handler.getWeather();
            Log.v(TAG, "parser success");
        }catch(Exception e){
            Log.v(TAG, "e : " + e);
        }
        return  weather;
    }
}
