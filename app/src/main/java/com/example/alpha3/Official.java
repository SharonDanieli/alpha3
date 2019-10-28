package com.example.alpha3;

public class Official extends Person {
    public String role;
    public int team;

    public Official()
    {

    }

    public Official(String fname, String lname, String id, String role, int team) {
        super(fname, lname, id);
        this.role = role;
        this.team = team;
    }
}
