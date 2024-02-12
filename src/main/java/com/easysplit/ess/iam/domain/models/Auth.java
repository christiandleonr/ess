package com.easysplit.ess.iam.domain.models;

/**
 * Authentication object to be serialized
 */
public class Auth {
    private String username;
    private String password;

    public Auth() {}

    public Auth(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Auth ( username : " + this.username + " | "
                + "password : " + this.password + " )";
    }
}
