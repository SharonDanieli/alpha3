package com.example.alpha3;

/**
 *Creates a person
 */

public class Person {
    public String fname;
    public String lname;
    public String id;

    public Person()
    {

    }

    /**
     * Creates a person
     * @param fname person's first name
     * @param lname person's last name
     * @param id person's ID
     */

    public Person(String fname, String lname, String id) {
        this.fname = fname;
        this.lname = lname;
        this.id = id;
    }
}
