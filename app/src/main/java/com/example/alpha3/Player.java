package com.example.alpha3;

public class Player extends Person {
    public String birth;
    public int team;
    public int num;
    public String imgURL;

    public Player()
    {

    }

    public Player(String fname, String lname, String birth, String id, int team, int num, String imgURL) {
        super(fname, lname, id);
        this.birth = birth;
        this.team = team;
        this.num = num;
        this.imgURL = imgURL;
    }
}
