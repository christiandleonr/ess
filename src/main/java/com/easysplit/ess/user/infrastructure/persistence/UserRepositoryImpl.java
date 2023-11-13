package com.easysplit.ess.user.infrastructure.persistence;

import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.sql.UserQueries;
import com.easysplit.shared.infrastructure.exceptions.InternalServerErrorException;
import com.easysplit.shared.infrastructure.exceptions.NotFoundException;
import com.easysplit.shared.infrastructure.persistence.utils.PersistenceHelper;
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
    private final JdbcTemplate jdbc;
    private final PersistenceHelper persistenceHelper;

    @Autowired
    public UserRepositoryImpl(JdbcTemplate jdbc, PersistenceHelper persistenceHelper){
        this.jdbc = jdbc;
        this.persistenceHelper = persistenceHelper;
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntity user) throws InternalServerErrorException {
        String userGuid = UUID.randomUUID().toString();
        Timestamp createdDate = persistenceHelper.getCurrentDate();

        try {
            jdbc.update(UserQueries.INSERT_USER,
                    userGuid, user.getName(), user.getLastname(), user.getUsername(), createdDate);
        } catch (Exception e) {
            // TODO Add logs
            throw new InternalServerErrorException(); // TODO Add error title, error message and cause
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
            // TODO Add logs
            throw new InternalServerErrorException(); // TODO Add error title, error message and cause
        }


        if (userEntity == null) {
            // TODO Add logs
           throw new NotFoundException(); // TODO Add error title and error message
        }

       return userEntity;
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
