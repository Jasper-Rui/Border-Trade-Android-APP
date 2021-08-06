package com.example.myfirstapp;

public class User {

    //variables
    private String email;
    private String password;
    private String fullName;

//
//    ****** Instance variables do not need to be passed to constructor as firebase only works with
//    empty constructor *******
//
//    USE SETTER METHODS TO CONSTRUCT
//
//    public User(String em, String fN, String pass) {
//        this.email = em;
//        this.password = pass;
//        this.fullName = fN;
//    }

    public User(){}

    /**
     * Retrieves the user's email
     * @return the user's email
     */
    public String getEmail(){
        return email;
    }

    /**
     * Sets the user's email
     * @param em the new email
     */
    public void setEmail(String em){
        this.email = em;
    }

    /**
     * Retrieves the user's password
     * @return the user's password
     */
    public String getPassword(){
        return password;
    }

    /**
     * Sets the user's passwors
     * @param pass the new password
     */
    public void setPassword(String pass) {
        this.password = pass;
    }

    /**
     * Retrieves the user's full name
     * @return the user's full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets the user's full name
     * @param fN the new full name
     */
    public void setFullName(String fN) {
        this.fullName = fN;
    }

    /**
     * Puts the user's info into string format
     * @return the string containing the user's info
     */
    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", fullName='" + fullName + '\'' +
                '}';
    }
}
