package com.easysplit.ess.iam.infrastructure.persistence;

import com.easysplit.ess.iam.domain.contracts.RefreshTokenRepository;
import com.easysplit.ess.iam.domain.models.RefreshTokenEntity;
import com.easysplit.ess.iam.domain.sql.RefreshTokenQueries;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.NotFoundException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;

@Repository
public class IamRepositoryImpl implements RefreshTokenRepository {
    private static final String CLASS_NAME = IamRepositoryImpl.class.getName();
    private final UserRepository userRepository;
    private final JdbcTemplate jdbc;
    private final InfrastructureHelper infrastructureHelper;
    private static final Logger logger = LoggerFactory.getLogger(IamRepositoryImpl.class);

    @Autowired
    public IamRepositoryImpl(UserRepository userRepository,
                             InfrastructureHelper infrastructureHelper,
                             JdbcTemplate jdbc) {
        this.userRepository = userRepository;
        this.infrastructureHelper = infrastructureHelper;
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public RefreshTokenEntity createRefreshToken(String username, String jwtToken) {
        RefreshTokenEntity refreshToken = null;

        try {
            UserEntity user = userRepository.getUserByUsername(username, true /* throwException */);

            PreparedStatementCreator preparedStatementCreator = connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        RefreshTokenQueries.INSERT_REFRESH_TOKEN,
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setString(1, jwtToken);
                ps.setString(2, user.getUserGuid());
                return ps;
            };

            deleteRefreshTokenByUser(user.getUserGuid());
            jdbc.update(preparedStatementCreator);

            refreshToken = new RefreshTokenEntity();
            refreshToken.setToken(jwtToken);
            refreshToken.setUser(user);
        } catch (NotFoundException e) {
            logger.error(CLASS_NAME + ".createRefreshToken() - Something went wrong while reading a resource", e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createRefreshToken() - Something went wrong while inserting the refresh token", e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.AUTHENTICATION_ERROR_TITLE,
                    ErrorKeys.AUTHENTICATION_ERROR_MESSAGE,
                    null
            );
        }

        return refreshToken;
    }

    @Override
    public RefreshTokenEntity getByToken(String token) {
        RefreshTokenEntity refreshTokenEntity = null;
        try {
            refreshTokenEntity = jdbc.query(RefreshTokenQueries.GET_REFRESH_TOKEN_BY_TOKEN,
                    this::toRefreshTokenEntity,
                    token
            );
        } catch (NotFoundException e) {
            logger.error(CLASS_NAME + ".getByToken() - Something went wrong while reading a resource", e);
            throw e;
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".getByToken() - Something went wrong while reading the refresh token with token: " + token, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.AUTHENTICATION_ERROR_TITLE,
                    ErrorKeys.AUTHENTICATION_ERROR_MESSAGE,
                    null
            );
        }

        if (refreshTokenEntity == null) {
            logger.debug(CLASS_NAME + ".getByToken() - token " + token + " not found");
            infrastructureHelper.throwNotFoundException(
                    ErrorKeys.REFRESH_TOKEN_NOT_FOUND_TITLE,
                    ErrorKeys.REFRESH_TOKEN_NOT_FOUND_MESSAGE,
                    new Object[] {token}
            );
        }

        return refreshTokenEntity;
    }

    @Override
    @Transactional
    public void deleteRefreshToken(String token) {
        // Throw Not Found Exception if the token does not exist
        getByToken(token);

        int rowsDeleted = 0;
        try {
            jdbc.update(RefreshTokenQueries.DELETE_REFRESH_TOKEN, token);
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".deleteRefreshToken() - Something went wrong while deleting the refresh token with token: " + token, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.AUTHENTICATION_ERROR_TITLE,
                    ErrorKeys.AUTHENTICATION_ERROR_MESSAGE,
                    null
            );
        }

        logger.info(CLASS_NAME + ".deleteRefreshToken() - rows deleted: " + rowsDeleted);
    }

    @Override
    @Transactional
    public void deleteRefreshTokenByUser(String userGuid) {
        int rowsDeleted = 0;
        try {
            jdbc.update(RefreshTokenQueries.DELETE_REFRESH_TOKEN_BY_USER, userGuid);
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".deleteRefreshTokenByUser() - Something went wrong while deleting the refresh token with user id: " + userGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.AUTHENTICATION_ERROR_TITLE,
                    ErrorKeys.AUTHENTICATION_ERROR_MESSAGE,
                    null
            );
        }

        logger.info(CLASS_NAME + ".deleteRefreshToken() - rows deleted: " + rowsDeleted);
    }

    private RefreshTokenEntity toRefreshTokenEntity(ResultSet rs) throws SQLException {
        RefreshTokenEntity refreshTokenEntity = null;

        if (rs.next()) {
            refreshTokenEntity = new RefreshTokenEntity();

            refreshTokenEntity.setId(rs.getLong(RefreshTokenQueries.ID_COLUMN.toLowerCase()));
            refreshTokenEntity.setToken(rs.getString(RefreshTokenQueries.TOKEN_COLUMN.toLowerCase()));

            String userGuid = rs.getString(RefreshTokenQueries.USERGUID_COLUMN.toLowerCase());
            UserEntity user = userRepository.getUser(userGuid);
            refreshTokenEntity.setUser(user);
        }

        return refreshTokenEntity;
    }
}
