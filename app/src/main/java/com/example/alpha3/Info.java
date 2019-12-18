package com.example.alpha3;

import java.util.Calendar;

public class Info {
    public String city;
    public String hall;
    // pool % country code N* division category
    public String date;
    public String time;
    public String team1;
    public String team2;

    public Info(String city, String hall, String time, String team1, String team2) {
        this.city = city;
        this.hall = hall;
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
