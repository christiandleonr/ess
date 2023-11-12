package com.easysplit.ess.user.domain.models;

import java.sql.Timestamp;

public class User {
    private String userGuid;
    private String name;
    private String lastname;
    private String username;
    private Timestamp createdDate;

    public User(String userGuid, String name, String lastname, String username, Timestamp createdDate) {
        this.userGuid = userGuid;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.createdDate = createdDate;
    }

    public String getUserGuid() {
        return userGuid;
    }

    public void setUserGuid(String userGuid) {
        this.userGuid = userGuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }
}
