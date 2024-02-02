package com.easysplit.ess.iam.domain.contracts;

import com.easysplit.ess.iam.domain.models.RefreshTokenEntity;

/**
 * Class that handle the database operations related to the refresh token
 */
public interface RefreshTokenRepository {
    /**
     * Inserts a new refresh token to be validated on authentication
     *
     * @param refreshToken refresh token object
     * @return created refresh token
     */
    RefreshTokenEntity createRefreshToken(RefreshTokenEntity refreshToken);

    /**
     * Loads the refresh token details based on the actual token
     *
     * @param token token to be look up for
     * @return refresh token details
     */
    RefreshTokenEntity getByToken(String token);

    /**
     * Deletes a refresh token by the token
     *
     * @param token to be deleted
     */
    void deleteRefreshToken(String token);
}
