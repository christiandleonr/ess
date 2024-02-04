package com.easysplit.ess.iam.infrastructure;

import com.easysplit.ess.iam.domain.contracts.JwtService;
import com.easysplit.ess.iam.domain.contracts.RefreshTokenService;
import com.easysplit.ess.iam.domain.models.Auth;
import com.easysplit.ess.iam.domain.models.RefreshToken;
import com.easysplit.ess.iam.domain.models.Token;
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
    private static final Logger logger = LoggerFactory.getLogger(IamController.class);
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public IamController(AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping
    public ResponseEntity<Token> authenticate(@RequestBody Auth auth) {
        Token token = null;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword())
            );
            if (authentication.isAuthenticated()) {
                token = refreshTokenService.buildEssToken(auth.getUsername());
            }
        } catch (Exception e) {
            // TODO Work on exceptions
            throw e;
        }

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<Token> refreshToken(@RequestBody RefreshToken refreshToken) {
        Token token = null;

        try {
            token = refreshTokenService.refreshToken(refreshToken);
        } catch (Exception e) {
            // TODO Work on exceptions
            throw e;
        }

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
