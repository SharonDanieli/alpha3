package com.example.alpha3;

public class Player {
    public String fname;
    public String lname;
    public String birth;
    public String id;
    public String team;
    public int num;
    public String imgURL;

    public Player()
    {

    }

    public Player(String fname, String lname, String birth, String id, String team, int num, String imgURL) {
        this.fname = fname;
        this.lname = lname;
        this.birth = birth;
        this.id = id;
        this.team = team;
        this.num = num;
        this.imgURL = imgURL;
    }
}
