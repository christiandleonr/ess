package com.easysplit.ess.iam.application;

import com.easysplit.ess.iam.domain.contracts.JwtService;
import com.easysplit.ess.iam.domain.contracts.RefreshTokenRepository;
import com.easysplit.ess.iam.domain.contracts.RefreshTokenService;
import com.easysplit.ess.iam.domain.models.IamUserDetails;
import com.easysplit.ess.iam.domain.models.RefreshToken;
import com.easysplit.ess.iam.domain.models.Token;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class IamServiceImpl implements RefreshTokenService, UserDetailsService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Autowired
    public IamServiceImpl(JwtService jwtService, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @Override
    public RefreshToken createRefreshToken(String username) {
        String refreshToken = jwtService.generateToken(username, true /* isRefreshToken */);

        return refreshTokenRepository.createRefreshToken(
                username,
                refreshToken
        ).toRefreshToken();
    }

    @Override
    public Token refreshToken(RefreshToken refreshToken) {
        // Load the token details
        refreshToken = getByToken(refreshToken.getToken());
        verifyExpiration(refreshToken);

        return buildEssToken(refreshToken.getUser().getUsername(), refreshToken.getToken());
    }

    @Override
    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.getByToken(token).toRefreshToken();
    }

    @Override
    public void verifyExpiration(RefreshToken refreshToken) {
        Date currentDate = new Date();

        Date expiryDate = jwtService.extractExpiration(refreshToken.getToken());

        if (expiryDate.before(currentDate)) {
            refreshTokenRepository.deleteRefreshToken(refreshToken.getToken());
            throw new UnauthorizedException(); // TODO Work on exceptions
        }
    }

    @Override
    public Token buildEssToken(String username) {
        RefreshToken refreshToken = createRefreshToken(username);
        return buildEssToken(username, refreshToken.getToken());
    }

    @Override
    public Token buildEssToken(String username, String refreshToken) {
        Token token = new Token();

        String accessToken = jwtService.generateToken(username, false /* isRefreshToken */);

        token.setToken(accessToken);
        token.setRefreshToken(refreshToken);

        return token;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username, true /* throwException */).toUser();

        return new IamUserDetails(user);
    }
}
