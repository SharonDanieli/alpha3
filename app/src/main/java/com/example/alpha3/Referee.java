package com.example.alpha3;

/**
 * Creates a referee
 * The class inherits from the Person class
 */

public class Referee extends Person {
    public String country;

    public Referee()
    {

    }

    /**
     * Creates a referee
     * @param fname referee's first name
     * @param lname referee's last name
     * @param id referee's ID
     * @param country referee's country code
     */

    public Referee(String fname, String lname, String id, String country) {
        super(fname, lname, id);
        this.country = country;
    }
}
