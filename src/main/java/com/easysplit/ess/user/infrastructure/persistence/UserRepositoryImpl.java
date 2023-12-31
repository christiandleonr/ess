package com.easysplit.ess.user.infrastructure.persistence;

import com.easysplit.ess.user.domain.models.FriendshipEntity;
import com.easysplit.ess.user.domain.models.FriendshipStatus;
import com.easysplit.ess.user.domain.sql.FriendshipsQueries;
import com.easysplit.ess.user.application.UserServiceImpl;
import com.easysplit.ess.user.domain.contracts.FriendsRepository;
import com.easysplit.ess.user.domain.contracts.UserRepository;
import com.easysplit.ess.user.domain.models.UserEntity;
import com.easysplit.ess.user.domain.sql.UserQueries;
import com.easysplit.shared.domain.exceptions.ErrorKeys;
import com.easysplit.shared.domain.exceptions.InternalServerErrorException;
import com.easysplit.shared.infrastructure.helpers.InfrastructureHelper;
import com.easysplit.shared.utils.EssUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository, FriendsRepository {
    private static final String CLASS_NAME = UserRepositoryImpl.class.getName();
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private final JdbcTemplate jdbc;
    private final InfrastructureHelper infrastructureHelper;

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
                    userGuid, user.getName(), user.getLastname(), user.getUsername(), user.getEmail(), user.getPhone(), createdDate);
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

        if (EssUtils.isNullOrEmpty(userGuid)) {
            return null;
        }

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

        if (userEntity == null) {
            logger.debug(CLASS_NAME + ".getUser() - User with id " + userGuid + " not found");
            infrastructureHelper.throwNotFoundException(
                    ErrorKeys.GET_USER_NOT_FOUND_TITLE,
                    ErrorKeys.GET_USER_NOT_FOUND_MESSAGE,
                    new Object[]{userGuid}
            );
        }

        return userEntity;
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        UserEntity userEntity = null;

        try {
            userEntity = jdbc.query(UserQueries.GET_USER_BY_USERNAME,
                    this::toUserEntity,
                    username);
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".getUser() - Something went wrong while reading the user with username: " + username, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_BY_USERNAME_MESSAGE,
                    new Object[] {username},
                    e
            );
        }

        return userEntity;
    }

    @Override
    public UserEntity getUserByEmail(String email) {
        UserEntity userEntity = null;

        try {
            userEntity = jdbc.query(UserQueries.GET_USER_BY_EMAIL,
                    this::toUserEntity,
                    email);
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".getUser() - Something went wrong while reading the user with email: " + email, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_BY_EMAIL_MESSAGE,
                    new Object[] {email},
                    e
            );
        }

        return userEntity;
    }

    @Override
    public UserEntity getUserByPhone(String phone) {
        UserEntity userEntity = null;

        try {
            userEntity = jdbc.query(UserQueries.GET_USER_BY_PHONE,
                    this::toUserEntity,
                    phone);
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".getUser() - Something went wrong while reading the user with phone: " + phone, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_USER_ERROR_TITLE,
                    ErrorKeys.CREATE_USER_BY_PHONE_MESSAGE,
                    new Object[] {phone},
                    e
            );
        }

        return userEntity;
    }

    @Override
    @Transactional
    public void deleteUserById(String userGuid) {
        // Throws a NotFoundException if user does not exist
        getUser(userGuid);

        int rowsDeleted = 0;
        try {
            rowsDeleted = jdbc.update(UserQueries.DELETE_USER_BY_ID, userGuid);
        } catch (Exception e) {
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_USER_ERROR_TITLE,
                    ErrorKeys.DELETE_USER_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e
            );
        }

        logger.info(CLASS_NAME + ".deleteUserById() - Users deleted: " + rowsDeleted);
    }

    @Override
    @Transactional
    public FriendshipEntity addFriend(FriendshipEntity friendship) throws InternalServerErrorException {
        String friendshipGuid = UUID.randomUUID().toString();
        Timestamp createdDate = infrastructureHelper.getCurrentDate();

        // Throws NotFoundException if any of both users is not found
        UserEntity friend = getUser(friendship.getFriend().getUserGuid());
        UserEntity addedBy = getUser(friendship.getAddedBy().getUserGuid());

        try {
            jdbc.update(FriendshipsQueries.CREATE_FRIENDSHIP,
                    friendshipGuid,
                    friend.getUserGuid(),
                    FriendshipStatus.PENDING.getValue(),
                    createdDate,
                    addedBy.getUserGuid() // TODO change this to get the created by from authentication
            );
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".createFriendship() - Something went wrong while creating the friendship: " + friendship, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_TITLE,
                    ErrorKeys.CREATE_FRIENDSHIP_ERROR_MESSAGE,
                    new Object[]{ friendship },
                    e
            );
        }

        friendship.setFriendshipGuid(friendshipGuid);
        friendship.setFriend(friend);
        friendship.setStatus(FriendshipStatus.PENDING);
        friendship.setCreatedDate(createdDate);
        friendship.setAddedBy(addedBy);

        return friendship;
    }

    @Override
    public List<UserEntity> loadFriends(String userGuid, int limit, int offset) {
        List<UserEntity> friends = new ArrayList<>();

        if (EssUtils.isNullOrEmpty(userGuid)) {
            return friends;
        }

        try {
            List<String> friendsIds = jdbc.query(FriendshipsQueries.GET_FRIENDS,
                    (rs, rowNum) -> rs.getString(UserQueries.USERGUID_COLUMN),
                    userGuid, userGuid, userGuid, limit, offset);

            for (String friendId: friendsIds) {
                friends.add(getUser(friendId));
            }
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".loadFriends() - Something went wrong while reading the user's friends for user with id: " + userGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.LIST_FRIENDS_ERROR_TITLE,
                    ErrorKeys.LIST_FRIENDS_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e
            );
        }

        return friends;
    }

    @Override
    public int countFriends(String userGuid) {
        int totalCount = 0;
        if (EssUtils.isNullOrEmpty(userGuid)) {
            return totalCount;
        }

        try {
            totalCount = jdbc.query(FriendshipsQueries.COUNT_FRIENDS,
                    (preparedStatement) -> {
                        preparedStatement.setString(1, userGuid);
                    }, (rs) -> {
                        if (!rs.next()) {
                            return 0;
                        }
                        return rs.getInt(1);
                    });
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".countFriends() - Something went wrong while the user's friends for user with id: " + userGuid, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.LIST_FRIENDS_ERROR_TITLE,
                    ErrorKeys.LIST_FRIENDS_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e
            );
        }

        return totalCount;
    }

    @Override
    @Transactional
    public void deleteUserFriendships(String userGuid) {
        // Throws a NotFoundException if user does not exist
        getUser(userGuid);

        int rowsDeleted = 0;
        try {
            rowsDeleted = jdbc.update(FriendshipsQueries.DELETE_USER_FRIENDSHIPS, userGuid);
        } catch (Exception e) {
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.DELETE_USER_ERROR_TITLE,
                    ErrorKeys.DELETE_USER_ERROR_MESSAGE,
                    new Object[] {userGuid},
                    e
            );
        }

        logger.info(CLASS_NAME + ".deleteUserFriendships() - User friendships deleted: " + rowsDeleted);
    }

    @Override
    public FriendshipEntity loadFriendship(String friend, String addedBy) {
        FriendshipEntity friendshipEntity = null;

        if (EssUtils.isNullOrEmpty(friend) || EssUtils.isNullOrEmpty(addedBy)) {
            return null;
        }

        try {
            friendshipEntity = jdbc.query(FriendshipsQueries.LOAD_FRIENDSHIP_DETAILS,
                    this::toFriendshipEntity,
                    friend, addedBy);
        } catch (Exception e) {
            logger.error(CLASS_NAME + ".loadFriendship() - Something went wrong while loading the friendship details with friend: "
                    + friend + " and addedBy: " + addedBy, e);
            infrastructureHelper.throwInternalServerErrorException(
                    ErrorKeys.LOAD_FRIENDSHIP_ERROR_TITLE,
                    ErrorKeys.LOAD_FRIENDSHIP_ERROR_MESSAGE,
                    new Object[] {friend, addedBy},
                    e
            );
        }

        return friendshipEntity;
    }

    /**
     * Generates a list of user from a result set. Executes the method next() in loop to
     * go through all the rows
     *
     * @param rs result set
     * @return list of users
     */
    private List<UserEntity> toUserEntities(ResultSet rs) throws SQLException {
        List<UserEntity> users = new ArrayList<>();

        while(rs.next()) {
            users.add(buildEntity(rs));
        }

        return users;
    }

    /**
     * Generates a user from a result set, executes the method next()
     * to jump to the first row
     *
     * @param rs result set
     * @return user
     */
    private UserEntity toUserEntity(ResultSet rs) throws SQLException {
        UserEntity userEntity = null;

        if (rs.next()) {
            userEntity = buildEntity(rs);
        }

        return userEntity;
    }

    /**
     * Builds a user entity from a result set
     *
     * @param rs result set
     * @return user entity created from a result set
     */
    private UserEntity buildEntity(ResultSet rs) throws SQLException {

        UserEntity userEntity = new UserEntity();

        userEntity.setUserGuid(rs.getString(UserQueries.USERGUID_COLUMN.toLowerCase()));
        userEntity.setName(rs.getString(UserQueries.NAME_COLUMN.toLowerCase()));
        userEntity.setLastname(rs.getString(UserQueries.LASTNAME_COLUMN.toLowerCase()));
        userEntity.setUsername(rs.getString(UserQueries.USERNAME_COLUMN.toLowerCase()));
        userEntity.setEmail(rs.getString(UserQueries.EMAIL_COLUMN.toLowerCase()));
        userEntity.setPhone(rs.getString(UserQueries.PHONE_COLUMN.toLowerCase()));
        userEntity.setCreatedDate(rs.getTimestamp(UserQueries.CREATE_DATE_COLUMN.toLowerCase()));

        return userEntity;
    }

    /**
     * Build a friendship entity from a result set
     *
     * @param rs result set from which the friendship entity is build
     * @return friendship entity
     */
    private FriendshipEntity toFriendshipEntity(ResultSet rs) throws SQLException {
        FriendshipEntity friendshipEntity = null;

        if (rs.next()) {
            friendshipEntity = new FriendshipEntity();

            friendshipEntity.setFriendshipGuid(rs.getString(FriendshipsQueries.FRIENDSHIPGUID_COLUMN.toLowerCase()));

            String friendGuid = rs.getString(FriendshipsQueries.FRIEND_COLUMN.toLowerCase());
            UserEntity friend = getUser(friendGuid);
            friendshipEntity.setFriend(friend);

            String status = rs.getString(FriendshipsQueries.STATUS_COLUMN.toLowerCase());
            FriendshipStatus friendshipStatus = FriendshipStatus.valueOfString(status);
            friendshipEntity.setStatus(friendshipStatus);

            friendshipEntity.setCreatedDate(rs.getTimestamp(FriendshipsQueries.CREATED_DATE_COLUMN.toLowerCase()));

            String addedByGuid = rs.getString(FriendshipsQueries.ADDED_BY_COLUMN.toLowerCase());
            UserEntity addedBy = getUser(addedByGuid);
            friendshipEntity.setAddedBy(addedBy);
        }

        return friendshipEntity;
    }
}
