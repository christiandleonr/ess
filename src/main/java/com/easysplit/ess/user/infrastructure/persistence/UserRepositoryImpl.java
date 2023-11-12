package com.easysplit.ess.user.infrastructure.persistence;

import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.sql.UserQueries;
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
    public UserEntity createUser(UserEntity user) {
        String userGuid = UUID.randomUUID().toString();
        Timestamp createdDate = persistenceHelper.getCurrentDate();

        jdbc.update(UserQueries.INSERT_USER,
                userGuid, user.getName(), user.getLastname(), user.getUsername(), createdDate);

        user.setUserGuid(userGuid);
        user.setCreatedDate(createdDate);

        return user;
    }

    @Override
    public UserEntity getUser(String userGuid) {
        return jdbc.query(UserQueries.GET_USER, this::toUserEntity, userGuid);
    }

    private UserEntity toUserEntity(ResultSet rs) throws SQLException {
        rs.next();

        UserEntity userEntity = new UserEntity();

        userEntity.setUserGuid(rs.getString(UserQueries.USERGUID_COLUMN.toLowerCase()));
        userEntity.setName(rs.getString(UserQueries.NAME_COLUMN.toLowerCase()));
        userEntity.setLastname(rs.getString(UserQueries.LASTNAME_COLUMN.toLowerCase()));
        userEntity.setUsername(rs.getString(UserQueries.USERNAME_COLUMN.toLowerCase()));
        userEntity.setCreatedDate(rs.getTimestamp(UserQueries.CREATE_DATE_COLUMN.toLowerCase()));

        return userEntity;
    }
}
