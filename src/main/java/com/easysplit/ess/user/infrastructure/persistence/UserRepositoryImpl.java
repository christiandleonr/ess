package com.easysplit.ess.user.infrastructure.persistence;

import com.easysplit.ess.user.application.UserServiceImpl;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.sql.UserQueries;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String CLASS_NAME = UserRepositoryImpl.class.getName();
    private final JdbcTemplate jdbc;
    private final InfrastructureHelper infrastructureHelper;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbc, InfrastructureHelper infrastructureHelper){
        this.jdbc = jdbc;
        this.infrastructureHelper = infrastructureHelper;
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntity user) throws InternalServerErrorException {
        String userGuid = UUID.randomUUID().toString();
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        try {
            jdbc.update(UserQueries.INSERT_USER,
                    userGuid, user.getName(), user.getLastname(), user.getUsername(), createdDate);
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createUser() - Something went wrong while creating the user: " + user, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_ERROR_MESSAGE,
                    new Object[]{user},
                    e
            );
        }

        user.setUserGuid(userGuid);
        user.setCreatedDate(createdDate);

        return user;
    }

    @Override
    public UserEntity getUser(String userGuid) {
        UserEntity userEntity = null;

        try {
            userEntity = jdbc.query(UserQueries.GET_USER,
                    this::toUserEntity,
                    userGuid);
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".getUser() - Something went wrong while reading the user with id: " + userGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.GET_USER_ERROR_TITLE,
                    ErrorKeys.GET_USER_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e
            );
        }

        return userEntity;
    }

    @Override
    public boolean validateUsernameNotExist(String username) {
        UserEntity userEntity = null;

        try {
            userEntity = jdbc.query(UserQueries.GET_USER_BY_USERNAME,
                    this::toUserEntity,
                    username);
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".getUser() - Something went wrong while reading the user with username: " + username, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_ERROR_MESSAGE,
                    new Object[] {username},
                    e
            );
        }

        return userEntity == null;
    }

    private UserEntity toUserEntity(ResultSet rs) throws SQLException {

        UserEntity userEntity = null;

        if (rs.next()) {
            userEntity = new UserEntity();

            userEntity.setUserGuid(rs.getString(UserQueries.USERGUID_COLUMN.toLowerCase()));
            userEntity.setName(rs.getString(UserQueries.NAME_COLUMN.toLowerCase()));
            userEntity.setLastname(rs.getString(UserQueries.LASTNAME_COLUMN.toLowerCase()));
            userEntity.setUsername(rs.getString(UserQueries.USERNAME_COLUMN.toLowerCase()));
            userEntity.setCreatedDate(rs.getTimestamp(UserQueries.CREATE_DATE_COLUMN.toLowerCase()));
        }


        return userEntity;
    }
}
