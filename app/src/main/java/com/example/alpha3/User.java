package com.example.alpha3;

/**
 * Creates a user
 */

public class User {
    public String uID;
    public String name;
    public String phone;
    public String email;
    public boolean authorized;

    public User (){}

    /**
     * Creates a user
     * @param uID user's key ID
     * @param name user's name
     * @param phone user's phone number
     * @param email user's Email adress
     * @param authorized is the user authorized?
     */

    public User(String uID, String name, String phone, String email, boolean authorized) {
        this.uID = uID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.authorized = authorized;
    }

    /**
     * Gets user's key ID
     * @return A String of user's key ID
     */

    public String getuID() {
        return this.uID;
    }

    /**
     * Gets user's name
     * @return A String of user's name
     */

    public String getName() {
        return this.name;
    }

    /**
     * Gets user's Email adress
     * @return A String of user's Email adress
     */

    public String getEmail() {
        return this.email;
    }

    /**
     * Gets user's phone number
     * @return A String of user's phone number
     */

    public String getPhone() {
        return this.phone;
    }

    /**
     * Gets user's type
     * @return A boolean that says if the user is authorized or not
     */

    public Boolean getAuthorized() {
        return this.authorized;
    }

}