package com.easysplit.ess.iam.infrastructure;

import com.easysplit.ess.iam.domain.contracts.JwtService;
import com.easysplit.ess.iam.domain.models.Auth;
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
    private final JwtService jwtService;

    public IamController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping
    public ResponseEntity<String> authenticate(@RequestBody Auth auth) {
        String token = null;

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword())
            );
            if (authentication.isAuthenticated()) {
                token = jwtService.generateToken(auth.getUsername());
            }
        } catch (Exception e) {
            // TODO Work on exceptions
            throw e;
        }

        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
