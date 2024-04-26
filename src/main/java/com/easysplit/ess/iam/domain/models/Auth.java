package com.easysplit.ess.iam.domain.models;

/**
 * Authentication object to be serialized
 */
public class Auth {
    private String email;
    private String password;

    public Auth() {}

    public Auth(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Auth ( email : " + this.email + " | "
                + "password : " + this.password + " )";
    }
}
