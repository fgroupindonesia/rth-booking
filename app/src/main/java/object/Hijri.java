package object;

import org.json.JSONObject;

import helper.RespondHelper;

public class Hijri {
    // this class is based upon
    // http://api.aladhan.com/v1/gToH?date=18-03-2022
    // returned call
    private String date;
    private String format;
    private int day;
    private int year;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

}
