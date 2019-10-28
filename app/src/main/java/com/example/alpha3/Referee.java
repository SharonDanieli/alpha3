package com.example.alpha3;

public class Referee extends Person {
    public String country;

    public Referee()
    {

    }

    public Referee(String fname, String lname, String id, String country) {
        super(fname, lname, id);
        this.country = country;
    }
}
