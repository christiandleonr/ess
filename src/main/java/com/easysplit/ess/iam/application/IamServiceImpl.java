package com.easysplit.ess.iam.application;

import com.easysplit.ess.iam.domain.contracts.RefreshTokenRepository;
import com.easysplit.ess.iam.domain.contracts.RefreshTokenService;
import com.easysplit.ess.iam.domain.models.IAMUserDetails;
import com.easysplit.ess.iam.domain.models.RefreshToken;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.User;
import com.easysplit.shared.domain.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.sql.Timestamp;

public class IamServiceImpl implements RefreshTokenService, UserDetailsService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Autowired
    public IamServiceImpl(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public RefreshToken createRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.createRefreshToken(
                refreshToken.toRefreshTokenEntity()
        ).toRefreshToken();
    }

    @Override
    public RefreshToken getByToken(String token) {
        return refreshTokenRepository.getByToken(token).toRefreshToken();
    }

    @Override
    public void verifyExpiration(RefreshToken refreshToken) {
        Timestamp currentDate = new Timestamp(System.currentTimeMillis());
        if (refreshToken.getExpiryDate().before(currentDate)) {
            refreshTokenRepository.deleteRefreshToken(refreshToken.getToken());
            throw new UnauthorizedException(); // TODO Work on exceptions
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.getUserByUsername(username, true /* throwException */).toUser();

        return new IAMUserDetails(user);
    }
}
