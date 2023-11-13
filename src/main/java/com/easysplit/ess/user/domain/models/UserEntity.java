package com.easysplit.ess.user.domain.models;

import com.easysplit.shared.domain.exceptions.IllegalArgumentException;

import java.sql.Timestamp;

/**
 * User object to be validated and used for database operations
 */
public class UserEntity {
    private static final int USER_NAME_LENGTH = 100;
    private static final int USER_LASTNAME_LENGTH = 100;
    private static final int USER_USERNAME_LENGTH = 50;

    private String userGuid;
    private String name;
    private String lastname;
    private String username;
    private Timestamp createdDate;

    public UserEntity() {

    }

    public UserEntity(String userGuid, String name, String lastname, String username, Timestamp createdDate) {
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

    /**
     * Validates the user attributes
     */
    public void validate() {
        validateUserName();
        validateLastname();
        validateUsername();
    }

    /**
     * Validates the user name, name cannot be empty and
     * the number of characters cannot exceed 100.
     */
    private void validateUserName() {
        if (this.name.isEmpty()) {
            throw new IllegalArgumentException(); // TODO Add details
        }

        if (this.name.length() > USER_NAME_LENGTH) {
            throw new IllegalArgumentException(); // TODO Add details
        }
    }

    /**
     * Validates the user lastname, lastname cannot be empty and
     * the number of characters cannot exceed 100.
     */
    private void validateLastname() {
        if (this.name.isEmpty()) {
            throw new IllegalArgumentException(); // TODO Add details
        }

        if (this.lastname.length() > USER_LASTNAME_LENGTH) {
            throw new IllegalArgumentException(); // TODO Add details
        }
    }

    /**
     * Validates the user username, username cannot be empty and
     * the number of characters cannot exceed 50.
     */
    private void validateUsername() {
        if (this.username.isEmpty()) {
            throw new IllegalArgumentException(); // TODO Add details
        }

        if (this.username.length() > USER_USERNAME_LENGTH) {
            throw new IllegalArgumentException(); // TODO Add details
        }
    }
}
