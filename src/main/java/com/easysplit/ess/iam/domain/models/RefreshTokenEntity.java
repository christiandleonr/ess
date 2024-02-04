package com.easysplit.ess.iam.domain.models;

import com.easysplit.ess.user.domain.models.UserEntity;

import java.sql.Timestamp;

/**
 * Entity class that represents the <i>refresh_token</i> table
 */
public class RefreshTokenEntity {
    private long id;
    private String token;
    private UserEntity user;

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

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    /**
     * Generates a refresh token from this entity class
     *
     * @return refresh token details
     */
    public RefreshToken toRefreshToken() {
        RefreshToken refreshToken = RefreshTokenMapper.INSTANCE.toRefreshToken(this);
        refreshToken.setUser(this.user.toUser());

        return refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshToken ( id : " + this.id + " | "
                + "token : " + this.token
                + "user : " + this.user + " )";
    }
}
