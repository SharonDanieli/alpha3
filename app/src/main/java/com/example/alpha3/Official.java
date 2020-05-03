package com.example.alpha3;

/**
 * Creates an official person
 * The class inherits from the Person class
 */

public class Official extends Person {
    public String role;
    public int team;

    public Official()
    {

    }

    /**
     * Creates an official person
     * @param fname official's first name
     * @param lname official's last name
     * @param id official's ID
     * @param role official's role name
     * @param team official's team number
     */

    public Official(String fname, String lname, String id, String role, int team) {
        super(fname, lname, id);
        this.role = role;
        this.team = team;
    }
}
