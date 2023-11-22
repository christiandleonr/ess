package com.easysplit.ess.user.domain.models;

import java.sql.Timestamp;

/**
 * User object to be serialized and pure data validation
 */
public class User {
    private String id;
    private String name;
    private String lastname;
    private String username;
    private Timestamp createdDate;

    public User(String id, String name, String lastname, String username, Timestamp createdDate) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
