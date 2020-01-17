package com.example.alpha3;

import java.util.Calendar;
//this is a class, not an activity.
public class Info {
    public String name; // name of the competition
    public String city;
    public String countryCode;
    public String hall;
    public int phase;
    public int num;
    public int category;
    public boolean division;
    public String date;
    public String time;
    public String team1;
    public String team2;

    public Info(String name, String city, String countryCode, String hall, int phase, int num, int category, boolean division, String time, String team1, String team2) {
        this.name = name;
        this.city = city;
        this.countryCode = countryCode;
        this.hall = hall;
        this.phase = phase;
        this.num = num;
        this.category = category;
        this.division = division;
        this.time = time;
        this.team1 = team1;
        this.team2 = team2;

        Calendar c = Calendar.getInstance();
        date = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.YEAR);
    }

    public Info()
    {

    }
}
