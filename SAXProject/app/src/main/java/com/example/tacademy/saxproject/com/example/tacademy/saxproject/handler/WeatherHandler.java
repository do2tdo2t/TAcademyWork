package com.example.tacademy.saxproject.com.example.tacademy.saxproject.handler;

import com.example.tacademy.saxproject.com.example.tacademy.saxproject.vo.Local;
import com.example.tacademy.saxproject.com.example.tacademy.saxproject.vo.Weather;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Tacademy on 2016-07-27.
 */
public class WeatherHandler extends DefaultHandler {
    private Weather weather;
    private Local local;
    String tagName = "";
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String data = new String(ch, start, length);
        switch(tagName){
            case "local" :
                switch (level){
                    case 1 :
                        local.name = data;
                        break;
                }
                break;
        }

    }


    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        switch (localName){
            case "local" :
                weather.list.add(local);
                level--;
        }
        tagName = "";
    }

    int level = 0;

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        switch (localName){
            case "weather":
                weather = new Weather();
                weather.year = attributes.getValue("year");
                weather.month = attributes.getValue("month");
                weather.day = attributes.getValue("day");
                weather.hour = attributes.getValue("hour");
                break;
            case "local" :
                local = new Local();
                local.desc = attributes.getValue("desc");
                local.icon = attributes.getValue("icon");
                local.rn_hr1 = attributes.getValue("rn_hr1");
                local.stn_id = attributes.getValue("stn_id");
                local.ta = attributes.getValue("ta");
                level++;
                break;
        }
        tagName = localName;
    }

    public Weather getWeather(){
        return  weather;
    }
}
