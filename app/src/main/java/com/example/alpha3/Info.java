package com.example.alpha3;

/**
 * Creates game details
 */

public class Info {
    public String name;
    public String city;
    public String countryCode;
    public String hall;
    public String phase;
    public int num;
    public String category;
    public boolean division;
    public String time;
    public String team1;
    public String team2;

    /**
     * Creates game details object
     * @param name name of the competition
     * @param city city where the competition takes place
     * @param countryCode country code where the competition takes place
     * @param hall hall name where the competition takes place
     * @param phase competition phase
     * @param num match number
     * @param category competition category
     * @param division players division (men or women)
     * @param time start time
     * @param team1 participating team
     * @param team2 participating team
     */

    public Info(String name, String city, String countryCode, String hall, String phase, int num, String category, boolean division, String time, String team1, String team2) {
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
    }

    public Info()
    {

    }
}
