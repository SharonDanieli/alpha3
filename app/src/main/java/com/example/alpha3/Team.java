package com.example.alpha3;

/**
 * Creates a team
 */

public class Team {
    public int num;
    public String name;
    public String email;

    public Team(){}

    /**
     * Creates a team
     * @param num team number
     * @param name team name
     * @param email team Email adress
     */

    public Team(int num, String name, String email) {
        this.num = num;
        this.name = name;
        this.email = email;
    }
}
