package com.easysplit.ess.users.builders;

import com.easysplit.ess.user.domain.models.User;

public class UserBuilder {
    private String id;
    private String name;
    private String lastname;
    private String username;
    private String password;
    private String email;
    private String phone;

    public UserBuilder setId(String id) {
        this.id = id;
        return this;
    }

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public User build() {
        return new User(
                id,
                name,
                lastname,
                username,
                password,
                email,
                phone,
                null);
    }
}
