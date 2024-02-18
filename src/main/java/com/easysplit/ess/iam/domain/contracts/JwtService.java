package com.easysplit.ess.iam.domain.contracts;

import com.easysplit.ess.iam.domain.models.Token;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.function.Function;

/**
 * Class that handle operations related to the JWT
 */
public interface JwtService {
    /**
     * Extract the username from a previously created token
     *
     * @param token token provided by user
     * @return username
     */
    String extractUsername(String token);

    /**
     * Extracts the claims from a given token
     *
     * @param token token from which we get the claims
     * @param claimsResolver claim resolver
     * @return claims
     */
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    /**
     * Extracts the expiration date from a token
     *
     * @param token token from which we get the expiration gate
     * @return expiration date
     */
    Date extractExpiration(String token);

    /**
     * Validates token with user details
     *
     * @param token token provided by user
     * @param userDetails user details
     * @return true if the token is valid
     */
    Boolean validateToken(String token, UserDetails userDetails);

    /**
     * Generates a token with the given username
     *
     * @param username username to encrypt into the token
     * @param isRefreshToken true is the token being generated is a refresh token
     * @return token
     */
    String generateToken(String username, boolean isRefreshToken);
}
