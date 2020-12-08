package com.example.alpha3;

/**
 * Creates a team
 */

public class Team {
    public int num;
    public String city;
    public String name;
    public String email;

    public Team(){}

    /**
     * Creates a team
     * @param num team number
     * @param city team name
     * @param name team name
     * @param email team Email adress
     */

    public Team(int num, String name, String city, String email) {
        this.num = num;
        this.name = name;
        this.city = city;
        this.email = email;
    }
}
