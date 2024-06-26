package com.easysplit.ess.iam.domain.models;

import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.models.Link;

import java.util.List;

/**
 * Refresh token object to be serialized
 */
public class RefreshToken {
    private long id;
    private String token;
    private User user;
    private List<Link> links;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * Generates a refresh token entity from this class
     *
     * @return refresh token entity
     */
    public RefreshTokenEntity toRefreshTokenEntity() {
        RefreshTokenEntity refreshToken = RefreshTokenMapper.INSTANCE.toRefreshTokenEntity(this);
        refreshToken.setUser(this.user.toUserEntity());

        return refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshToken ( id : " + this.id + " | "
                + "token : " + this.token
                + "user : " + this.user + " )";
    }
}
