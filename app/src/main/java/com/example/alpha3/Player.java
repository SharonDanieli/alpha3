package com.example.alpha3;

/**
 * Creates a player
 * The class inherits from the Person class
 */

public class Player extends Person {
    public String birth;
    public int team;
    public int num;
    public String imgURL;

    public Player()
    {

    }

    /**
     * Creates a player
     * @param fname player's first name
     * @param lname player's last name
     * @param birth player's birth date
     * @param id player's ID
     * @param team player's team number
     * @param num player's shirt number
     */

    public Player(String fname, String lname, String birth, String id, int team, int num) {
        super(fname, lname, id);
        this.birth = birth;
        this.team = team;
        this.num = num;
    }

    /**
     *
     * @return A String of the displayed player details
     */

    public String toString() {
        return this.fname +" "+ this.lname +" " + this.num;
    }
}
