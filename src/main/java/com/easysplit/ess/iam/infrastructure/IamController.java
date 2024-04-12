package com.easysplit.ess.iam.infrastructure;

import com.easysplit.ess.iam.domain.contracts.JwtService;
import com.easysplit.ess.iam.domain.contracts.RefreshTokenService;
import com.easysplit.ess.iam.domain.models.Auth;
import com.easysplit.ess.iam.domain.models.RefreshToken;
import com.easysplit.ess.iam.domain.models.Token;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/iam")
public class IamController {
    private static final String CLASS_NAME = IamController.class.getName();
    private static final Logger logger = LoggerFactory.getLogger(IamController.class);
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final InfrastructureHelper infrastructureHelper;

    public IamController(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JwtService jwtService, InfrastructureHelper infrastructureHelper) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.infrastructureHelper = infrastructureHelper;
    }

    @PostMapping
    public ResponseEntity<Token> authenticate(@RequestBody Auth auth) {
        Token token = null;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.getEmail(), auth.getPassword())
            );
            if (authentication.isAuthenticated()) {
                token = refreshTokenService.buildEssToken(auth.getEmail());
            }
        } catch (NotFoundException e) {
            logger.error(CLASS_NAME + ".authenticate() - Something went wrong while reading the user with email " + auth.getEmail(), e);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error(CLASS_NAME + ".authenticate() - Something went wrong while authenticating the user", e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".authenticate() - Something went wrong while authenticating the user", e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.AUTHENTICATION_ERROR_TITLE,
                    ErrorKeys.AUTHENTICATION_ERROR_MESSAGE,
                    null,
                    e.getCause()
            );
        }

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Token> refreshToken(@RequestBody RefreshToken refreshToken) {
        Token token = null;

        try {
            token = refreshTokenService.refreshToken(refreshToken);
        } catch (NotFoundException e) {
            logger.error(CLASS_NAME + ".refreshToken() - Something went wrong while reading the refresh token with token " + refreshToken.getToken(), e);
            throw e;
        } catch (InternalServerErrorException e) {
            logger.error(CLASS_NAME + ".refreshToken() - Something went wrong while refreshing the token", e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".refreshToken() - Something went wrong while refreshing the token", e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.AUTHENTICATION_ERROR_TITLE,
                    ErrorKeys.AUTHENTICATION_ERROR_MESSAGE,
                    null,
                    e.getCause()
            );
        }

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
