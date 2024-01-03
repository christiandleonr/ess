package com.easysplit.ess.user.domain.models;

import java.sql.Timestamp;

/**
 * User object to be used for database operations
 */
public class UserEntity {
    private String userGuid;
    private String name;
    private String lastname;
    private String username;
    private String email;
    private String phone;
    private Timestamp createdDate;

    public UserEntity() {}

    public UserEntity(String userGuid, String name, String lastname, String username, String email, String phone, Timestamp createdDate) {
        this.userGuid = userGuid;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.phone = phone;
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

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "id : " + this.userGuid + " | "
                + "name : " + this.name + " | "
                + "lastname : " + this.lastname + " | "
                + "username : " + this.username + " | "
                + "createdDate : " + this.createdDate;
    }

    /**
     * Generates a user from this entity class
     *
     * @return equivalent user
     */
    public User toUser() {
        return UserMapper.INSTANCE.toUser(this);
    }
}