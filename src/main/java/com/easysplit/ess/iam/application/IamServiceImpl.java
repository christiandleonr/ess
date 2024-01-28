package com.easysplit.ess.iam.application;

import com.easysplit.ess.iam.domain.contracts.RefreshTokenRepository;
import com.easysplit.ess.iam.domain.contracts.RefreshTokenService;
import com.easysplit.ess.iam.domain.models.RefreshToken;
import com.easysplit.shared.domain.exceptions.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Instant;

public class IamServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public IamServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
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


}
