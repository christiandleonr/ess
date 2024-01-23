package com.easysplit.ess.user.domain.models;

import com.easysplit.shared.domain.models.Link;

import java.sql.Timestamp;
import java.util.List;

/**
 * User object to be serialized and pure data validation
 */
public class User {
    private String id;
    private String name;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private String phone;
    private Timestamp createdDate;
    private List<Link> links;

    public User() {}

    public User(String id, String name, String lastname, String username, String email, String phone, Timestamp createdDate) {
        this.id = id;
        this.name = name;
        this.lastname = lastname;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.createdDate = createdDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email= email;
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

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    @Override
    public String toString() {
        return "id : " + this.id + " | "
                + "name : " + this.name + " | "
                + "lastname : " + this.lastname + " | "
                + "username : " + this.username + " | "
                + "email : " + this.email + " | "
                + "phone : " + this.phone + " | "
                + "createdDate : " + this.createdDate;
    }

    /**
     * Generates a user entity from this class
     *
     * @return equivalent user entity
     */
    public UserEntity toUserEntity() {
        return UserMapper.INSTANCE.toUserEntity(this);
    }
}
