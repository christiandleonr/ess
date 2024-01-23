package com.easysplit.ess.iam.application;

import com.easysplit.ess.iam.domain.contracts.RefreshTokenRepository;
import com.easysplit.ess.iam.domain.contracts.RefreshTokenService;
import com.easysplit.ess.iam.domain.models.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;

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
}
