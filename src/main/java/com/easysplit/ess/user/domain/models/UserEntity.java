package com.easysplit.ess.user.domain.models;

import java.sql.Timestamp;
import java.util.List;

/**
 * User object to be used for database operations
 */
public class UserEntity {
    private String userGuid;
    private String name;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private String phone;
    private List<RoleEntity> roles;
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
        this.email = email;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public List<RoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleEntity> roles) {
        this.roles = roles;
    }

    public Timestamp getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Timestamp createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "User ( id : " + this.userGuid + " | "
                + "name : " + this.name + " | "
                + "lastname : " + this.lastname + " | "
                + "username : " + this.username + " | "
                + "email : " + this.email + " | "
                + "phone : " + this.phone + " | "
                + "roles : " + this.roles + " | "
                + "createdDate : " + this.createdDate + " )";
    }

    /**
     * Generates a user from this entity class
     *
     * @return equivalent user
     */
    public User toUser() {
        User user = UserMapper.INSTANCE.toUser(this);
        user.setRoles(
            RoleMapper.INSTANCE.toListOfRoles(this.roles)
        );
        
        return user;
    }
}