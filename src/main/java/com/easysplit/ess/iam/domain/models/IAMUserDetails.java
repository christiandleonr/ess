package com.easysplit.ess.iam.domain.models;

import com.easysplit.ess.user.domain.models.Role;
import com.easysplit.ess.user.domain.models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IAMUserDetails extends User implements UserDetails {
    private final String username;
    private final String password;
    Collection<? extends GrantedAuthority> authorities;

    public IAMUserDetails(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();

        List<GrantedAuthority> auths = new ArrayList<>();

        for (Role role: user.getRoles()) {
            auths.add(new SimpleGrantedAuthority(role.getName().toUpperCase()));
        }

        this.authorities = auths;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
