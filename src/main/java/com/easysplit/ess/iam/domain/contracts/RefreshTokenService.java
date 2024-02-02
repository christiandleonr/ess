package com.easysplit.ess.iam.domain.contracts;

import com.easysplit.ess.iam.domain.models.RefreshToken;

/**
 * Class that handle the business logic for updating and validating the refresh token
 */
public interface RefreshTokenService {
    /**
     * Creates a new refresh token to be validated on authentication
     *
     * @param refreshToken refresh token object
     * @return created refresh token
     */
    RefreshToken createRefreshToken(RefreshToken refreshToken);

    /**
     * Gets the refresh token details based on the actual token
     *
     * @param token token to be look up for
     * @return refresh token details
     */
    RefreshToken getByToken(String token);

    /**
     * Verifies that a refresh token has not expired
     *
     * @param refreshToken token to be validated
     */
    void verifyExpiration(RefreshToken refreshToken);
}
