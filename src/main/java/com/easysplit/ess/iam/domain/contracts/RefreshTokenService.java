package com.easysplit.ess.iam.domain.contracts;

import com.easysplit.ess.iam.domain.models.RefreshToken;
import com.easysplit.ess.iam.domain.models.Token;

/**
 * Class that handle the business logic for updating and validating the refresh token
 */
public interface RefreshTokenService {
    /**
     * Creates a new refresh token to be validated on authentication
     *
     * @param username
     * @return created refresh token
     */
    RefreshToken createRefreshToken(String username);

    /**
     * Refreshes the access token if the refresh token has not expired yet.
     *
     * @param refreshToken
     * @return new generated ESS token
     */
    Token refreshToken(RefreshToken refreshToken);

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

    /**
     * Generates a custom ESS token, composed by an access token and a refresh token.
     * This method creates a new refresh token with the username encrypted
     *
     * @param username authenticated user
     * @return ESS token
     */
    Token buildEssToken(String username);

    /**
     * Generates a custom ESS token, composed by an access token and a refresh token
     *
     * @param username authenticated user
     * @param refreshToken
     * @return ESS token
     */
    Token buildEssToken(String username, String refreshToken);
}
